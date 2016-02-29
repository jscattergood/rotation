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

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.*;

/**
 * @author <a href="https://github.com/jscattergood">John Scattergood</a> 2/16/2016
 */
@Entity
public class Rotation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column(nullable = false)
    private String name;
    @ManyToOne(optional = false)
    private Role role;
    @Column(nullable = false)
    private Date startDate;
    @Column(nullable = false)
    private Integer interval;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "rotation", orphanRemoval = true)
    private List<RotationMember> members = new ArrayList<>();

    protected Rotation() {
    }

    public Rotation(String name, Role role, Date startDate, Integer interval) {
        this.name = name;
        this.role = role;
        this.startDate = startDate;
        this.interval = interval;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Integer getInterval() {
        return interval;
    }

    public void setInterval(Integer interval) {
        this.interval = interval;
    }

    public List<RotationMember> getMembers() {
        return members;
    }

    public void setMembers(List<RotationMember> members) {
        this.members = members;
    }

    @JsonIgnore
    public Map<String, Object> getAttributes() {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("id", getId());
        attributes.put("name", getName());
        attributes.put("role", getRole());
        return attributes;
    }
}

