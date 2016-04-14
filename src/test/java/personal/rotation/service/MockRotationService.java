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

import org.mockito.Mockito;
import personal.rotation.domain.Person;
import personal.rotation.domain.Role;
import personal.rotation.domain.Rotation;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static personal.rotation.service.RotationService.*;

/**
 * @author <a href="mailto:john.scattergood@navis.com">John Scattergood</a> 3/12/2016
 */
public class MockRotationService {
    private final RotationService mock;

    public MockRotationService() {
        mock = Mockito.mock(RotationService.class);
        createServiceResult();
    }

    private void createServiceResult() {
        Person person = new Person("Jane", "Smith", "jane.smith@mail.com");
        person.setId(2);
        Map<String, Object> details = new HashMap<>();
        details.put(INTERVAL, 1L);
        details.put(START_DATE, new Date());
        details.put(END_DATE, new Date(Long.MAX_VALUE));
        details.put(REMAINING_DAYS, 0);
        details.put(PERSON, person);

        Rotation rotation = new Rotation("rotation 1", new Role("role"), new Date(), 1);
        rotation.setId(1);
        Map<Rotation, Map<String, Object>> returnMap = new HashMap<>();
        returnMap.put(rotation, details);

        Mockito.when(mock.findNextRotationDetails()).thenReturn(returnMap);
    }

    public RotationService getRotationService() {
        return mock;
    }
}
