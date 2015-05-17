'use strict';

angular.module('bankingApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('label', {
                parent: 'entity',
                url: '/label',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'bankingApp.label.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/label/labels.html',
                        controller: 'LabelController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('label');
                        return $translate.refresh();
                    }]
                }
            })
            .state('labelDetail', {
                parent: 'entity',
                url: '/label/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'bankingApp.label.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/label/label-detail.html',
                        controller: 'LabelDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('label');
                        return $translate.refresh();
                    }]
                }
            });
    });
