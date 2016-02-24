'use strict';

angular.module('jpmorganApp')
    .controller('StockDetailController', function ($scope, $rootScope, $stateParams, entity, Stock, StockTrade) {
        $scope.stock = entity;
        $scope.load = function (id) {
            Stock.get({id: id}, function(result) {
                $scope.stock = result;
            });
        };
        var unsubscribe = $rootScope.$on('jpmorganApp:stockUpdate', function(event, result) {
            $scope.stock = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
