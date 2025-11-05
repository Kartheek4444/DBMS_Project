package com.dbmsproject.car_rental.service.impl;

import com.dbmsproject.car_rental.dto.MaintenanceDto;
import com.dbmsproject.car_rental.exception.ResourceNotFoundException;
import com.dbmsproject.car_rental.mapper.MaintenanceMapper;
import com.dbmsproject.car_rental.model.Maintenance;
import com.dbmsproject.car_rental.model.Staff;
import com.dbmsproject.car_rental.model.Vehicle;
import com.dbmsproject.car_rental.repository.MaintenanceRepository;
import com.dbmsproject.car_rental.repository.StaffRepository;
import com.dbmsproject.car_rental.repository.VehicleRepository;
import com.dbmsproject.car_rental.service.MaintenanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MaintenanceServiceImpl implements MaintenanceService {

    private final MaintenanceRepository maintenanceRepository;
    private final VehicleRepository vehicleRepository;
    private final StaffRepository staffRepository;
    private final MaintenanceMapper maintenanceMapper;

    @Override
    public MaintenanceDto createMaintenance(MaintenanceDto maintenanceDto) {
        Vehicle vehicle = vehicleRepository.findById(maintenanceDto.getVehicleId())
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with id: " + maintenanceDto.getVehicleId()));

        Staff staff = null;
        if (maintenanceDto.getStaffId() != null) {
            staff = staffRepository.findById(maintenanceDto.getStaffId())
                    .orElseThrow(() -> new ResourceNotFoundException("Staff not found with id: " + maintenanceDto.getStaffId()));
        }

        Maintenance maintenance = MaintenanceMapper.toEntity(maintenanceDto);
        maintenance.setMaintenanceId(null);
        maintenance.setVehicle(vehicle);
        maintenance.setHandledBy(staff);

        Maintenance savedMaintenance = maintenanceRepository.save(maintenance);
        return maintenanceMapper.toDto(savedMaintenance);
    }

    @Override
    @Transactional(readOnly = true)
    public MaintenanceDto getMaintenance(Long maintenanceId) {
        return maintenanceRepository.findById(maintenanceId)
                .map(maintenanceMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Maintenance not found with id: " + maintenanceId));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MaintenanceDto> getAllMaintenance(Pageable pageable) {
        return maintenanceRepository.findAll(pageable)
                .map(maintenanceMapper::toDto);
    }

    @Override
    public MaintenanceDto updateMaintenance(Long maintenanceId, MaintenanceDto maintenanceDto) {
        Maintenance existing = maintenanceRepository.findById(maintenanceId)
                .orElseThrow(() -> new ResourceNotFoundException("Maintenance not found with id: " + maintenanceId));

        if (maintenanceDto.getVehicleId() != null) {
            Vehicle vehicle = vehicleRepository.findById(maintenanceDto.getVehicleId())
                    .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found"));
            existing.setVehicle(vehicle);
        }

        if (maintenanceDto.getStaffId() != null) {
            Staff staff = staffRepository.findById(maintenanceDto.getStaffId())
                    .orElseThrow(() -> new ResourceNotFoundException("Staff not found"));
            existing.setHandledBy(staff);
        }

        existing.setServiceDate(maintenanceDto.getServiceDate());
        existing.setServiceType(maintenanceDto.getServiceType());
        existing.setCost(maintenanceDto.getCost());
        existing.setDescription(maintenanceDto.getDescription());
        existing.setNextServiceDate(maintenanceDto.getNextServiceDate());
        existing.setNextServiceMileage(maintenanceDto.getNextServiceMileage());

        Maintenance savedMaintenance = maintenanceRepository.save(existing);
        return maintenanceMapper.toDto(savedMaintenance);
    }

    @Override
    public void deleteMaintenance(Long maintenanceId) {
        if (!maintenanceRepository.existsById(maintenanceId)) {
            throw new ResourceNotFoundException("Maintenance not found with id: " + maintenanceId);
        }
        maintenanceRepository.deleteById(maintenanceId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MaintenanceDto> getVehicleMaintenance(Long vehicleId, Pageable pageable) {
        return maintenanceRepository.findAllByVehicleVehicleId(vehicleId, pageable)
                .map(maintenanceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MaintenanceDto> getStaffMaintenance(Long staffId, Pageable pageable) {
        return maintenanceRepository.findAllByHandledByStaffId(staffId, pageable)
                .map(maintenanceMapper::toDto);
    }
}