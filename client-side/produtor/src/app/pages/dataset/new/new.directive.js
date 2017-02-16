(function () {
  'use strict';

  angular.module('BlurAdmin.pages.dataset')
      .directive('tagInput', tagInput)
      .directive('forceBind', forceBind);

  /** @ngInject */
  function tagInput() {
    return {
      restrict: 'A',
      link: function( $scope, elem, attr) {
        $(elem).tagsinput({
          tagClass:  'label label-' + attr.tagInput
        });
      }
    };
  }
  //Executa antes de submeter o formulário
  function forceBind() {
    return {
      require: '^form',
      priority: -1,
      link: function (scope, element, attrs, form) {
        element.bind('submit', function() {
          if (form.$valid) {
            angular.forEach(form, function(value, key) {  
              if (value.hasOwnProperty('$modelValue')) {
                if (!value.$viewValue) {
                  value.$setViewValue("");
                }
              }
            });
          }
        });
      }
    };
  };

})();