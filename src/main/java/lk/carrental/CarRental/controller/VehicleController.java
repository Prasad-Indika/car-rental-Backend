package lk.carrental.CarRental.controller;

import lk.carrental.CarRental.dto.VehicleDto;
import lk.carrental.CarRental.model.Vehicle;
import lk.carrental.CarRental.service.VehicleService;
import lk.carrental.CarRental.util.JWTTokenGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(path = "api/vehicle")
public class VehicleController {

    final VehicleService vehicleService;
    final JWTTokenGenerator jwtTokenGenerator;

    @Autowired
    public VehicleController(VehicleService vehicleService, JWTTokenGenerator jwtTokenGenerator) {
        this.vehicleService = vehicleService;
        this.jwtTokenGenerator = jwtTokenGenerator;
    }

    @PostMapping
    public ResponseEntity<Object> saveVehicle(@ModelAttribute VehicleDto dto) throws IOException, URISyntaxException {
        Vehicle save = vehicleService.saveVehicle(dto);
        return new ResponseEntity<>(save, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Vehicle>> getAllVehicles (@RequestHeader(name = "Authorization") String authorizationHeader){
        if(this.jwtTokenGenerator.validateJwtToken(authorizationHeader)){
            List<Vehicle> all = vehicleService.getAllVehicles();
            return new ResponseEntity<>(all,HttpStatus.OK);
        }else {
            return null;
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteVehicle(@PathVariable Long id, @RequestHeader(name = "Authorization") String authorizationHeader) {
        if (jwtTokenGenerator.validateJwtToken(authorizationHeader)) {
            String deleted = vehicleService.deleteVehicle(id);
            return new ResponseEntity<>(deleted, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Invalid Token" , HttpStatus.FORBIDDEN);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateVehicle(@PathVariable Long id , @ModelAttribute VehicleDto dto , @RequestHeader(name = "Authorization") String authorizationHeader ){
        if (jwtTokenGenerator.validateJwtToken(authorizationHeader)) {
            Vehicle update = vehicleService.updateVehicle(id,dto);
            return new ResponseEntity<>(update, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Invalid Token" , HttpStatus.FORBIDDEN);
        }
    }






}
