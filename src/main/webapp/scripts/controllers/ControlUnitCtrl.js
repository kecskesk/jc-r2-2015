/* global app */

'use strict';

app.controller('ControlUnitCtrl', function ($scope, $interval, controlUnitService) {
    controlUnitService.startBinding();

    $scope.latestStanding = "";
    $scope.initLatestStanding = function () {
        $interval($scope.getLastestStanding, 500);
    };

    $scope.getLastestStanding = function () {
        $scope.latestStanding = controlUnitService.getLatestStanding();   
    };
    
    $scope.setSelectedPosition = function(x, y) {
        $scope.selectedX = x;
        $scope.selectedY = y;
    };


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

    $scope.setStrategy = function () {
        if ($scope.selectedStrategy === "DEF") {
            if ($scope.add) {
                controlUnitService.addDefaultStrategyOfBuilder($scope.selectedUnit, $scope.selectedDirection);
            } else {
                controlUnitService.changeToDefaultStrategyOfBuilder($scope.selectedUnit, $scope.selectedDirection);
            }
        }

        if ($scope.selectedStrategy === "CELL") {
            if ($scope.add) {
                controlUnitService.addToCellStrategyOfBuilder($scope.selectedUnit, $scope.selectedDirection);
            } else {
                controlUnitService.changeToCellStrategyOfBuilder($scope.selectedUnit, $scope.selectedDirection);
            }
        }

        if ($scope.selectedStrategy === "GOTO") {
            if ($scope.add) {
                controlUnitService.addGoToStrategyOfBuilder($scope.selectedUnit, $scope.selectedX, $scope.selectedY);
            } else {
                controlUnitService.changeToGoToStrategyOfBuilder($scope.selectedUnit, $scope.selectedX, $scope.selectedY);
            }
        }
    };

    $scope.changeMode = function () {
        $scope.add = !$scope.add;
    };

    $scope.showCell = function (cell) {
        if (cell)
            if (cell.object)
                if (cell.object == "OBSIDIAN")
                    return 'o';
        if (cell.object == "ROCK")
            return 'c';
        if (cell.object == "TUNNEL")
            return 'u';
        if (cell.object == "SHUTTLE")
            return 's';
        if (cell.object == "BUILDER_UNIT")
            return 'b';
        if (cell.object == "GRANITE")
            return 'g';
        return 'x';
    };

    $scope.getColor = function (celltype) {
        if (celltype == "o") {
            return 'black';
        }
        if (celltype == "c") {
            return 'gray';
        }
        if (celltype == "u") {
            return 'blue';
        }
        if (celltype == "s") {
            return 'yellow';
        }
        if (celltype == "b") {
            return 'cyan';
        }
        if (celltype == "g") {
            return 'black';
        }
        if (celltype == "x") {
            return 'white';
        }
        return 'red';
    };
});