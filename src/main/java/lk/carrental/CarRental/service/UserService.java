package lk.carrental.CarRental.service;

import lk.carrental.CarRental.dto.UserDto;
import lk.carrental.CarRental.model.User;
import lk.carrental.CarRental.repo.UserRepo;
import lk.carrental.CarRental.util.JWTTokenGenerator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    final UserRepo userRepo;
    final ModelMapper modelMapper;
    //final JWTTokenGenerator jwtTokenGenerator;

    @Autowired
    public UserService(UserRepo userRepo, ModelMapper modelMapper) {
        this.userRepo = userRepo;
        this.modelMapper = modelMapper;
    }

    public User saveUSer(UserDto userDto){
        if (userDto != null){
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String encodedPassword = passwordEncoder.encode(userDto.getPassword());

            return userRepo.save(new User(userDto.getFirstName(), userDto.getLastName(), userDto.getUserName(), encodedPassword, userDto.getRole()));
        }
        return null;
    }

    public User getUserById(String id){
        if (userRepo.existsById(Long.valueOf(id))){
            return userRepo.findById(Long.valueOf(id)).get();
        }else {
            return null;
        }
    }

    public User userLogin(UserDto dto){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        List<User> users = userRepo.findByUserName(dto.getUserName());
        for (User user:users) {
            if(passwordEncoder.matches(dto.getPassword(),user.getPassword())){
                return user;
            }
        }
        return null;
    }

    public List<User> getAllUsers(){
        return userRepo.findAll();
    }

    public String deleteUser(Long id){
        if(userRepo.existsById(id)){
            userRepo.deleteById(id);
            return "Success";
        }
        else{
            return "NoUser";
        }
    }

}
