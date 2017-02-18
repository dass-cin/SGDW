/**
 * @author v.lugovsky
 * created on 16.12.2015
 */
(function () {
  'use strict';

  angular.module('BlurAdmin.pages.dataset', [])
    .config(routeConfig);

  /** @ngInject */
  function routeConfig($stateProvider, $urlRouterProvider) {
    $stateProvider
        .state('dataset', {
          url: '/dataset',
          template : '<ui-view  autoscroll="true" autoscroll-body-top></ui-view>',
          abstract: true,
          title: 'Conj. de Dados',
          sidebarMeta: {
            icon: 'ion-grid',
            order: 300,
          },
        })
        .state('dataset.list', {
          url: '/list',
          templateUrl: 'app/pages/dataset/list/list.view.html',
          title: 'Lista ',
          controller: 'DatasetListCtrl',
          controllerAs: 'vm',
          sidebarMeta: {
            order: 100,
          },
        })
        .state('dataset.details', {
          url: '/details/:collector_dataset',
          templateUrl: 'app/pages/dataset/details/details.view.html',
          title: 'Detalhes - Conjunto de Dados',
          controller: 'DatasetDetailsCtrl',
          controllerAs: 'vm',
        })
        .state('dataset.detailsAt', {
          url: '/details/:collector_dataset/atualizado',
          templateUrl: 'app/pages/dataset/details/details.view.html',
          title: 'Detalhes - Conjunto de Dados',
          controller: 'DatasetDetailsCtrl',
          controllerAs: 'vm',
        })
        .state('dataset.detailsPre', {
          url: '/details/:collector_dataset/preservado',
          templateUrl: 'app/pages/dataset/details/details.view.html',
          title: 'Detalhes - Conjunto de Dados',
          controller: 'DatasetDetailsCtrl',
          controllerAs: 'vm',
        })
        .state('dataset.version', {
          url: '/details/:collector_dataset/:version',
          templateUrl: 'app/pages/dataset/details/version/version.view.html',
          title: 'Detalhes - Conjunto de Dados (Versão)',
          controller: 'DatasetDetailsVersionCtrl',
          controllerAs: 'vm',
        })
        .state('dataset.new', {
          url: '/new/',
          templateUrl: 'app/pages/dataset/new/new.view.html',
          title: 'Novo',
          controller: 'DatasetNewCtrl',
          controllerAs: 'vm',
          sidebarMeta: {
            order: 0,
          },
        });
    $urlRouterProvider.when('/dataset','/dataset/list');
  }

})();
