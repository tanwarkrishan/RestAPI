package com.rubicon.demo.model;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="waterorder")
public class Order {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int orderid;
	private int farmid;
	private String startdatetime;
	private int duration;
	
	public int getOrderid() {
		return orderid;
	}
	public void setOrderid(int orderid) {
		this.orderid = orderid;
	}
	public int getFarmid() {
		return farmid;
	}
	public void setFarmid(int farmid) {
		this.farmid = farmid;
	}
	public String getStartdatetime() {
		return startdatetime;
	}
	public void setStartdatetime(String startdatetime) {
		this.startdatetime = startdatetime;
	}
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
	
	@Override
	public String toString() {
		return "Order [orderid=" + orderid + ", farmid=" + farmid + ", startdatetime=" + startdatetime + ", duration="
				+ duration + "]";
	}
	
}
