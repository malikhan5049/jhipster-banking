'use strict';

angular.module('bankingApp')
    .controller('BankAccountDetailController', function ($scope, $stateParams, BankAccount, User, Operation) {
        $scope.bankAccount = {};
        $scope.load = function (id) {
            BankAccount.get({id: id}, function(result) {
              $scope.bankAccount = result;
            });
        };
        $scope.load($stateParams.id);
    });
