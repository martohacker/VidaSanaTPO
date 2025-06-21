package com.vidasana;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/medicos")
public class MedicoController {
    @Autowired
    private MedicoRepository medicoRepository;

    @GetMapping
    public List<Medico> getAll() {
        return medicoRepository.findAll();
    }

    @PostMapping
    public Medico create(@RequestBody Medico medico) {
        return medicoRepository.save(medico);
    }

    @GetMapping("/{id}")
    public Medico getById(@PathVariable String id) {
        return medicoRepository.findById(id).orElse(null);
    }

    @PutMapping("/{id}")
    public Medico update(@PathVariable String id, @RequestBody Medico medico) {
        return medicoRepository.save(medico);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        medicoRepository.deleteById(id);
    }
} 