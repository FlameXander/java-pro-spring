package ru.flamexander.transfer.service.limits.backend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "limits")
public class Limit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "client_id")
    private Long clientId;

    @Column(name = "limit_amount")
    private BigDecimal limit;

    @Column(name = "limit_max")
    private BigDecimal limitMax;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public Limit(Long clientId, BigDecimal limit){
        this.clientId = clientId;
        this.limit = limit;
        this.limitMax = limit;
    }
}
