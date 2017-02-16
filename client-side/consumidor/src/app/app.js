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
    'BlurAdmin.pages'

  ])
  .run(function($rootScope, $state, $location, $http ) {
    
      $rootScope.$on('$stateChangeStart', function(evt, to, params) {
          if (to.redirectTo) {
              evt.preventDefault();
              $state.go(to.redirectTo, params, {
                  location: 'replace'
              });
          }
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
  ]);
