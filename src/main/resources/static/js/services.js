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

var app = angular.module('app.services', ['ngResource']);

app.factory('scheduleService', ['$resource', function ($resource) {
    return $resource('/schedule/');
}]);

app.factory('rotationService', ['$resource', function ($resource) {
    return {
        rotations: $resource('/rotations/'),
        rotation: $resource('/rotation/:rotationId', { rotationId: '@id' })
    };
}]);

app.factory('personService', ['$resource', function ($resource) {
    return {
        persons: $resource('/persons/'),
        person: $resource('/person/:personId', { personId:'@id' })
    };
}]);

app.factory('roleService', ['$resource', function ($resource) {
    return {
        roles: $resource('/roles/'),
        role: $resource('/role/:roleId', { roleId: '@id'})
    };
}]);