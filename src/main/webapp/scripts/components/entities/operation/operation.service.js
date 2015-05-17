'use strict';

angular.module('bankingApp')
    .factory('Operation', function ($resource) {
        return $resource('api/operations/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.date = new Date(data.date);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
