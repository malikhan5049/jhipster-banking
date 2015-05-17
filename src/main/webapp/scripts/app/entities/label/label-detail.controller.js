'use strict';

angular.module('bankingApp')
    .controller('LabelDetailController', function ($scope, $stateParams, Label, Operation) {
        $scope.label = {};
        $scope.load = function (id) {
            Label.get({id: id}, function(result) {
              $scope.label = result;
            });
        };
        $scope.load($stateParams.id);
    });
