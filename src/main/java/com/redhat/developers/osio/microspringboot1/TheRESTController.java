package com.redhat.developers.osio.microspringboot1;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;

import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@EnableCircuitBreaker
@RestController
public class TheRESTController {
    // for inside of Kubernetes, lookup by service name
    // String customer_url = "http://microspringboot2:8080/";
    // for running outside of Kubernetes on local workstation
    String customer_url = "http://localhost:8082";
    
    // for inside of Kubernetes, lookup by service name
    // String orders_url = "http://microspringboot3:8080/";
    // for running outside of Kubernetes on local workstation
    String orders_url = "http://localhost:8083";
    
    /* autowire isn't working
    @Autowired
    private RestTemplate restTemplate;
    */
    RestTemplate restTemplate = new RestTemplate();

    @HystrixCommand(fallbackMethod = "topLevelFallback")
    @RequestMapping("/")
    public String getOrdersByCustomer() {
        System.out.println("\n\n** getOrdersByCustomer **\n\n");
        String customers = getCustomers();
        String orders = getOrders(customers);
        return orders;
    }

    @HystrixCommand(fallbackMethod = "getCustomersFallback")
    private String getCustomers() {
        System.out.println("* First Find Customers");
        ResponseEntity<String> response = restTemplate.getForEntity(
            customer_url + "/api/customer", String.class);        
        return response.getBody();        
    } // getCustomers

    @HystrixCommand(fallbackMethod = "getOrdersFallback")
    private String getOrders(String customers) {
        System.out.println("* Second Find Orders");
        ResponseEntity<String> response = restTemplate.getForEntity(
            orders_url + "/api/orders", String.class);        
        return customers + " - " + response.getBody();        
    }

    private String getCustomersFallback() {
        return "C997,C998";
    }
    
    private String getOrdersFallback() {
        return "O997,O998";
    }

    private String topLevelFallback() {
        return "Danger, Will Robinson";
    }
    
}