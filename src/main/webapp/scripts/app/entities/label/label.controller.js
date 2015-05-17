'use strict';

angular.module('bankingApp')
    .controller('LabelController', function ($scope, Label, Operation) {
        $scope.labels = [];
        $scope.operations = Operation.query();
        $scope.loadAll = function() {
            Label.query(function(result) {
               $scope.labels = result;
            });
        };
        $scope.loadAll();

        $scope.create = function () {
            Label.update($scope.label,
                function () {
                    $scope.loadAll();
                    $('#saveLabelModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            Label.get({id: id}, function(result) {
                $scope.label = result;
                $('#saveLabelModal').modal('show');
            });
        };

        $scope.delete = function (id) {
            Label.get({id: id}, function(result) {
                $scope.label = result;
                $('#deleteLabelConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Label.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteLabelConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.clear = function () {
            $scope.label = {label: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
