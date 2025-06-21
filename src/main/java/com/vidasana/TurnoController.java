package com.vidasana;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/turnos")
public class TurnoController {
    @Autowired
    private TurnoRepository turnoRepository;

    @GetMapping
    public List<Turno> getAll() {
        return turnoRepository.findAll();
    }

    @PostMapping
    public Turno create(@RequestBody Turno turno) {
        return turnoRepository.save(turno);
    }

    @GetMapping("/{id}")
    public Turno getById(@PathVariable String id) {
        return turnoRepository.findById(id).orElse(null);
    }

    @PutMapping("/{id}")
    public Turno update(@PathVariable String id, @RequestBody Turno turno) {
        turno.setId(id);
        return turnoRepository.save(turno);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        turnoRepository.deleteById(id);
    }
} 