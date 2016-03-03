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

package personal.rotation.jobs;

import org.joda.time.DateTimeConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import personal.rotation.domain.Person;
import personal.rotation.domain.Rotation;
import personal.rotation.notifier.Notifier;
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

    @Scheduled(fixedRate=60000)
    public void notifyIfRotationChange() {
        Date now = new Date();
        Map<Rotation, Map<String, Object>> details =  rotationService.findNextRotationDetails();
        details.forEach((r, d) -> {
            Person member = (Person) d.get(RotationService.MEMBER);
            Date startDate = (Date) d.get(RotationService.START_DATE);
            Date endDate = (Date) d.get(RotationService.END_DATE);
            Integer interval = (Integer) d.get(RotationService.INTERVAL);
            if (!alreadyNotified(r, interval, member)
                    && startDate.getTime() - now.getTime() < DateTimeConstants.MILLIS_PER_DAY) {
                notifier.send(r, interval, member, startDate, endDate);
            }
        });
    }

    private boolean alreadyNotified(Rotation rotation, Integer interval, Person member) {
        return false;
    }
}
