package com.consumer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.consumer.service.ConsumerService;

@RestController
public class ConsumerController {

	@Autowired
	private ConsumerService consumerService;
	
	@GetMapping(value = "/test")
	public String test() {
		return "SUCCESS - Testing Spring Boot consumer app !!";
	}
	
	@GetMapping(value = "/getUsers")
	public String[] getUsers() {
		return consumerService.getUsers();
	}
}
