'use strict';

angular.module('jpmorganApp')
	.controller('StockTradeDeleteController', function($scope, $uibModalInstance, entity, StockTrade) {

        $scope.stockTrade = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            StockTrade.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
