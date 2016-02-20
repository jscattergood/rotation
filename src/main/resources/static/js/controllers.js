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

var app = angular.module('app.controllers', []);

app.controller('headerCtrl', ['$scope',
    function ($scope) {
        function getWeek() {
            var today, todayNumber, lastSundayNbr, nextSundayNbr, lastSunday, nextSunday;
            today = new Date();
            todayNumber = today.getDay();
            lastSundayNbr = 0 - todayNumber;
            nextSundayNbr = 7 - todayNumber;
            lastSunday = new Date(today.getFullYear(), today.getMonth(), today.getDate() + lastSundayNbr);
            nextSunday = new Date(today.getFullYear(), today.getMonth(), today.getDate() + nextSundayNbr);
            return lastSunday.toDateString() + " - " + nextSunday.toDateString();
        }

        $scope.week = getWeek();
    }]);

app.controller('homeTabCtrl', ['$scope', '$location',
    function ($scope, $location) {
         $scope.scheduleTabActive = function() {
            return $location.path() === '/schedule' ? "active" : "";
        };
        $scope.personsTabActive = function() {
            return $location.path() === '/persons' ? "active" : "";
        };
        $scope.rolesTabActive = function() {
            return $location.path() === '/roles' ? "active" : "";
        };
        $scope.rotationsTabActive = function() {
            return $location.path() === '/rotations' ? "active" : "";
        };
    }]);

app.controller('scheduleCtrl', ['$scope', '$timeout', 'scheduleService',
    function ($scope, $timeout, scheduleService) {
        (function callService() {
            scheduleService.get(function (data) {
                $scope.schedule = data.schedule;
                $timeout(callService, 30000);
            });
        })();
    }]);

app.controller('personsCtrl', ['$scope', 'personService',
    function ($scope, personService) {
        personService.persons.query(function (data) {
            $scope.persons = data;
        });
    }]);

app.controller('rolesCtrl', ['$scope', 'roleService',
    function ($scope, roleService) {
        roleService.roles.query(function (data) {
            $scope.roles = data;
        });
    }]);

app.controller('rotationsCtrl', ['$scope', 'rotationService',
    function ($scope, rotationService) {
        rotationService.rotations.query(function (data) {
            $scope.rotations = data;
        });
    }]);