package com.rubicon.demo.conroller;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rubicon.demo.dao.OrderDao;
import com.rubicon.demo.model.Order;

@RestController
public class OrderController {

	@Autowired
	OrderDao repo;
	
	@GetMapping("/order/{farmid}") 
	public String getOrderStatus(@PathVariable("farmid") int farmid) throws ParseException{
		Optional<Order> order = repo.findByFarmid(farmid);
		String status = null;
		if(order.isPresent()) {
			Order existingOrder = order.get();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			Date orderDate = dateFormat.parse(existingOrder.getStartdatetime());
			Timestamp orderts = new Timestamp(orderDate.getTime());
			orderDate.setHours(orderDate.getHours() + existingOrder.getDuration());
			Timestamp orderDelivery = new Timestamp(orderDate.getTime());
			Date now = new Date();
			Timestamp nowts = new Timestamp(now.getTime());
			if(nowts.after(orderDelivery)) {
				status = "Delivered – Order has been delivered";
			} else if(nowts.before(orderts)) {
				status = "Requested – Order has been placed but not yet delivered";
			} else if(nowts.after(orderts) && nowts.before(orderDelivery)) {
				status = "InProgress – Order is being delivered right now";
			}
		} else {
			status = "Cancelled – Order was cancelled before delivery";
		}
		return status;
	}
	 
	@PutMapping(path="/order", consumes = {"application/json"})
	public String createOrder(@RequestBody Order order) throws ParseException {
		Optional<Order> ord = repo.findByFarmid(order.getFarmid());
		String status = null;
		if(!ord.isEmpty()) {
			Order existingOrder = ord.get();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		    Date orderDate = dateFormat.parse(existingOrder.getStartdatetime());
		    orderDate.setHours(orderDate.getHours() + existingOrder.getDuration());
		    Timestamp existingorderTs = new Timestamp(orderDate.getTime()); 
		    Date newOrder = dateFormat.parse(order.getStartdatetime());
		    Timestamp newOrderTs = new Timestamp(newOrder.getTime());
		    if(newOrderTs.after(existingorderTs)) {
		    	repo.save(order);
		    	status = "Order created";
		    } else if(newOrderTs.before(existingorderTs)) {
		    	status = "Order cannot be created";
		    }
		} else {
			repo.save(order);
			status = "Order created";
		}
		return status;
	}
	
	@DeleteMapping("/order/{farmid}")
	public String deleteOrder(@PathVariable int farmid) throws ParseException {
		Optional<Order> ord = repo.findByFarmid(farmid);
		String status = null;
		if(ord.isPresent()) {
			Order existingOrder = ord.get();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		    Date orderDate = dateFormat.parse(existingOrder.getStartdatetime());
		    Timestamp existingorderTs = new Timestamp(orderDate.getTime()); 
		    Date now = new Date();
			Timestamp nowts = new Timestamp(now.getTime());
			if(nowts.before(existingorderTs)) {
				repo.deleteById(existingOrder.getOrderid());
				status = "deleted";
			} else {
				status = "Order cannot be deleted";
			}
		} else {
			status = "Order doesn't exist";
		}
		
		return status;
	}
	
}
