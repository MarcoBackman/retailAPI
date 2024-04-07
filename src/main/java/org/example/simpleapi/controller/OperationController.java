package org.example.simpleapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.example.simpleapi.dao.TransactionDAOImpl;
import org.example.simpleapi.domain.Customer;
import org.example.simpleapi.domain.Transaction;
import org.example.simpleapi.repository.CustomerRepository;
import org.example.simpleapi.util.TestOnly;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Operation controller for manual operation
 */
@RestController
@RequestMapping("api/v2/ops")
@Log4j2
public class OperationController {

    private final Marker mk = MarkerManager.getMarker("OPS_CONTROLLER");
    private final CustomerRepository customerRepository;
    private final TransactionDAOImpl transactionDAO;

    OperationController(CustomerRepository customerRepository,
                        TransactionDAOImpl transactionDAO) {
        this.customerRepository = customerRepository;
        this.transactionDAO = transactionDAO;
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

    @TestOnly
    @Operation(description = "Add customer data manually. Need to provide id accordingly.")
    @PostMapping("/customer/add/")
    public ResponseEntity<?> addCustomerData(@RequestBody(required = true) Customer customer) {
        log.info(mk, "Adding customer manually customer id={}, name={}", customer.getCustomerId(), customer.getUserName());
        long generatedId = customerRepository.addCustomer(mk, customer);
        if (generatedId == -1) {
            return ResponseEntity.ok("Error on inserting new customer data");
        }
        return ResponseEntity.ok(generatedId);
    }

    @Operation(description = "Get existing transaction data by customerId.")
    @GetMapping("/transaction/customerId/{customerId}")
    public ResponseEntity<List<Transaction>> getTransactionDataByCustomerId(@PathVariable(required = true) Integer customerId) {
        log.info(mk, "Adding customer detail by customerId={}", customerId);
        List<Transaction> transactionList = transactionDAO.getRecordByCustomerId(mk, customerId);
        return ResponseEntity.ok(transactionList);
    }

    @Operation(description = "Get existing transaction data by transactionId.")
    @GetMapping("/transaction/transactionId/{transactionId}")
    public ResponseEntity<Transaction> getTransactionDataByTransactionId(@PathVariable(required = true) Integer transactionId) {
        log.info(mk, "Adding customer detail by transactionId={}", transactionId);
        Transaction transaction = transactionDAO.getRecordById(mk, transactionId);
        return ResponseEntity.ok(transaction);
    }


}
