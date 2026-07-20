package com.github.vitordalvi.ucloan.services;

import com.github.vitordalvi.ucloan.dto.request.UserAdminPatchRequestDto;
import com.github.vitordalvi.ucloan.dto.request.UserChangePasswordDtoRequest;
import com.github.vitordalvi.ucloan.dto.request.UserPatchRequestDto;
import com.github.vitordalvi.ucloan.dto.response.UserAdminResponseDto;
import com.github.vitordalvi.ucloan.dto.response.UserResponseDto;
import com.github.vitordalvi.ucloan.entities.ApplicationUser;
import com.github.vitordalvi.ucloan.exceptions.BusinessException;
import com.github.vitordalvi.ucloan.exceptions.ResourceNotFoundException;
import com.github.vitordalvi.ucloan.mapper.ApplicationUserMapper;
import com.github.vitordalvi.ucloan.repository.ApplicationUserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ApplicationUserService {

    private final ApplicationUserRepository applicationUserRepository;
    private final ApplicationUserMapper applicationUserMapper;
    private final PasswordEncoder passwordEncoder;

    public ApplicationUserService(ApplicationUserRepository applicationUserRepository,
                                  ApplicationUserMapper applicationUserMapper, PasswordEncoder passwordEncoder) {
        this.applicationUserRepository = applicationUserRepository;
        this.applicationUserMapper = applicationUserMapper;
        this.passwordEncoder = passwordEncoder;
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

    public void changePassword(Long id, UserChangePasswordDtoRequest dto) {
        var user = applicationUserRepository.findByIdAndEnabledTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("No user found for this Id!"));

        if (!dto.newPassword().equals(dto.confirmPassword()))
            throw new BusinessException("Mismatched passwords!");

        if (!passwordEncoder.matches(dto.currentPassword(), user.getPassword()))
            throw new BusinessException("Current password is incorrect!");

        user.setPassword(passwordEncoder.encode(dto.newPassword()));
        applicationUserRepository.save(user);
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
