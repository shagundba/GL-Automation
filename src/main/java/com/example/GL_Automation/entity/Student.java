package com.example.GL_Automation.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "students")
@Data
public class Student
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
       private Long id;

       private LocalDate date;

       private String data;
}
