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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import personal.rotation.configuration.ServiceTestContext;
import personal.rotation.domain.Rotation;
import personal.rotation.repository.MockRotationRepository;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
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
    private MockRotationRepository mockRotationRepository;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        this.mockRotationRepository = new MockRotationRepository();
    }

    @Test
    public void testServicePaths() throws Exception {
        mockMvc.perform(get("/rotations")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testFindCurrentRotationDetails() throws Exception {
        RotationService service = new RotationService();
        service.rotationRepository = mockRotationRepository.getRepository();

        List<Rotation> rotations = mockRotationRepository.getRotations();

        Map<Rotation, Map<String, Object>> currentRotationMembers = service.findCurrentRotationDetails();
        Map<String, Object> rotationDetail = currentRotationMembers.get(rotations.get(0));
        assertNotNull(rotationDetail);
        assertEquals(rotationDetail.get(RotationService.PERSON), rotations.get(0).getMembers().get(1).getPerson());
        assertNotEquals(rotationDetail.get(RotationService.START_DATE), new Date(0));
        assertNotEquals(rotationDetail.get(RotationService.END_DATE), new Date(Long.MAX_VALUE));

        currentRotationMembers = service.findNextRotationDetails();
        rotationDetail = currentRotationMembers.get(rotations.get(0));
        assertNotNull(rotationDetail);
        assertEquals(rotationDetail.get(RotationService.PERSON), rotations.get(0).getMembers().get(0).getPerson());
        assertNotEquals(rotationDetail.get(RotationService.START_DATE), new Date(0));
        assertNotEquals(rotationDetail.get(RotationService.END_DATE), new Date(Long.MAX_VALUE));
    }

}