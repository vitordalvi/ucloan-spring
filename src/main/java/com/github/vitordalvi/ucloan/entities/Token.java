package com.github.vitordalvi.ucloan.entities;

import com.github.vitordalvi.ucloan.entities.base.BaseEntity;
import com.github.vitordalvi.ucloan.entities.enums.TokenType;
import jakarta.persistence.*;

@Entity
@Table(name = "tb_token")
public class Token extends BaseEntity {

    private String token;

    @Enumerated(EnumType.STRING)
    private TokenType tokenType = TokenType.BEARER;

    private boolean revoked;

    private boolean expired;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private ApplicationUser applicationUser;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public void setTokenType(TokenType tokenType) {
        this.tokenType = tokenType;
    }

    public boolean isRevoked() {
        return revoked;
    }

    public void setRevoked(boolean revoked) {
        this.revoked = revoked;
    }

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

    public ApplicationUser getUser() {
        return applicationUser;
    }

    public void setUser(ApplicationUser applicationUser) {
        this.applicationUser = applicationUser;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private ApplicationUser user;
        private String token;
        private TokenType tokenType;
        private boolean expired;
        private boolean revoked;

        public Builder user(ApplicationUser user) {
            this.user = user;
            return this;
        }

        public Builder token(String token) {
            this.token = token;
            return this;
        }

        public Builder tokenType(TokenType tokenType) {
            this.tokenType = tokenType;
            return this;
        }

        public Builder expired(boolean expired) {
            this.expired = expired;
            return this;
        }

        public Builder revoked(boolean revoked) {
            this.revoked = revoked;
            return this;
        }

        public Token build() {
            Token token = new Token();
            token.setUser(this.user);
            token.setToken(this.token);
            token.setTokenType(this.tokenType);
            token.setExpired(this.expired);
            token.setRevoked(this.revoked);

            return token;
        }
    }
}
