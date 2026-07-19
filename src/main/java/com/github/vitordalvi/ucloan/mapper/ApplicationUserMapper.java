package com.github.vitordalvi.ucloan.mapper;

import com.github.vitordalvi.ucloan.dto.request.UserAdminPatchRequestDto;
import com.github.vitordalvi.ucloan.dto.request.UserPatchRequestDto;
import com.github.vitordalvi.ucloan.dto.request.UserRegisterRequestDto;
import com.github.vitordalvi.ucloan.dto.response.UserAdminResponseDto;
import com.github.vitordalvi.ucloan.dto.response.UserResponseDto;
import com.github.vitordalvi.ucloan.entities.ApplicationUser;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ApplicationUserMapper {

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "tokens", ignore = true)
    @Mapping(target = "accountNonExpired", ignore = true)
    @Mapping(target = "accountNonLocked", ignore = true)
    @Mapping(target = "credentialsNonExpired", ignore = true)
    ApplicationUser toEntity(UserRegisterRequestDto dto);

    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    @Mapping(target = "updatedBy", source = "updatedBy.id")
    @Mapping(target = "createdBy", source = "createdBy.id")
    UserAdminResponseDto toAdminDto(ApplicationUser user);
    UserResponseDto toDto(ApplicationUser user);

    List<UserAdminResponseDto> toAdminDtoList(List<ApplicationUser> user);
    List<UserResponseDto> toDtoList(List<ApplicationUser> user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void patchEntityFromDto(UserPatchRequestDto dto, @MappingTarget ApplicationUser user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void patchEntityFromAdminDto(UserAdminPatchRequestDto dto, @MappingTarget ApplicationUser user);

    void updateEntityFromDto(UserRegisterRequestDto dto, @MappingTarget ApplicationUser user);
}
