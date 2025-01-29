package com.springmvc.controllers;

import com.springmvc.entities.Cat;
import com.springmvc.exception.NotFoundException;
import com.springmvc.repositories.CatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController //класс является контроллером (обрабатывает HTTP запросы, возвращает JSON/XML), сочетание @Controller и @ResponseBody
@RequestMapping("/api/cats") //базовый URL для методов
public class CatController {
    @Autowired //автоматическое создание экземпляра и внедрение в контроллер
    private CatRepository catRepository;

    @GetMapping //обработка GET запросов по URL /api/cats
    public List<Cat> getAllCats() {
        return catRepository.findAll(); //результат JSON
    }

    @PostMapping //POST запросы
    public Cat createCat(@RequestBody Cat cat) { //данные извлекаются из тела запроса
        return catRepository.save(cat);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cat> getCatById(@PathVariable Long id) {
        return catRepository.findById(id)
                .map(cat -> ResponseEntity.ok(cat))
                .orElseThrow(() -> new NotFoundException("Cat not found"));
    }


    @PutMapping({"/id"})
    public ResponseEntity<Cat> updateCat(@PathVariable Long id, @RequestBody Cat catDetails) {
        return catRepository.findById(id)
                .map(cat -> {
                    cat.setName(catDetails.getName());
                    cat.setBreed(catDetails.getBreed());
                    return ResponseEntity.ok(catRepository.save(cat));
                }).orElseThrow(() -> new NotFoundException("Cat not found"));
    }

}
