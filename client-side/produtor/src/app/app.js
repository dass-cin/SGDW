'use strict';

angular
  .module('BlurAdmin', [
    'ngAnimate',
    'ui.bootstrap',
    'ui.sortable',
    'ui.router',
    'ngTouch',
    'toastr',
    'smart-table',
    "xeditable",
    'ui.slimscroll',
    'ngJsTree',
    'angular-progress-button-styles',

    'BlurAdmin.theme',
    'BlurAdmin.pages',
    'BlurAdmin.login',
    'BlurAdmin.authService',

  ])
  .run(function($rootScope, $state, $location, $http, AuthenticationService ) {
    
      $rootScope.isloggedon = AuthenticationService.isLoggedOn();
     
      // keep user logged in after page refresh
      if (window.sessionStorage.currentUser) {
          $http.defaults.headers.common.Authorization = 'Bearer ' + window.sessionStorage.currentUser.codigo;
      }

      // redirect to login page if not logged in and trying to access a restricted page
      $rootScope.$on('$locationChangeStart', function (event, next, current) {
          var publicPages = ['/login'];
          var restrictedPage = publicPages.indexOf($location.path()) === -1;
          if (restrictedPage && !window.sessionStorage.currentUser) {
              $location.path('/login');
          } else if (restrictedPage && window.sessionStorage.currentUser) {
            AuthenticationService.checarUsuario();
          }
      });

      $rootScope.$on('$stateChangeStart', function(evt, to, params) {
          if (to.redirectTo) {
              evt.preventDefault();
              $state.go(to.redirectTo, params, {
                  location: 'replace'
              });
          }
          $rootScope.isloggedon = AuthenticationService.isLoggedOn();
      });

  })

  .directive('updateTitle', ['$rootScope', '$timeout',
    function($rootScope, $timeout) {
      return {
        link: function(scope, element) {

          var listener = function(event, toState) {

            var title = 'SGDW';
            if (toState.data && toState.data.pageTitle) title = toState.data.pageTitle + " | SGDW";

            $timeout(function() {
              element.text(title);
            }, 0, false);
          };

          $rootScope.$on('$stateChangeSuccess', listener);
          
        }
      };
    }
  ])
  .directive('back', ['$window', function($window) {
        return {
            restrict: 'A',
            link: function (scope, elem, attrs) {
                elem.bind('click', function () {
                    $window.history.back();
                });
            }
        };
    }]);
