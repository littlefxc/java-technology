package com.littlefxc.example.activi6.service;

import com.littlefxc.example.activi6.entity.VacationForm;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VacationFormService extends JpaRepository<VacationForm, Integer> {

}