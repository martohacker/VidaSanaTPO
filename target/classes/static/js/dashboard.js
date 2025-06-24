async function loadDashboardData() {
    const data = await api.getDashboardData();
    if (data) {
        document.getElementById('total-pacientes').textContent = data.totalPacientes;
        document.getElementById('turnos-pendientes').textContent = data.totalTurnosPendientes;
        document.getElementById('promedio-sueno').textContent = data.promedioSueno.toFixed(1);
        document.getElementById('alertas-pendientes').textContent = data.totalAlertasPendientes || 0;
        
        // Cargar alertas recientes
        loadAlertasRecientes();
        
        // Cargar gráfico de pacientes por médico
        if (data.pacientesPorMedico) {
            const chartContainer = document.getElementById('pacientes-medico-chart');
            chartContainer.innerHTML = '';
            
            Object.entries(data.pacientesPorMedico).forEach(([medico, cantidad]) => {
                const div = document.createElement('div');
                div.className = 'chart-item';
                div.innerHTML = `
                    <span class="medico-nombre">${medico}</span>
                    <span class="paciente-count">${cantidad} pacientes</span>
                `;
                chartContainer.appendChild(div);
            });
        }
    }
}

async function loadAlertasRecientes() {
    const alertas = await api.getAlertas();
    const container = document.getElementById('alertas-recientes');
    container.innerHTML = '';
    
    if (alertas && alertas.length > 0) {
        // Mostrar solo las 5 alertas más recientes
        const alertasRecientes = alertas.slice(0, 5);
        alertasRecientes.forEach(alerta => {
            const div = document.createElement('div');
            div.className = `alerta-item ${alerta.tipo}`;
            div.innerHTML = `
                <div class="alerta-header">
                    <span class="alerta-tipo">${alerta.tipo}</span>
                    <span class="alerta-fecha">${new Date(alerta.fecha).toLocaleDateString()}</span>
                </div>
                <div class="alerta-paciente">${alerta.paciente ? (alerta.paciente.nombre + ' ' + alerta.paciente.apellido) : 'Paciente no disponible'}</div>
                <div class="alerta-descripcion">${alerta.descripcion}</div>
                <button class="btn-resolver" onclick="marcarAlertaResuelta('${alerta.id}')">Marcar Resuelta</button>
            `;
            container.appendChild(div);
        });
    } else {
        container.innerHTML = '<p>No hay alertas pendientes</p>';
    }
}

async function marcarAlertaResuelta(alertaId) {
    const result = await api.marcarAlertaResuelta(alertaId);
    if (result) {
        loadDashboardData(); // Recargar dashboard
    }
}

// Actualizar dashboard cada 30 segundos
setInterval(loadDashboardData, 30000);