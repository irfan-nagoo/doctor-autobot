package org.acme.autobot.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * @author irfan.nagoo
 */

@Entity
@Table(name = "umls_disease")
@Getter
@Setter
public class Disease {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String cui;
    private String name;
    @Column(name = "sem_type1")
    private String semType1;
    private String status;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "umls_symptom_disease",
            joinColumns = @JoinColumn(name = "dis_cui", referencedColumnName = "cui"),
            inverseJoinColumns = @JoinColumn(name = "sym_cui", referencedColumnName = "cui"))
    private Set<Symptom> symptoms;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "umls_disease_medicine",
            joinColumns = @JoinColumn(name = "dis_cui", referencedColumnName = "cui"),
            inverseJoinColumns = @JoinColumn(name = "med_cui", referencedColumnName = "cui"))
    private Set<Medicine> medicines;
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
