package lk.carrental.CarRental.controller;

import lk.carrental.CarRental.dto.CustomerDto;
import lk.carrental.CarRental.model.Customer;
import lk.carrental.CarRental.model.User;
import lk.carrental.CarRental.service.CustomerService;
import lk.carrental.CarRental.util.JWTTokenGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping(path = "api/customer")
public class CustomerController {

    final CustomerService customerService;
    final JWTTokenGenerator jwtTokenGenerator;

    @Autowired
    public CustomerController(CustomerService customerService, JWTTokenGenerator jwtTokenGenerator) {
        this.customerService = customerService;
        this.jwtTokenGenerator = jwtTokenGenerator;
    }

    @PostMapping
    public ResponseEntity<Object> saveCustomer(@RequestBody CustomerDto dto){
        Customer save = customerService.saveCustomer(dto);
        return new ResponseEntity<>(save, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Customer>> getAllCustomers (@RequestHeader(name = "Authorization") String authorizationHeader){
        if(this.jwtTokenGenerator.validateJwtToken(authorizationHeader)){
            List<Customer> all = customerService.getAllCustomers();
            return new ResponseEntity<>(all,HttpStatus.OK);
        }else {
            return null;
        }
    }

    @PostMapping("/login")
    public Map<String, String> customerLogin(@RequestBody CustomerDto dto) {
        Customer customer = customerService.customerLogin(dto);
        Map<String, String> response = new HashMap<>();
        if (customer == null) {
            response.put("massage", "wrong details");
        } else {
            String token = this.jwtTokenGenerator.generateJwtTokenForCustomer(customer);
            response.put("token", token);
        }
        return response;
    }

    @PostMapping("/get_customer_by_token")
    public ResponseEntity<Object> getCustomerInfoByToken(@RequestHeader(name = "Authorization") String authorizationHeader) {
        if (this.jwtTokenGenerator.validateJwtToken(authorizationHeader)) {
            Customer customerFromJwtToken = this.jwtTokenGenerator.getCustomerFromJwtToken(authorizationHeader);
            return new ResponseEntity<>(customerFromJwtToken, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("TOKEN INVALID", HttpStatus.UNAUTHORIZED);
        }
    }


}
