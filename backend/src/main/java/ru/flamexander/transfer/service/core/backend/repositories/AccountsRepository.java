package ru.flamexander.transfer.service.core.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.flamexander.transfer.service.core.backend.entities.Account;

import java.util.List;
import java.util.Optional;


@Repository
public interface AccountsRepository extends JpaRepository<Account, Long> {
    List<Account> findAllByClientId(Long clientId);
    Optional<Account> findByIdAndClientId(Long id, Long clientId);
    Optional<Account> findByAccountNumberAndClientId(String accountNumber, Long clientId);

//    @Modifying
//    @Query("update User u set u.firstname = ?1, u.lastname = ?2 where u.id = ?3")
//    void setUserInfoById(String firstname, String lastname, Integer userId);
}