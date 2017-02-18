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
            getAllVersions: _getAllVersions,
            getByVersion: _getByVersion,
            getPreservacao: _getPreservacao,            
        };

        return service;

        function _getAllDatasets ($callback) {
            return $http({
                    url: apiUrl() + 'open/list_datasets',
                    method: 'GET',
                    data: getCurrentUser()
                });
        }
        
        function _getAllFormats ($callback) {
            return $http({
                    url: apiUrl() + 'open/list_formats',
                    method: 'GET',
                    data: getCurrentUser()
                });
        }

        function _getByIdDataset (id) {
                var urlCompleta = apiUrl() + "open/" + id + "/about/";
                return $http.get(urlCompleta);
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
        
        function _getPreservacao (dataset) {
            return $http({
                    url: apiUrl() + 'open/' + dataset + "/verificar_preservacao",
                    method: 'GET'
                });
        }; 

    }

})();