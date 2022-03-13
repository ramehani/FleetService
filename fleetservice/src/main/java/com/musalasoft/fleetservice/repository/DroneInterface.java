package com.musalasoft.fleetservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.musalasoft.fleetservice.bean.DronBean;

public interface DroneInterface extends JpaRepository<DronBean, String>{

	List<DronBean> findByState(String string);


}
