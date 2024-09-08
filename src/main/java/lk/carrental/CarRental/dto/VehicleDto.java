package lk.carrental.CarRental.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class VehicleDto {
    private Long vehicleId;
    private String brandName;
    private String moduleName;
    private int passengers;
    private String fuelType;
    private String transmissionType;
    private String dailyRentalPrice;
    private String dailyLimitKilometers;
    private String extraKm;
    private String status;

    private List<MultipartFile> images;
}
