'use strict';

angular.module('bankingApp')
    .controller('OperationController', function ($scope, Operation, BankAccount, Label, ParseLinks) {
        $scope.operations = [];
        $scope.bankaccounts = BankAccount.query();
        $scope.labels = Label.query();
        $scope.page = 1;
        $scope.loadAll = function() {
            Operation.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.operations = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.create = function () {
            Operation.update($scope.operation,
                function () {
                    $scope.loadAll();
                    $('#saveOperationModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            Operation.get({id: id}, function(result) {
                $scope.operation = result;
                $('#saveOperationModal').modal('show');
            });
        };

        $scope.delete = function (id) {
            Operation.get({id: id}, function(result) {
                $scope.operation = result;
                $('#deleteOperationConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Operation.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteOperationConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.clear = function () {
            $scope.operation = {date: null, description: null, amount: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
