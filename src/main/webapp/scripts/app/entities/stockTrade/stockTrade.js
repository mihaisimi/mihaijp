'use strict';

angular.module('jpmorganApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('stockTrade', {
                parent: 'entity',
                url: '/stockTrades',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'jpmorganApp.stockTrade.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/stockTrade/stockTrades.html',
                        controller: 'StockTradeController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('stockTrade');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('stockTrade.detail', {
                parent: 'entity',
                url: '/stockTrade/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'jpmorganApp.stockTrade.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/stockTrade/stockTrade-detail.html',
                        controller: 'StockTradeDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('stockTrade');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'StockTrade', function($stateParams, StockTrade) {
                        return StockTrade.get({id : $stateParams.id});
                    }]
                }
            })
            .state('stockTrade.new', {
                parent: 'stockTrade',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/stockTrade/stockTrade-dialog.html',
                        controller: 'StockTradeDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    quantity: null,
                                    price: null,
                                    isSell: null,
                                    tradeDate: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('stockTrade', null, { reload: true });
                    }, function() {
                        $state.go('stockTrade');
                    })
                }]
            })
            .state('stockTrade.edit', {
                parent: 'stockTrade',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/stockTrade/stockTrade-dialog.html',
                        controller: 'StockTradeDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['StockTrade', function(StockTrade) {
                                return StockTrade.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('stockTrade', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('stockTrade.delete', {
                parent: 'stockTrade',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/stockTrade/stockTrade-delete-dialog.html',
                        controller: 'StockTradeDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['StockTrade', function(StockTrade) {
                                return StockTrade.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('stockTrade', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
