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

package personal.rotation.job;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import personal.rotation.domain.Person;
import personal.rotation.domain.Rotation;
import personal.rotation.notifier.Notifier;
import personal.rotation.repository.MockNotificationEventRepository;
import personal.rotation.service.MockRotationService;

import java.util.Date;

import static org.junit.Assert.assertNotNull;


/**
 * @author <a href="mailto:john.scattergood@navis.com">John Scattergood</a> 3/12/2016
 */
public class NotifierJobTest {

    @Test
    public void testNotifyOnRotationChange() throws Exception {
        NotifierJob job = new NotifierJob();
        job.rotationService = new MockRotationService().getRotationService();
        MockNotificationEventRepository mockRepository = new MockNotificationEventRepository();
        job.notificationEventRepository = mockRepository.getRepository();
        job.notifier = Mockito.mock(Notifier.class);

        mockRepository.setNotExists();
        job.notifyIfRotationChange();

        assertSent(job);
    }

    @Test
    public void testNotifyOnRotationNotChanged() throws Exception {
        NotifierJob job = new NotifierJob();
        job.rotationService = new MockRotationService().getRotationService();
        MockNotificationEventRepository mockRepository = new MockNotificationEventRepository();
        job.notificationEventRepository = mockRepository.getRepository();
        job.notifier = Mockito.mock(Notifier.class);

        mockRepository.setExists();
        job.notifyIfRotationChange();

        assertNotSent(job);
    }

    private void assertSent(NotifierJob job) {
        ArgumentCaptor<Rotation> rotation = ArgumentCaptor.forClass(Rotation.class);
        ArgumentCaptor<Long> interval = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Person> person = ArgumentCaptor.forClass(Person.class);
        ArgumentCaptor<Date> startDate = ArgumentCaptor.forClass(Date.class);
        ArgumentCaptor<Date> endDate = ArgumentCaptor.forClass(Date.class);

        Mockito.verify(job.notifier).send(rotation.capture(), interval.capture(),
                person.capture(), startDate.capture(), endDate.capture());
        assertNotNull(rotation.getValue());
        assertNotNull(interval.getValue());
        assertNotNull(person.getValue());
        assertNotNull(startDate.getValue());
        assertNotNull(endDate.getValue());
    }

    private void assertNotSent(NotifierJob job) {
        Mockito.verifyZeroInteractions(job.notifier);
    }

}