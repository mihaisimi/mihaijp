package com.jpmorgan.trade.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.jpmorgan.trade.domain.StockTrade;
import com.jpmorgan.trade.repository.StockTradeRepository;
import com.jpmorgan.trade.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing StockTrade.
 */
@RestController
@RequestMapping("/api")
public class StockTradeResource {

    private final Logger log = LoggerFactory.getLogger(StockTradeResource.class);
        
    @Inject
    private StockTradeRepository stockTradeRepository;
    
    /**
     * POST  /stockTrades -> Create a new stockTrade.
     */
    @RequestMapping(value = "/stockTrades",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<StockTrade> createStockTrade(@Valid @RequestBody StockTrade stockTrade) throws URISyntaxException {
        log.debug("REST request to save StockTrade : {}", stockTrade);
        if (stockTrade.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("stockTrade", "idexists", "A new stockTrade cannot already have an ID")).body(null);
        }
        StockTrade result = stockTradeRepository.save(stockTrade);
        return ResponseEntity.created(new URI("/api/stockTrades/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("stockTrade", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /stockTrades -> Updates an existing stockTrade.
     */
    @RequestMapping(value = "/stockTrades",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<StockTrade> updateStockTrade(@Valid @RequestBody StockTrade stockTrade) throws URISyntaxException {
        log.debug("REST request to update StockTrade : {}", stockTrade);
        if (stockTrade.getId() == null) {
            return createStockTrade(stockTrade);
        }
        StockTrade result = stockTradeRepository.save(stockTrade);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("stockTrade", stockTrade.getId().toString()))
            .body(result);
    }

    /**
     * GET  /stockTrades -> get all the stockTrades.
     */
    @RequestMapping(value = "/stockTrades",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<StockTrade> getAllStockTrades() {
        log.debug("REST request to get all StockTrades");
        return stockTradeRepository.findAll();
            }

    /**
     * GET  /stockTrades/:id -> get the "id" stockTrade.
     */
    @RequestMapping(value = "/stockTrades/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<StockTrade> getStockTrade(@PathVariable Long id) {
        log.debug("REST request to get StockTrade : {}", id);
        StockTrade stockTrade = stockTradeRepository.findOne(id);
        return Optional.ofNullable(stockTrade)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /stockTrades/:id -> delete the "id" stockTrade.
     */
    @RequestMapping(value = "/stockTrades/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteStockTrade(@PathVariable Long id) {
        log.debug("REST request to delete StockTrade : {}", id);
        stockTradeRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("stockTrade", id.toString())).build();
    }
}
