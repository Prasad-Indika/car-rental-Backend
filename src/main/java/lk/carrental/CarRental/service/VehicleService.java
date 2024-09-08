package lk.carrental.CarRental.service;

import lk.carrental.CarRental.dto.VehicleDto;
import lk.carrental.CarRental.model.Vehicle;
import lk.carrental.CarRental.model.VehicleImage;
import lk.carrental.CarRental.repo.VehicleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@Service
public class VehicleService {

    final VehicleRepo vehicleRepo;

    @Autowired
    public VehicleService(VehicleRepo vehicleRepo) {
        this.vehicleRepo = vehicleRepo;
    }

    public Vehicle saveVehicle(VehicleDto dto) throws IOException, URISyntaxException  {
        Vehicle newVehicle = new Vehicle(
                dto.getBrandName(),
                dto.getModuleName(),
                dto.getPassengers(),
                dto.getFuelType(),
                dto.getTransmissionType(),
                dto.getDailyRentalPrice(),
                dto.getDailyLimitKilometers(),
                dto.getExtraKm(),
                dto.getStatus()
        );

        List<VehicleImage> vehicleImgs = new ArrayList<>();

        for (MultipartFile file: dto.getImages()) {
            VehicleImage img = new VehicleImage();

            String absolutePath = new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI()).getParentFile().getParentFile().getAbsolutePath();
            File uploadDir = new File(absolutePath + "/src/main/resources/static/uploads");
            uploadDir.mkdir();
            file.transferTo(new File(uploadDir.getAbsolutePath() + "/" +file.getOriginalFilename()));
            img.setImage("uploads/" +file.getOriginalFilename());
            img.setVehicle(newVehicle);
            vehicleImgs.add(img);
        }

        newVehicle.setVehicleImgs(vehicleImgs);
        Vehicle save = vehicleRepo.save(newVehicle);

        return save;
    }

    public List<Vehicle> getAllVehicles(){
        return vehicleRepo.findAll();
    }

    public Vehicle updateVehicle(Long id,VehicleDto dto){
        if(vehicleRepo.existsById(id)){
            Vehicle vehicle = vehicleRepo.findById(id).get();

            vehicle.setBrandName(dto.getBrandName());
            vehicle.setModuleName(dto.getModuleName());
            vehicle.setPassengers(dto.getPassengers());
            vehicle.setFuelType(dto.getFuelType());
            vehicle.setTransmissionType(dto.getTransmissionType());
            vehicle.setDailyRentalPrice(dto.getDailyRentalPrice());
            vehicle.setDailyLimitKilometers(dto.getDailyLimitKilometers());
            vehicle.setExtraKm(dto.getExtraKm());

            return vehicleRepo.save(vehicle);
        }
        return null;
    }

    public String deleteVehicle(Long id) {
        if(vehicleRepo.existsById(id)){
            vehicleRepo.deleteById(id);
            return "Suceess";
        }
        return "Error";
    }



}
