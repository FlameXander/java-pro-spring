package ru.flamexander.transfer.service.core.backend.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.flamexander.transfer.service.core.backend.services.DocumentStatus;

import java.math.BigDecimal;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "transfers")
@Schema(description = "Документ перевода")
public class TransferDocument {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID документа", required = true)
    @Column(name = "id")
    private Long id;

    @Column(name = "request_id")
    @Schema(description = "ID запроса", required = true)
    private Long requestId;

    @Column(name = "sender_id")
    @Schema(description = "ID отправителя", required = true)
    private Long senderId;

    @Column(name = "send_account")
    @Schema(description = "Номер счета отправителя", required = true, minLength = 16, maxLength = 16, example = "1234123412341234")
    private String sendAccount;

    @Column(name = "receiver_id")
    @Schema(description = "ID получателя", required = true)
    private Long receiverId;

    @Column(name = "receiv_account")
    @Schema(description = "Номер счета получателя", required = true, minLength = 16, maxLength = 16, example = "1234123412341234")
    private String receivAccount;

    @Schema(description = "сумма перевода", required = true, example = "1000.99")
    private BigDecimal amount;

    @Column(name = "document_status")
    @Schema(description = "статус запроса", required = true, example = "создан, в обработке, выполнен, ошибка")
    private DocumentStatus documentStatus;

    @Column(name = "update_date_time")
    @Schema(description = "Дата-время результата", required = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private ZonedDateTime updateDateTime;

    public TransferDocument(Long requestId, Long senderId, String sendAccount, Long receiverId, String receivAccount, BigDecimal amount) {
        this.requestId = requestId;
        this.senderId = senderId;
        this.sendAccount = sendAccount;
        this.receiverId = receiverId;
        this.receivAccount = receivAccount;
        this.amount = amount;
        this.documentStatus = DocumentStatus.CREATED;
        this.updateDateTime =  ZonedDateTime.now(ZoneOffset.UTC);
    }
}
