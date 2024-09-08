package lk.carrental.CarRental;

import jakarta.annotation.PostConstruct;
import lk.carrental.CarRental.model.User;
import lk.carrental.CarRental.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

@SpringBootApplication
public class CarRentalApplication {

	final UserRepo userRepo;

	@Autowired
	public CarRentalApplication(UserRepo userRepo) {
		this.userRepo = userRepo;
	}

	public static void main(String[] args) {
		SpringApplication.run(CarRentalApplication.class, args);
	}

	@PostConstruct
	public void initUsers() {
		if (userRepo.count() == 0) {
			BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
			String encodedPassword = passwordEncoder.encode("1234");
			List<User> users = List.of(
					new User("Prasad", "Indika", "prasad@gmail.com", encodedPassword, "Admin")
			);
			userRepo.saveAll(users);
		}
	}

}
