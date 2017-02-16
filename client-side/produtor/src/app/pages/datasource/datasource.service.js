(function() {
    'use strict';

    angular
        .module('BlurAdmin.pages.datasource')
        .factory('DataSourceService', DataSourceService);

    DataSourceService.$inject = ['$rootScope', '$http'];

    function DataSourceService ($rootScope, $http) {

        var service = {
            getAllDataSourceTypes: _getAllDataSourceTypes,
            getAllDataSource: _getAllDataSource,
            setDataSource: _setDataSource,
            testDataSource: _testDataSource
        };

        return service;

        function _getAllDataSourceTypes ($callback) {
            return $http({
                    url: apiUrl() + 'admin/list_databases_types',
                    method: 'POST',
                    data: getCurrentUser()
                });
        };
        
        function _getAllDataSource ($callback) {
            return $http({
                    url: apiUrl() + 'admin/list_databases',
                    method: 'POST',
                    data: getCurrentUser()
                });
        };        

        function _setDataSource(datasource, $callback) {
            $http({
                url: apiUrl() + 'admin/add_database',
                method: 'POST',
                data: datasource
            }).then(
                function (response) {
                    $callback(true);     
                }, function() {
                    $callback(false);
                });
        };

        function _testDataSource(datasource, $callback) {
            $http({
                url: apiUrl() + 'admin/test_database',
                method: 'POST',
                data: datasource
            }).then(
                function (response) {
                    $callback(true);     
                }, function() {
                    $callback(false);
                });
        };        

    }

})();
