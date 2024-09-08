package lk.carrental.CarRental.service;

import lk.carrental.CarRental.dto.CustomerDto;
import lk.carrental.CarRental.dto.UserDto;
import lk.carrental.CarRental.model.Customer;
import lk.carrental.CarRental.model.User;
import lk.carrental.CarRental.repo.CustomerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class CustomerService {

    final CustomerRepo customerRepo;

    @Autowired
    public CustomerService(CustomerRepo customerRepo) {
        this.customerRepo = customerRepo;
    }

    public Customer saveCustomer(CustomerDto customerDto){
        if (customerDto != null){
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String encodedPassword = passwordEncoder.encode(customerDto.getPassword());

            Date currentDate = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            String formattedDateTime = formatter.format(currentDate);

            return customerRepo.save(new Customer(
                        customerDto.getFirstName(),
                        customerDto.getLastName(),
                        customerDto.getUserName(),
                        formattedDateTime,
                        encodedPassword
                    )
            );
        }
        return null;
    }

    public List<Customer> getAllCustomers(){
        return customerRepo.findAll();
    }

    public Customer customerLogin(CustomerDto dto){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        List<Customer> customers = customerRepo.findByUserName(dto.getUserName());
        for (Customer customer:customers) {
            if(passwordEncoder.matches(dto.getPassword(),customer.getPassword())){
                return customer;
            }
        }
        return null;
    }

    public Customer getCustomerById(String id){
        if (customerRepo.existsById(Long.valueOf(id))){
            return customerRepo.findById(Long.valueOf(id)).get();
        }else {
            return null;
        }
    }
}
