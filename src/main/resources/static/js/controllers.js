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
        $scope.scheduleTabActive = function () {
            return $location.path() === '/schedule' ? "active" : "";
        };
        $scope.personsTabActive = function () {
            return $location.path() === '/persons' ? "active" : "";
        };
        $scope.rolesTabActive = function () {
            return $location.path() === '/roles' ? "active" : "";
        };
        $scope.rotationsTabActive = function () {
            return $location.path() === '/rotations' ? "active" : "";
        };
    }]);

app.controller('scheduleCtrl', ['$scope', '$timeout', 'Schedule',
    function ($scope, $timeout, Schedule) {
        (function callService() {
            Schedule.get(function (data) {
                $scope.schedule = data;
                $timeout(callService, 30000);
            });
        })();
    }]);

app.controller('personsCtrl', ['$scope', 'Person',
    function ($scope, Person) {
        Person.query(function (data) {
            $scope.gridOptions.data = data;
        });

        $scope.gridOptions = {
            enableRowSelection: true,
            enableSelectAll: true,
            enableFiltering: true
        };

        $scope.gridOptions.columnDefs = [
            {name: 'firstName', cellEditableCondition: true},
            {name: 'lastName', cellEditableCondition: true},
            {name: 'email', cellEditableCondition: true}
        ];

        $scope.addNewItem = function () {
            $scope.gridOptions.data.push({firstName: '', lastName: '', email: ''});
        };

        $scope.deleteItem = function () {
            angular.forEach($scope.gridApi.selection.getSelectedRows(), function (row, index) {
                var person = new Person(row);
                if (person.id) {
                    row.isSaving = true;
                    $scope.gridApi.rowEdit.setSavePromise(row, person.$remove(function () {
                        $scope.gridOptions.data.splice($scope.gridOptions.data.lastIndexOf(row), 1);
                    }));
                }
            });
        };

        $scope.saveRow = function (row) {
            var person = new Person(row);
            if (person.id) {
                $scope.gridApi.rowEdit.setSavePromise(row, person.$update());
            }
            else {
                $scope.gridApi.rowEdit.setSavePromise(row, person.$save(function (result) {
                    row.id = result.id;
                }));
            }
        };

        $scope.gridOptions.onRegisterApi = function (gridApi) {
            $scope.gridApi = gridApi;
            gridApi.rowEdit.on.saveRow($scope, $scope.saveRow);
        };
    }]);

app.controller('rolesCtrl', ['$scope', 'Role',
    function ($scope, Role) {
        Role.query(function (data) {
            $scope.gridOptions.data = data;
        });

        $scope.gridOptions = {
            enableRowSelection: true,
            enableSelectAll: true,
            enableFiltering: true
        };

        $scope.gridOptions.columnDefs = [
            {name: 'name', cellEditableCondition: true}
        ];

        $scope.addNewItem = function () {
            $scope.gridOptions.data.push({name: ''});
        };

        $scope.deleteItem = function () {
            angular.forEach($scope.gridApi.selection.getSelectedRows(), function (row, index) {
                var role = new Role(row);
                if (role.id) {
                    row.isSaving = true;
                    $scope.gridApi.rowEdit.setSavePromise(row, role.$remove(function () {
                        $scope.gridOptions.data.splice($scope.gridOptions.data.lastIndexOf(row), 1);
                    }));
                }
            });
        };

        $scope.saveRow = function (row) {
            var role = new Role(row);
            if (role.id) {
                $scope.gridApi.rowEdit.setSavePromise(row, role.$update());
            }
            else {
                $scope.gridApi.rowEdit.setSavePromise(row, role.$save(function (result) {
                    row.id = result.id;
                }));
            }
        };

        $scope.gridOptions.onRegisterApi = function (gridApi) {
            $scope.gridApi = gridApi;
            gridApi.rowEdit.on.saveRow($scope, $scope.saveRow);
        };
    }]);

app.controller('rotationsCtrl', ['$scope', '$uibModal', 'Rotation', 'Role',
    function ($scope, $uibModal, Rotation, Role) {
        $scope.gridOptions = {
            enableRowSelection: true,
            enableSelectAll: true,
            enableFiltering: true,
            rowHeight: 38
        };

        Role.query(function (data) {
            $scope.roles = data;

            Rotation.query(function (data) {
                $scope.gridOptions.data = data;
            });

            $scope.gridOptions.columnDefs = [
                {field: 'id', name: '', cellTemplate: 'templates/edit-button.html', width: 34 },
                {name: 'name', cellEditableCondition: true},
                {name: 'role', cellEditableCondition: true, type: 'object',
                    cellFilter: "griddropdown:editDropdownOptionsArray:editDropdownIdLabel:editDropdownValueLabel:row.entity.role.name",
                    enableCellEdit: true,
                    editType: 'dropdown',
                    editDropdownIdLabel: 'id',
                    editDropdownValueLabel: 'name',
                    editableCellTemplate: 'ui-grid/dropdownEditor',
                    editDropdownOptionsArray: $scope.roles
                },
                {name: 'startDate', cellEditableCondition: true, type: 'date', cellFilter: 'date:"yyyy-MM-dd"'},
                {name: 'interval', cellEditableCondition: true, type: 'number'}
            ];

        });

        $scope.addNewItem = function () {
            $scope.gridOptions.data.push({name: ''});
        };

        $scope.deleteItem = function () {
            angular.forEach($scope.gridApi.selection.getSelectedRows(), function (row) {
                var rotation = new Rotation(row);
                if (rotation.id) {
                    row.isSaving = true;
                    $scope.gridApi.rowEdit.setSavePromise(row, rotation.$remove(function () {
                        $scope.gridOptions.data.splice($scope.gridOptions.data.lastIndexOf(row), 1);
                    }));
                }
            });
        };

        $scope.saveRow = function (row) {
            if (typeof row.role === 'number') {
                row.role = $scope.roles.find(function (element) {
                    return element.id === row.role;
                });
            }
            var rotation = new Rotation(row);
            if (rotation.id) {
                $scope.gridApi.rowEdit.setSavePromise(row, rotation.$update());
            }
            else {
                $scope.gridApi.rowEdit.setSavePromise(row, rotation.$save());
            }
        };

        $scope.editRow = function (grid, row) {
            $uibModal.open({
                templateUrl: 'templates/rotationMembers.html',
                controller: 'rotationMembersCtrl',
                resolve: {
                    grid: function () { return grid; },
                    row: function () { return row; }
                }
            });
        };

        $scope.gridOptions.onRegisterApi = function (gridApi) {
            $scope.gridApi = gridApi;
            gridApi.rowEdit.on.saveRow($scope, $scope.saveRow);
        };
    }]);

app.controller('rotationMembersCtrl', ['$scope', '$uibModalInstance', 'Person', 'grid', 'row',
    function ($scope, $uibModalInstance, Person, grid, row) {
        $scope.rotation = angular.copy(row.entity);
        $scope.gridOptions = {
            enableRowSelection: true,
            enableSelectAll: true,
            enableFiltering: true
        };


        Person.query(function (data) {
            $scope.persons = data;

            $scope.gridOptions.columnDefs = [
                {name: 'sequence', cellEditableCondition: true, type: 'number'},
                {name: 'person', cellEditableCondition: true, type: 'object',
                    cellFilter: "griddropdown:editDropdownOptionsArray:editDropdownIdLabel:editDropdownValueLabel:row.entity.person.fullName",
                    enableCellEdit: true,
                    editType: 'dropdown',
                    editDropdownIdLabel: 'id',
                    editDropdownValueLabel: 'fullName',
                    editableCellTemplate: 'ui-grid/dropdownEditor',
                    editDropdownOptionsArray: $scope.persons
                }
            ];

            $scope.gridOptions.data = $scope.rotation.members ? $scope.rotation.members : [];
        });

        $scope.addNewItem = function () {
            $scope.gridOptions.data.push({firstName: '', person: ''});
        };

        $scope.deleteItem = function () {
            angular.forEach($scope.gridApi.selection.getSelectedRows(), function (row, index) {
                $scope.rotation.members.splice($scope.rotation.members.lastIndexOf(row), 1);
                row.isSaving = true;
                $scope.gridApi.rowEdit.setSavePromise(row, $scope.rotation.$update())
            });
        };

        $scope.saveRow = function (row) {
            if (typeof row.person === 'number') {
                row.person = $scope.persons.find(function (element) {
                    return element.id === row.person;
                });
            }
            row.rotation = { 'id' : $scope.rotation.id };
            if ($scope.rotation.members.indexOf(row) === -1) {
                $scope.rotation.members.push(row);
            }
            $scope.gridApi.rowEdit.setSavePromise(row, $scope.rotation.$update());
        };

        $scope.close = function () {
            row.entity = angular.extend(row.entity, $scope.rotation);
            $uibModalInstance.close(row.entity);
        };

        $scope.gridOptions.onRegisterApi = function (gridApi) {
            $scope.gridApi = gridApi;
            gridApi.rowEdit.on.saveRow($scope, $scope.saveRow);
        };
    }]);
