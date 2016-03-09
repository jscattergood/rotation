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

package personal.rotation.notifier;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import personal.rotation.configuration.NotifierTestContext;
import personal.rotation.domain.Person;
import personal.rotation.domain.Role;
import personal.rotation.domain.Rotation;
import personal.rotation.repository.MockNotificationEventRepository;

import java.util.Date;

import static org.junit.Assert.assertNotNull;

/**
 * @author <a href="https://github.com/jscattergood">John Scattergood</a> 3/8/2016
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {NotifierTestContext.class})
public class EmailNotifierTest {
    MockNotificationEventRepository mockRepository;

    @Before
    public void setup() {
        this.mockRepository = new MockNotificationEventRepository();
    }

    @Test
    public void testSend() throws Exception {
        Person person = new Person("Joe", "Smith", "joe.smith@mail.com");
        Rotation rotation = new Rotation("rotation 1", new Role("role"),  new Date(), 7);

        EmailNotifier notifier = new EmailNotifier("smtp.mail.com", "rotation@mail.com", "",
                mockRepository.getRepository(), getMockSender());
        notifier.setSmtpPort("25");
        notifier.setTransportProtocol("smtp");
        notifier.send(rotation, 1L, person, new Date(), new Date());
        assertNotNull(mockRepository.getSavedEvent());
    }

    private EmailSender getMockSender() {
        return (email, subject, body, props, protocol) -> {
        };
    }
}