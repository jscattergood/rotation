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

import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import personal.rotation.domain.NotificationEvent;

import java.util.Collections;

/**
 * @author <a href="https://github.com/jscattergood">John Scattergood</a> 3/8/2016
 */
public class MockNotificationEventRepository {
    NotificationEventRepository mock;

    public MockNotificationEventRepository() {
        this.mock = Mockito.mock(NotificationEventRepository.class);
        createNotifications();

    }

    private void createNotifications() {
        NotificationEvent event = new NotificationEvent(1, 1L, 1, "joe.smith@mail.com");
        Mockito.when(mock.findAll()).thenReturn(Collections.singletonList(event));
    }

    public NotificationEvent getSavedEvent() {
        ArgumentCaptor<NotificationEvent> captor = ArgumentCaptor.forClass(NotificationEvent.class);
        Mockito.verify(mock).save(captor.capture());
        return captor.getValue();
    }

    public NotificationEventRepository getRepository() {
        return mock;
    }
}
