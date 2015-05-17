'use strict';

angular.module('bankingApp')
    .controller('OperationDetailController', function ($scope, $stateParams, Operation, BankAccount, Label) {
        $scope.operation = {};
        $scope.load = function (id) {
            Operation.get({id: id}, function(result) {
              $scope.operation = result;
            });
        };
        $scope.load($stateParams.id);
    });
