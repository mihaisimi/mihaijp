package com.jpmorgan.trade.repository;

import com.jpmorgan.trade.domain.StockTrade;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the StockTrade entity.
 */
public interface StockTradeRepository extends JpaRepository<StockTrade,Long> {

}
