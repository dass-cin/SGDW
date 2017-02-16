(function () {
    'use strict';

    angular
        .module('BlurAdmin.authService', [])
        .factory('AuthenticationService', AuthenticationService);

    AuthenticationService.$inject = ['$rootScope', '$http', '$location'];

    function AuthenticationService ($rootScope, $http, $location) {

         var service = {
            Login: _Login,
            isLoggedOn: _isLoggedOn,
            checarUsuario: _checarUsuario
        };

        return service; 

        function _Login(usuario, callback) {
                $http({
                    url: apiUrl() + 'admin/login',
                    method: 'POST',
                    data: usuario,
                    transformResponse:[function(data){
                        return data;
                    }]
                }).then(

                function (response) {
                    var token = JSON.parse(response.data).codigo;
                    
                    if (response.data != "0" && response.data != undefined && response.data != "") {
                        window.sessionStorage.setItem("currentUser", JSON.stringify({ username: usuario.usuario, codigo: token}));
                        
                        // add jwt token to auth header for all requests made by the $http service
                        $http.defaults.headers.common.Authorization = 'Bearer ' + token;

                        callback(true);
                    }else{
                        callback(false);
                    }     
                }, function() {
                    callback(false);
                });
        }

        function _isLoggedOn(){
            var login = false;
            if (window.sessionStorage.getItem("currentUser")) {
              login = true;
            } 
            return login;
        }

        function _checarUsuario(){
            $http({
                url: apiUrl() + 'admin/checar_token_usuario',
                method: 'POST',
                data: getCurrentUser(),
                transformResponse:[function(data){
                    return data;
                }]
            }).then(

                function (response) {

                    var retorno = response.data;
                    if (retorno != "true" && retorno != true) {
                        $location.path('/logout'); 
                    }                    

                }, function() {
                    $location.path('/logout');
                });
        };     

    }
})();