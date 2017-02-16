(function () {
  'use strict';

  angular.module('BlurAdmin.pages.datasource')
      .controller('DataSourceCtrl', DataSourceCtrl);

  /** @ngInject */
  function DataSourceCtrl($scope, toastr, DataSourceService) {
   var vm = this;
   vm.rowCollectionTypes = [];
   vm.rowCollection = [];

   DataSourceService.getAllDataSourceTypes().success(
      function(data, status, headers, config) {
 
          vm.rowCollectionTypes = angular.fromJson(data);
          toastr.success("Dados carregados", 'Sucesso!');
 
      }).error(function(data, status, headers, config) {         
 
        toastr.error("Erro ao carregar os dados das Fontes de Dados", 'Erro!'); //success,info,warning
 
    });

    DataSourceService.getAllDataSource().success(
      function(data, status, headers, config) {
 
          vm.rowCollection = angular.fromJson(data);
 
      }).error(function(data, status, headers, config) {         
 
        toastr.error("Erro ao carregar os dados das Fontes de Dados", 'Erro!'); //success,info,warning
 
    });

  }

})();

