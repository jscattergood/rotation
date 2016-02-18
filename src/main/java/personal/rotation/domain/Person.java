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
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="https://github.com/jscattergood">John Scattergood</a> 2/16/2016
 */
@Entity
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column(nullable = false)
    private String firstName;
    private String lastName;
    @Column(nullable = false)
    private String email;
    @ManyToMany
    private List<Role> potentialRoles = new ArrayList<>();

    protected Person() {
    }

    public Person(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Role> getPotentialRoles() {
        return potentialRoles;
    }

    public void setPotentialRoles(List<Role> roles) {
        if (roles == null) {
            this.potentialRoles.clear();
        }
        else {
            this.potentialRoles = roles;
        }
    }

    public void addPotentialRole(Role role) {
        if (role != null && !getPotentialRoles().contains(role)) {
                getPotentialRoles().add(role);
        }
    }
}
