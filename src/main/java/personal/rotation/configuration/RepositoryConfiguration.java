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

package personal.rotation.configuration;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import personal.rotation.reactive.ObservableTxFactory;

/**
 * @author <a href="https://github.com/jscattergood">John Scattergood</a> 2/16/2016
 */
@Configuration
@EnableAutoConfiguration
@EntityScan(basePackages = {"personal.rotation.domain"})
@EnableJpaRepositories(basePackages = {"personal.rotation.repository"})
@EnableTransactionManagement
public class RepositoryConfiguration {
    @Bean
    ObservableTxFactory observableTxFactory() {
        return new ObservableTxFactory();
    }
}