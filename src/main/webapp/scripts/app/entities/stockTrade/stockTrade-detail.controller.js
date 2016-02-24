'use strict';

angular.module('jpmorganApp')
    .controller('StockTradeDetailController', function ($scope, $rootScope, $stateParams, entity, StockTrade, Stock) {
        $scope.stockTrade = entity;
        $scope.load = function (id) {
            StockTrade.get({id: id}, function(result) {
                $scope.stockTrade = result;
            });
        };
        var unsubscribe = $rootScope.$on('jpmorganApp:stockTradeUpdate', function(event, result) {
            $scope.stockTrade = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
