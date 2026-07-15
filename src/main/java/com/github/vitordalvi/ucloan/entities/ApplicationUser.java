package com.github.vitordalvi.ucloan.entities;

import com.github.vitordalvi.ucloan.entities.base.AuditableBaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "tb_user")
public class ApplicationUser extends AuditableBaseEntity {

    private String name;
    private String email;

    /* Depois quando implementar o Spring Security, vou
        adicionar função de autenticação (senha), autoridade
        (roles e permissions)e  JWT
    */

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
