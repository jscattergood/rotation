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

package personal.rotation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import personal.rotation.domain.Rotation;
import personal.rotation.repository.RotationRepository;

import java.util.List;

/**
 * @author <a href="https://github.com/jscattergood">John Scattergood</a> 2/17/2016
 */
@Service
@Transactional
@RestController
public class RotationService {
    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    RotationRepository rotationRepository;

    @RequestMapping("/rotations")
    public List<Rotation> getRotations() {
        return rotationRepository.findAll();
    }

    @RequestMapping("/rotations/{id}")
    public Rotation getRotation(@PathVariable("id") Integer id) {
        return rotationRepository.findOne(id);
    }

    @RequestMapping(value = "/rotations", method = RequestMethod.POST)
    public Rotation createRotation(@RequestBody Rotation rotation) {
        return rotationRepository.save(rotation);
    }

    @RequestMapping(value = "/rotations/{id}", method = RequestMethod.PUT)
    public Rotation updateRotation(@PathVariable("id") Integer id, @RequestBody Rotation rotation) {
        rotation.setId(id);
        return rotationRepository.save(rotation);
    }

    @RequestMapping(value = "/rotations/{id}", method = RequestMethod.DELETE)
    public void deletePerson(@PathVariable("id") Integer id) {
        Rotation person = rotationRepository.findOne(id);
        if (person != null ){
            rotationRepository.delete(id);
        }
    }

}
