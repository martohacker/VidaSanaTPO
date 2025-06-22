document.addEventListener('DOMContentLoaded', () => {
    const turnoForm = document.getElementById('turno-form');
    const turnosTable = document.getElementById('turnos-table').querySelector('tbody');

    // Cargar listas desplegables y turnos
    loadTurnoPacientes();
    loadTurnoMedicos();
    loadTurnos();

    // Manejar envío del formulario
    turnoForm.addEventListener('submit', async (e) => {
        e.preventDefault();

        const turno = {
            pacienteId: document.getElementById('turno-paciente').value,
            medicoId: document.getElementById('turno-medico').value,
            fechaHora: document.getElementById('turno-fecha').value,
            estado: document.getElementById('turno-estado').value
        };

        const result = await api.createTurno(turno);
        if (result) {
            alert('Turno registrado con éxito');
            turnoForm.reset();
            loadTurnos();
        }
    });
});

async function loadTurnoPacientes() {
    const pacientes = await api.getPacientes();
    const select = document.getElementById('turno-paciente');
    select.innerHTML = '<option value="">Seleccione un paciente</option>';

    if (pacientes && pacientes.length > 0) {
        pacientes.forEach(paciente => {
            const option = document.createElement('option');
            option.value = paciente.id;
            option.textContent = `${paciente.nombre} ${paciente.apellido} (DNI: ${paciente.dni})`;
            select.appendChild(option);
        });
    }
}

async function loadTurnoMedicos() {
    const medicos = await api.getMedicos();
    const select = document.getElementById('turno-medico');
    select.innerHTML = '<option value="">Seleccione un médico</option>';

    if (medicos && medicos.length > 0) {
        medicos.forEach(medico => {
            const option = document.createElement('option');
            option.value = medico.id;
            option.textContent = `${medico.nombre} ${medico.apellido} (${medico.disciplina})`;
            select.appendChild(option);
        });
    }
}

async function loadTurnos() {
    const turnos = await api.getTurnos();
    const tableBody = document.getElementById('turnos-table').querySelector('tbody');
    tableBody.innerHTML = '';

    if (turnos && turnos.length > 0) {
        // Necesitamos obtener nombres de pacientes y médicos
        const pacientes = await api.getPacientes();
        const medicos = await api.getMedicos();

        turnos.forEach(turno => {
            const paciente = pacientes.find(p => p.id === turno.pacienteId);
            const medico = medicos.find(m => m.id === turno.medicoId);

            const row = document.createElement('tr');

            row.innerHTML = `
                <td>${paciente ? `${paciente.nombre} ${paciente.apellido}` : 'Desconocido'}</td>
                <td>${medico ? `${medico.nombre} ${medico.apellido}` : 'Desconocido'}</td>
                <td>${new Date(turno.fechaHora).toLocaleString()}</td>
                <td>${turno.estado}</td>
                <td>
                    <button class="action-btn edit-btn" onclick="editTurno('${turno.id}')">Editar</button>
                    <button class="action-btn delete-btn" onclick="deleteTurno('${turno.id}')">Eliminar</button>
                </td>
            `;

            tableBody.appendChild(row);
        });
    } else {
        const row = document.createElement('tr');
        row.innerHTML = '<td colspan="5">No hay turnos registrados</td>';
        tableBody.appendChild(row);
    }
}

async function editTurno(id) {
    const turno = await api.getTurno(id);
    if (turno) {
        document.getElementById('turno-paciente').value = turno.pacienteId;
        document.getElementById('turno-medico').value = turno.medicoId;
        document.getElementById('turno-fecha').value = turno.fechaHora;
        document.getElementById('turno-estado').value = turno.estado;

        // Cambiar el botón de submit para actualizar
        const form = document.getElementById('turno-form');
        const submitBtn = form.querySelector('button[type="submit"]');
        submitBtn.textContent = 'Actualizar';
        submitBtn.onclick = async (e) => {
            e.preventDefault();

            const updatedTurno = {
                pacienteId: document.getElementById('turno-paciente').value,
                medicoId: document.getElementById('turno-medico').value,
                fechaHora: document.getElementById('turno-fecha').value,
                estado: document.getElementById('turno-estado').value
            };

            const result = await api.updateTurno(id, updatedTurno);
            if (result) {
                alert('Turno actualizado con éxito');
                form.reset();
                submitBtn.textContent = 'Programar Turno';
                submitBtn.onclick = null;
                loadTurnos();
            }
        };
    }
}

async function deleteTurno(id) {
    if (confirm('¿Está seguro de que desea eliminar este turno?')) {
        const success = await api.deleteTurno(id);
        if (success) {
            alert('Turno eliminado con éxito');
            loadTurnos();
        }
    }
}