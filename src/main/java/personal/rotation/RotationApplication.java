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
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import ro.isdc.wro.manager.factory.WroManagerFactory;

import java.util.Properties;

@SpringBootApplication
@EnableScheduling
public class RotationApplication {

    private static final String WRO_CONFIG = "wro.groovy";

    @Bean
    WroManagerFactory wroManagerFactory() {
        Properties configProperties = new Properties();
        return new GroovyWroManagerFactory(WRO_CONFIG, configProperties);
    }

    public static void main(String[] args) {
        SpringApplication.run(RotationApplication.class, args);
    }
}
