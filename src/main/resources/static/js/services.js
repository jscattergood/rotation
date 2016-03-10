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

app.factory('Schedule', ['$resource', function ($resource) {
    return { current: $resource('/schedule/current'),
             next: $resource('/schedule/next')
    };
}]);

app.factory('Rotation', ['$resource', function ($resource) {
    return $resource('/rotations/:rotationId', { rotationId: '@id' }, {
        update: {
            method: 'PUT'
        }
    });
}]);

app.factory('Person', ['$resource', function ($resource) {
    return $resource('/people/:personId', { personId:'@id' }, {
        update: {
            method: 'PUT'
        }
    });
}]);

app.factory('Role', ['$resource', function ($resource) {
    return $resource('/roles/:roleId', { roleId: '@id'}, {
        update: {
            method: 'PUT'
        }
    });
}]);