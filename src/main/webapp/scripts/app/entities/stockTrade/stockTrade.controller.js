'use strict';

angular.module('jpmorganApp')
    .controller('StockTradeController', function ($scope, $state, StockTrade) {

        $scope.stockTrades = [];
        $scope.loadAll = function() {
            StockTrade.query(function(result) {
               $scope.stockTrades = result;
            });
        };
        $scope.loadAll();


        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.stockTrade = {
                quantity: null,
                price: null,
                isSell: null,
                tradeDate: null,
                id: null
            };
        };
    });
