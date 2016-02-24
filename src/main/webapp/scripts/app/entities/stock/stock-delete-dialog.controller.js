'use strict';

angular.module('jpmorganApp')
	.controller('StockDeleteController', function($scope, $uibModalInstance, entity, Stock) {

        $scope.stock = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Stock.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
