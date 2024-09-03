package ru.flamexander.transfer.service.core.backend.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.flamexander.transfer.service.core.backend.statuses.TransferStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name="transfers")
public class Transfer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name= "sender_id", nullable = false)
    private Long senderId;
    @Column(name= "recipient_id", nullable = false)
    private Long recipientId;
    @Column(name= "sender_account", nullable = false)
    private String senderAccountNumber;
    @Column(name= "recipient_account", nullable = false)
    private String recipientAccountNumber;

    @Column(name= "amount", nullable = false)
    private BigDecimal transferAmount;

    private TransferStatus status;
    @Column(name = "date", nullable = false)
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    public Transfer(Long senderId, String senderAccountNumber,
                    Long recipientId, String recipientAccountNumber,
                    BigDecimal transferAmount){

        this.senderId = senderId;
        this.senderAccountNumber = senderAccountNumber;
        this.recipientId = recipientId;
        this.recipientAccountNumber = recipientAccountNumber;
        this.transferAmount = transferAmount;
        status = TransferStatus.CREATED;
    }
}
