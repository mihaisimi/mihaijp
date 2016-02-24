'use strict';

angular.module('jpmorganApp')
    .controller('MainController', function ($scope, Principal, Stock, StockRatings) {
        Principal.identity().then(function(account) {
            $scope.account = account;
            $scope.isAuthenticated = Principal.isAuthenticated;
        });

        $scope.stocks = [];
        $scope.selectedStock = {};
        $scope.loadAll = function() {
            Stock.query(function(result) {
                $scope.stocks = result;
                console.log($scope.stocks);
            });
        };

        $scope.loadAll();

        $scope.stocksGridOptions = {
            data: 'stocks',
            enableSorting:true,
            multiSelect:false,
            enableRowSelection: true,
            enableFullRowSelection: true,
            enableRowHeaderSelection: false,
            enableColumnMenus:false,
            columnDefs: [
                {
                    field: 'id',
                    name: 'id',
                    displayName: 'Id',
                    type:"number"
                },
                {
                    field: 'symbol',
                    name: 'symbol',
                    displayName: 'Symbol'
                },
                {
                    field:'lastDividend',
                    name: 'lastDividend',
                    visible: true,
                    displayName: 'Last Dividend',
                    type:'number'
                },
                {
                    field:'parValue',
                    name:'parValue',
                    displayName:'Par Value',
                    visible:true,
                    type:'number'
                },
                {
                    name:'type',
                    field:'type',
                    visible: true
                }
            ]
        };

        $scope.stocksGridOptions.onRegisterApi = function(gridApi) {
            //set gridApi on scope
            $scope.gridApi = gridApi;

            gridApi.selection.on.rowSelectionChanged($scope, function (row) {
                var stockId = row.entity.id;
                StockRatings.get({id: stockId}, function(result) {
                    $scope.stock = result;
                    console.log(result);
                });

            });
        };

    });
