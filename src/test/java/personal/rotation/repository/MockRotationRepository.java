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

package personal.rotation.repository;

import org.mockito.Mockito;
import org.springframework.data.domain.Sort;
import personal.rotation.domain.*;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.IntStream;

public class MockRotationRepository {

    private final List<Rotation> rotations;
    private RotationRepository mock;

    public MockRotationRepository() {
        mock = Mockito.mock(RotationRepository.class);
        rotations = createRotations();
    }

    private static List<RotationMember> getMembers(Rotation rotation, Person... people) {
        List<RotationMember> members = new ArrayList<>();
        IntStream.range(0, people.length)
                .forEach(idx -> members.add(new RotationMember(rotation, people[idx], idx)));
        RotationMember member = members.get(people.length - 1);
        RotationDelegate delegate = new RotationDelegate(member, people[0]);
        delegate.setStartDate(new Date(0));
        delegate.setEndDate(new Date(Long.MAX_VALUE));
        member.setDelegates(Collections.singletonList(delegate));
        return members;
    }

    private List<Rotation> createRotations() {
        List<Rotation> rotations = new ArrayList<>();
        Role role = new Role("Role");
        Person person1 = new Person("John", "Smith", "john.smith@email.com");
        Person person2 = new Person("Jane", "Smith", "jane.smith@email.com");
        Person person3 = new Person("Jackie", "Smith", "jackie.smith@email.com");

        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime startTime = now.minusDays(29);
        Date startDate = Date.from(startTime.toInstant());
        Rotation rotation = new Rotation("Rotation", role, startDate, 7);
        rotation.setMembers(getMembers(rotation, person1, person2, person3));
        rotations.add(rotation);

        Mockito.when(mock.findAll(new Sort(Sort.Direction.DESC, "startDate")))
                .thenReturn(rotations);
        return rotations;
    }

    public RotationRepository getRepository() {
        return mock;
    }

    public List<Rotation> getRotations() {
        return rotations;
    }
}