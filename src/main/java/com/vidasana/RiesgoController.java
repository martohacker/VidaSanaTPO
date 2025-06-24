package com.vidasana;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/riesgo")
public class RiesgoController {
    @Autowired
    private RiesgoService riesgoService;

    @GetMapping("/{pacienteId}")
    public EvaluacionRiesgoDTO getRiesgo(@PathVariable String pacienteId) {
        return riesgoService.calcularRiesgo(pacienteId);
    }
} 