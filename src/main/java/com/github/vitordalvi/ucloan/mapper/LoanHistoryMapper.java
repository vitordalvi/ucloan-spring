package com.github.vitordalvi.ucloan.mapper;

import com.github.vitordalvi.ucloan.dto.response.LoanHistoryResponseDto;
import com.github.vitordalvi.ucloan.entities.LoanHistory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LoanHistoryMapper {

    @Mapping(target = "loanId", source = "loan.id")
    @Mapping(target = "changedById", source = "changedBy.id")
    LoanHistoryResponseDto toDto(LoanHistory loanHistory);
}
