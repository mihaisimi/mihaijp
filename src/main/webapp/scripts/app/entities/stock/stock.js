'use strict';

angular.module('jpmorganApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('stock', {
                parent: 'entity',
                url: '/stocks',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'jpmorganApp.stock.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/stock/stocks.html',
                        controller: 'StockController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('stock');
                        $translatePartialLoader.addPart('stockTypeEnum');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('stock.detail', {
                parent: 'entity',
                url: '/stock/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'jpmorganApp.stock.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/stock/stock-detail.html',
                        controller: 'StockDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('stock');
                        $translatePartialLoader.addPart('stockTypeEnum');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Stock', function($stateParams, Stock) {
                        return Stock.get({id : $stateParams.id});
                    }]
                }
            })
            .state('stock.new', {
                parent: 'stock',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/stock/stock-dialog.html',
                        controller: 'StockDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    symbol: null,
                                    type: null,
                                    lastDividend: null,
                                    fixedDividend: null,
                                    parValue: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('stock', null, { reload: true });
                    }, function() {
                        $state.go('stock');
                    })
                }]
            })
            .state('stock.edit', {
                parent: 'stock',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/stock/stock-dialog.html',
                        controller: 'StockDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Stock', function(Stock) {
                                return Stock.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('stock', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('stock.delete', {
                parent: 'stock',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/stock/stock-delete-dialog.html',
                        controller: 'StockDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Stock', function(Stock) {
                                return Stock.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('stock', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
