(function() {
    'use strict';

    angular
        .module('BlurAdmin.pages.dataset')
        .factory('DatasetService', DatasetService);

    DatasetService.$inject = ['$q', '$filter', '$timeout', '$rootScope', '$http'];

    function DatasetService ($q, $filter, $timeout, $rootScope, $http) {

        var service = {
            getAllDatasets: _getAllDatasets,
            getByIdDataset: _getByIdDataset,
            getAllFormats: _getAllFormats,
            getAllDataSource: _getAllDataSource,
            setDataset: _setDataset,
            setAtualizarDataset: _setAtualizarDataset,
            getAllVersions: _getAllVersions,
            getByVersion: _getByVersion,
            setPreservarDataset: _setPreservarDataset,
        };

        return service;

        function _getAllDatasets ($callback) {
            return $http({
                    url: apiUrl() + 'admin/list_datasets',
                    method: 'POST',
                    data: getCurrentUser()
                });
        }
        
        function _getAllFormats ($callback) {
            return $http({
                    url: apiUrl() + 'admin/list_formats',
                    method: 'POST',
                    data: getCurrentUser()
                });
        }

        function _getByIdDataset (id, codigo) {
                var urlCompleta = apiUrl() + "admin/" + id + "/about/" + codigo +"";
                return $http.get(urlCompleta);
        };

        function _getAllDataSource ($callback) {
            return $http({
                    url: apiUrl() + 'admin/list_databases',
                    method: 'POST',
                    data: getCurrentUser()
                });
        };        

        function _setDataset(dataset, $callback) {
            $http({
                url: apiUrl() + 'admin/add_dataset',
                method: 'POST',
                data: dataset
            }).then(
                function (response) {
                    $callback(true);     
                }, function() {
                    $callback(false);
                });
        };

        function _setAtualizarDataset(atualizacaoManual, $callback) {
            $http({
                url: apiUrl() + 'admin/atualizar_dataset',
                method: 'POST',
                data: atualizacaoManual
            }).then(
                function (response) {
                    $callback(true);     
                }, function() {
                    $callback(false);
                });
        };       

        function _setPreservarDataset(atualizacaoManual, $callback) {
            $http({
                url: apiUrl() + 'admin/preservar_dataset',
                method: 'POST',
                data: atualizacaoManual
            }).then(
                function (response) {
                    $callback(true);     
                }, function() {
                    $callback(false);
                });
        };  

        function _getAllVersions (dataset) {
            return $http({
                    url: apiUrl() + 'open/' + dataset + '/list_versions',
                    method: 'GET'
                });
        }; 

        function _getByVersion (dataset, version) {
            return $http({
                    url: apiUrl() + 'open/' + dataset + "/about/" + version,
                    method: 'GET'
                });
        }; 
        
    }

})();


/*
(function () {
	'use strict';

	angular
		.module('BlurAdmin.pages.dataset', [])
		.factory("DatasetService", DatasetService);


		function DatasetService(config, $http) {

			var _getAllDatasets = function() {
				return $http.post(apiUrl() + '/admin/list_dataset', angular.toJson(getToken()))
		    };//http://localhost:8080/contex/myapp/dataset/all

		  var _getByIdDataset = function(id) {
		    	return $http
		    	.get("myapp/dataset/" + id);
		    };

		    var _saveDataset = function(dataset) {
		    	return $http.post("myapp/dataset/salvar", angular.toJson(dataset));
		    };

		    var _updateDataset = function(dataset) {
		    	return $http.put("myapp/dataset/" + dataset.id, angular.toJson(dataset));
		    };

		    var _deleteDataset = function(id) {
		    	return $http.delete("myapp/dataset/" + id);
		    };

		    var _calculate = function(people) {

		    	var _nascimento = people.nascimento;
		    	var _dataCorrente = people.dataCorrente;            

		    	var _age = new Date(_dataCorrente).getFullYear()
		    	- new Date(_nascimento).getFullYear();

		    	return {
		    		age : _age,
		    	};
		    };
			
		    return {
		    	getAllDatasets : _getAllDatasets,
		   	getByIdDataset :_getByIdDataset,
		    	saveDataset : _saveDataset,
		    	updateDataset : _updateDataset,
		    	deleteDataset:_deleteDataset,
		    	calculate : _calculate
		    	
		    };

    };
});*/


 /*
    //getByIdDataset
    //saveDataset salva um novo registro de uma dataset
    $scope.saveDataset = function(dataset) {
        DatasetService.saveDataset(dataset).success(
                function(data, status, headers, config) {
                    listarDatasets();
                    $scope.message = "registro Dataset savo com sucesso!";
                }).error(function(data, status, headers, config) {
            switch (status) {
            case 401: {
                $scope.message = "Você precisa ser autenticado!"
                break;
            }
            case 500: {
                $scope.message = "Erro!";
                break;
            }
            }
            console.log(data, status);
        });
    };
     
    //updateDataset atualiza registro de uma dataset quando selecionado na tabela
    $scope.updateDataset = function(dataset) {
        DatasetService.updateDataset(dataset).success(
                function(data, status, headers, config) {
                    listarDatasets();
                    $scope.message = "registro Dataset savo com sucesso!";
                }).error(function(data, status, headers, config) {
            switch (status) {
            case 401: {
                $scope.message = "Você precisa ser autenticado!"
                break;
            }
            case 500: {
                $scope.message = "Erro!";
                break;
            }
            }
            console.log(data, status);
        });
    };
     
    //deleteDataset remove um registro de uma dataset quando selecionado na tabela
    $scope.deleteDataset = function(dataset) {
        DatasetService.deleteDataset(dataset.id).success(
                function(data, status, headers, config) {
                    listarDatasets();
                    $scope.message = "registro Dataset savo com sucesso!";
                }).error(function(data, status, headers, config) {
            switch (status) {
            case 401: {
                $scope.message = "Você precisa ser autenticado!"
                break;
            }
            case 500: {
                $scope.message = "Erro!";
                break;
            }
            }
            console.log(data, status);
        });
    };

    */