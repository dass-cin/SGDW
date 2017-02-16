(function () {
  'use strict';

  angular.module('BlurAdmin.pages.datasource', [])
      .config(routeConfig);

  /** @ngInject */
 /** @ngInject */
  function routeConfig($stateProvider, $urlRouterProvider) {
    $stateProvider
        .state('datasource', {
          url: '/datasource',
          template : '<ui-view  autoscroll="true" autoscroll-body-top></ui-view>',
          abstract: true,
          title: 'Fonte de Dados',
          sidebarMeta: {
            icon: 'ion-gear-a',
            order: 100,
          },
        })
        .state('datasource.new', {
          url: '/new',
          templateUrl: 'app/pages/datasource/new/new.view.html',
          title: 'Nova ',
          controller: 'DataSourceNewCtrl',
          controllerAs: 'vm',
          sidebarMeta: {
            order: 100,
          },
        })
        .state('datasource.list', {
          url: '/list',
          templateUrl: 'app/pages/datasource/list/list.view.html',
          title: 'Lista ',
          controller: 'DataSourceCtrl',
          controllerAs: 'vm',
          sidebarMeta: {
            order: 200,
          },
        });
    $urlRouterProvider.when('/datasource','/datasource/list');
  }

})();