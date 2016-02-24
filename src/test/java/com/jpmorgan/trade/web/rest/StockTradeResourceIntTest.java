package com.jpmorgan.trade.web.rest;

import com.jpmorgan.trade.Application;
import com.jpmorgan.trade.domain.StockTrade;
import com.jpmorgan.trade.repository.StockTradeRepository;

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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the StockTradeResource REST controller.
 *
 * @see StockTradeResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class StockTradeResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.of("Z"));


    private static final Long DEFAULT_QUANTITY = 1L;
    private static final Long UPDATED_QUANTITY = 2L;

    private static final BigDecimal DEFAULT_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRICE = new BigDecimal(2);

    private static final Boolean DEFAULT_IS_SELL = false;
    private static final Boolean UPDATED_IS_SELL = true;

    private static final ZonedDateTime DEFAULT_TRADE_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_TRADE_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_TRADE_DATE_STR = dateTimeFormatter.format(DEFAULT_TRADE_DATE);

    @Inject
    private StockTradeRepository stockTradeRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restStockTradeMockMvc;

    private StockTrade stockTrade;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        StockTradeResource stockTradeResource = new StockTradeResource();
        ReflectionTestUtils.setField(stockTradeResource, "stockTradeRepository", stockTradeRepository);
        this.restStockTradeMockMvc = MockMvcBuilders.standaloneSetup(stockTradeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        stockTrade = new StockTrade();
        stockTrade.setQuantity(DEFAULT_QUANTITY);
        stockTrade.setPrice(DEFAULT_PRICE);
        stockTrade.setIsSell(DEFAULT_IS_SELL);
        stockTrade.setTradeDate(DEFAULT_TRADE_DATE);
    }

    @Test
    @Transactional
    public void createStockTrade() throws Exception {
        int databaseSizeBeforeCreate = stockTradeRepository.findAll().size();

        // Create the StockTrade

        restStockTradeMockMvc.perform(post("/api/stockTrades")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(stockTrade)))
                .andExpect(status().isCreated());

        // Validate the StockTrade in the database
        List<StockTrade> stockTrades = stockTradeRepository.findAll();
        assertThat(stockTrades).hasSize(databaseSizeBeforeCreate + 1);
        StockTrade testStockTrade = stockTrades.get(stockTrades.size() - 1);
        assertThat(testStockTrade.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testStockTrade.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testStockTrade.getIsSell()).isEqualTo(DEFAULT_IS_SELL);
        assertThat(testStockTrade.getTradeDate()).isEqualTo(DEFAULT_TRADE_DATE);
    }

    @Test
    @Transactional
    public void checkQuantityIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockTradeRepository.findAll().size();
        // set the field null
        stockTrade.setQuantity(null);

        // Create the StockTrade, which fails.

        restStockTradeMockMvc.perform(post("/api/stockTrades")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(stockTrade)))
                .andExpect(status().isBadRequest());

        List<StockTrade> stockTrades = stockTradeRepository.findAll();
        assertThat(stockTrades).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockTradeRepository.findAll().size();
        // set the field null
        stockTrade.setPrice(null);

        // Create the StockTrade, which fails.

        restStockTradeMockMvc.perform(post("/api/stockTrades")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(stockTrade)))
                .andExpect(status().isBadRequest());

        List<StockTrade> stockTrades = stockTradeRepository.findAll();
        assertThat(stockTrades).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTradeDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockTradeRepository.findAll().size();
        // set the field null
        stockTrade.setTradeDate(null);

        // Create the StockTrade, which fails.

        restStockTradeMockMvc.perform(post("/api/stockTrades")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(stockTrade)))
                .andExpect(status().isBadRequest());

        List<StockTrade> stockTrades = stockTradeRepository.findAll();
        assertThat(stockTrades).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllStockTrades() throws Exception {
        // Initialize the database
        stockTradeRepository.saveAndFlush(stockTrade);

        // Get all the stockTrades
        restStockTradeMockMvc.perform(get("/api/stockTrades?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(stockTrade.getId().intValue())))
                .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY.intValue())))
                .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.intValue())))
                .andExpect(jsonPath("$.[*].isSell").value(hasItem(DEFAULT_IS_SELL.booleanValue())))
                .andExpect(jsonPath("$.[*].tradeDate").value(hasItem(DEFAULT_TRADE_DATE_STR)));
    }

    @Test
    @Transactional
    public void getStockTrade() throws Exception {
        // Initialize the database
        stockTradeRepository.saveAndFlush(stockTrade);

        // Get the stockTrade
        restStockTradeMockMvc.perform(get("/api/stockTrades/{id}", stockTrade.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(stockTrade.getId().intValue()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY.intValue()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.intValue()))
            .andExpect(jsonPath("$.isSell").value(DEFAULT_IS_SELL.booleanValue()))
            .andExpect(jsonPath("$.tradeDate").value(DEFAULT_TRADE_DATE_STR));
    }

    @Test
    @Transactional
    public void getNonExistingStockTrade() throws Exception {
        // Get the stockTrade
        restStockTradeMockMvc.perform(get("/api/stockTrades/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStockTrade() throws Exception {
        // Initialize the database
        stockTradeRepository.saveAndFlush(stockTrade);

		int databaseSizeBeforeUpdate = stockTradeRepository.findAll().size();

        // Update the stockTrade
        stockTrade.setQuantity(UPDATED_QUANTITY);
        stockTrade.setPrice(UPDATED_PRICE);
        stockTrade.setIsSell(UPDATED_IS_SELL);
        stockTrade.setTradeDate(UPDATED_TRADE_DATE);

        restStockTradeMockMvc.perform(put("/api/stockTrades")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(stockTrade)))
                .andExpect(status().isOk());

        // Validate the StockTrade in the database
        List<StockTrade> stockTrades = stockTradeRepository.findAll();
        assertThat(stockTrades).hasSize(databaseSizeBeforeUpdate);
        StockTrade testStockTrade = stockTrades.get(stockTrades.size() - 1);
        assertThat(testStockTrade.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testStockTrade.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testStockTrade.getIsSell()).isEqualTo(UPDATED_IS_SELL);
        assertThat(testStockTrade.getTradeDate()).isEqualTo(UPDATED_TRADE_DATE);
    }

    @Test
    @Transactional
    public void deleteStockTrade() throws Exception {
        // Initialize the database
        stockTradeRepository.saveAndFlush(stockTrade);

		int databaseSizeBeforeDelete = stockTradeRepository.findAll().size();

        // Get the stockTrade
        restStockTradeMockMvc.perform(delete("/api/stockTrades/{id}", stockTrade.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<StockTrade> stockTrades = stockTradeRepository.findAll();
        assertThat(stockTrades).hasSize(databaseSizeBeforeDelete - 1);
    }
}
