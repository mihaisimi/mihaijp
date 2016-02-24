package com.jpmorgan.trade.repository;

import com.jpmorgan.trade.domain.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Stock entity.
 */
public interface StockRepository extends JpaRepository<Stock,Long> {

    @Query("select distinct stock from Stock stock left join fetch stock.stockTrades where stock.id =:id")
    Stock findOneWithEagerTrades(@Param("id") Long id);

    //@Query(value = "select  stock_id, SUM(price * quantity) / SUM(quantity) from stock_trade group by stock_id",nativeQuery = true)
    @Query(value = "SELECT EXP( SUM( LOG( avgPrice ) ) / COUNT( avgPrice ) ) FROM  " +
        "(select  SUM(price * quantity) / SUM(quantity) as avgPrice from stock_trade st " +
        "where time_to_sec(timediff(now(),st.trade_date))<900 " +
        "group by st.stock_id) " +
        "as a",nativeQuery = true)
    Object findAveragePriceForStocks();

}
