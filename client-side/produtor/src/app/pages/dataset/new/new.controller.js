(function () {
  'use strict';

  angular.module('BlurAdmin.pages.dataset')
      .controller('DatasetNewCtrl', DatasetNewCtrl);

  /** @ngInject */
  function DatasetNewCtrl($scope, DatasetService, toastr, $location) {
   var vm = this;

    vm.dataset = {};
    vm.datasourceSubmit = cadastrar;

    $scope.generateURI = function(sa) {
      vm.dataset.identifierURI = (angular.copy(removeAccents(vm.dataset.datasetTitle))).toLowerCase();
    };

    DatasetService.getAllDataSource().success(
      function(data, status, headers, config) {
 
          vm.availableOptions = angular.fromJson(data);
          toastr.success("Dados carregados", 'Sucesso!');
 
      }).error(function(data, status, headers, config) {         
 
        toastr.error("Erro ao carregar os dados das Fontes de Dados", 'Erro!'); //success,info,warning
 
    });

    function removeAccents (str) {
        var map = {
            'a' : 'á|à|ã|â|À|Á|Ã|Â',
            'e' : 'é|è|ê|É|È|Ê',
            'i' : 'í|ì|î',
            'o' : 'ó|ò|ô|õ|Ó|Ò|Ô|Õ',
            'u' : 'ú|ù|û|ü|Ú|Ù|Û|Ü',
            'c' : 'ç|Ç',
            'n' : 'ñ|Ñ',
            '-'  : ' '
        };
        
        angular.forEach(map, function (pattern, newValue) {
            str = str.replace(new RegExp(pattern, 'g'), newValue);
        });

        return str;
    };

    function cadastrar() {
        vm.dataset.codigo = JSON.parse(getCurrentUser()).codigo; //add token

        DatasetService.setDataset(vm.dataset,function (result) {
            if (result === true) {
              toastr.info("Conjunto de dados cadastrado com sucesso!", 'Sucesso!');
            $location.path('/dataset/list');
            } else {
                toastr.error("Erro ao inserir Conjunto de Dados!", 'Erro!'); //success,info,warning
            }
        });

      };


  }

})();

