package com.GL._Automation_System.Entitty;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@jakarta.persistence.Entity
@Table(name = "import_data")
public class GLAutoData {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "chequeId")
    private String  chequeId;
    @Column(name = "applicant_name")
    private String applicantName;
    @Column(name = "sanction_date")
    private Date sanction;
}