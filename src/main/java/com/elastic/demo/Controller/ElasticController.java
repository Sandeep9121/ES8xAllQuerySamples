package com.elastic.demo.Controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.elastic.clients.elasticsearch._types.ElasticsearchException;

@RestController
@RequestMapping("/api/part")
public class ElasticController {
	@Autowired
	VehicleService vehicleService;

	
	// curl to execute the api 
	/*
	 * 
	 curl --location --request GET 'http://localhost:8080/api/part' \
--header 'Content-Type: application/json'
	 */


	    @GetMapping
	    public ResponseEntity<List<Object>> createEmp() throws ElasticsearchException, IOException{
	    	//List<Object> vs=
	    			
	    			vehicleService.getVehicles();
	        return new  ResponseEntity<>(null,HttpStatus.OK);
	    }
	    
	    

}
