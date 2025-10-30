package com.dbmsproject.car_rental.mapper;

import com.dbmsproject.car_rental.dto.MaintenanceDto;
import com.dbmsproject.car_rental.model.Maintenance;
import com.dbmsproject.car_rental.model.Staff;
import com.dbmsproject.car_rental.model.Vehicle;

public class MaintenanceMapper {

    public static MaintenanceDto toDto(Maintenance maintenance) {
        if (maintenance == null) {
            return null;
        }

        return MaintenanceDto.builder()
                .maintenanceId(maintenance.getMaintenanceId())
                .vehicleId(maintenance.getVehicle() != null ? maintenance.getVehicle().getVehicleId() : null)
                .staffId(maintenance.getHandledBy() != null ? maintenance.getHandledBy().getStaffId() : null)
                .serviceDate(maintenance.getServiceDate())
                .serviceType(maintenance.getServiceType())
                .cost(maintenance.getCost())
                .description(maintenance.getDescription())
                .nextServiceDate(maintenance.getNextServiceDate())
                .nextServiceMileage(maintenance.getNextServiceMileage())
                .build();
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
