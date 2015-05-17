'use strict';

angular.module('bankingApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('operation', {
                parent: 'entity',
                url: '/operation',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'bankingApp.operation.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/operation/operations.html',
                        controller: 'OperationController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('operation');
                        return $translate.refresh();
                    }]
                }
            })
            .state('operationDetail', {
                parent: 'entity',
                url: '/operation/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'bankingApp.operation.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/operation/operation-detail.html',
                        controller: 'OperationDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('operation');
                        return $translate.refresh();
                    }]
                }
            });
    });
