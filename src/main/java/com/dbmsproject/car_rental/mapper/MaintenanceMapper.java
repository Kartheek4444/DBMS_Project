package com.dbmsproject.car_rental.mapper;

import com.dbmsproject.car_rental.dto.MaintenanceDto;
import com.dbmsproject.car_rental.model.Maintenance;
import com.dbmsproject.car_rental.model.Staff;
import com.dbmsproject.car_rental.model.Vehicle;
import org.springframework.stereotype.Component;

@Component
public class MaintenanceMapper {

    public MaintenanceDto toDto(Maintenance maintenance) {
        if (maintenance == null) {
            return null;
        }

        MaintenanceDto.MaintenanceDtoBuilder builder = MaintenanceDto.builder()
                .maintenanceId(maintenance.getMaintenanceId())
                .vehicleId(maintenance.getVehicle().getVehicleId())
                .serviceDate(maintenance.getServiceDate())
                .serviceType(maintenance.getServiceType())
                .cost(maintenance.getCost())
                .description(maintenance.getDescription())
                .nextServiceDate(maintenance.getNextServiceDate())
                .nextServiceMileage(maintenance.getNextServiceMileage());

        // Add vehicle name
        if (maintenance.getVehicle() != null) {
            String vehicleName = maintenance.getVehicle().getMake() + " " +
                    maintenance.getVehicle().getModel() + " (" +
                    maintenance.getVehicle().getLicensePlate() + ")";
            builder.vehicleName(vehicleName);
        }

        // Add staff information
        if (maintenance.getHandledBy() != null) {
            builder.staffId(maintenance.getHandledBy().getStaffId())
                    .staffName(maintenance.getHandledBy().getFirstName() + " " +
                            maintenance.getHandledBy().getLastName());
        }

        return builder.build();
    }


    public static Maintenance toEntity(MaintenanceDto dto) {
        if (dto == null) {
            return null;
        }

        return Maintenance.builder()
                .maintenanceId(dto.getMaintenanceId())
                .vehicle(dto.getVehicleId() != null ? Vehicle.builder().vehicleId(dto.getVehicleId()).build() : null)
                .handledBy(dto.getStaffId() != null ? Staff.builder().staffId(dto.getStaffId()).build() : null)
                .serviceDate(dto.getServiceDate())
                .serviceType(dto.getServiceType())
                .cost(dto.getCost())
                .description(dto.getDescription())
                .nextServiceDate(dto.getNextServiceDate())
                .nextServiceMileage(dto.getNextServiceMileage())
                .build();
    }
}
