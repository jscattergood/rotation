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
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="https://github.com/jscattergood">John Scattergood</a> 2/16/2016
 */
@Entity
public class RotationMember {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne(optional = false)
    private Rotation rotation;

    @ManyToOne(optional = false)
    private Person person;

    @Column(nullable = false)
    private Integer sequence;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "member")
    private List<RotationDelegate> delegate = new ArrayList<>();

    protected RotationMember() {
    }

    public RotationMember(Rotation rotation, Person person, Integer sequence) {
        this.rotation = rotation;
        this.person = person;
        this.sequence = sequence;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @JsonIgnore
    public Rotation getRotation() {
        return rotation;
    }

    @JsonProperty("rotation")
    public void setRotation(Rotation rotation) {
        this.rotation = rotation;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public List<RotationDelegate> getDelegate() {
        return delegate;
    }

    public void setDelegate(List<RotationDelegate> delegate) {
        this.delegate = delegate;
    }
}
