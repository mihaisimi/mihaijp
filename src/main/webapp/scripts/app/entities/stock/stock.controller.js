'use strict';

angular.module('jpmorganApp')
    .controller('StockController', function ($scope, $state, Stock) {

        $scope.stocks = [];
        $scope.loadAll = function() {
            Stock.query(function(result) {
               $scope.stocks = result;
            });
        };
        $scope.loadAll();


        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.stock = {
                symbol: null,
                type: null,
                lastDividend: null,
                fixedDividend: null,
                parValue: null,
                id: null
            };
        };
    });
