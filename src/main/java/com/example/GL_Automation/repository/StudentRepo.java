package com.example.GL_Automation.repository;

import com.example.GL_Automation.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface StudentRepo extends JpaRepository<Student,Long> 
{

    List<Student> findByDate(LocalDate date);
}
