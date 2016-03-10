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

angular.module('app', [
        'ngRoute',
        'ui.grid',
        'ui.grid.edit',
        'ui.grid.cellNav',
        'ui.grid.rowEdit',
        'ui.grid.selection',
        'ui.bootstrap',
        'app.controllers',
        'app.directives',
        'app.filters',
        'app.services'
    ])
    .config(function ($routeProvider) {
        $routeProvider
            .when('/configuration', {
                templateUrl: 'templates/people.html',
                controller: 'peopleCtrl'
            })
            .when('/rotations', {
                templateUrl: 'templates/rotations.html',
                controller: 'rotationsCtrl'
            })
            .when('/roles', {
                templateUrl: 'templates/roles.html',
                controller: 'rolesCtrl'
            })
            .when('/people', {
                templateUrl: 'templates/people.html',
                controller: 'peopleCtrl'
            })
            .when('/schedule', {
                templateUrl: 'templates/schedule.html',
                controller: 'scheduleCtrl'
            })
            .otherwise('/schedule');
    });