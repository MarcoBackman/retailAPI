package org.example.simpleapi.repository;

import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Marker;
import org.example.simpleapi.dao.CustomerDAOImpl;
import org.example.simpleapi.domain.Customer;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class CustomerRepository {

    private final CustomerDAOImpl customerDAO;

    CustomerRepository(CustomerDAOImpl customerDAO) {
        this.customerDAO = customerDAO;
    }

    public Customer getCustomerById(Marker mk, int id) {
        Customer result = customerDAO.getRecordById(mk, id);
        if (result == null) {
            log.warn(mk, "No customer found for customerId={}", id);
        }
        return result;
    }
    public int addCustomer(Marker mk, Customer customer) {
        return customerDAO.addRecord(mk, customer);
    }

    public int updateCustomer(Marker mk, Customer customer) {
        return customerDAO.updateRecord(mk, customer);
    }

}
