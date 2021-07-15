package com.rubicon.demo.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rubicon.demo.model.Order;

@Repository
public interface OrderDao extends JpaRepository<Order, Integer> {

	Optional<Order> findByFarmid(int farmid);
}
