package ru.flamexander.transfer.service.limits.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.flamexander.transfer.service.limits.backend.entities.Limit;

import java.util.Optional;

public interface LimitsRepository extends JpaRepository<Limit, Long> {
    Optional<Limit> findByUserId(Long userId);
}
