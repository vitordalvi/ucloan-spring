package com.github.vitordalvi.ucloan.mapper;

import com.github.vitordalvi.ucloan.entities.ApplicationUser;
import com.github.vitordalvi.ucloan.entities.Token;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TokenMapper {

    // ignore do id por causa do auto increment
    @Mapping(target = "id", ignore = true)

    // parametro que vem em cada atributo
    @Mapping(target = "user", source = "user")
    @Mapping(target = "token", source = "jwtToken")

    // valores default
    @Mapping(target = "tokenType", expression = "java(com.github.vitordalvi.ucloan.entities.enums.TokenType.BEARER)")
    @Mapping(target = "expired", constant = "false")
    @Mapping(target = "revoked", constant = "false")
    Token toEntity (ApplicationUser user, String jwtToken);
}
