package com.jpmorgan.trade.domain;

import java.time.ZonedDateTime;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A StockTrade.
 */
@Entity
@Table(name = "stock_trade")
public class StockTrade implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "quantity", nullable = false)
    private Long quantity;
    
    @NotNull
    @Column(name = "price", precision=10, scale=2, nullable = false)
    private BigDecimal price;
    
    @Column(name = "is_sell")
    private Boolean isSell;
    
    @NotNull
    @Column(name = "trade_date", nullable = false)
    private ZonedDateTime tradeDate;
    
    @ManyToOne
    @JoinColumn(name = "stock_id")
    private Stock stock;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getQuantity() {
        return quantity;
    }
    
    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }
    
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Boolean getIsSell() {
        return isSell;
    }
    
    public void setIsSell(Boolean isSell) {
        this.isSell = isSell;
    }

    public ZonedDateTime getTradeDate() {
        return tradeDate;
    }
    
    public void setTradeDate(ZonedDateTime tradeDate) {
        this.tradeDate = tradeDate;
    }

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StockTrade stockTrade = (StockTrade) o;
        if(stockTrade.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, stockTrade.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "StockTrade{" +
            "id=" + id +
            ", quantity='" + quantity + "'" +
            ", price='" + price + "'" +
            ", isSell='" + isSell + "'" +
            ", tradeDate='" + tradeDate + "'" +
            '}';
    }
}
