package com.dbmsproject.car_rental.service.impl;

import com.dbmsproject.car_rental.dto.MaintenanceDto;
import com.dbmsproject.car_rental.exception.ResourceNotFoundException;
import com.dbmsproject.car_rental.mapper.MaintenanceMapper;
import com.dbmsproject.car_rental.model.Maintenance;
import com.dbmsproject.car_rental.repository.MaintenanceRepository;
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
    private final MaintenanceMapper maintenanceMapper;

    @Override
    public MaintenanceDto createMaintenance(MaintenanceDto maintenanceDto) {
        Maintenance maintenance = maintenanceMapper.toEntity(maintenanceDto);
        maintenance.setMaintenanceId(null); // Ensure new record creation
        Maintenance savedMaintenance = maintenanceRepository.save(maintenance);
        return maintenanceMapper.toDto(savedMaintenance);
    }

    @Override
    @Transactional(readOnly = true)
    public MaintenanceDto getMaintenance(Long maintenanceId) {
        return maintenanceRepository.findById(maintenanceId)
                .map(maintenanceMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Maintenance", "id", maintenanceId));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MaintenanceDto> getAllMaintenance(Pageable pageable) {
        return maintenanceRepository.findAll(pageable)
                .map(maintenanceMapper::toDto);
    }

    @Override
    public MaintenanceDto updateMaintenance(Long maintenanceId, MaintenanceDto maintenanceDto) {
        if (!maintenanceRepository.existsById(maintenanceId)) {
            throw new ResourceNotFoundException("Maintenance", "id", maintenanceId);
        }
        maintenanceDto.setMaintenanceId(maintenanceId);
        Maintenance maintenance = maintenanceMapper.toEntity(maintenanceDto);
        Maintenance savedMaintenance = maintenanceRepository.save(maintenance);
        return maintenanceMapper.toDto(savedMaintenance);
    }

    @Override
    public void deleteMaintenance(Long maintenanceId) {
        if (!maintenanceRepository.existsById(maintenanceId)) {
            throw new ResourceNotFoundException("Maintenance", "id", maintenanceId);
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
