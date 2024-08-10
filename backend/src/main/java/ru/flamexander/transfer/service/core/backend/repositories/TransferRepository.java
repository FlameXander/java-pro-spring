package ru.flamexander.transfer.service.core.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.flamexander.transfer.service.core.backend.entities.Transfer;

import java.util.List;

@Repository
public interface TransferRepository extends JpaRepository<Transfer, Long> {
    @Query("SELECT t FROM Transfer t WHERE t.senderId = :clientId OR t.recipientId = :clientId")
    List<Transfer> findAllBySourceOrDestinationClientId(@Param("clientId") Long clientId);
}
