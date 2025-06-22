const API_BASE_URL = 'http://localhost:8080';

// Funciones genéricas para llamadas API
async function fetchData(endpoint) {
    try {
        const response = await fetch(`${API_BASE_URL}${endpoint}`);
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        return await response.json();
    } catch (error) {
        console.error('Error fetching data:', error);
        return null;
    }
}

async function postData(endpoint, data) {
    try {
        const response = await fetch(`${API_BASE_URL}${endpoint}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(data),
        });
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        return await response.json();
    } catch (error) {
        console.error('Error posting data:', error);
        return null;
    }
}

async function putData(endpoint, data) {
    try {
        const response = await fetch(`${API_BASE_URL}${endpoint}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(data),
        });
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        return await response.json();
    } catch (error) {
        console.error('Error updating data:', error);
        return null;
    }
}

async function deleteData(endpoint) {
    try {
        const response = await fetch(`${API_BASE_URL}${endpoint}`, {
            method: 'DELETE',
        });
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        return true;
    } catch (error) {
        console.error('Error deleting data:', error);
        return false;
    }
}

// Funciones específicas para cada recurso
const api = {
    // Pacientes
    getPacientes: () => fetchData('/pacientes'),
    createPaciente: (paciente) => postData('/pacientes', paciente),
    updatePaciente: (id, paciente) => putData(`/pacientes/${id}`, paciente),
    deletePaciente: (id) => deleteData(`/pacientes/${id}`),

    // Médicos
    getMedicos: () => fetchData('/medicos'),
    createMedico: (medico) => postData('/medicos', medico),
    updateMedico: (id, medico) => putData(`/medicos/${id}`, medico),
    deleteMedico: (id) => deleteData(`/medicos/${id}`),

    // Turnos
    getTurnos: () => fetchData('/turnos'),
    createTurno: (turno) => postData('/turnos', turno),
    updateTurno: (id, turno) => putData(`/turnos/${id}`, turno),
    deleteTurno: (id) => deleteData(`/turnos/${id}`),

    // Dashboard
    getDashboardData: () => fetchData('/dashboard'),

    // Hábitos y síntomas
    getHabitosByPaciente: (pacienteId) => fetchData(`/habitosysintomas?pacienteId=${pacienteId}`),

    // Riesgo
    getRiesgoPaciente: (pacienteId) => fetchData(`/riesgo/${pacienteId}`)
};

// Función para mostrar secciones
function showSection(sectionId) {
    document.querySelectorAll('section').forEach(section => {
        section.classList.remove('active');
    });
    document.getElementById(sectionId).classList.add('active');
}

// Inicialización
document.addEventListener('DOMContentLoaded', () => {
    // Mostrar dashboard por defecto
    showSection('dashboard');
    loadDashboardData();
});