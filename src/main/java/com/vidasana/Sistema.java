package com.vidasana;

public interface Sistema {
    Paciente registrarPaciente(Paciente paciente);
    Medico registrarMedico(Medico medico);
    Especialista registrarEspecialista(Especialista especialista);
    Turno registrarTurno(Turno turno);
    void cancelarTurno();
    void eliminarPaciente();
    Historial verHistorial();
    Medico buscarMedicoPorDni(int dni);
    Paciente buscarPacientePorDni(int dni);
    void modificarHistorial();
    void verTodosLosTurnos();
}

 