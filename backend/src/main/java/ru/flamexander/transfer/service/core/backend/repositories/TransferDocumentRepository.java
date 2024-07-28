package ru.flamexander.transfer.service.core.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.flamexander.transfer.service.core.backend.entities.TransferDocument;

import java.util.List;

@Repository
public interface TransferDocumentRepository extends JpaRepository<TransferDocument, Long> {
    List<TransferDocument> findAllBySenderId(Long clientId);
    List<TransferDocument> findAllByReceiverId(Long clientId);
}
