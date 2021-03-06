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
import org.springframework.web.bind.annotation.*;
import personal.rotation.domain.Person;
import personal.rotation.repository.PersonRepository;

import java.util.List;

/**
 * @author <a href="https://github.com/jscattergood">John Scattergood</a> 2/17/2016
 */
@Transactional
@RestController
public class PersonService {
    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    PersonRepository personRepository;

    @RequestMapping("/people")
    public List<Person> getPeople() {
        return personRepository.findAll();
    }

    @RequestMapping("/people/{id}")
    public Person getPerson(@PathVariable("id") Integer id) {
        return personRepository.findOne(id);
    }

    @RequestMapping(value = "/people", method = RequestMethod.POST)
    public Person createPerson(@RequestBody Person person) {
        return personRepository.save(person);
    }

    @RequestMapping(value = "/people/{id}", method = RequestMethod.PUT)
    public Person updatePerson(@PathVariable("id") Integer id, @RequestBody Person person) {
        person.setId(id);
        return personRepository.save(person);
    }

    @RequestMapping(value = "/people/{id}", method = RequestMethod.DELETE)
    public void deletePerson(@PathVariable("id") Integer id) {
        Person person = personRepository.findOne(id);
        if (person != null ){
            personRepository.delete(id);
        }
    }
}
