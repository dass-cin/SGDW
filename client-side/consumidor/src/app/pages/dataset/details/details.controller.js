  (function () {
  'use strict';

  angular
    .module('BlurAdmin.pages.dataset')
    .controller('DatasetDetailsCtrl', DatasetDetailsCtrl);

    function DatasetDetailsCtrl($stateParams, $scope, DatasetService, toastr) {

    var vm = this;
    vm.datasetURI             = $stateParams.collector_dataset;
    vm.download               = {};
    vm.api                    = {};
    vm.linkMetadadosApi       = getApiMetadados();
    vm.linkUri                = getUriDownload();
    vm.datasetAtualizar       = {};
    vm.linkVersoes            = getApiVersoes();
    vm.atualizarDatasetSubmit = atualizar;
    vm.rowCollection          = [];   

    DatasetService.getPreservacao(vm.datasetURI).success(
      function(data) {     
        console.log(data); 
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
      return apiUrl() + "open/" + vm.datasetURI + "/format/";
    };

    function getApiMetadados(){
      return apiUrl() + "open/" + vm.datasetURI + "/about/";
    }

    function getApiVersoes() {
      return apiUrl() + "open/" + vm.datasetURI + "/list_versions";
    }

    DatasetService.getByIdDataset($stateParams.collector_dataset).success(
      function(data, status, headers, config) {
         
          vm.rowCollectionDetails = data;
          vm.proxAtualizacao = vm.rowCollectionDetails.next_update;
          vm.datasetTitle = vm.rowCollectionDetails.dataset_title;
          toastr.success("Dados carregados", 'Sucesso!');

      }).error(function(data, status, headers, config) {      

         toastr.error("Erro ao carregar os dados dos Conjuntos de Dados", 'Erro!'); //success,info,warning

    });    

    function atualizar() {
      vm.datasetAtualizar.datasetUri = vm.datasetURI;
      vm.datasetAtualizar.codigo = JSON.parse(getCurrentUser()).codigo; //add token

      DatasetService.setAtualizarDataset(vm.datasetAtualizar,function (result) {
          if (result === true) {
            toastr.info("Conjunto de dados ATUALIZADO com sucesso!", 'Sucesso!');
            $location.path('/dataset/'+ vm.datasetURI);
          } else {
              toastr.error("Erro ao Atualizar Conjunto de Dados!", 'Erro!'); //success,info,warning
          }
      });

    };

    
    DatasetService.getAllVersions(vm.datasetURI).success(
      function(data, status, headers, config) {
          
          vm.rowCollection = angular.fromJson(data); 
          toastr.success("Versões carregadas", 'Sucesso!');

      }).error(function(data, status, headers, config) {         
        
        toastr.error("Erro ao carregar as versões do conjunto de dados", 'Erro!'); //success,info,warning
        
    });


  };


})();
