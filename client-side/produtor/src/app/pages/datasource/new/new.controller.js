(function () {
  'use strict';

  angular.module('BlurAdmin.pages.datasource')
      .controller('DataSourceNewCtrl', DataSourceNewCtrl);

  /** @ngInject */
  function DataSourceNewCtrl($scope, DataSourceService, toastr, $location) {
   var vm = this;

    vm.datasource = {};
    vm.datasourceSubmit = cadastrar;
    vm.standardItem = {};
    vm.availableOptions=[];

    DataSourceService.getAllDataSourceTypes().success(
      function(data, status, headers, config) {
 
          vm.availableOptions = angular.fromJson(data);
          toastr.success("Dados carregados", 'Sucesso!');
 
      }).error(function(data, status, headers, config) {         
 
        toastr.error("Erro ao carregar os dados das Fontes de Dados", 'Erro!'); //success,info,warning
 
    });


	function cadastrar() {

        vm.datasource.codigo = JSON.parse(getCurrentUser()).codigo; //add token

        DataSourceService.testDataSource(vm.datasource, function(result){
           if (result === true) {
            DataSourceService.setDataSource(vm.datasource,function (result) {
              if (result === true) {
                toastr.info("Fonte de dados inserida com sucesso!", 'Sucesso!');
                $location.path('/datasource/list');
              } else {
                  toastr.error("Erro ao inserir Fonte de Dados!", 'Erro!'); //success,info,warning
              }
            });
           } else {
              toastr.error("Não é possível estabelecer uma conexão com os dados informados.", 'Erro!'); //success,info,warning
           }
        });

        

    };

  }

})();

