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
@RestController
@Transactional
public class PersonService {
    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    PersonRepository personRepository;

    @RequestMapping("/persons")
    public List<Person> getPersons() {
        return personRepository.findAll();
    }

    @RequestMapping("/person")
    public Person getPerson(@RequestParam("id") Integer id) {
        return personRepository.findOne(id);
    }

    @RequestMapping(value = "/person", method = RequestMethod.POST)
    public Person createOrUpdatePerson(@RequestBody Person person) {
        return personRepository.save(person);
    }
}
