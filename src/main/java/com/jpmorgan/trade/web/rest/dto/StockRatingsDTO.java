package com.jpmorgan.trade.web.rest.dto;

import com.jpmorgan.trade.domain.Stock;
import com.jpmorgan.trade.domain.StockTrade;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * Created by mihai on 24/02/16.
 */
public class StockRatingsDTO {
    Stock stock;
    BigDecimal dividendYield;
    BigDecimal peRatio;
    BigDecimal geometricMean;
    BigDecimal tickerPrice;
    BigDecimal gbceAllShares;

    public StockRatingsDTO() {
    }

    public StockRatingsDTO(Stock stock) {
        this.stock = stock;
        //BigDecimal meanPrice = null;

        SummaryStatistics stats = new SummaryStatistics();

        int nb = 0;
        BigDecimal totalStockTradePrice = BigDecimal.ZERO;
        double quantity = 0;
        for(StockTrade st: stock.getStockTrades()){
            //if zero or negative prices we will skip
            if(st.getPrice().compareTo(BigDecimal.ZERO)<=0){
                continue;
            }
            nb++;
            totalStockTradePrice = totalStockTradePrice.add(st.getPrice().multiply(BigDecimal.valueOf(st.getQuantity())));
            stats.addValue(st.getPrice().doubleValue());
            quantity+=st.getQuantity();
        }
        if(nb==0){
            return;
        }

        this.tickerPrice = totalStockTradePrice.divide(BigDecimal.valueOf(quantity), MathContext.DECIMAL64);
        double doubleGeometricMean = stats.getGeometricMean();
        this.geometricMean = BigDecimal.valueOf(doubleGeometricMean);

        if(!this.tickerPrice.equals(BigDecimal.ZERO)) {
            if (stock.getFixedDividend() == null || stock.getParValue() == null) {
                this.dividendYield = stock.getLastDividend().divide(tickerPrice, MathContext.DECIMAL64);
            } else {
                this.dividendYield = stock.getFixedDividend().multiply(stock.getParValue()).divide(tickerPrice, MathContext.DECIMAL64);
            }

        }
        if(!stock.getLastDividend().equals(BigDecimal.ZERO)){
            this.peRatio = this.tickerPrice.divide(stock.getLastDividend(), MathContext.DECIMAL64);
        }


    }

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }


    public BigDecimal getDividendYield() {
        return dividendYield;
    }

    public void setDividendYield(BigDecimal dividendYield) {
        this.dividendYield = dividendYield;
    }

    public BigDecimal getPeRatio() {
        return peRatio;
    }

    public void setPeRatio(BigDecimal peRatio) {
        this.peRatio = peRatio;
    }

    public BigDecimal getGeometricMean() {
        return geometricMean;
    }

    public void setGeometricMean(BigDecimal geometricMean) {
        this.geometricMean = geometricMean;
    }

    public BigDecimal getTickerPrice() {
        return tickerPrice;
    }

    public void setTickerPrice(BigDecimal tickerPrice) {
        this.tickerPrice = tickerPrice;
    }

    public BigDecimal getGbceAllShares() {
        return gbceAllShares;
    }

    public void setGbceAllShares(BigDecimal gbceAllShares) {
        this.gbceAllShares = gbceAllShares;
    }


}
