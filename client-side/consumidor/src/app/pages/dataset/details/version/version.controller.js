(function () {
  'use strict';

  angular
    .module('BlurAdmin.pages.dataset')
    .controller('DatasetDetailsVersionCtrl', DatasetDetailsVersionCtrl);

    function DatasetDetailsVersionCtrl($stateParams, $scope, DatasetService, toastr) {

    var vm = this;
    vm.datasetURI             = $stateParams.collector_dataset;
    vm.version                = $stateParams.version;
    vm.download               = {};
    vm.api                    = {};
    vm.linkUri                = getUriDownload();
    vm.rowCollection          = [];    
    vm.dataVersion            = [];
    vm.rowCollectionPageSize  = 1;   

    DatasetService.getPreservacao(vm.datasetURI).success(
      function(data) {      
          if (data==true) {
             window.location = "410.html";
          } 
      }); 

    DatasetService.getAllFormats().success(
      function(data, status, headers, config) {      

          vm.download = angular.fromJson(data);

      }).error(function(data, status, headers, config) {      

         toastr.error("Erro ao carregar os Formatos de dados", 'Erro!'); //success,info,warning
    });   

    function getUriDownload(){
      return apiUrl() + "open/" + vm.datasetURI + "/version/" + vm.version + "/format/";
    };

    DatasetService.getByIdDataset($stateParams.collector_dataset).success(
      function(data, status, headers, config) {
         
          vm.rowCollectionDetails = data;
          vm.datasetTitle = vm.rowCollectionDetails.dataset_title;
          vm.rowCollection.dataset_title = vm.datasetTitle;
          vm.rowCollection.contact_point = vm.rowCollectionDetails.contact_point;
          toastr.success("Dados carregados", 'Sucesso!');

      }).error(function(data, status, headers, config) {      

         toastr.error("Erro ao carregar os dados dos Conjuntos de Dados", 'Erro!'); //success,info,warning

    });    

    
    DatasetService.getByVersion(vm.datasetURI, vm.version).success(
      function(data, status, headers, config) {
          
          vm.rowCollection = angular.fromJson(data);  
          toastr.success("Metadados da versão foram carregados", 'Sucesso!');

      }).error(function(data, status, headers, config) {         
        
        toastr.error("Erro ao carregar as versões do conjunto de dados", 'Erro!'); //success,info,warning
        
    });


  };


})();
