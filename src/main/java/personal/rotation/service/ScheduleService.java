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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import personal.rotation.domain.Rotation;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="https://github.com/jscattergood">John Scattergood</a> 2/18/2016
 */
@Transactional
@RestController
public class ScheduleService {
    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    RotationService rotationService;

    @RequestMapping("/schedule/last")
    public List<Map<String, Object>> getLastRotation() {
        Map<Rotation, Map<String, Object>> details = rotationService.findLastRotationDetails();
        return formatResult(details);
    }

    @RequestMapping("/schedule/current")
    public List<Map<String, Object>> getCurrentRotation() {
        Map<Rotation, Map<String, Object>> details = rotationService.findCurrentRotationDetails();
        return formatResult(details);
    }

    @RequestMapping("/schedule/next")
    public List<Map<String, Object>> getNextRotation() {
        Map<Rotation, Map<String, Object>> details = rotationService.findNextRotationDetails();
        return formatResult(details);
    }

    private List<Map<String, Object>> formatResult(Map<Rotation, Map<String, Object>> details) {
        List<Map<String,Object>> result = new ArrayList<>();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        details.forEach((r,d) -> {
            d.forEach((k, v) -> {
                if (v instanceof Date) {
                    d.put(k, df.format(v));
                }
            });
            result.add(d);
        });
        return result;
    }
}
