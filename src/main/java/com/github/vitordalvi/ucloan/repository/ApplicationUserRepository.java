package com.github.vitordalvi.ucloan.repository;

import com.github.vitordalvi.ucloan.entities.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApplicationUserRepository extends JpaRepository<ApplicationUser, Long> {
    Optional<ApplicationUser> findByEmail(String email);
    Optional<ApplicationUser> findByEmailAndEnabledTrue(String email);
    Optional<ApplicationUser> findByIdAndEnabledTrue(Long id);

    boolean existsByEmailAndEnabledTrue(String email);

}
