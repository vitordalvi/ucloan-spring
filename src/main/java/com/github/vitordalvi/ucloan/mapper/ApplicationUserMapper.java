package com.github.vitordalvi.ucloan.mapper;

import com.github.vitordalvi.ucloan.dto.request.UserPatchRequestDto;
import com.github.vitordalvi.ucloan.dto.request.UserRegisterRequestDto;
import com.github.vitordalvi.ucloan.entities.ApplicationUser;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ApplicationUserMapper {

    ApplicationUser toEntity(UserRegisterRequestDto dto);
    UserRegisterRequestDto toDto(ApplicationUser user);

    List<ApplicationUser> toDtoList(List<UserRegisterRequestDto> dto);

    void patchEntityFromDto(UserPatchRequestDto dto, @MappingTarget ApplicationUser user);
    void updateEntityFromDto(UserRegisterRequestDto dto, @MappingTarget ApplicationUser user);
}
