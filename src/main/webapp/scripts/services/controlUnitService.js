/* global app */
'use strict';

app.factory('controlUnitService', function ($http, $interval, applicationProperties) {

    var url = applicationProperties.url;

    var latestStanding;
    var bindingPromise;

    var bindLatestStanding = function () {
        $http.get(url + "getStanding").then(function (response) {
            latestStanding = response.data;
        });
    };

    var startGame = function () {
        $http.post(url + "startGame");
    };

    var stopGame = function () {
        $http.post(url + "stopGame");
    };

    var changeToDefaultStrategyOfBuilder = function (_id, _direction) {
        var data = {
            id : _id,
            direction : _direction
        }
        $http.post(url + "changeToDefaultStrategyOfRobot", data);
    };

    var addDefaultStrategyOfBuilder = function (_id, _direction) {
        var data = {
            id : _id,
            direction : _direction
        }
        $http.post(url + "addDefaultStrategyToRobot", data);
    };

    var changeToGoToStrategyOfBuilder = function (_id, _x, _y) {
        var data = {
            id : parseInt(_id),
            x : parseInt(_x),
            y : parseInt(_y)
        };
        $http.post(url + "changeToGoToStrategyOfBuilder", data);
    };

    var addGoToStrategyOfBuilder = function (_id, _x, _y) {
        var data = {
            id : _id,
            x : _x,
            y : _y
        }
        $http.post(url + "addGoToStrategyToRobot", data);
    };

    var startBinding = function () {
        bindingPromise = $interval(bindLatestStanding, 500);
    };

    var stopBinding = function () {
        $interval.cancel(bindingPromise);
    };

    var getLatestStanding = function () {
        return latestStanding;
    };

    return {
        getLatestStanding: getLatestStanding,
        startBinding: startBinding,
        stopBinding: stopBinding,
        startGame: startGame,
        stopGame: stopGame,
        changeToDefaultStrategyOfBuilder: changeToDefaultStrategyOfBuilder,
        addDefaultStrategyOfBuilder: addDefaultStrategyOfBuilder,
        changeToGoToStrategyOfBuilder: changeToGoToStrategyOfBuilder,
        addGoToStrategyOfBuilder: addGoToStrategyOfBuilder
    };
});