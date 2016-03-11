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

package personal.rotation.job;

import org.joda.time.DateTimeConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import personal.rotation.domain.Person;
import personal.rotation.domain.Rotation;
import personal.rotation.notifier.Notifier;
import personal.rotation.repository.NotificationEventRepository;
import personal.rotation.service.RotationService;

import java.util.Date;
import java.util.Map;

/**
 * @author <a href="https://github.com/jscattergood">John Scattergood</a> 2/23/2016
 */
@Component
public class NotifierJob {
    @Autowired
    Notifier notifier;
    @Autowired
    RotationService rotationService;
    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    NotificationEventRepository notificationEventRepository;

    @Scheduled(fixedRate=60000)
    public void notifyIfRotationChange() {
        Date now = new Date();
        Map<Rotation, Map<String, Object>> details =  rotationService.findNextRotationDetails();
        details.forEach((r, d) -> {
            Person person = (Person) d.get(RotationService.MEMBER_PERSON);
            Date startDate = (Date) d.get(RotationService.START_DATE);
            Date endDate = (Date) d.get(RotationService.END_DATE);
            Long interval = (Long) d.get(RotationService.INTERVAL);
            if (!alreadyNotified(r, interval, person)
                    && Math.abs(now.getTime() - startDate.getTime()) < DateTimeConstants.MILLIS_PER_DAY) {
                notifier.send(r, interval, person, startDate, endDate);
            }
        });
    }

    private boolean alreadyNotified(Rotation rotation, Long interval, Person person) {
        return notificationEventRepository.existsByRotationIdAndIntervalAndPersonId(rotation.getId(), interval,
                person.getId());
    }
}
