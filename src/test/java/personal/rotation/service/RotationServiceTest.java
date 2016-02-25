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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import personal.rotation.configuration.ServiceTestContext;
import personal.rotation.domain.Person;
import personal.rotation.domain.Role;
import personal.rotation.domain.Rotation;
import personal.rotation.domain.RotationMember;
import personal.rotation.repository.RotationRepository;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author <a href="https://github.com/jscattergood">John Scattergood</a> 2/17/2016
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ServiceTestContext.class})
@WebAppConfiguration
public class RotationServiceTest {
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void testServicePaths() throws Exception {
        mockMvc.perform(get("/rotations")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testFindCurrentRotationMembers() throws Exception {
        RotationService service = new RotationService();
        service.rotationRepository = getMockRotationRepository();

        assertTrue(service.findCurrentRotationMembers().isEmpty());

        List<Rotation> rotations = createRotations();
        when(service.rotationRepository.findAll(new Sort(Sort.Direction.DESC, "startDate")))
                .thenReturn(rotations);

        assertTrue(service.findCurrentRotationMembers().contains(rotations.get(0).getMembers().get(1)));
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
        return rotations;
    }

    private List<RotationMember> getMembers(Rotation rotation, Person... persons) {
        List<RotationMember> members = new ArrayList<>();
        IntStream.range(0, persons.length)
                .forEach(idx -> members.add(new RotationMember(rotation, persons[idx], idx)));
        return members;
    }

    public RotationRepository getMockRotationRepository() {
        return Mockito.mock(RotationRepository.class);
    }
}