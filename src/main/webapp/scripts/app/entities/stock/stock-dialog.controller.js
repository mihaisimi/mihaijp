'use strict';

angular.module('jpmorganApp').controller('StockDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Stock', 'StockTrade',
        function($scope, $stateParams, $uibModalInstance, entity, Stock, StockTrade) {

        $scope.stock = entity;
        $scope.stocktrades = StockTrade.query();
        $scope.load = function(id) {
            Stock.get({id : id}, function(result) {
                $scope.stock = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('jpmorganApp:stockUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.stock.id != null) {
                Stock.update($scope.stock, onSaveSuccess, onSaveError);
            } else {
                Stock.save($scope.stock, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
