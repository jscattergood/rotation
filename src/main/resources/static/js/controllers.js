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

app.controller('configurationTabCtrl', ['$scope', '$location',
    function ($scope, $location) {
        $scope.scheduleTabActive = function () {
            return $location.path() === '/schedule' ? "active" : "";
        };
        $scope.peopleTabActive = function () {
            return $location.path() === '/people' ? "active" : "";
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
        (function getCurrent() {
            Schedule.current.query(function (data) {
                $scope.current = data;
                $timeout(getCurrent, 30000);
            });
        })();
        (function getNext() {
            Schedule.next.query(function (data) {
                $scope.next = data;
                $timeout(getNext, 30000);
            });
        })();
    }]);

app.controller('peopleCtrl', ['$scope', 'Person',
    function ($scope, Person) {
        Person.query(function (data) {
            $scope.gridOptions.data = data;
        });

        $scope.gridOptions = {
            enableRowSelection: true,
            enableSelectAll: true,
            enableFiltering: true,
            enableCellEditOnFocus: true
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
                        $scope.gridOptions.data.splice($scope.gridOptions.data.indexOf(row), 1);
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
            enableFiltering: true,
            enableCellEditOnFocus: true
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
                        $scope.gridOptions.data.splice($scope.gridOptions.data.indexOf(row), 1);
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
                {field: 'id', name: '', cellEditableCondition: false, cellTemplate: 'templates/edit-button.html', width: 34 },
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
                        $scope.gridOptions.data.splice($scope.gridOptions.data.indexOf(row), 1);
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
                $scope.gridApi.rowEdit.setSavePromise(row, rotation.$save(function (result) {
                    row.id = result.id;
                }));
            }
        };

        $scope.editRow = function (grid, row) {
            $uibModal.open({
                templateUrl: 'templates/rotationMembers.html',
                controller: 'rotationMembersCtrl',
                resolve: {
                    parentGrid: function () { return grid; },
                    parentRow: function () { return row; }
                }
            });
        };

        $scope.gridOptions.onRegisterApi = function (gridApi) {
            $scope.gridApi = gridApi;
            gridApi.rowEdit.on.saveRow($scope, $scope.saveRow);
        };
    }]);

app.controller('rotationMembersCtrl', ['$scope',
                                        '$uibModal',
                                        '$uibModalInstance',
                                        'Person',
                                        'Rotation',
                                        'parentGrid',
                                        'parentRow',
    function ($scope, $uibModal, $uibModalInstance, Person, Rotation, parentGrid, parentRow) {
        $scope.rotation = new Rotation(parentRow.entity);
        $scope.gridOptions = {
            enableRowSelection: true,
            enableSelectAll: true,
            enableFiltering: true,
            enableCellEditOnFocus: true,
            rowHeight: 38
        };

        Person.query(function (data) {
            $scope.people = data;

            $scope.gridOptions.columnDefs = [
                {field: 'id', name: '', cellEditableCondition: false, cellTemplate: 'templates/edit-button.html', width: 34 },
                {name: 'sequence', cellEditableCondition: true, type: 'number'},
                {name: 'person', cellEditableCondition: true, type: 'object',
                    cellFilter: "griddropdown:editDropdownOptionsArray:editDropdownIdLabel:editDropdownValueLabel:row.entity.person.fullName",
                    enableCellEdit: true,
                    editType: 'dropdown',
                    editDropdownIdLabel: 'id',
                    editDropdownValueLabel: 'fullName',
                    editableCellTemplate: 'ui-grid/dropdownEditor',
                    editDropdownOptionsArray: $scope.people
                }
            ];
            if (!$scope.rotation.members) {
                $scope.rotation.members = [];
            }
            $scope.gridOptions.data = $scope.rotation.members;
        });

        $scope.addNewItem = function () {
            $scope.gridOptions.data.push({sequence: $scope.gridOptions.data.length + 1, person: ''});
        };

        $scope.deleteItem = function () {
            angular.forEach($scope.gridApi.selection.getSelectedRows(), function (row, index) {
                $scope.rotation.members.splice($scope.rotation.members.indexOf(row), 1);
                row.isSaving = true;
                $scope.gridApi.rowEdit.setSavePromise(row, $scope.rotation.$update(function (result) {
                    $scope.rotation = result;
                }));
            });
        };

        $scope.saveRow = function (row) {
            if (typeof row.person === 'number') {
                row.person = $scope.people.find(function (element) {
                    return element.id === row.person;
                });
            }
            row.rotation = { 'id' : parentRow.entity.id };
            if (!$scope.rotation.members) {
                $scope.rotation.members = [];
            }
            var index = $scope.rotation.members.indexOf(row);
            if (index !== -1) {
                $scope.rotation.members[index] = row;
            }
            else {
                $scope.rotation.members.push(row);
            }
            $scope.gridApi.rowEdit.setSavePromise(row, $scope.rotation.$update(function (result) {
                $scope.rotation = result;
                $scope.gridOptions.data = $scope.rotation.members;
            }));
        };

        $scope.editRow = function (grid, row) {
            $uibModal.open({
                templateUrl: 'templates/memberDelegates.html',
                controller: 'memberDelegatesCtrl',
                resolve: {
                    parentGrid: function () { return grid; },
                    parentRow: function () { return row; },
                    rotation: function() { return $scope.rotation; }
                }
            });
        };

        $scope.close = function () {
            parentRow.entity = angular.extend(parentRow.entity, $scope.rotation);
            $uibModalInstance.close(parentRow.entity);
        };

        $scope.gridOptions.onRegisterApi = function (gridApi) {
            $scope.gridApi = gridApi;
            gridApi.rowEdit.on.saveRow($scope, $scope.saveRow);
        };
    }]);

app.controller('memberDelegatesCtrl', ['$scope', '$uibModalInstance', 'Person', 'parentGrid', 'parentRow', 'rotation',
    function ($scope, $uibModalInstance, Person, parentGrid, parentRow, rotation) {
        $scope.rotation = rotation;
        $scope.member = angular.copy(parentRow.entity);
        $scope.gridOptions = {
            enableRowSelection: true,
            enableSelectAll: true,
            enableFiltering: true,
            enableCellEditOnFocus: true
        };


        Person.query(function (data) {
            $scope.people = data;

            $scope.gridOptions.columnDefs = [
                {name: 'delegate', cellEditableCondition: true, type: 'object',
                    cellFilter: "griddropdown:editDropdownOptionsArray:editDropdownIdLabel:editDropdownValueLabel:row.entity.person.fullName",
                    enableCellEdit: true,
                    editType: 'dropdown',
                    editDropdownIdLabel: 'id',
                    editDropdownValueLabel: 'fullName',
                    editableCellTemplate: 'ui-grid/dropdownEditor',
                    editDropdownOptionsArray: $scope.people
                },
                {name: 'startDate', cellEditableCondition: true, type: 'date', cellFilter: 'date:"yyyy-MM-dd"'},
                {name: 'endDate', cellEditableCondition: true, type: 'date', cellFilter: 'date:"yyyy-MM-dd"'}
            ];

            if (!$scope.member.delegates) {
                $scope.member.delegates = [];
            }
            $scope.gridOptions.data = $scope.member.delegates;
        });

        $scope.addNewItem = function () {
            $scope.gridOptions.data.push({delegate: '', startDate: '', endDate: ''});
        };

        function update(row) {
            var member = $scope.rotation.members.find(function (element) {
                return element.id === parentRow.entity.id;
            });
            member.delegates = angular.copy($scope.member.delegates);
            $scope.gridApi.rowEdit.setSavePromise(row, $scope.rotation.$update(function (result) {
                $scope.rotation = result;
                $scope.member = $scope.rotation.members.find(function (element) {
                    return element.id === $scope.member.id;
                });
                $scope.gridOptions.data = $scope.member.delegates;
            }));
        }

        $scope.deleteItem = function () {
            angular.forEach($scope.gridApi.selection.getSelectedRows(), function (row, index) {
                $scope.member.delegates.splice($scope.member.delegates.indexOf(row), 1);
                row.isSaving = true;
                update(row);
            });
        };

        $scope.saveRow = function (row) {
            if (typeof row.delegate === 'number') {
                row.delegate = $scope.people.find(function (element) {
                    return element.id === row.delegate;
                });
            }
            row.member = { 'id' : parentRow.entity.id };
            if (!$scope.member.delegates) {
                $scope.member.delegates = [];
            }
            var index = $scope.member.delegates.indexOf(row);
            if (index !== -1) {
                $scope.member.delegates[index] = row;
            }
            else {
                $scope.member.delegates.push(row);
            }
            update(row);
        };

        $scope.close = function () {
            parentRow.entity = angular.extend(parentRow.entity, $scope.member);
            $uibModalInstance.close(parentRow.entity);
        };

        $scope.gridOptions.onRegisterApi = function (gridApi) {
            $scope.gridApi = gridApi;
            gridApi.rowEdit.on.saveRow($scope, $scope.saveRow);
        };
    }]);

app.controller('notificationsCtrl', ['$scope', 'Notification',
    function ($scope, Notification) {
        Notification.query(function (data) {
            $scope.gridOptions.data = data;
        });

        $scope.gridOptions = {
            enableRowSelection: false,
            enableSelectAll: false,
            enableFiltering: true,
            enableCellEditOnFocus: false
        };

        $scope.gridOptions.columnDefs = [
            {name: 'rotationId'},
            {name: 'rotationInterval'},
            {name: 'personId'},
            {name: 'emailAddress'},
            {name: 'notificationTime', type: 'date', cellFilter: 'date:"yyyy-MM-dd HH:mm"'}
        ];

    }]);