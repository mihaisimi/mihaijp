'use strict';

describe('Controller Tests', function() {

    describe('StockTrade Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockStockTrade, MockStock;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockStockTrade = jasmine.createSpy('MockStockTrade');
            MockStock = jasmine.createSpy('MockStock');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'StockTrade': MockStockTrade,
                'Stock': MockStock
            };
            createController = function() {
                $injector.get('$controller')("StockTradeDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'jpmorganApp:stockTradeUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
