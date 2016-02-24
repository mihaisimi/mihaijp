package com.jpmorgan.trade.web.rest;

import com.jpmorgan.trade.Application;
import com.jpmorgan.trade.domain.Stock;
import com.jpmorgan.trade.repository.StockRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.jpmorgan.trade.domain.enumeration.StockTypeEnum;

/**
 * Test class for the StockResource REST controller.
 *
 * @see StockResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class StockResourceIntTest {

    private static final String DEFAULT_SYMBOL = "AAAAA";
    private static final String UPDATED_SYMBOL = "BBBBB";
    
    private static final StockTypeEnum DEFAULT_TYPE = StockTypeEnum.COMMON;
    private static final StockTypeEnum UPDATED_TYPE = StockTypeEnum.PREFERRED;

    private static final BigDecimal DEFAULT_LAST_DIVIDEND = new BigDecimal(1);
    private static final BigDecimal UPDATED_LAST_DIVIDEND = new BigDecimal(2);

    private static final BigDecimal DEFAULT_FIXED_DIVIDEND = new BigDecimal(1);
    private static final BigDecimal UPDATED_FIXED_DIVIDEND = new BigDecimal(2);

    private static final BigDecimal DEFAULT_PAR_VALUE = new BigDecimal(1);
    private static final BigDecimal UPDATED_PAR_VALUE = new BigDecimal(2);

    @Inject
    private StockRepository stockRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restStockMockMvc;

    private Stock stock;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        StockResource stockResource = new StockResource();
        ReflectionTestUtils.setField(stockResource, "stockRepository", stockRepository);
        this.restStockMockMvc = MockMvcBuilders.standaloneSetup(stockResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        stock = new Stock();
        stock.setSymbol(DEFAULT_SYMBOL);
        stock.setType(DEFAULT_TYPE);
        stock.setLastDividend(DEFAULT_LAST_DIVIDEND);
        stock.setFixedDividend(DEFAULT_FIXED_DIVIDEND);
        stock.setParValue(DEFAULT_PAR_VALUE);
    }

    @Test
    @Transactional
    public void createStock() throws Exception {
        int databaseSizeBeforeCreate = stockRepository.findAll().size();

        // Create the Stock

        restStockMockMvc.perform(post("/api/stocks")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(stock)))
                .andExpect(status().isCreated());

        // Validate the Stock in the database
        List<Stock> stocks = stockRepository.findAll();
        assertThat(stocks).hasSize(databaseSizeBeforeCreate + 1);
        Stock testStock = stocks.get(stocks.size() - 1);
        assertThat(testStock.getSymbol()).isEqualTo(DEFAULT_SYMBOL);
        assertThat(testStock.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testStock.getLastDividend()).isEqualTo(DEFAULT_LAST_DIVIDEND);
        assertThat(testStock.getFixedDividend()).isEqualTo(DEFAULT_FIXED_DIVIDEND);
        assertThat(testStock.getParValue()).isEqualTo(DEFAULT_PAR_VALUE);
    }

    @Test
    @Transactional
    public void checkSymbolIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockRepository.findAll().size();
        // set the field null
        stock.setSymbol(null);

        // Create the Stock, which fails.

        restStockMockMvc.perform(post("/api/stocks")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(stock)))
                .andExpect(status().isBadRequest());

        List<Stock> stocks = stockRepository.findAll();
        assertThat(stocks).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockRepository.findAll().size();
        // set the field null
        stock.setType(null);

        // Create the Stock, which fails.

        restStockMockMvc.perform(post("/api/stocks")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(stock)))
                .andExpect(status().isBadRequest());

        List<Stock> stocks = stockRepository.findAll();
        assertThat(stocks).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllStocks() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stocks
        restStockMockMvc.perform(get("/api/stocks?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(stock.getId().intValue())))
                .andExpect(jsonPath("$.[*].symbol").value(hasItem(DEFAULT_SYMBOL.toString())))
                .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
                .andExpect(jsonPath("$.[*].lastDividend").value(hasItem(DEFAULT_LAST_DIVIDEND.intValue())))
                .andExpect(jsonPath("$.[*].fixedDividend").value(hasItem(DEFAULT_FIXED_DIVIDEND.intValue())))
                .andExpect(jsonPath("$.[*].parValue").value(hasItem(DEFAULT_PAR_VALUE.intValue())));
    }

    @Test
    @Transactional
    public void getStock() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get the stock
        restStockMockMvc.perform(get("/api/stocks/{id}", stock.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(stock.getId().intValue()))
            .andExpect(jsonPath("$.symbol").value(DEFAULT_SYMBOL.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.lastDividend").value(DEFAULT_LAST_DIVIDEND.intValue()))
            .andExpect(jsonPath("$.fixedDividend").value(DEFAULT_FIXED_DIVIDEND.intValue()))
            .andExpect(jsonPath("$.parValue").value(DEFAULT_PAR_VALUE.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingStock() throws Exception {
        // Get the stock
        restStockMockMvc.perform(get("/api/stocks/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStock() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

		int databaseSizeBeforeUpdate = stockRepository.findAll().size();

        // Update the stock
        stock.setSymbol(UPDATED_SYMBOL);
        stock.setType(UPDATED_TYPE);
        stock.setLastDividend(UPDATED_LAST_DIVIDEND);
        stock.setFixedDividend(UPDATED_FIXED_DIVIDEND);
        stock.setParValue(UPDATED_PAR_VALUE);

        restStockMockMvc.perform(put("/api/stocks")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(stock)))
                .andExpect(status().isOk());

        // Validate the Stock in the database
        List<Stock> stocks = stockRepository.findAll();
        assertThat(stocks).hasSize(databaseSizeBeforeUpdate);
        Stock testStock = stocks.get(stocks.size() - 1);
        assertThat(testStock.getSymbol()).isEqualTo(UPDATED_SYMBOL);
        assertThat(testStock.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testStock.getLastDividend()).isEqualTo(UPDATED_LAST_DIVIDEND);
        assertThat(testStock.getFixedDividend()).isEqualTo(UPDATED_FIXED_DIVIDEND);
        assertThat(testStock.getParValue()).isEqualTo(UPDATED_PAR_VALUE);
    }

    @Test
    @Transactional
    public void deleteStock() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

		int databaseSizeBeforeDelete = stockRepository.findAll().size();

        // Get the stock
        restStockMockMvc.perform(delete("/api/stocks/{id}", stock.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Stock> stocks = stockRepository.findAll();
        assertThat(stocks).hasSize(databaseSizeBeforeDelete - 1);
    }
}
