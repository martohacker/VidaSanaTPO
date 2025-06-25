document.addEventListener('DOMContentLoaded', () => {
    const pacienteForm = document.getElementById('paciente-form');
    const pacientesTable = document.getElementById('pacientes-table').querySelector('tbody');

    // Cargar lista de pacientes
    loadPacientes();

    // Manejar envío del formulario
    pacienteForm.addEventListener('submit', async (e) => {
        e.preventDefault();

        // Validación frontend
        const nombre = document.getElementById('paciente-nombre').value.trim();
        const apellido = document.getElementById('paciente-apellido').value.trim();
        const email = document.getElementById('paciente-email').value.trim();
        const dni = document.getElementById('paciente-dni').value.trim();
        const nacionalidad = document.getElementById('paciente-nacionalidad').value.trim();
        const edad = document.getElementById('paciente-edad').value.trim();
        const fechaNac = document.getElementById('paciente-fecha-nac').value.trim();
        const sueno = document.getElementById('paciente-sueno').value.trim();

        if (!nombre || !apellido || !email || !dni || !nacionalidad || !edad || !fechaNac) {
            alert('Por favor, complete todos los campos obligatorios.');
            return;
        }
        if (!/^[^@\s]+@[^@\s]+\.[^@\s]+$/.test(email)) {
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

        const paciente = {
            nombre,
            apellido,
            email,
            dni: parseInt(dni),
            nacionalidad,
            edad: parseInt(edad),
            fechaNac,
            sueño: sueno
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
    console.log('Pacientes recibidos:', pacientes);
    const tableBody = document.getElementById('pacientes-table').querySelector('tbody');
    tableBody.innerHTML = '';

    if (pacientes && pacientes.length > 0) {
        pacientes.forEach(paciente => {
            // Si no tiene 'id' pero sí '_id', lo asignamos
            if (!paciente.id && paciente._id) {
                paciente.id = paciente._id;
            }
            const row = document.createElement('tr');

            row.innerHTML = `
                <td>${paciente.nombre}</td>
                <td>${paciente.apellido}</td>
                <td>${paciente.dni}</td>
                <td>${paciente.email}</td>
                <td>
                    <button class="action-btn edit-btn" onclick="editPaciente('${paciente.id}')">Editar</button>
                    <button class="action-btn delete-btn" onclick="deletePaciente('${paciente.id}')">Eliminar</button>
                    <button class="action-btn" onclick='showHistorialModal(${JSON.stringify(paciente)})'>Ver historial</button>
                    <button class="action-btn" onclick='showHabitosModal(${JSON.stringify(paciente)})'>Hábitos/Síntomas</button>
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
        document.getElementById('paciente-sueno').value = paciente.sueño || '';

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
                fechaNac: document.getElementById('paciente-fecha-nac').value,
                sueño: document.getElementById('paciente-sueno').value
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
    console.log('Intentando eliminar paciente con id:', id);
    if (confirm('¿Está seguro de que desea eliminar este paciente?')) {
        try {
            const success = await api.deletePaciente(id);
            if (success) {
                alert('Paciente eliminado con éxito');
                loadPacientes();
            } else {
                alert('No se pudo eliminar el paciente. Puede que tenga turnos asociados o un error en el servidor.');
            }
        } catch (error) {
            alert('Ocurrió un error al intentar eliminar el paciente.');
            console.error(error);
        }
    }
}

// --- HISTORIAL MÉDICO ---
let pacienteActualId = null;

// Autocompletar y deshabilitar campo médico si el usuario es médico
function autocompletarMedicoEnFormularios() {
    if (window.currentUser && currentUser.rol === 'MEDICO') {
        const medicoNombre = currentUser.nombreCompleto || currentUser.nombre || '';
        [
            'nota-medico',
            'diagnostico-medico',
            'receta-medico',
            'examen-medico',
            'tratamiento-medico',
            'alergia-medico',
            'vacuna-medico'
        ].forEach(id => {
            const input = document.getElementById(id);
            if (input) {
                input.value = medicoNombre;
                input.disabled = true;
            }
        });
    } else {
        // Si no es médico, habilitar el campo
        [
            'nota-medico',
            'diagnostico-medico',
            'receta-medico',
            'examen-medico',
            'tratamiento-medico',
            'alergia-medico',
            'vacuna-medico'
        ].forEach(id => {
            const input = document.getElementById(id);
            if (input) {
                input.disabled = false;
            }
        });
    }
}

function showHistorialModal(paciente) {
    pacienteActualId = paciente.id;
    document.getElementById('historial-nombre').textContent = paciente.nombre + ' ' + paciente.apellido;
    document.getElementById('modal-historial').style.display = 'block';
    autocompletarMedicoEnFormularios();
    loadHistorial();
}

document.getElementById('close-historial').onclick = function() {
    document.getElementById('modal-historial').style.display = 'none';
    pacienteActualId = null;
};

// Función para cambiar el formulario según el tipo seleccionado
function cambiarFormularioHistorial() {
    const tipo = document.getElementById('tipo-entrada').value;
    const formularios = document.querySelectorAll('.form-entrada');
    
    // Ocultar todos los formularios
    formularios.forEach(form => form.style.display = 'none');
    
    // Mostrar el formulario correspondiente
    document.getElementById(`form-${tipo}`).style.display = 'flex';
    autocompletarMedicoEnFormularios();
}

async function loadHistorial() {
    if (!pacienteActualId) return;
    
    try {
        const response = await fetch(`/historial/${pacienteActualId}`);
        const historial = await response.json();
        const lista = document.getElementById('historial-lista');
        lista.innerHTML = '';
        
        if (historial && historial.entradas && historial.entradas.length > 0) {
            historial.entradas.forEach((entrada, i) => {
                const div = document.createElement('div');
                div.className = `entrada-historial tipo-${entrada.tipo.toLowerCase()}`;
                
                const fecha = new Date(entrada.fecha).toLocaleDateString('es-ES');
                const tipoDescripcion = entrada.tipo.descripcion || entrada.tipo;
                
                let camposHTML = '';
                if (entrada.camposEspecificos && Object.keys(entrada.camposEspecificos).length > 0) {
                    camposHTML = '<div class="entrada-campos">';
                    Object.entries(entrada.camposEspecificos).forEach(([clave, valor]) => {
                        camposHTML += `<div class="entrada-campo"><strong>${clave}:</strong> ${valor}</div>`;
                    });
                    camposHTML += '</div>';
                }
                
                div.innerHTML = `
                    <div class="entrada-header">
                        <span class="entrada-tipo ${entrada.tipo.toLowerCase()}">${tipoDescripcion}</span>
                        <span class="entrada-fecha">${fecha}</span>
                    </div>
                    <div class="entrada-titulo">${entrada.titulo}</div>
                    <div class="entrada-descripcion">${entrada.descripcion}</div>
                    ${camposHTML}
                    <div style="margin-top: 0.5rem;">
                        <strong>Médico:</strong> ${entrada.medico ? entrada.medico.nombre : 'No especificado'}
                    </div>
                `;
                
                lista.appendChild(div);
            });
        } else {
            lista.innerHTML = '<p style="color: #666; font-style: italic;">No hay entradas en el historial.</p>';
        }
    } catch (error) {
        console.error('Error al cargar historial:', error);
        document.getElementById('historial-lista').innerHTML = 
            '<p style="color: #dc3545;">Error al cargar el historial.</p>';
    }
}

// Configurar formularios para diferentes tipos de entradas
document.getElementById('form-nota').onsubmit = async function(e) {
    e.preventDefault();
    const medico = document.getElementById('nota-medico').value.trim();
    const titulo = document.getElementById('nota-titulo').value.trim();
    const descripcion = document.getElementById('nota-descripcion').value.trim();
    
    if (!medico || !titulo || !descripcion) return;
    
    try {
        await fetch(`/historial/${pacienteActualId}/nota`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ medico, titulo, descripcion })
        });
        this.reset();
        loadHistorial();
    } catch (error) {
        console.error('Error al agregar nota:', error);
        alert('Error al agregar la nota');
    }
};

document.getElementById('form-diagnostico').onsubmit = async function(e) {
    e.preventDefault();
    const medico = document.getElementById('diagnostico-medico').value.trim();
    const titulo = document.getElementById('diagnostico-titulo').value.trim();
    const descripcion = document.getElementById('diagnostico-descripcion').value.trim();
    
    if (!medico || !titulo || !descripcion) return;
    
    try {
        await fetch(`/historial/${pacienteActualId}/diagnostico`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ medico, titulo, descripcion })
        });
        this.reset();
        loadHistorial();
    } catch (error) {
        console.error('Error al agregar diagnóstico:', error);
        alert('Error al agregar el diagnóstico');
    }
};

document.getElementById('form-receta').onsubmit = async function(e) {
    e.preventDefault();
    const medico = document.getElementById('receta-medico').value.trim();
    const titulo = document.getElementById('receta-titulo').value.trim();
    const descripcion = document.getElementById('receta-descripcion').value.trim();
    const medicamentos = document.getElementById('receta-medicamentos').value.trim();
    const dosis = document.getElementById('receta-dosis').value.trim();
    const duracion = document.getElementById('receta-duracion').value.trim();
    
    if (!medico || !titulo || !descripcion || !medicamentos || !dosis || !duracion) return;
    
    try {
        await fetch(`/historial/${pacienteActualId}/receta`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ medico, titulo, descripcion, medicamentos, dosis, duracion })
        });
        this.reset();
        loadHistorial();
    } catch (error) {
        console.error('Error al agregar receta:', error);
        alert('Error al agregar la receta');
    }
};

document.getElementById('form-examen').onsubmit = async function(e) {
    e.preventDefault();
    const medico = document.getElementById('examen-medico').value.trim();
    const titulo = document.getElementById('examen-titulo').value.trim();
    const descripcion = document.getElementById('examen-descripcion').value.trim();
    const resultado = document.getElementById('examen-resultado').value.trim();
    
    if (!medico || !titulo || !descripcion || !resultado) return;
    
    try {
        await fetch(`/historial/${pacienteActualId}/examen`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ medico, titulo, descripcion, resultado })
        });
        this.reset();
        loadHistorial();
    } catch (error) {
        console.error('Error al agregar examen:', error);
        alert('Error al agregar el examen');
    }
};

document.getElementById('form-tratamiento').onsubmit = async function(e) {
    e.preventDefault();
    const medico = document.getElementById('tratamiento-medico').value.trim();
    const titulo = document.getElementById('tratamiento-titulo').value.trim();
    const descripcion = document.getElementById('tratamiento-descripcion').value.trim();
    const duracion = document.getElementById('tratamiento-duracion').value.trim();
    
    if (!medico || !titulo || !descripcion || !duracion) return;
    
    try {
        await fetch(`/historial/${pacienteActualId}/tratamiento`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ medico, titulo, descripcion, duracion })
        });
        this.reset();
        loadHistorial();
    } catch (error) {
        console.error('Error al agregar tratamiento:', error);
        alert('Error al agregar el tratamiento');
    }
};

document.getElementById('form-alergia').onsubmit = async function(e) {
    e.preventDefault();
    const medico = document.getElementById('alergia-medico').value.trim();
    const titulo = document.getElementById('alergia-titulo').value.trim();
    const descripcion = document.getElementById('alergia-descripcion').value.trim();
    const alergeno = document.getElementById('alergia-alergeno').value.trim();
    
    if (!medico || !titulo || !descripcion || !alergeno) return;
    
    try {
        await fetch(`/historial/${pacienteActualId}/alergia`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ medico, titulo, descripcion, alergeno })
        });
        this.reset();
        loadHistorial();
    } catch (error) {
        console.error('Error al agregar alergia:', error);
        alert('Error al agregar la alergia');
    }
};

document.getElementById('form-vacuna').onsubmit = async function(e) {
    e.preventDefault();
    const medico = document.getElementById('vacuna-medico').value.trim();
    const titulo = document.getElementById('vacuna-titulo').value.trim();
    const descripcion = document.getElementById('vacuna-descripcion').value.trim();
    const vacuna = document.getElementById('vacuna-vacuna').value.trim();
    const lote = document.getElementById('vacuna-lote').value.trim();
    
    if (!medico || !titulo || !descripcion || !vacuna || !lote) return;
    
    try {
        await fetch(`/historial/${pacienteActualId}/vacuna`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ medico, titulo, descripcion, vacuna, lote })
        });
        this.reset();
        loadHistorial();
    } catch (error) {
        console.error('Error al agregar vacuna:', error);
        alert('Error al agregar la vacuna');
    }
};

// --- HÁBITOS Y SÍNTOMAS DIARIOS ---
let pacienteActualIdHabitos = null;

function showHabitosModal(paciente) {
    pacienteActualIdHabitos = paciente.id;
    document.getElementById('habitos-nombre').textContent = paciente.nombre + ' ' + paciente.apellido;
    document.getElementById('modal-habitos').style.display = 'block';
    // Establecer fecha actual por defecto
    document.getElementById('habitos-fecha').value = new Date().toISOString().split('T')[0];
    loadHabitos();
    // Cargar el scoring de riesgo
    mostrarScoring(paciente.id);
}

document.getElementById('close-habitos').onclick = function() {
    document.getElementById('modal-habitos').style.display = 'none';
    pacienteActualIdHabitos = null;
};

// Función para mostrar el scoring de riesgo
async function mostrarScoring(pacienteId) {
    try {
        const response = await fetch(`/riesgo/${pacienteId}`);
        const data = await response.json();
        
        const scoringInfo = document.getElementById('scoring-info');
        
        if (data && data.nivel) {
            // Determinar el color según el nivel de riesgo
            let colorClass = '';
            let icon = '';
            switch(data.nivel.toLowerCase()) {
                case 'bajo':
                    colorClass = 'text-success';
                    icon = '🟢';
                    break;
                case 'medio':
                    colorClass = 'text-warning';
                    icon = '🟡';
                    break;
                case 'alto':
                    colorClass = 'text-danger';
                    icon = '🔴';
                    break;
                default:
                    colorClass = 'text-info';
                    icon = '🔵';
            }
            
            scoringInfo.innerHTML = `
                <div style="margin-bottom: 1rem;">
                    <strong>Puntaje de Riesgo:</strong> 
                    <span style="font-size: 1.2em; font-weight: bold; color: #007bff;">${data.score}</span>
                </div>
                <div style="margin-bottom: 1rem;">
                    <strong>Nivel de Riesgo:</strong> 
                    <span class="${colorClass}" style="font-weight: bold;">${icon} ${data.nivel.toUpperCase()}</span>
                </div>
                <div>
                    <strong>Recomendaciones:</strong>
                    <ul style="margin: 0.5rem 0 0 0; padding-left: 1.5rem;">
                        ${data.recomendaciones.map(r => `<li style="margin-bottom: 0.3rem;">${r}</li>`).join('')}
                    </ul>
                </div>
            `;
        } else {
            scoringInfo.innerHTML = `
                <p style="margin: 0; color: #666; font-style: italic;">
                    No hay suficientes datos para calcular el riesgo. 
                    Agrega hábitos y síntomas para obtener una evaluación.
                </p>
            `;
        }
    } catch (error) {
        console.error('Error al cargar el scoring:', error);
        document.getElementById('scoring-info').innerHTML = `
            <p style="margin: 0; color: #dc3545;">
                Error al cargar la evaluación de riesgo.
            </p>
        `;
    }
}

async function loadHabitos() {
    if (!pacienteActualIdHabitos) return;
    const habitos = await api.getHabitosSintomas(pacienteActualIdHabitos);
    const lista = document.getElementById('habitos-lista');
    lista.innerHTML = '';
    if (habitos && habitos.length > 0) {
        habitos.forEach((habito, i) => {
            const li = document.createElement('li');
            li.textContent = `${habito.fecha} - ${habito.descripcion}`;
            const btn = document.createElement('button');
            btn.textContent = 'Eliminar';
            btn.onclick = async () => {
                await api.deleteHabitoSintoma(pacienteActualIdHabitos, i);
                loadHabitos();
                // Actualizar scoring después de eliminar
                mostrarScoring(pacienteActualIdHabitos);
            };
            li.appendChild(btn);
            lista.appendChild(li);
        });
    } else {
        lista.innerHTML = '<li>No hay registros de hábitos/síntomas.</li>';
    }
}

document.getElementById('form-habitos').onsubmit = async function(e) {
    e.preventDefault();
    const fecha = document.getElementById('habitos-fecha').value;
    const descripcion = document.getElementById('habitos-descripcion').value.trim();
    if (!fecha || !descripcion) return;
    
    const habitoSintoma = {
        fecha: fecha,
        descripcion: descripcion
    };
    
    await api.addHabitoSintoma(pacienteActualIdHabitos, habitoSintoma);
    document.getElementById('habitos-descripcion').value = '';
    loadHabitos();
    // Actualizar scoring después de agregar nuevo hábito/síntoma
    mostrarScoring(pacienteActualIdHabitos);
};

// --- SIMULACIÓN DE DATOS DE PRUEBA ---

// Cargar pacientes en el selector de simulación
async function cargarPacientesSimulacion() {
    try {
        const pacientes = await api.getPacientes();
        const select = document.getElementById('simulacion-paciente');
        select.innerHTML = '<option value="">Seleccione un paciente</option>';
        
        pacientes.forEach(paciente => {
            const option = document.createElement('option');
            option.value = paciente.id || paciente._id;
            option.textContent = `${paciente.nombre} ${paciente.apellido} (${paciente.dni})`;
            select.appendChild(option);
        });
    } catch (error) {
        console.error('Error al cargar pacientes:', error);
        alert('Error al cargar la lista de pacientes');
    }
}

// Simular hábitos y síntomas
async function simularHabitos() {
    const pacienteId = document.getElementById('simulacion-paciente').value;
    if (!pacienteId) {
        alert('Por favor, seleccione un paciente');
        return;
    }
    
    mostrarResultadoSimulacion('Generando hábitos y síntomas...');
    
    try {
        const response = await fetch(`/simulacion/habitos/${pacienteId}?dias=30`, {
            method: 'POST'
        });
        const resultado = await response.json();
        
        mostrarResultadoSimulacion(`
            <div class="actividad-exitosa">${resultado.mensaje}</div>
            <div style="margin-top: 1rem;">
                <strong>Paciente:</strong> ${resultado.paciente}<br>
                <strong>Hábitos generados:</strong> ${resultado.habitos.length}
            </div>
            <details style="margin-top: 1rem;">
                <summary>Ver hábitos generados</summary>
                <ul style="margin-top: 0.5rem; max-height: 200px; overflow-y: auto;">
                    ${resultado.habitos.map(h => `<li>${h}</li>`).join('')}
                </ul>
            </details>
        `);
        
        // Recargar la lista de pacientes para mostrar los nuevos datos
        loadPacientes();
    } catch (error) {
        console.error('Error en simulación de hábitos:', error);
        mostrarResultadoSimulacion(`<div class="actividad-error">Error al generar hábitos: ${error.message}</div>`);
    }
}

// Simular datos de sensores
async function simularSensores() {
    const pacienteId = document.getElementById('simulacion-paciente').value;
    if (!pacienteId) {
        alert('Por favor, seleccione un paciente');
        return;
    }
    
    mostrarResultadoSimulacion('Generando datos de sensores...');
    
    try {
        const response = await fetch(`/simulacion/sensores/${pacienteId}?dias=7`, {
            method: 'POST'
        });
        const resultado = await response.json();
        
        let datosHTML = '<table style="width: 100%; margin-top: 1rem; border-collapse: collapse;">';
        datosHTML += '<tr><th>Fecha</th><th>Sueño</th><th>Pasos</th><th>Presión</th><th>FC</th><th>Temp</th><th>Peso</th></tr>';
        
        resultado.datos.forEach(dia => {
            datosHTML += `<tr>
                <td>${dia.fecha}</td>
                <td>${dia.sueño_horas}h</td>
                <td>${dia.pasos}</td>
                <td>${dia.presion_sistolica}/${dia.presion_diastolica}</td>
                <td>${dia.frecuencia_cardiaca}</td>
                <td>${dia.temperatura.toFixed(1)}°C</td>
                <td>${dia.peso.toFixed(1)}kg</td>
            </tr>`;
        });
        datosHTML += '</table>';
        
        mostrarResultadoSimulacion(`
            <div class="actividad-exitosa">${resultado.mensaje}</div>
            <div style="margin-top: 1rem;">
                <strong>Paciente:</strong> ${resultado.paciente}
            </div>
            ${datosHTML}
        `);
    } catch (error) {
        console.error('Error en simulación de sensores:', error);
        mostrarResultadoSimulacion(`<div class="actividad-error">Error al generar datos de sensores: ${error.message}</div>`);
    }
}

// Simular historial médico
async function simularHistorial() {
    const pacienteId = document.getElementById('simulacion-paciente').value;
    if (!pacienteId) {
        alert('Por favor, seleccione un paciente');
        return;
    }
    
    mostrarResultadoSimulacion('Generando historial médico...');
    
    try {
        const response = await fetch(`/simulacion/historial/${pacienteId}`, {
            method: 'POST'
        });
        const resultado = await response.json();
        
        mostrarResultadoSimulacion(`
            <div class="actividad-exitosa">${resultado.mensaje}</div>
            <div style="margin-top: 1rem;">
                <strong>Paciente:</strong> ${resultado.paciente}<br>
                <strong>Entradas generadas:</strong> ${resultado.entradas.length}
            </div>
            <details style="margin-top: 1rem;">
                <summary>Ver entradas generadas</summary>
                <ul style="margin-top: 0.5rem;">
                    ${resultado.entradas.map(e => `<li>${e}</li>`).join('')}
                </ul>
            </details>
        `);
    } catch (error) {
        console.error('Error en simulación de historial:', error);
        mostrarResultadoSimulacion(`<div class="actividad-error">Error al generar historial: ${error.message}</div>`);
    }
}

// Simulación completa
async function simulacionCompleta() {
    const pacienteId = document.getElementById('simulacion-paciente').value;
    if (!pacienteId) {
        alert('Por favor, seleccione un paciente');
        return;
    }
    
    mostrarResultadoSimulacion('Iniciando simulación completa...');
    
    try {
        const response = await fetch(`/simulacion/completa/${pacienteId}?diasHabitos=30&diasSensores=7`, {
            method: 'POST'
        });
        const resultado = await response.json();
        
        let actividadesHTML = '<ul style="margin-top: 1rem;">';
        resultado.actividades.forEach(actividad => {
            const clase = actividad.startsWith('✓') ? 'actividad-exitosa' : 'actividad-error';
            actividadesHTML += `<li class="${clase}">${actividad}</li>`;
        });
        actividadesHTML += '</ul>';
        
        mostrarResultadoSimulacion(`
            <div class="actividad-exitosa">${resultado.mensaje}</div>
            <div style="margin-top: 1rem;">
                <strong>Paciente ID:</strong> ${resultado.pacienteId}
            </div>
            ${actividadesHTML}
            <div style="margin-top: 1rem; padding: 1rem; background-color: #e8f5e8; border-radius: 4px;">
                <strong>¡Simulación completada!</strong><br>
                Ahora puedes ver los datos generados en:<br>
                • Hábitos/Síntomas del paciente<br>
                • Historial médico<br>
                • Scoring de riesgo actualizado
            </div>
        `);
        
        // Recargar la lista de pacientes para mostrar los nuevos datos
        loadPacientes();
    } catch (error) {
        console.error('Error en simulación completa:', error);
        mostrarResultadoSimulacion(`<div class="actividad-error">Error en simulación completa: ${error.message}</div>`);
    }
}

// Mostrar resultado de la simulación
function mostrarResultadoSimulacion(mensaje) {
    const resultadoDiv = document.getElementById('simulacion-resultado');
    const mensajeDiv = document.getElementById('simulacion-mensaje');
    
    resultadoDiv.style.display = 'block';
    mensajeDiv.innerHTML = mensaje;
    
    // Hacer scroll hacia el resultado
    resultadoDiv.scrollIntoView({ behavior: 'smooth' });
}