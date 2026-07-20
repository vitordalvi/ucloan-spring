package com.github.vitordalvi.ucloan.mapper;

import com.github.vitordalvi.ucloan.dto.request.CreateLoanRequestDto;
import com.github.vitordalvi.ucloan.dto.response.LoanResponseDto;
import com.github.vitordalvi.ucloan.entities.Loan;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LoanMapper {

    @Mapping(target = "loanStatus", ignore = true)
    @Mapping(target = "startDate", ignore = true)
    Loan toEntity(CreateLoanRequestDto dto);

    @Mapping(target = "borrower", source = "borrower.Id")
    @Mapping(target = "equipment", source = "equipment.Id")
    LoanResponseDto toDto(Loan loan);
}
