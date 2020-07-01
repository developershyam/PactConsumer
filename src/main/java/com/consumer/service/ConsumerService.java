package com.consumer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ConsumerService {

	@Autowired
	private RestTemplate restTemplate;
	
	@Value("${provider.baseURL}")
	private String providerPath;
	
	public String[] getUsers() {
		return restTemplate.getForObject(providerPath+"/getUsers", String[].class);
	}
}
