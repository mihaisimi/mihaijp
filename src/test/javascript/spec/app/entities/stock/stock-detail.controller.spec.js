'use strict';

describe('Controller Tests', function() {

    describe('Stock Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockStock, MockStockTrade;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockStock = jasmine.createSpy('MockStock');
            MockStockTrade = jasmine.createSpy('MockStockTrade');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Stock': MockStock,
                'StockTrade': MockStockTrade
            };
            createController = function() {
                $injector.get('$controller')("StockDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'jpmorganApp:stockUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
