/* global app */

'use strict';

app.controller('ControlUnitCtrl', function ($scope, controlUnitService) {
    controlUnitService.startBinding();

    $scope.latestStanding = controlUnitService.getLatestStanding;
    $scope.startGame = controlUnitService.startGame;
    $scope.stopGame = controlUnitService.stopGame;
    $scope.startBinding = controlUnitService.startBinding;
    $scope.stopBinding = controlUnitService.stopBinding;
    
    $scope.selectedDirection = "";
    $scope.selectedStrategy = "";
    $scope.selectedUnit = 0;
    $scope.selectedX = 0;
    $scope.selectedY = 0;
    
    $scope.add = false;
    
    $scope.setStrategy = function() {
        if($scope.selectedStrategy === "DEF") {
            if($scope.add){
                controlUnitService.addDefaultStrategyOfBuilder($scope.selectedUnit, $scope.selectedDirection);
            } else {
                controlUnitService.changeToDefaultStrategyOfBuilder($scope.selectedUnit, $scope.selectedDirection);
            }
        }  
        
        if($scope.selectedStrategy === "CELL") {
            
        }  
        
        if($scope.selectedStrategy === "GOTO") {
            if($scope.add){
                controlUnitService.addGoToStrategyOfBuilder($scope.selectedUnit, $scope.selectedX, $scope.selectedY);
            } else {
                controlUnitService.changeToGoToStrategyOfBuilder($scope.selectedUnit, $scope.selectedX, $scope.selectedY);
            }
        }
    }
    
    $scope.changeMode = function() {
        $scope.add = !$scope.add;
    }
});