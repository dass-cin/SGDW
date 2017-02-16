(function() {
    'use strict';

    angular.module('BlurAdmin.login', [])
        .config(routeConfig);

    /** @ngInject */
    function routeConfig($stateProvider) {
        $stateProvider
            .state('login', {
                url: '/login',
                templateUrl: 'app/pages/login/login.view.html',
                controller: 'LoginCtrl',
                controllerAs: 'vm',
                data : { pageTitle: 'Login' }
            })
            .state('logout', {
                url: "/logout",
                templateUrl: '',
                controller: function ($http, $location, AuthenticationService) {
                    // If we got here from a url of /contacts/42
                    window.sessionStorage.removeItem("currentUser");
                    $http.defaults.headers.common.Authorization = '';
                    window.location = '/';
                    console.log("Logout");
            }});
    }
})();
