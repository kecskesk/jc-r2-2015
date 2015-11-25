/* global app */

'use strict';

app.controller('ControlUnitCtrl', function ($scope, controlUnitService) {
    controlUnitService.startBinding();

    $scope.latestStanding = controlUnitService.getLatestStanding;
    $scope.startGame = controlUnitService.startGame;
    $scope.stopGame = controlUnitService.stopGame;
    $scope.startBinding = controlUnitService.startBinding;
    $scope.stopBinding = controlUnitService.stopBinding;
});