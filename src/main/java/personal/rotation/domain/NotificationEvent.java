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

package personal.rotation.domain;

import javax.persistence.*;
import java.util.Date;

/**
 * @author <a href="mailto:john.scattergood@navis.com">John Scattergood</a> 3/2/2016
 */
@Entity
public class NotificationEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column(nullable = false)
    private Integer rotationId;
    @Column(nullable = false)
    private Long rotationInterval;
    private Integer personId;
    private String emailAddress;
    @Column(nullable = false)
    private Date notificationTime;

    protected NotificationEvent() {
    }

    public NotificationEvent(Integer rotationId, Long rotationInterval, Integer personId, String emailAddress) {
        this.rotationId = rotationId;
        this.rotationInterval = rotationInterval;
        this.personId = personId;
        this.emailAddress = emailAddress;
        this.notificationTime = new Date();
    }

    public Integer getId() {
        return id;
    }

    public Integer getRotationId() {
        return rotationId;
    }

    public Long getRotationInterval() {
        return rotationInterval;
    }

    public Integer getPersonId() {
        return personId;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public Date getNotificationTime() {
        return notificationTime;
    }
}
