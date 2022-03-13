package com.musalasoft.fleetservice.controller;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.musalasoft.fleetservice.bean.DronBean;
import com.musalasoft.fleetservice.bean.DroneMedication;
import com.musalasoft.fleetservice.bean.DroneMedicationWrapper;
import com.musalasoft.fleetservice.repository.DroneInterface;
import com.musalasoft.fleetservice.repository.DroneMedicationRepo;
import com.musalasoft.fleetservice.util.Model;
import com.musalasoft.fleetservice.util.State;
import com.musalasoft.fleetservice.webhander.ResponseHandler;

@RestController
@RequestMapping("/fleetservice")
public class FleetController {

	Logger log = LogManager.getLogger(this.getClass());

	@Autowired
	DroneInterface drd;

	@Autowired
	DroneMedicationRepo medicationrepo;

	@RequestMapping(value = "/registerDrone", method = RequestMethod.POST)
	public ResponseHandler registerDrone(@RequestBody DronBean drone) {
		ResponseHandler rsp = new ResponseHandler();

		Optional<DronBean> drdbean = drd.findById(drone.getSerial_no());

		if (drdbean.isPresent()) {
			rsp.setResponsecode("99");
			rsp.setResponseDescription("Drone Serial Already exist");
		} else {

			boolean isModelValid = true;

			try {
				Model.valueOf(drone.getModel().toUpperCase());
			} catch (IllegalArgumentException e) {
				isModelValid = false;
			}
			boolean isStatusValid = true;

			try {
				State.valueOf(drone.getState().toUpperCase());
			} catch (IllegalArgumentException e) {
				isStatusValid = false;
			}

			if (!isModelValid) {
				rsp.setResponsecode("99");
				rsp.setResponseDescription("Model Does Not Exist");
			} else if (!isStatusValid) {
				rsp.setResponsecode("99");
				rsp.setResponseDescription("State Does Not Exist");
			}else if (drone.getWeight() > 500) {
				rsp.setResponsecode("99");
				rsp.setResponseDescription("Maximum Capasity is 500g only");
			} else {
				drd.save(drone);
				rsp.setResponsecode("00");
				rsp.setResponseDescription("Drone Save Sucessfully");
			}

		}
		log.info(" dron vale {}", drone.getSerial_no());

		return rsp;
	}

	@RequestMapping(value = "/getIdleDrones", method = RequestMethod.GET)
	public ResponseHandler getIdleDrones() {
		ResponseHandler rsp = new ResponseHandler();

		List<DronBean> drdbean = drd.findByState("IDLE");

		if (drdbean.size() == 0) {
			rsp.setResponsecode("99");
			rsp.setResponseDescription("No Idle Drone Available");
		} else {
			rsp.setResponsecode("00");
			rsp.setResponseDescription("Available Drones");
			rsp.setData(drdbean);
		}

		return rsp;
	}

	@RequestMapping(value = "/getDroneDetails/{serial_no}", method = RequestMethod.GET)
	public ResponseHandler getIdleDrones(@PathVariable("serial_no") String serial_no) {
		ResponseHandler rsp = new ResponseHandler();

		log.info(" serial no : {}", serial_no);

		Optional<DronBean> drdbean = drd.findById(serial_no);

		if (!drdbean.isPresent()) {
			rsp.setResponsecode("99");
			rsp.setResponseDescription("No Drone Available");
		} else {
			rsp.setResponsecode("00");
			rsp.setResponseDescription("Available Drones");
			rsp.setData(drdbean.get().getBattery_capasity());
		}

		return rsp;
	}

	@RequestMapping(value = "/loadDrone", method = RequestMethod.POST)
	public ResponseHandler loadDrones(@RequestBody DroneMedicationWrapper drbm) {
		ResponseHandler rsp = new ResponseHandler();

		Optional<DronBean> drdbean = drd.findById(drbm.getSerial_no());

		if (!drdbean.isPresent()) {
			rsp.setResponsecode("99");
			rsp.setResponseDescription("No Drone Available");
		} else {

			DronBean dbmnea = drdbean.get();

			if (!dbmnea.getState().equals("IDLE")) {
				rsp.setResponsecode("99");
				rsp.setResponseDescription("This Drone either loaded or on delivery state");
				return rsp;
			} else if (dbmnea.getBattery_capasity() < 25) {
				rsp.setResponsecode("99");
				rsp.setResponseDescription("Drone low battery");
				return rsp;
			}

			double current_droneCapsity = drdbean.get().getWeight();
			double loaded_capsity = 0;

			boolean isrecordvalid = true;

			for (DroneMedication drbmbean : drbm.getMedicatinlist()) {
				loaded_capsity += drbmbean.getWeight();

				if (!patternMatcher("^[A-Za-z0-9_-]+$", drbmbean.getName())) {
					isrecordvalid = false;
				}

				if (!patternMatcher("^[A-Za-z0-9_]+$", drbmbean.getCode())) {
					isrecordvalid = false;
				}

			}

			if (loaded_capsity > current_droneCapsity) {
				rsp.setResponsecode("99");
				rsp.setResponseDescription("Drone is Overloaded");
			} else if (!isrecordvalid) {
				rsp.setResponsecode("99");
				rsp.setResponseDescription("Data Contains an issue");
			} else {
				for (DroneMedication drbmbean : drbm.getMedicatinlist()) {
					drbmbean.setSerial_no(drbm.getSerial_no());
					medicationrepo.save(drbmbean);
				}
				DronBean dsbbean = drdbean.get();
				dsbbean.setState("LOADED");
				drd.save(dsbbean);
				rsp.setResponsecode("00");
				rsp.setResponseDescription("Drone Medication save sucessfull");
			}

		}

		return rsp;
	}

	@RequestMapping(value = "/getDroneLoadedDetails/{serial_no}", method = RequestMethod.POST)
	public ResponseHandler getDroneLoaded(@PathVariable("serial_no") String serial_no) {
		ResponseHandler rsp = new ResponseHandler();

		Optional<DronBean> drdbean = drd.findById(serial_no);

		if (!drdbean.isPresent()) {
			rsp.setResponsecode("99");
			rsp.setResponseDescription("No Drone Available");
		} else {

			Optional<DroneMedication> record = medicationrepo.findById(serial_no);

			if (record.isPresent()) {
				rsp.setResponsecode("00");
				rsp.setResponseDescription("Record Available");
				rsp.setData(record);
			} else {
				rsp.setResponsecode("99");
				rsp.setResponseDescription("Drone is empty");
				rsp.setData(record);
			}

		}

		return rsp;
	}

	boolean patternMatcher(String regxpat, String value) {
		return Pattern.compile(regxpat).matcher(value).matches();
	}
}
