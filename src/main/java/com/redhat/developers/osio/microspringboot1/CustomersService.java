package com.redhat.developers.osio.microspringboot1;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "customer", url = "${microspringboot1.customer_url}",
        fallback = CustomersService.CustomerServiceFallback.class)
public interface CustomersService {

    @RequestMapping(path = "/api/customer")
    String getCustomers();


    @RequestMapping(path = "/api/customer/orders", method = RequestMethod.POST)
    String getCustomerOrders(String customers);

    @Component
    class CustomerServiceFallback implements CustomersService {
        @Override
        public String getCustomers() {
            System.out.println("Fallback on getCustomers()");
            return "C100,C101,C103";
        }

        @Override
        public String getCustomerOrders(String customers) {
            System.out.println("Fallback on getCustomerOrders()");
            return "Fallback(No Orders)";
        }
    }

}
