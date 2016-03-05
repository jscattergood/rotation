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

package personal.rotation;

import de.infinit.spring.boot.autoconfigure.wro4j.GroovyWroManagerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;
import personal.rotation.notifier.EmailNotifier;
import personal.rotation.notifier.Notifier;
import personal.rotation.repository.NotificationEventRepository;
import ro.isdc.wro.manager.factory.WroManagerFactory;

import java.util.Properties;

@SpringBootApplication
@EnableScheduling
public class RotationApplication {

    private static final String WRO_CONFIG = "wro.groovy";

    @Bean
    WroManagerFactory wroManagerFactory() {
        Properties configProperties = new Properties();
        configProperties.put("preProcessors", "cssUrlRewriting");
        return new GroovyWroManagerFactory(WRO_CONFIG, configProperties);
    }

    @Bean
    @Autowired
    Notifier notifier(Environment env,
                      @SuppressWarnings("SpringJavaAutowiringInspection")
                      NotificationEventRepository notificationEventRepository) {
        String host = env.getProperty("emailHost");
        String user = env.getProperty("emailAuthUser");
        String pass = env.getProperty("emailAuthPass");
        if (host != null && user != null && pass != null) {
            return new EmailNotifier(host, user, pass, notificationEventRepository);
        }
        // return a stub implementation if not configured
        return (rotation, rotationInterval, person, startDate, endDate) -> { };
    }

    public static void main(String[] args) {
        SpringApplication.run(RotationApplication.class, args);
    }
}
