package com.redhat.developers.osio.microspringboot1;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@Slf4j
public class TheRESTController {


    @Autowired
    RestTemplate restTemplate;

    @Autowired
    CustomersService customersService;


    @RequestMapping("/")
    @HystrixCommand(fallbackMethod = "topLevelFallback")
    public String getCustomersWithOrders() {
        log.info("getCustomers()");
        String customers = customersService.getCustomers();
        log.info("Got Customers: ", customers);

        String customerOrders = customersService.getCustomerOrders(customers);
        
        return customerOrders;
    } // getCustomers

    private String topLevelFallback() {
        return "Danger, Will Robinson";
    }

}