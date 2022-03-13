package com.musalasoft.fleetservice.bean;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "DRONE_MEDICATION")
public class DroneMedication {

	@Id
	String serial_no;
	String name;
	double weight;
	String code;
	String image;

}
