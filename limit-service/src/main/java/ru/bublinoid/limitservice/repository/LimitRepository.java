package ru.bublinoid.limitservice.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.bublinoid.limitservice.entity.Limit;

import java.util.Optional;

public interface LimitRepository extends JpaRepository<Limit, Long> {
    Optional<Limit> findByClientId(Long clientId);

    @Modifying
    @Query(value = "UPDATE limits SET limit = limit_max", nativeQuery = true)
    void resetAllLimits();
}