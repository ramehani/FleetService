package com.musalasoft.fleetservice.webhander;

import lombok.Data;

@Data
public class ResponseHandler {

	private String responsecode;
	private String responseDescription;
	private Object data;
	
}
