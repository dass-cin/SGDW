/**
 * @author v.lugovksy
 * created on 16.12.2015
 */
(function () {
  'use strict';

  angular.module('BlurAdmin.theme.components')
      .controller('MsgCenterCtrl', MsgCenterCtrl);

  /** @ngInject */
  function MsgCenterCtrl($scope, $sce) {
    $scope.notifications = [
      {
        template: '<strong>Programada!</strong> Segurança.',
        time: '13h00',
        image: 'assets/img/scheduler.svg'
      },
      {
        template: '<strong>Executando...</strong> Atores de Recife.',
        time: '12h00',
        image: 'assets/img/task.svg'
      },
      {
        template: '<strong>Executando...</strong> Saúde .',
        time: '12h00',
        image: 'assets/img/task.svg'
      },
      {
        template: '<strong>Erro!</strong>  Ônibus',
        time: '11h00',
        image: 'assets/img/error.svg'
      },
      {
        template: '<strong>Erro!</strong> Turismo.',
        time: '11h00',
        image: 'assets/img/error.svg',
      }
    ];

    $scope.getMessage = function(msg) {
      var text = msg.template;
      return $sce.trustAsHtml(text);
    };

 
  }
})();