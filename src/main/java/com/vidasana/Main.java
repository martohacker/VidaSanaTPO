package com.vidasana;

import java.time.LocalDate;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Sistema sistema = new SistemaImpl();
        Scanner scanner = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("\n===== MENÚ DEL SISTEMA MÉDICO =====");
            System.out.println("1. Registrar paciente");
            System.out.println("2. Registrar médico");
            System.out.println("3. Registrar turno");
            System.out.println("4. Cancelar turno");
            System.out.println("5. Modificar historial médico del paciente");
            System.out.println("6. Eliminar paciente");
            System.out.println("7. Ver historial");
            System.out.println("8. Ver turnos");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opción: ");
            opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1:
                    System.out.print("Nombre del paciente: ");
                    String nombre = scanner.nextLine();

                    System.out.println("Apellido del paciente: ");
                    String apellido = scanner.nextLine();

                    System.out.println("Email del paciente: ");
                    String email = scanner.nextLine();

                    System.out.print("DNI del paciente: ");
                    int dni = scanner.nextInt();
                    scanner.nextLine();

                    System.out.println("Nacionalidad del paciente: ");
                    String nacionalidad = scanner.nextLine();

                    System.out.println("Edad del paciente: ");
                    int edad = scanner.nextInt();
                    scanner.nextLine();

                    System.out.println("Fecha de nacimiento del paciente(yyyy-MM-dd): ");
                    String input = scanner.nextLine();

                    LocalDate fechaNac = LocalDate.parse(input);
                    scanner.nextLine();

                    Paciente p = new Paciente(nombre, apellido, email, dni, nacionalidad, edad, fechaNac);
                    sistema.registrarPaciente(p);
                    System.out.println("Paciente registrado con éxito.");
                    break;

                case 2:
                    System.out.print("Nombre del medico: ");
                    String nombreMedico = scanner.nextLine();

                    System.out.println("Apellido del medico: ");
                    String apellidoMedico = scanner.nextLine();

                    System.out.println("Email del medico: ");
                    String emailMedico = scanner.nextLine();

                    System.out.print("DNI del medico: ");
                    int dniMedico = scanner.nextInt();
                    scanner.nextLine();

                    System.out.println("Edad del medico: ");
                    int edadMedico = scanner.nextInt();
                    scanner.nextLine();

                    System.out.println("Fecha de nacimiento del medico(yyyy-MM-dd): ");
                    String inputMedico = scanner.nextLine();
                    LocalDate fechaNacMedico = LocalDate.parse(inputMedico);

                    System.out.println("Disciplina del medico: ");
                    String disciplina = scanner.nextLine();
                    scanner.nextLine();

                    Medico m = new Medico(nombreMedico, apellidoMedico, emailMedico, dniMedico, edadMedico, fechaNacMedico, disciplina);
                    sistema.registrarMedico(m);
                    System.out.println("Médico registrado con éxito.");
                    break;

                case 3:
                    System.out.print("DNI del paciente: ");
                    int dniPacienteTurno = scanner.nextInt();
                    scanner.nextLine();

                    System.out.print("DNI del médico: ");
                    int dniMedicoTurno = scanner.nextInt();
                    scanner.nextLine();

                    System.out.println("Fecha del turno(yyyy-MM-dd): ");
                    String inputTurno = scanner.nextLine();
                    //LocalDate fecha = LocalDate.parse(inputTurno);

                    Paciente paciente = sistema.buscarPacientePorDni(dniPacienteTurno);
                    Medico medico = sistema.buscarMedicoPorDni(dniMedicoTurno);

                    if (paciente != null && medico != null) {
                        Turno turno = new Turno(
                            String.valueOf(paciente.getDni()),
                            String.valueOf(medico.getDni()),
                            inputTurno + "T10:00:00Z", // ejemplo de formato ISO
                            "pendiente"
                        );
                        sistema.registrarTurno(turno);
                        System.out.println("Turno registrado con éxito.");
                    } else {
                        System.out.println("No se encontró paciente o médico con esos DNI.");
                    }
                    break;

                case 4:
                    sistema.cancelarTurno();
                    break;
                case 5:
                    sistema.modificarHistorial();
                    break;

                case 6:
                    sistema.eliminarPaciente();
                    break;

                case 7:
                    Historial h = sistema.verHistorial();
                    if (h != null) {
                        System.out.println("Historial del paciente: " + h);
                    } else {
                        System.out.println("No hay historial disponible.");
                    }
                    break;
                case 8:
                    sistema.verTodosLosTurnos();
                    break;

                case 0:
                    System.out.println("Saliendo del sistema...");
                    break;

                default:
                    System.out.println("Opción inválida.");
            }

        } while (opcion != 0);
    }
}

 