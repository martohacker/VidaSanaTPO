package com.vidasana;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/habitosysintomas")
public class HabitoYSintomaController {
    @Autowired
    private HabitoYSintomaRepository repository;

    @GetMapping
    public List<HabitoYSintoma> getAll() {
        return repository.findAll();
    }

    @PostMapping
    public HabitoYSintoma create(@RequestBody HabitoYSintoma habitoYSintoma) {
        return repository.save(habitoYSintoma);
    }

    @GetMapping("/{id}")
    public HabitoYSintoma getById(@PathVariable String id) {
        return repository.findById(id).orElse(null);
    }

    @PutMapping("/{id}")
    public HabitoYSintoma update(@PathVariable String id, @RequestBody HabitoYSintoma habitoYSintoma) {
        habitoYSintoma.setId(id);
        return repository.save(habitoYSintoma);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        repository.deleteById(id);
    }
} 