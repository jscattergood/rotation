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
import java.util.Date;
import java.util.Map;

/**
 * @author <a href="https://github.com/jscattergood">John Scattergood</a> 2/16/2016
 */
@Entity
public class RotationDelegate {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @ManyToOne(optional = false)
    private RotationMember member;
    @ManyToOne(optional = false)
    private Person delegate;
    private Date startDate;
    private Date endDate;

    protected RotationDelegate() {
    }

    public RotationDelegate(RotationMember member, Person delegate) {
        this.member = member;
        this.delegate = delegate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @JsonIgnore
    public RotationMember getMember() {
        return member;
    }

    @JsonProperty("member")
    public Map<String, Object> getMemberAttributes() {
        return member.getAttributes();
    }

    @JsonProperty("member")
    public void setMember(RotationMember member) {
        this.member = member;
    }

    public Person getDelegate() {
        return delegate;
    }

    public void setDelegate(Person delegate) {
        this.delegate = delegate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
