package lk.carrental.CarRental.repo;

import lk.carrental.CarRental.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleRepo extends JpaRepository<Vehicle,Long> {

}
