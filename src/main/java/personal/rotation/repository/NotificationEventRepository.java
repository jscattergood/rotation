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

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import personal.rotation.domain.NotificationEvent;

/**
 * @author <a href="mailto:john.scattergood@navis.com">John Scattergood</a> 3/2/2016
 */
public interface NotificationEventRepository extends JpaRepository<NotificationEvent, Integer> {
    @Query("select count(e) > 0 " +
            "from NotificationEvent e " +
            "where e.rotationId = :rotationId " +
            "and e.rotationInterval = :interval " +
            "and e.personId = :personId")
    boolean existsByRotationIdAndIntervalAndPersonId(@Param("rotationId") Integer rotationId,
                                                     @Param("interval") Integer interval,
                                                     @Param("personId") Integer personId);
}
