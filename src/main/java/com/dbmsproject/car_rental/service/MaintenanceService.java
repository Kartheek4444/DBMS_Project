package com.dbmsproject.car_rental.service;

import com.dbmsproject.car_rental.dto.MaintenanceDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MaintenanceService {
    MaintenanceDto createMaintenance(MaintenanceDto maintenanceDto);
    MaintenanceDto getMaintenance(Long maintenanceId);
    Page<MaintenanceDto> getAllMaintenance(Pageable pageable);
    MaintenanceDto updateMaintenance(Long maintenanceId, MaintenanceDto maintenanceDto);
    void deleteMaintenance(Long maintenanceId);
    Page<MaintenanceDto> getVehicleMaintenance(Long vehicleId, Pageable pageable);
    Page<MaintenanceDto> getStaffMaintenance(Long staffId, Pageable pageable);
}
