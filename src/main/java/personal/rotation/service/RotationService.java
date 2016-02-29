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

import org.joda.time.DateTimeConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import personal.rotation.domain.Role;
import personal.rotation.domain.Rotation;
import personal.rotation.domain.RotationMember;
import personal.rotation.repository.RotationRepository;

import java.util.*;

/**
 * @author <a href="https://github.com/jscattergood">John Scattergood</a> 2/17/2016
 */
@Service
@Transactional
@RestController
public class RotationService {
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
    public List<RotationMember> findLastRotationMembers() {
        return findRotationMembers(-1);
    }

    /**
     * This function finds the current rotation members based upon the defined rotation schedule
     *
     * @return list of rotation members
     */
    public List<RotationMember> findCurrentRotationMembers() {
        return findRotationMembers(0);
    }

    /**
     * This function finds the next rotation members based upon the defined rotation schedule
     *
     * @return list of rotation members
     */
    public List<RotationMember> findNextRotationMembers() {
        return findRotationMembers(1);
    }

    private List<RotationMember> findRotationMembers(int intervalOffset) {
        Date now = new Date();
        Map<Role, Rotation> currentRotations = new HashMap<>();
        List<RotationMember> currentMembers = new ArrayList<>();
        rotationRepository
                .findAll(new Sort(Sort.Direction.DESC, "startDate")).stream()
                .filter(r -> r.getStartDate().before(now))
                .forEach(r -> currentRotations.putIfAbsent(r.getRole(), r));

        currentRotations.forEach((role, rotation) -> {
            List<RotationMember> members = rotation.getMembers();
            if (!members.isEmpty()) {
                if (members.size() == 1) {
                    currentMembers.addAll(members);
                    return;
                }

                long startDateMillis = rotation.getStartDate().getTime();
                long nowDateMillis = now.getTime();
                long intervalMillis = rotation.getInterval().longValue() * DateTimeConstants.MILLIS_PER_DAY;

                long intervals = (nowDateMillis - startDateMillis) / intervalMillis;
                int countOfMembers = members.size();
                Long sequence = (intervals + intervalOffset) % countOfMembers;

                currentMembers.add(members.get(sequence.intValue()));
            }
        });

        currentMembers.sort((o1, o2) -> {
            String roleName1 = o1.getRotation().getRole().getName();
            String roleName2 = o2.getRotation().getRole().getName();
            return roleName1.compareTo(roleName2);
        });
        return currentMembers;
    }
}
