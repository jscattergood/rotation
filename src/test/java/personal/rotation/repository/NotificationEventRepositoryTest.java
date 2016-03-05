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
import personal.rotation.domain.NotificationEvent;

import static org.junit.Assert.*;

/**
 * @author <a href="mailto:john.scattergood@navis.com">John Scattergood</a> 3/2/2016
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {RepositoryConfiguration.class})
@Component
@SuppressWarnings("SpringJavaAutowiringInspection")
public class NotificationEventRepositoryTest {
    @Autowired
    NotificationEventRepository eventRepository;

    @Test
    public void testSaveEvent() {
        //setup event
        NotificationEvent event = new NotificationEvent(1, 1L, 1, "joe.smith@mail.com");

        //save event, verify has ID value after save
        assertNull(event.getId()); //null before save
        eventRepository.save(event);
        assertNotNull(event.getId()); //not null after save

        //fetch from DB
        NotificationEvent fetchedNotificationEvent = eventRepository.findOne(event.getId());

        //should not be null
        assertNotNull(fetchedNotificationEvent);

        //should equal
        assertEquals(event.getId(), fetchedNotificationEvent.getId());

        //verify count of events in DB
        long eventCount = eventRepository.count();
        assertEquals(eventCount, 1);

        //get all events, list should only have one
        long count = eventRepository.findAll().size();
        assertEquals(count, 1L);

    }

    @Test
    public void testExistsByRotationIdAndIntervalAndPersonId() {
        NotificationEvent event = new NotificationEvent(1, 1L, 1, "joe.smith@mail.com");
        // initialize event
        assertNull(event.getId());
        eventRepository.save(event);
        assertNotNull(event.getId());

        // check for existence
        assertTrue(eventRepository.existsByRotationIdAndIntervalAndPersonId(event.getRotationId(),
                event.getRotationInterval(), event.getPersonId()));

        // check for non-existent event
        assertFalse(eventRepository.existsByRotationIdAndIntervalAndPersonId(2,2L,2));
    }
}