package com.example.lab2projekt.domain.Services;

import com.example.lab2projekt.domain.Objects.CoverType;
import com.example.lab2projekt.domain.repositories.CoverTypeRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CoverTypeService {

    private final CoverTypeRepository coverTypeRepository;

    public CoverTypeService(CoverTypeRepository coverTypeRepository) {
        this.coverTypeRepository = coverTypeRepository;
    }
    @PreAuthorize("hasRole('ADMIN') or hasAnyRole('USER')")
    public List<CoverType> findAllCoverTypes() {
        return coverTypeRepository.findAll();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void updateCoverTypeName(String newName, String oldName) {
        coverTypeRepository.updateCoverTypeName(newName, oldName);
    }
    @PreAuthorize("hasRole('ADMIN') or hasAnyRole('USER')")
    public Optional<CoverType> findById(Integer coverTypeId){

        return coverTypeRepository.findById(coverTypeId);
    }
}
