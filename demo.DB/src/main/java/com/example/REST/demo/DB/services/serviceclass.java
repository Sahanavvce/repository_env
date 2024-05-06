package com.example.REST.demo.DB.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.REST.demo.DB.Exception.CustomerIdNotFoundException;
import com.example.REST.demo.DB.Exception.CustomersNotFound;
import com.example.REST.demo.DB.controller.ErrorResponse;
import com.example.REST.demo.DB.controller.Response;
import com.example.REST.demo.DB.model.Customer;
import com.example.REST.demo.DB.repository.cusRepo;

@Service
public class serviceclass implements serviceInterface {

	@Autowired
	private cusRepo CusRepo;

	@Override
	public ResponseEntity insert(List<Customer> customers) {
	    try {
	        List<Customer> invalidCustomers = new ArrayList<>();
	        List<Customer> validCustomers = new ArrayList<>();

	        for (Customer customer : customers) {
	            if (customer.getPhonenum().length() != 10) {
	                invalidCustomers.add(customer);
	            } else if (customer.getName().isEmpty()) {
	                invalidCustomers.add(customer);
	            } else if (customer.getAddress().isEmpty()) {
	                invalidCustomers.add(customer);
	            } else {
	                validCustomers.add(customer);
	            }
	        }

	        if (!invalidCustomers.isEmpty()) {
	            ErrorResponse res = new ErrorResponse("Not acceptable", 406, "Customers are not valid.Check once. "
	            		+ "All fields should be not null and phone number should be 10 digits.");
	            return new ResponseEntity<>(res, HttpStatus.NOT_ACCEPTABLE);
	        }

	        CusRepo.saveAll(validCustomers);
	        Response res = new Response("Success", 201, "Customers created successfully", validCustomers);
	        return new ResponseEntity<>(res, HttpStatus.CREATED);

	    } catch (Exception e) {
	        Response res = new Response("Failure", 500, "Internal server error", null);
	        return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}

	@Override
	public ResponseEntity getAllCus() {
		List<Customer> customers = CusRepo.findAll();
		try {
			if (!customers.isEmpty()) {
				Response res = new Response("Success", 200, "All customers retrieved successfully", customers);
				return new ResponseEntity<>(res, HttpStatus.OK);
			} else {
				ErrorResponse res = new ErrorResponse("Failure", 404, CustomersNotFound.msg);
				return new ResponseEntity<>(res, HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			Response res = new Response("Failure", 500, "Internal server error", null);
			return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public ResponseEntity<?> getCusbyId(int id) throws CustomersNotFound,NullPointerException{
		try {
			if (CusRepo.existsById(id)) {
				Optional<Customer> customer = CusRepo.findById(id);
				Response res = new Response("Success", 200, "Customer retrieved successfully", customer);
				return new ResponseEntity<>(res, HttpStatus.OK);
			} else {
				ErrorResponse res = new ErrorResponse("Failure", 404, CustomerIdNotFoundException.msg);
				return new ResponseEntity<>(res, HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			Response res = new Response("Failure", 500, "Internal server error", null);
			return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public ResponseEntity deleteById(Integer id) throws CustomersNotFound{
		try {
			if (CusRepo.existsById(id)) {
				CusRepo.deleteById(id);
				Response res = new Response("Success", 200, "Customer deleted successfully", null);
				return new ResponseEntity<>(res, HttpStatus.OK);
			} else {
				ErrorResponse res = new ErrorResponse("Failure", 404, CustomerIdNotFoundException.msg);
				return new ResponseEntity<>(res, HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			Response res = new Response("Failure", 500, "Internal server error", null);
			return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public ResponseEntity updatebyId(int id, Customer customer) {
		// TODO Auto-generated method stub

		try {
			if (CusRepo.existsById(id)) {
				if (customer.getPhonenum().length() == 10) {
					Customer cus = CusRepo.findById(id).get();
					cus.setPhonenum(customer.getPhonenum());
					CusRepo.save(cus);
				}
				if (customer.getName() != "") {
					Customer cus = CusRepo.findById(id).get();
					cus.setName(customer.getName());
					CusRepo.save(cus);
				}
				if (customer.getAddress() != "") {
					Customer cus = CusRepo.findById(id).get();
					cus.setAddress(customer.getAddress());
					CusRepo.save(cus);
				}
				Response res = new Response("Success", 200, "Customer updated succesfully", customer);
				return new ResponseEntity<>(res, HttpStatus.OK);
			} else {
				ErrorResponse res = new ErrorResponse("Failure", 404, CustomerIdNotFoundException.msg);
				return new ResponseEntity<>(res, HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			Response res = new Response("Failure", 500, "Internal server error", null);
			return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}