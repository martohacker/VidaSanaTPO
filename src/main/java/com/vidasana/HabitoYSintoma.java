package com.vidasana;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Document(collection = "HabitosYSintomas")
public class HabitoYSintoma {
    @Id
    private String id;
    private String pacienteId;
    private String fecha;
    private int sueno;
    private String alimentacion;
    private List<String> sintomas;

    public HabitoYSintoma() {}

    public HabitoYSintoma(String pacienteId, String fecha, int sueno, String alimentacion, List<String> sintomas) {
        this.pacienteId = pacienteId;
        this.fecha = fecha;
        this.sueno = sueno;
        this.alimentacion = alimentacion;
        this.sintomas = sintomas;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getPacienteId() { return pacienteId; }
    public void setPacienteId(String pacienteId) { this.pacienteId = pacienteId; }
    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }
    public int getSueno() { return sueno; }
    public void setSueno(int sueno) { this.sueno = sueno; }
    public String getAlimentacion() { return alimentacion; }
    public void setAlimentacion(String alimentacion) { this.alimentacion = alimentacion; }
    public List<String> getSintomas() { return sintomas; }
    public void setSintomas(List<String> sintomas) { this.sintomas = sintomas; }
} 