package com.jpmorgan.trade.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.jpmorgan.trade.domain.enumeration.StockTypeEnum;

/**
 * A Stock.
 */
@Entity
@Table(name = "stock")
public class Stock implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "symbol", nullable = false)
    private String symbol;
    
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private StockTypeEnum type;
    
    @Column(name = "last_dividend", precision=10, scale=2)
    private BigDecimal lastDividend;
    
    @Column(name = "fixed_dividend", precision=10, scale=2)
    private BigDecimal fixedDividend;
    
    @Column(name = "par_value", precision=10, scale=2)
    private BigDecimal parValue;
    
    @OneToMany(mappedBy = "stock")
    @JsonIgnore
    private Set<StockTrade> stockTrades = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSymbol() {
        return symbol;
    }
    
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public StockTypeEnum getType() {
        return type;
    }
    
    public void setType(StockTypeEnum type) {
        this.type = type;
    }

    public BigDecimal getLastDividend() {
        return lastDividend;
    }
    
    public void setLastDividend(BigDecimal lastDividend) {
        this.lastDividend = lastDividend;
    }

    public BigDecimal getFixedDividend() {
        return fixedDividend;
    }
    
    public void setFixedDividend(BigDecimal fixedDividend) {
        this.fixedDividend = fixedDividend;
    }

    public BigDecimal getParValue() {
        return parValue;
    }
    
    public void setParValue(BigDecimal parValue) {
        this.parValue = parValue;
    }

    public Set<StockTrade> getStockTrades() {
        return stockTrades;
    }

    public void setStockTrades(Set<StockTrade> stockTrades) {
        this.stockTrades = stockTrades;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Stock stock = (Stock) o;
        if(stock.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, stock.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Stock{" +
            "id=" + id +
            ", symbol='" + symbol + "'" +
            ", type='" + type + "'" +
            ", lastDividend='" + lastDividend + "'" +
            ", fixedDividend='" + fixedDividend + "'" +
            ", parValue='" + parValue + "'" +
            '}';
    }
}
