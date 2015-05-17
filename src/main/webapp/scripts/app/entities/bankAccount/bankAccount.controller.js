'use strict';

angular.module('bankingApp')
    .controller('BankAccountController', function ($scope, BankAccount, User, Operation) {
        $scope.bankAccounts = [];
        $scope.users = User.query();
        $scope.operations = Operation.query();
        $scope.loadAll = function() {
            BankAccount.query(function(result) {
               $scope.bankAccounts = result;
            });
        };
        $scope.loadAll();

        $scope.create = function () {
            BankAccount.update($scope.bankAccount,
                function () {
                    $scope.loadAll();
                    $('#saveBankAccountModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            BankAccount.get({id: id}, function(result) {
                $scope.bankAccount = result;
                $('#saveBankAccountModal').modal('show');
            });
        };

        $scope.delete = function (id) {
            BankAccount.get({id: id}, function(result) {
                $scope.bankAccount = result;
                $('#deleteBankAccountConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            BankAccount.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteBankAccountConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.clear = function () {
            $scope.bankAccount = {name: null, balance: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
