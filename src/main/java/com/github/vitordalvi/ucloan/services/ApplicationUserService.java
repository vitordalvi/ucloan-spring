package com.github.vitordalvi.ucloan.services;

import com.github.vitordalvi.ucloan.dto.request.UserAdminPatchRequestDto;
import com.github.vitordalvi.ucloan.dto.request.UserPatchRequestDto;
import com.github.vitordalvi.ucloan.dto.response.UserAdminResponseDto;
import com.github.vitordalvi.ucloan.dto.response.UserResponseDto;
import com.github.vitordalvi.ucloan.entities.ApplicationUser;
import com.github.vitordalvi.ucloan.exceptions.ResourceNotFoundException;
import com.github.vitordalvi.ucloan.mapper.ApplicationUserMapper;
import com.github.vitordalvi.ucloan.repository.ApplicationUserRepository;
import org.springframework.stereotype.Service;

@Service
public class ApplicationUserService {

    private final ApplicationUserRepository applicationUserRepository;
    private final ApplicationUserMapper applicationUserMapper;

    public ApplicationUserService(ApplicationUserRepository applicationUserRepository,
                                  ApplicationUserMapper applicationUserMapper) {
        this.applicationUserRepository = applicationUserRepository;
        this.applicationUserMapper = applicationUserMapper;
    }

    public UserResponseDto getUser(Long id) {
        ApplicationUser entity = applicationUserRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No user found for this Id"));

        return applicationUserMapper.toDto(entity);
    }

    public UserResponseDto updateUser(Long id, UserPatchRequestDto dto) {
        ApplicationUser entity = applicationUserRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No user found for this Id!"));

        applicationUserMapper.patchEntityFromDto(dto, entity);
        applicationUserRepository.save(entity);

        return applicationUserMapper.toDto(entity);
    }

    public UserAdminResponseDto getUserAsAdmin(Long id) {
        ApplicationUser entity = applicationUserRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No user found for this Id!"));

        return applicationUserMapper.toAdminDto(entity);
    }

    public UserAdminResponseDto updateUserAsAdmin(Long id, UserAdminPatchRequestDto dto) {
        ApplicationUser entity = applicationUserRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No user found for this Id!"));

        applicationUserMapper.patchEntityFromAdminDto(dto, entity);
        applicationUserRepository.save(entity);

        return applicationUserMapper.toAdminDto(entity);
    }
}
