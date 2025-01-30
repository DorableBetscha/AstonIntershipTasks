package com.springmvc.controllers;

import com.springmvc.entities.Veterinarian;
import com.springmvc.exception.NotFoundException;
import com.springmvc.repositories.VeterinarianRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/veterinarians")
public class VeterinarianController {
    @Autowired
    private final VeterinarianRepository veterinarianRepository;

    public VeterinarianController(VeterinarianRepository veterinarianRepository) {
        this.veterinarianRepository = veterinarianRepository;
    }

    @GetMapping
    public List<Veterinarian> getAllVeterinarians() {
        return veterinarianRepository.findAll();
    }


}
