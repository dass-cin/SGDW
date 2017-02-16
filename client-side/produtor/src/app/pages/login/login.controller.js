(function () {
    'use strict';

    angular
        .module('BlurAdmin.login')
        .controller('LoginCtrl', LoginCtrl);

    function LoginCtrl($location, AuthenticationService) {
        var vm = this;

        vm.login = login;

        function login() {
            AuthenticationService.Login(vm, function (result) {
                if (result === true) {
                    $location.path('/dashboard');
                } else {
                    vm.error = 'Usuário ou senha incorretos';
                }
            });
        };
    }

})();