package com.github.vitordalvi.ucloan.mapper;

import com.github.vitordalvi.ucloan.dto.request.CreateLoanRequestDto;
import com.github.vitordalvi.ucloan.dto.request.LoanPatchRequestDto;
import com.github.vitordalvi.ucloan.dto.response.LoanAdminResponseDto;
import com.github.vitordalvi.ucloan.dto.response.LoanResponseDto;
import com.github.vitordalvi.ucloan.entities.Loan;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LoanMapper {

    @Mapping(target = "loanStatus", ignore = true)
    @Mapping(target = "startDate", ignore = true)
    Loan toEntity(CreateLoanRequestDto dto);

    @Mapping(target = "borrowerId", source = "borrower.id")
    @Mapping(target = "equipmentId", source = "equipment.id")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    @Mapping(target = "updatedBy", source = "updatedBy.id")
    @Mapping(target = "createdBy", source = "createdBy.id")
    LoanAdminResponseDto toAdminDto(Loan loan);

    @Mapping(target = "borrowerId", source = "borrower.id")
    @Mapping(target = "equipmentId", source = "equipment.id")
    LoanResponseDto toDto(Loan loan);

    List<LoanResponseDto> toDtoList(List<Loan> loan);
    List<LoanAdminResponseDto> toAdminDtoList(List<Loan> loan);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void patchEntityFromDto(LoanPatchRequestDto dto, @MappingTarget Loan loan);
}
