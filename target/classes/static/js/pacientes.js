document.addEventListener('DOMContentLoaded', () => {
    const pacienteForm = document.getElementById('paciente-form');
    const pacientesTable = document.getElementById('pacientes-table').querySelector('tbody');

    // Cargar lista de pacientes
    loadPacientes();

    // Manejar envío del formulario
    pacienteForm.addEventListener('submit', async (e) => {
        e.preventDefault();

        const paciente = {
            nombre: document.getElementById('paciente-nombre').value,
            apellido: document.getElementById('paciente-apellido').value,
            email: document.getElementById('paciente-email').value,
            dni: parseInt(document.getElementById('paciente-dni').value),
            nacionalidad: document.getElementById('paciente-nacionalidad').value,
            edad: parseInt(document.getElementById('paciente-edad').value),
            fechaNac: document.getElementById('paciente-fecha-nac').value
        };

        const result = await api.createPaciente(paciente);
        if (result) {
            alert('Paciente registrado con éxito');
            pacienteForm.reset();
            loadPacientes();
        }
    });
});

async function loadPacientes() {
    const pacientes = await api.getPacientes();
    const tableBody = document.getElementById('pacientes-table').querySelector('tbody');
    tableBody.innerHTML = '';

    if (pacientes && pacientes.length > 0) {
        pacientes.forEach(paciente => {
            const row = document.createElement('tr');

            row.innerHTML = `
                <td>${paciente.nombre}</td>
                <td>${paciente.apellido}</td>
                <td>${paciente.dni}</td>
                <td>${paciente.email}</td>
                <td>
                    <button class="action-btn edit-btn" onclick="editPaciente('${paciente.id}')">Editar</button>
                    <button class="action-btn delete-btn" onclick="deletePaciente('${paciente.id}')">Eliminar</button>
                </td>
            `;

            tableBody.appendChild(row);
        });
    } else {
        const row = document.createElement('tr');
        row.innerHTML = '<td colspan="5">No hay pacientes registrados</td>';
        tableBody.appendChild(row);
    }
}

async function editPaciente(id) {
    const paciente = await api.getPaciente(id);
    if (paciente) {
        document.getElementById('paciente-nombre').value = paciente.nombre;
        document.getElementById('paciente-apellido').value = paciente.apellido;
        document.getElementById('paciente-email').value = paciente.email;
        document.getElementById('paciente-dni').value = paciente.dni;
        document.getElementById('paciente-nacionalidad').value = paciente.nacionalidad;
        document.getElementById('paciente-edad').value = paciente.edad;
        document.getElementById('paciente-fecha-nac').value = paciente.fechaNac;

        // Cambiar el botón de submit para actualizar
        const form = document.getElementById('paciente-form');
        const submitBtn = form.querySelector('button[type="submit"]');
        submitBtn.textContent = 'Actualizar';
        submitBtn.onclick = async (e) => {
            e.preventDefault();

            const updatedPaciente = {
                nombre: document.getElementById('paciente-nombre').value,
                apellido: document.getElementById('paciente-apellido').value,
                email: document.getElementById('paciente-email').value,
                dni: parseInt(document.getElementById('paciente-dni').value),
                nacionalidad: document.getElementById('paciente-nacionalidad').value,
                edad: parseInt(document.getElementById('paciente-edad').value),
                fechaNac: document.getElementById('paciente-fecha-nac').value
            };

            const result = await api.updatePaciente(id, updatedPaciente);
            if (result) {
                alert('Paciente actualizado con éxito');
                form.reset();
                submitBtn.textContent = 'Registrar';
                submitBtn.onclick = null;
                loadPacientes();
            }
        };
    }
}

async function deletePaciente(id) {
    if (confirm('¿Está seguro de que desea eliminar este paciente?')) {
        const success = await api.deletePaciente(id);
        if (success) {
            alert('Paciente eliminado con éxito');
            loadPacientes();
        }
    }
}