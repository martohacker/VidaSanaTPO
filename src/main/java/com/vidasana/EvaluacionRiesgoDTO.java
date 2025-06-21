package com.vidasana;

public class EvaluacionRiesgoDTO {
    private String pacienteId;
    private String riesgo;
    private String recomendacion;

    public EvaluacionRiesgoDTO() {}
    public EvaluacionRiesgoDTO(String pacienteId, String riesgo, String recomendacion) {
        this.pacienteId = pacienteId;
        this.riesgo = riesgo;
        this.recomendacion = recomendacion;
    }
    public String getPacienteId() { return pacienteId; }
    public void setPacienteId(String pacienteId) { this.pacienteId = pacienteId; }
    public String getRiesgo() { return riesgo; }
    public void setRiesgo(String riesgo) { this.riesgo = riesgo; }
    public String getRecomendacion() { return recomendacion; }
    public void setRecomendacion(String recomendacion) { this.recomendacion = recomendacion; }
} 