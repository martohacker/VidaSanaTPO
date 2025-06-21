package com.vidasana;

import java.util.*;

public class SistemaImpl implements Sistema {
    private List<Paciente> pacientes = new ArrayList<>();
    private List<Medico> medicos = new ArrayList<>();
    private List<Turno> turnos = new ArrayList<>();

    @Override
    public Paciente registrarPaciente(Paciente p) {
        pacientes.add(p);
        return p;
    }

    @Override
    public Medico registrarMedico(Medico m) {
        medicos.add(m);
        return m;
    }

    @Override
    public Especialista registrarEspecialista(Especialista e) {
        medicos.add(e);
        return e;
    }

    @Override
    public Turno registrarTurno(Turno t) {
        turnos.add(t);
        return t;
    }

    @Override
    public void cancelarTurno() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Ingrese DNI del paciente para cancelar turno: ");
        int dni = scanner.nextInt();

        Turno turnoACancelar = null;
        for (Turno t : turnos) {
            if (t.getPacienteId().equals(String.valueOf(dni))) {
                turnoACancelar = t;
                break;
            }
        }

        if (turnoACancelar != null) {
            turnos.remove(turnoACancelar);
            System.out.println("Turno cancelado con éxito.");
        } else {
            System.out.println("No se encontró un turno para ese DNI.");
        }
    }

    @Override
    public void eliminarPaciente() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Ingrese DNI del paciente a eliminar: ");
        int dni = scanner.nextInt();

        Paciente pacienteAEliminar = null;
        for (Paciente p : pacientes) {
            if (p.getDni() == dni) {
                pacienteAEliminar = p;
                break;
            }
        }

        if (pacienteAEliminar != null) {
            Iterator<Turno> it = turnos.iterator();
            while (it.hasNext()) {
                Turno t = it.next();
                if (t.getPacienteId().equals(String.valueOf(pacienteAEliminar.getDni()))) {
                    it.remove();
                }
            }
            for (Medico m : medicos) {
                m.getPacientes().remove(pacienteAEliminar);
                m.removerTurnosDePaciente(pacienteAEliminar);
            }
            pacientes.remove(pacienteAEliminar);
            System.out.println("Paciente eliminado con éxito.");
        } else {
            System.out.println("No se encontró un paciente con ese DNI.");
        }
    }

    @Override
    public Historial verHistorial() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Ingrese DNI del paciente: ");
        int dni = scanner.nextInt();

        Paciente paciente = buscarPacientePorDni(dni);
        if (paciente == null) {
            System.out.println("Paciente no encontrado.");
            return null;
        }

        Historial historial = paciente.getHistorialMedico();
        if (historial == null || historial.getEntradas().isEmpty()) {
            System.out.println("El historial está vacío.");
            return historial;
        }

        System.out.println(historial);
        return historial;
    }
    @Override
    public Paciente buscarPacientePorDni(int dni) {
        for (Paciente p : pacientes) {
            if (p.getDni() == dni) return p;
        }
        return null;
    }
    @Override
    public Medico buscarMedicoPorDni(int dni) {
        for (Medico m : medicos) {
            if (m.getDni() == dni) return m;
        }
        return null;
    }
    @Override
    public void modificarHistorial() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Ingrese DNI del paciente: ");
        int dniPaciente = scanner.nextInt();
        scanner.nextLine();

        Paciente paciente = buscarPacientePorDni(dniPaciente);
        if (paciente == null) {
            System.out.println("Paciente no encontrado.");
            return;
        }

        System.out.println("Historial actual:");
        System.out.println(paciente.getHistorialMedico());

        System.out.println("1. Agregar entrada");
        System.out.println("2. Eliminar entrada");
        System.out.print("Seleccione una opción: ");
        int opcion = scanner.nextInt();
        scanner.nextLine();

        if (opcion == 1) {
            System.out.print("Ingrese DNI del médico: ");
            int dniMedico = scanner.nextInt();
            scanner.nextLine();

            Medico medico = buscarMedicoPorDni(dniMedico);
            if (medico == null) {
                System.out.println("Médico no encontrado.");
                return;
            }

            System.out.print("Descripción del evento médico: ");
            String evento = scanner.nextLine();
            paciente.getHistorialMedico().agregarEntrada(medico, evento);
            System.out.println("Evento agregado al historial.");
        } else if (opcion == 2) {
            System.out.print("Número de entrada a eliminar: ");
            int indice = scanner.nextInt();
            scanner.nextLine();
            paciente.getHistorialMedico().eliminarEntrada(indice - 1);
            System.out.println("Evento eliminado.");
        } else {
            System.out.println("Opción no válida.");
        }
    }
    @Override
    public void verTodosLosTurnos() {
        if (turnos.isEmpty()) {
            System.out.println("No hay turnos registrados.");
            return;
        }

        System.out.println("Listado de turnos registrados:");
        for (Turno t : turnos) {
            System.out.println(t);
        }
    }
}

 