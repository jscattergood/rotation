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
import personal.rotation.reactive.ObservableTxFactory;
import rx.Observable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;

import static org.junit.Assert.*;

/**
 * @author <a href="https://github.com/jscattergood">John Scattergood</a> 2/17/2016
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {RepositoryConfiguration.class})
@Component
@SuppressWarnings("SpringJavaAutowiringInspection")
public class PersonRepositoryTest {
    @Autowired
    PersonRepository personRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    ObservableTxFactory observableTxFactory;

    @Test
    public void testSavePerson(){
        //setup person
        Person person = new Person("Joe", "Smith", "joe.smith@mail.com");

        //save person, verify has ID value after save
        assertNull(person.getId()); //null before save
        personRepository.save(person);
        assertNotNull(person.getId()); //not null after save

        //fetch from DB
        Person fetchedPerson = personRepository.findOne(person.getId());

        //should not be null
        assertNotNull(fetchedPerson);

        //should equal
        assertEquals(person.getId(), fetchedPerson.getId());
        assertEquals(person.getFirstName(), fetchedPerson.getFirstName());

        //update description and save
        fetchedPerson.setFirstName("John");
        personRepository.save(fetchedPerson);

        //get from DB, should be updated
        Person fetchedUpdatedPerson = personRepository.findOne(fetchedPerson.getId());
        assertEquals(fetchedPerson.getFirstName(), fetchedUpdatedPerson.getFirstName());

        //verify count of people in DB
        long personCount = personRepository.count();
        assertEquals(personCount, 1);

        //get all people, list should only have one
        long count = personRepository.findAll().size();
        assertEquals(count, 1L);
    }

    @Test
    public void testReactiveSavePerson() {
        Person person = new Person("Jane", "Doe", "jane.doe@mail.com");
        assertNull(person.getId()); //null before save

        //save person
        Observable.create(s -> {
            s.onNext(personRepository.save(person));
        }).subscribe();

        //fetch from DB
        Person fetchedPerson = personRepository.findOne(person.getId());

        //should not be null
        assertNotNull(fetchedPerson);

        //should equal
        assertEquals(person.getId(), fetchedPerson.getId());
        assertEquals(person.getFirstName(), fetchedPerson.getFirstName());

        //save person, verify has ID value after save
        observableTxFactory.create(s -> {
            Person p = new Person("Julie", "Smith", "julie.smith@mail.com");
            s.onNext(personRepository.save(p));
        }).subscribe();

        //fetch from DB
        final Person foundPerson = StreamSupport.stream(personRepository.findAll().spliterator(), false)
                .filter(p -> p.getFirstName().equals("Julie") && p.getLastName().equals("Smith"))
                .findFirst()
                .get();

        //should not be null
        assertNotNull(foundPerson);
    }

    @Test
    @Transactional
    public void testAddPotentialRoles() {
        // setup roles to be used
        List<Role> roles = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            Role role = new Role("Role"+i);
            roles.add(role);
        }
        roleRepository.save(roles);

        // verify that the roles were saved
        assertTrue(roleRepository.findAll().containsAll(roles));

        // save the person with the potential roles
        Person person = new Person("Joe", "Smith", "joe.smith@mail.com");
        person.setPotentialRoles(roles);
        personRepository.save(person);

        // verify that the potential roles were saved
        Person fetchedPerson = personRepository.findOne(person.getId());
        assertNotNull(fetchedPerson);
        assertTrue(fetchedPerson.getPotentialRoles().containsAll(roles));
    }
}