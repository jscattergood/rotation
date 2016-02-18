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
import personal.rotation.configuration.RepositoryConfiguration;
import personal.rotation.domain.Role;

import static org.junit.Assert.*;

/**
 * @author <a href="https://github.com/jscattergood">John Scattergood</a> 2/17/2016
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {RepositoryConfiguration.class})
@Component
@SuppressWarnings("SpringJavaAutowiringInspection")
public class RoleRepositoryTest {
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PersonRepository personRepository;

    @Test
    public void testSaveRole() {
        //setup role
        Role role = new Role("Job1");

        //save role, verify has ID value after save
        assertNull(role.getId()); //null before save
        roleRepository.save(role);
        assertNotNull(role.getId()); //not null after save

        //fetch from DB
        Role fetchedRole = roleRepository.findOne(role.getId());

        //should not be null
        assertNotNull(fetchedRole);

        //should equal
        assertEquals(role.getId(), fetchedRole.getId());
        assertEquals(role.getName(), fetchedRole.getName());

        //update name and save
        fetchedRole.setName("Job2");
        roleRepository.save(fetchedRole);

        //get from DB, should be updated
        Role fetchedUpdatedRole = roleRepository.findOne(fetchedRole.getId());
        assertEquals(fetchedRole.getName(), fetchedUpdatedRole.getName());

        //verify count of roles in DB
        long roleCount = roleRepository.count();
        assertEquals(roleCount, 1);

        //get all roles, list should only have one
        long count = roleRepository.findAll().size();
        assertEquals(count, 1L);
    }
}
