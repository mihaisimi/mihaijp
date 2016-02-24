package com.jpmorgan.trade.repository;

import com.jpmorgan.trade.domain.Stock;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Stock entity.
 */
public interface StockRepository extends JpaRepository<Stock,Long> {

}
