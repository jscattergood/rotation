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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import personal.rotation.domain.Role;
import personal.rotation.domain.RotationMember;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="https://github.com/jscattergood">John Scattergood</a> 2/18/2016
 */
@Service
@Transactional
@RestController
public class ScheduleService {
    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    RotationService rotationService;

    @RequestMapping("/schedule")
    public Map<Role, RotationMember> getSchedule() {
        List<RotationMember> currentMembers = rotationService.findCurrentRotationMembers();
        HashMap<Role, RotationMember> schedule = new HashMap<>();
        currentMembers.forEach(rm -> schedule.put(rm.getRotation().getRole(), rm));
        return schedule;
    }
}
