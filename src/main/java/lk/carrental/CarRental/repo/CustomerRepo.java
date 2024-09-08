package lk.carrental.CarRental.repo;

import lk.carrental.CarRental.model.Customer;
import lk.carrental.CarRental.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerRepo extends JpaRepository<Customer,Long> {
    List<Customer> findByUserName(String userName);
}
