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
        stopGame: stopGame
    };
});