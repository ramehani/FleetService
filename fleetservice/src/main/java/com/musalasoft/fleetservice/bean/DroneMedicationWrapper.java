package com.musalasoft.fleetservice.bean;

import java.util.List;

import lombok.Data;

@Data
public class DroneMedicationWrapper {
	
	String serial_no;
	List<DroneMedication> medicatinlist;

}
