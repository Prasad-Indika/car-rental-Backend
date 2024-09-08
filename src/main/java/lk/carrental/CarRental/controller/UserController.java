package lk.carrental.CarRental.controller;

import lk.carrental.CarRental.dto.UserDto;
import lk.carrental.CarRental.model.User;
import lk.carrental.CarRental.model.Vehicle;
import lk.carrental.CarRental.service.UserService;
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
@RequestMapping(path = "api/user")
public class UserController {

    final UserService userService;
    final JWTTokenGenerator jwtTokenGenerator;

    @Autowired
    public UserController(UserService userService, JWTTokenGenerator jwtTokenGenerator) {
        this.userService = userService;
        this.jwtTokenGenerator = jwtTokenGenerator;
    }

    @PostMapping
    public ResponseEntity<Object> saveUser(@RequestBody UserDto dto){
        User save = userService.saveUSer(dto);
        return new ResponseEntity<>(save, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public Map<String, String> userLogin(@RequestBody UserDto dto) {
        User user = userService.userLogin(dto);
        Map<String, String> response = new HashMap<>();
        if (user == null) {
            response.put("massage", "wrong details");
        } else {
            String token = this.jwtTokenGenerator.generateJwtTokenForUser(user);
            response.put("token", token);
        }
        return response;
    }

    @PostMapping("/get_user_by_token")
    public ResponseEntity<Object> getUserInfoByToken(@RequestHeader(name = "Authorization") String authorizationHeader) {
        if (this.jwtTokenGenerator.validateJwtToken(authorizationHeader)) {
            User userFromJwtToken = this.jwtTokenGenerator.getUserFromJwtToken(authorizationHeader);
            return new ResponseEntity<>(userFromJwtToken, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("TOKEN INVALID", HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers (@RequestHeader(name = "Authorization") String authorizationHeader){
        if(this.jwtTokenGenerator.validateJwtToken(authorizationHeader)){
            List<User> all = userService.getAllUsers();
            return new ResponseEntity<>(all,HttpStatus.OK);
        }else {
            return null;
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable Long id, @RequestHeader(name = "Authorization") String authorizationHeader) {
        if (jwtTokenGenerator.validateJwtToken(authorizationHeader)) {
            String delete = userService.deleteUser(id);
            return new ResponseEntity<>(delete, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Invalid Token", HttpStatus.FORBIDDEN);
        }
    }

}
