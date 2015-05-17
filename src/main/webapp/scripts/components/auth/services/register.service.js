'use strict';

angular.module('bankingApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


