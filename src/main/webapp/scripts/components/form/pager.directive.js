/* globals $ */
'use strict';

angular.module('bankingApp')
    .directive('bankingAppPager', function() {
        return {
            templateUrl: 'scripts/components/form/pager.html'
        };
    });
