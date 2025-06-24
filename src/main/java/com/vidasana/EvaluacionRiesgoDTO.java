package com.vidasana;
import java.util.List;

public class EvaluacionRiesgoDTO {
    private int score;
    private String nivel;
    private List<String> recomendaciones;

    public EvaluacionRiesgoDTO(int score, String nivel, List<String> recomendaciones) {
        this.score = score;
        this.nivel = nivel;
        this.recomendaciones = recomendaciones;
    }
    public int getScore() { return score; }
    public String getNivel() { return nivel; }
    public List<String> getRecomendaciones() { return recomendaciones; }
} 