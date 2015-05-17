/* globals $ */
'use strict';

angular.module('bankingApp')
    .directive('bankingAppPagination', function() {
        return {
            templateUrl: 'scripts/components/form/pagination.html'
        };
    });
