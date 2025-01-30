package com.springmvc.controllers;

import com.springmvc.entities.Owner;
import com.springmvc.exception.NotFoundException;
import com.springmvc.repositories.OwnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/owners")
public class OwnerController {
    @Autowired
    private OwnerRepository ownerRepository;

    @GetMapping
    public List<Owner> getAllOwners() {
        return ownerRepository.findAll();
    }
    @PostMapping
    public Owner createOwner(@RequestBody Owner owner) {
        return ownerRepository.save(owner);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Owner> getOwnerById(@PathVariable Long id) {
        return ownerRepository.findById(id)
                .map(owner -> ResponseEntity.ok(owner))
                .orElseThrow(() -> new NotFoundException("Owner not found"));
    }
    @PutMapping("/{id}")
    public Owner updateOwner(@PathVariable Long id, @RequestBody Owner ownerDetails) {
        Owner owner = ownerRepository.findById(id).orElseThrow(() -> new NotFoundException("Owner not found"));
        owner.setName(ownerDetails.getName());
        return ownerRepository.save(owner);
    }
    @DeleteMapping("/{id}")
    public void deleteOwner(@PathVariable Long id) {
        ownerRepository.deleteById(id);
    }
}
