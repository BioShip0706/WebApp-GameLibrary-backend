package it.BioShip.GameLibrary.entity.common;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;

@MappedSuperclass @Setter @Getter
public class Creation implements Serializable {

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
}
