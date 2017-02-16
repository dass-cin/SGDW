(function () {
  'use strict';

  angular.module('BlurAdmin.pages.dataset')
      .controller('DatasetListCtrl', DatasetListCtrl);

  /** @ngInject */
  function DatasetListCtrl($scope, $filter, editableOptions, editableThemes, $http, DatasetService, toastr) {

    var vm = this;
    vm.rowCollection2 = [];    
    vm.rowCollection = [];    
    vm.getters = [];

    vm.rowCollectionPageSize = 1;
    
    DatasetService.getAllDatasets().success(
      function(data, status, headers, config) {
          
          vm.rowCollection = angular.fromJson(data);  
          toastr.success("Dados carregados", 'Sucesso!');

      }).error(function(data, status, headers, config) {         
        
        toastr.error("Erro ao carregar os dados dos Conjuntos de Dados", 'Erro!'); //success,info,warning
        
    });

    vm.getters={
        collector_dataset: function (value) {
            //this will sort by the length of the first name string
            return value.collector_dataset.length;
        }
    }
    

  };

})();
