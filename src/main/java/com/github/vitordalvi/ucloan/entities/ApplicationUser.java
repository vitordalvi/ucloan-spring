package com.github.vitordalvi.ucloan.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_user")
public class ApplicationUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 120)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    /* Depois quando implementar o Spring Security, vou
        adicionar função de autenticação (senha), autoridade
        (roles e permissions)e  JWT
    */

    public Long getId() {
        return id;
    }

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
