/* global angular */

'use strict';

var app = angular.module('Apollo2App', ['ui.router', 'ngMaterial']);

app.config(function ($stateProvider, $urlRouterProvider) {

    $urlRouterProvider.otherwise("/controlUnit");

    $stateProvider
            .state("controlUnit", {
                url: "/controlUnit",
                templateUrl: "views/controlUnit.html",
                controller: "ControlUnitCtrl"
            });

}).constant('applicationProperties', {
    url: 'http://localhost:8080/'
});