'use strict';

angular.module('jpmorganApp').controller('StockTradeDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'StockTrade', 'Stock',
        function($scope, $stateParams, $uibModalInstance, entity, StockTrade, Stock) {

        $scope.stockTrade = entity;
        $scope.stocks = Stock.query();
        $scope.load = function(id) {
            StockTrade.get({id : id}, function(result) {
                $scope.stockTrade = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('jpmorganApp:stockTradeUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.stockTrade.id != null) {
                StockTrade.update($scope.stockTrade, onSaveSuccess, onSaveError);
            } else {
                StockTrade.save($scope.stockTrade, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.datePickerForTradeDate = {};

        $scope.datePickerForTradeDate.status = {
            opened: false
        };

        $scope.datePickerForTradeDateOpen = function($event) {
            $scope.datePickerForTradeDate.status.opened = true;
        };
}]);
