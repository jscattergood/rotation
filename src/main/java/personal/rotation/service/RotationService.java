/*
 * Copyright 2016 John Scattergood
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package personal.rotation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import personal.rotation.domain.*;
import personal.rotation.repository.RotationRepository;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

import static org.joda.time.DateTimeConstants.MILLIS_PER_DAY;

/**
 * @author <a href="https://github.com/jscattergood">John Scattergood</a> 2/17/2016
 */
@Transactional
@RestController
public class RotationService {
    public static final String START_DATE = "startDate";
    public static final String END_DATE = "endDate";
    public static final String REMAINING_DAYS = "remainingDays";
    public static final String PERSON = "person";
    public static final String ROLE = "role";
    public static final String INTERVAL = "interval";
    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    RotationRepository rotationRepository;

    @RequestMapping("/rotations")
    public List<Rotation> getRotations() {
        return rotationRepository.findAll();
    }

    @RequestMapping("/rotations/{id}")
    public Rotation getRotation(@PathVariable("id") Integer id) {
        return rotationRepository.findOne(id);
    }

    @RequestMapping(value = "/rotations", method = RequestMethod.POST)
    public Rotation createRotation(@RequestBody Rotation rotation) {
        return rotationRepository.save(rotation);
    }

    @RequestMapping(value = "/rotations/{id}", method = RequestMethod.PUT)
    public Rotation updateRotation(@PathVariable("id") Integer id, @RequestBody Rotation rotation) {
        rotation.setId(id);
        return rotationRepository.save(rotation);
    }

    @RequestMapping(value = "/rotations/{id}", method = RequestMethod.DELETE)
    public void deleteRotation(@PathVariable("id") Integer id) {
        Rotation person = rotationRepository.findOne(id);
        if (person != null ){
            rotationRepository.delete(id);
        }
    }

    /**
     * This function finds the last rotation members based upon the defined rotation schedule
     *
     * @return list of rotation members
     */
    public Map<Rotation, Map<String, Object>> findLastRotationDetails() {
        return findRotationDetails(-1);
    }

    /**
     * This function finds the current rotation members based upon the defined rotation schedule
     *
     * @return list of rotation members
     */
    public Map<Rotation, Map<String, Object>> findCurrentRotationDetails() {
        return findRotationDetails(0);
    }

    /**
     * This function finds the next rotation members based upon the defined rotation schedule
     *
     * @return list of rotation members
     */
    public Map<Rotation, Map<String, Object>> findNextRotationDetails() {
        return findRotationDetails(1);
    }

    private Map<Rotation, Map<String, Object>> findRotationDetails(int intervalOffset) {
        Map<Role, Rotation> currentRotations = getCurrentRotationsByRole();

        Date now = new Date();
        Map<Rotation, Map<String, Object>> rotationDetails =
                new TreeMap<>((r1, r2) -> r1.getName().compareTo(r2.getName()));
        currentRotations.forEach((role, rotation) ->
                rotationDetails.put(rotation, findRotationDetails(intervalOffset, now.getTime(), rotation)));
        return rotationDetails;
    }

    private Map<String, Object> findRotationDetails(int intervalOffset, long nowMillis, Rotation rotation) {
        Map<String, Object> result = new HashMap<>();
        Date rotationStartDate = rotation.getStartDate();
        long startDateMillis = rotationStartDate.getTime();
        Integer interval = rotation.getInterval();
        long intervalMillis = interval.longValue() * MILLIS_PER_DAY;
        long intervals = (nowMillis - startDateMillis) / intervalMillis;
        intervals = intervals + intervalOffset;

        result.put(ROLE, rotation.getRole());
        result.put(INTERVAL, intervals);

        List<RotationMember> members = rotation.getMembers();
        if (!members.isEmpty()) {
            int countOfMembers = members.size();
            Long sequence = intervals % countOfMembers;
            RotationMember member = members.get(sequence.intValue());
            // check for delegates
            List<RotationDelegate> delegates = member.getDelegates();
            Optional<RotationDelegate> delegate = delegates.stream().filter(p ->
                    nowMillis >= p.getStartDate().getTime() &&
                    nowMillis <= (p.getEndDate().getTime() + MILLIS_PER_DAY)).findFirst();
            Date intervalStart = getStartDate(rotationStartDate, interval, intervals);
            Date intervalEnd = getEndDate(rotationStartDate, interval, intervals);

            final Person person;
            final Date startDate;
            final Date endDate;
            if (delegate.isPresent()) {
                person = delegate.get().getDelegate();
                startDate = delegate.get().getStartDate();
                endDate = delegate.get().getEndDate();
            }
            else {
                person = member.getPerson();
                startDate = intervalStart;
                endDate = intervalEnd;
            }
            result.put(PERSON, person);
            result.put(START_DATE, intervalStart.before(startDate) ? startDate : intervalStart);
            result.put(END_DATE, intervalEnd.after(endDate) ? endDate : intervalEnd);

            double remainingDays = intervalEnd.after(endDate) ?
                    getRemainingDays(nowMillis, endDate) : getRemainingDays(nowMillis, intervalEnd);
            result.put(REMAINING_DAYS, remainingDays);
        }
        return result;
    }

    private double getRemainingDays(long nowMillis, Date intervalEnd) {
        return (Math.ceil((intervalEnd.getTime() - nowMillis) / (double) MILLIS_PER_DAY)) + 1; // add the last day
    }

    private Date getStartDate(Date startDate, Integer interval, long intervals) {
        ZonedDateTime date = ZonedDateTime.ofInstant(startDate.toInstant(), ZoneId.systemDefault());
        date = date.plusDays(interval * intervals);
        return Date.from(date.toInstant());
    }

    private Date getEndDate(Date startDate, Integer interval, long intervals) {
        ZonedDateTime date = ZonedDateTime.ofInstant(startDate.toInstant(), ZoneId.systemDefault());
        date = date.plusDays((interval * (intervals + 1)) - 1);
        return Date.from(date.toInstant());
    }

    private Map<Role, Rotation> getCurrentRotationsByRole() {
        Date now = new Date();
        Map<Role, Rotation> rotations = new HashMap<>();
        rotationRepository
                .findAll(new Sort(Sort.Direction.DESC, START_DATE)).stream()
                .filter(r -> r.getStartDate().before(now))
                .forEach(r -> rotations.putIfAbsent(r.getRole(), r));
        return rotations;
    }
}
