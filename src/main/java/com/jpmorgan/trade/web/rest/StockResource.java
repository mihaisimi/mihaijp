package com.jpmorgan.trade.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.jpmorgan.trade.domain.Stock;
import com.jpmorgan.trade.repository.StockRepository;
import com.jpmorgan.trade.web.rest.dto.StockRatingsDTO;
import com.jpmorgan.trade.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Stock.
 */
@RestController
@RequestMapping("/api")
public class StockResource {

    private final Logger log = LoggerFactory.getLogger(StockResource.class);

    @Inject
    private StockRepository stockRepository;

    /**
     * POST  /stocks -> Create a new stock.
     */
    @RequestMapping(value = "/stocks",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Stock> createStock(@Valid @RequestBody Stock stock) throws URISyntaxException {
        log.debug("REST request to save Stock : {}", stock);
        if (stock.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("stock", "idexists", "A new stock cannot already have an ID")).body(null);
        }
        Stock result = stockRepository.save(stock);
        return ResponseEntity.created(new URI("/api/stocks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("stock", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /stocks -> Updates an existing stock.
     */
    @RequestMapping(value = "/stocks",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Stock> updateStock(@Valid @RequestBody Stock stock) throws URISyntaxException {
        log.debug("REST request to update Stock : {}", stock);
        if (stock.getId() == null) {
            return createStock(stock);
        }
        Stock result = stockRepository.save(stock);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("stock", stock.getId().toString()))
            .body(result);
    }

    /**
     * GET  /stocks -> get all the stocks.
     */
    @RequestMapping(value = "/stocks",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Stock> getAllStocks() {
        log.debug("REST request to get all Stocks");
        return stockRepository.findAll();
            }

    /**
     * GET  /stocks/:id -> get the "id" stock.
     */
    @RequestMapping(value = "/stocks/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Stock> getStock(@PathVariable Long id) {
        log.debug("REST request to get Stock : {}", id);
        Stock stock = stockRepository.findOne(id);
        return Optional.ofNullable(stock)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * GET  /stockRatings/:id -> get the "id" stock.
     */
    @RequestMapping(value = "/stockRatings/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public StockRatingsDTO getStockRatings(@PathVariable Long id) {
        log.debug("REST request to get StockRatings : {}", id);
        Stock stock = stockRepository.findOneWithEagerTrades(id);
        //find all average prices at the database level
        Object stockAveragePrice = stockRepository.findAveragePriceForStocks();
        StockRatingsDTO stockRatingsDTO = new StockRatingsDTO(stock);
        if(stockAveragePrice != null && stockAveragePrice instanceof Double){
            stockRatingsDTO.setGbceAllShares(BigDecimal.valueOf((Double)stockAveragePrice));
        }
        return stockRatingsDTO;
    }

    /**
     * DELETE  /stocks/:id -> delete the "id" stock.
     */
    @RequestMapping(value = "/stocks/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteStock(@PathVariable Long id) {
        log.debug("REST request to delete Stock : {}", id);
        stockRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("stock", id.toString())).build();
    }
}
