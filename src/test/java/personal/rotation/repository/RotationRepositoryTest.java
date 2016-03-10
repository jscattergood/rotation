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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import personal.rotation.configuration.RepositoryConfiguration;
import personal.rotation.domain.Person;
import personal.rotation.domain.Role;
import personal.rotation.domain.Rotation;
import personal.rotation.domain.RotationMember;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author <a href="https://github.com/jscattergood">John Scattergood</a> 2/17/2016
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {RepositoryConfiguration.class})
@Component
@SuppressWarnings("SpringJavaAutowiringInspection")
public class RotationRepositoryTest {
    @Autowired
    RotationRepository rotationRepository;
    @Autowired
    PersonRepository personRepository;
    @Autowired
    RoleRepository roleRepository;

    @Test
    public void testSaveRotation() {
        //setup role
        Role role = new Role("role");
        roleRepository.save(role);

        //setup rotation
        Rotation rotation = new Rotation("Rotation1", role, new Date(), 7);

        //save rotation, verify has ID value after save
        assertNull(rotation.getId()); //null before save
        rotationRepository.save(rotation);
        assertNotNull(rotation.getId()); //not null after save

        //fetch from DB
        Rotation fetchedRotation = rotationRepository.findOne(rotation.getId());

        //should not be null
        assertNotNull(fetchedRotation);

        //should equal
        assertEquals(rotation.getId(), fetchedRotation.getId());
        assertEquals(rotation.getName(), fetchedRotation.getName());

        //update name and save
        fetchedRotation.setName("Rotation2");
        rotationRepository.save(fetchedRotation);

        //get from DB, should be updated
        Rotation fetchedUpdatedRotation = rotationRepository.findOne(fetchedRotation.getId());
        assertEquals(fetchedRotation.getName(), fetchedUpdatedRotation.getName());

        //verify count of rotations in DB
        long rotationCount = rotationRepository.count();
        assertEquals(rotationCount, 1);

        //get all rotations, list should only have one
        long count = rotationRepository.findAll().size();
        assertEquals(count, 1L);
    }

    @Test
    @Transactional
    public void testAddRotationMembers() {
        Role role = new Role("role");
        roleRepository.save(role);

        //setup people to be used
        List<Person> people = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            Person person = new Person("John", Integer.toString(i), "john." + i + "@mail.com");
            person.addPotentialRole(role);
            people.add(person);
        }
        personRepository.save(people);

        //create members and add them to the rotation
        Rotation rotation = new Rotation("rotation", role, new Date(), 7);
        for (int i = 0; i < people.size(); i++) {
            Person person = people.get(i);
            RotationMember member = new RotationMember(rotation, person, i);
            rotation.getMembers().add(member);
        }
        rotationRepository.save(rotation);

        //fetch the rotation again and check members
        Rotation fetchedRotation = rotationRepository.findOne(rotation.getId());
        List<RotationMember> members = fetchedRotation.getMembers();
        assertNotNull(members);

        members.forEach(rm -> assertTrue(people.contains(rm.getPerson())));
    }

    @Test
    @Transactional
    public void testRemoveRotationMembers() {
        Role role = new Role("role");
        roleRepository.save(role);

        //setup people to be used
        List<Person> people = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            Person person = new Person("John", Integer.toString(i), "john." + i + "@mail.com");
            person.addPotentialRole(role);
            people.add(person);
        }
        personRepository.save(people);

        //create members and add them to the rotation
        Rotation rotation = new Rotation("rotation", role, new Date(), 7);
        for (int i = 0; i < people.size(); i++) {
            Person person = people.get(i);
            RotationMember member = new RotationMember(rotation, person, i);
            rotation.getMembers().add(member);
        }
        rotationRepository.save(rotation);

        //fetch the rotation again and check members
        Rotation fetchedRotation = rotationRepository.findOne(rotation.getId());
        List<RotationMember> members = fetchedRotation.getMembers();
        assertNotNull(members);

        members.forEach(rm -> assertTrue(people.contains(rm.getPerson())));

        RotationMember firstMember = members.get(0);
        rotation.getMembers().remove(firstMember);
        rotationRepository.save(rotation);

        //fetch the rotation again and check members
        fetchedRotation = rotationRepository.findOne(rotation.getId());
        members = fetchedRotation.getMembers();
        assertNotNull(members);

        members.forEach(rm -> assertFalse(rm.getPerson().equals(firstMember.getPerson())));
    }
}
