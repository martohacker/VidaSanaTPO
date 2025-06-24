document.addEventListener('DOMContentLoaded', () => {
    const medicoForm = document.getElementById('medico-form');
    const medicosTable = document.getElementById('medicos-table').querySelector('tbody');

    // Cargar lista de médicos
    loadMedicos();

    // Manejar envío del formulario
    medicoForm.addEventListener('submit', async (e) => {
        e.preventDefault();

        // Validación frontend
        const nombre = document.getElementById('medico-nombre').value.trim();
        const apellido = document.getElementById('medico-apellido').value.trim();
        const mail = document.getElementById('medico-email').value.trim();
        const dni = document.getElementById('medico-dni').value.trim();
        const edad = document.getElementById('medico-edad').value.trim();
        const fechaNac = document.getElementById('medico-fecha-nac').value.trim();
        const disciplina = document.getElementById('medico-disciplina').value.trim();

        if (!nombre || !apellido || !mail || !dni || !edad || !fechaNac || !disciplina) {
            alert('Por favor, complete todos los campos obligatorios.');
            return;
        }
        if (!/^[^@\s]+@[^@\s]+\.[^@\s]+$/.test(mail)) {
            alert('Ingrese un email válido.');
            return;
        }
        if (isNaN(parseInt(dni)) || parseInt(dni) <= 0) {
            alert('El DNI debe ser un número mayor que 0.');
            return;
        }
        if (isNaN(parseInt(edad)) || parseInt(edad) <= 0) {
            alert('La edad debe ser un número mayor que 0.');
            return;
        }

        const medico = {
            nombre,
            apellido,
            mail,
            dni: parseInt(dni),
            edad: parseInt(edad),
            fechaNac,
            disciplina
        };

        const result = await api.createMedico(medico);
        if (result) {
            alert('Médico registrado con éxito');
            medicoForm.reset();
            loadMedicos();
            loadTurnoMedicos(); // Actualizar lista de médicos en turnos
        }
    });
});

async function loadMedicos() {
    const medicos = await api.getMedicos();
    const tableBody = document.getElementById('medicos-table').querySelector('tbody');
    tableBody.innerHTML = '';

    if (medicos && medicos.length > 0) {
        medicos.forEach(medico => {
            const row = document.createElement('tr');

            row.innerHTML = `
                <td>${medico.nombre}</td>
                <td>${medico.apellido}</td>
                <td>${medico.dni}</td>
                <td>${medico.disciplina}</td>
                <td>
                    <button class="action-btn edit-btn" onclick="editMedico('${medico.id}')">Editar</button>
                    <button class="action-btn delete-btn" onclick="deleteMedico('${medico.id}')">Eliminar</button>
                </td>
            `;

            tableBody.appendChild(row);
        });
    } else {
        const row = document.createElement('tr');
        row.innerHTML = '<td colspan="5">No hay médicos registrados</td>';
        tableBody.appendChild(row);
    }
}

async function editMedico(id) {
    const medico = await api.getMedico(id);
    if (medico) {
        document.getElementById('medico-nombre').value = medico.nombre;
        document.getElementById('medico-apellido').value = medico.apellido;
        document.getElementById('medico-email').value = medico.mail;
        document.getElementById('medico-dni').value = medico.dni;
        document.getElementById('medico-edad').value = medico.edad;
        document.getElementById('medico-fecha-nac').value = medico.fechaNac;
        document.getElementById('medico-disciplina').value = medico.disciplina;

        // Cambiar el botón de submit para actualizar
        const form = document.getElementById('medico-form');
        const submitBtn = form.querySelector('button[type="submit"]');
        submitBtn.textContent = 'Actualizar';
        submitBtn.onclick = async (e) => {
            e.preventDefault();

            const updatedMedico = {
                nombre: document.getElementById('medico-nombre').value,
                apellido: document.getElementById('medico-apellido').value,
                mail: document.getElementById('medico-email').value,
                dni: parseInt(document.getElementById('medico-dni').value),
                edad: parseInt(document.getElementById('medico-edad').value),
                fechaNac: document.getElementById('medico-fecha-nac').value,
                disciplina: document.getElementById('medico-disciplina').value
            };

            const result = await api.updateMedico(id, updatedMedico);
            if (result) {
                alert('Médico actualizado con éxito');
                form.reset();
                submitBtn.textContent = 'Registrar';
                submitBtn.onclick = null;
                loadMedicos();
                loadTurnoMedicos(); // Actualizar lista de médicos en turnos
            }
        };
    }
}

async function deleteMedico(id) {
    if (confirm('¿Está seguro de que desea eliminar este médico?')) {
        const success = await api.deleteMedico(id);
        if (success) {
            alert('Médico eliminado con éxito');
            loadMedicos();
            loadTurnoMedicos(); // Actualizar lista de médicos en turnos
        }
    }
}