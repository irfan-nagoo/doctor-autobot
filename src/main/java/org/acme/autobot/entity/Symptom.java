package org.acme.autobot.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author irfan.nagoo
 */

@Entity
@Table(name = "umls_symptom")
@Getter
@Setter
public class Symptom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String cui;
    private String name;
    private String status;
    @Version
    private Long version;
    @Column(name = "create_date", updatable = false)
    private LocalDateTime createDate;
    @Column(name = "created_by", updatable = false)
    private String createdBy;
    @Column(name = "update_date")
    private LocalDateTime updateDate;
    @Column(name = "updated_by")
    private String updatedBy;
}
