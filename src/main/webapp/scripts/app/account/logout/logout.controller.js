'use strict';

angular.module('bankingApp')
    .controller('LogoutController', function (Auth) {
        Auth.logout();
    });
