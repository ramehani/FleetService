package com.musalasoft.fleetservice.bean;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Entity
@Table(name = "DRONE")	
@Data
public class DronBean {

	@Id
	String serial_no;
	
	String model;
	double weight;
	@JsonProperty("battery")
	double battery_capasity;
	String state;
	
}
