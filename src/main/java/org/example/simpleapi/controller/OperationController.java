package org.example.simpleapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.example.simpleapi.domain.Customer;
import org.example.simpleapi.repository.CustomerRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ops")
@Log4j2
public class OperationController {

    private final Marker mk = MarkerManager.getMarker("OPS_CONTROLLER");
    private final CustomerRepository customerRepository;

    OperationController(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Operation(description = "Retrieve specific customer info by customerId")
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<Customer> getCustomerInfoById(@PathVariable(name = "customerId") Integer customerId) {
        log.info(mk, "Fetching customer detail by customerId={}", customerId);
        Customer customer = customerRepository.getCustomerById(mk, customerId);
        return ResponseEntity.ok(customer);
    }

    @Operation(description = "Update customer data by customerId.")
    @PatchMapping("/customer/")
    public ResponseEntity<Integer> updateCustomerDetailsByCustomerId(@RequestBody(required = true) Customer customer) {
        log.info(mk, "Executing update customer detail by customerId={}", customer.getCustomerId());
        int result = customerRepository.updateCustomer(mk, customer);
        return ResponseEntity.ok(result);
    }

    @Operation(description = "Add existing customer data by customerId.")
    @PostMapping("/customer/")
    public ResponseEntity<Integer> addCustomerData(@RequestBody(required = true) Customer customer) {
        log.info(mk, "Adding customer detail by customerId={}", customer.getCustomerId());
        int result = customerRepository.addCustomer(mk, customer);
        return ResponseEntity.ok(result);
    }


}
