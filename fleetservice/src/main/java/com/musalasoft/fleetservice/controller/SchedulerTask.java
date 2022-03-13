package com.musalasoft.fleetservice.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.musalasoft.fleetservice.bean.DronBean;
import com.musalasoft.fleetservice.repository.DroneInterface;

@Component
public class SchedulerTask {

	Logger log = LogManager.getLogger(this.getClass());

	@Autowired
	DroneInterface drd;

	@Scheduled(fixedRate = 5000)
	public void cronJobSch() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		Date now = new Date();
		String strDate = sdf.format(now);
		log.info("drone battery reader :: " + strDate);
		
		List<DronBean> drdbeanlist = drd.findAll();
		
		for(DronBean dbean: drdbeanlist)
		{
			log.info("drone {} battery level {} " , dbean.getSerial_no(), dbean.getBattery_capasity());
		}
		
		
	}
}
