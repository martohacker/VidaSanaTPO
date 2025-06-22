async function loadDashboardData() {
    const data = await api.getDashboardData();
    if (data) {
        document.getElementById('total-pacientes').textContent = data.totalPacientes;
        document.getElementById('turnos-pendientes').textContent = data.totalTurnosPendientes;
        document.getElementById('promedio-sueno').textContent = data.promedioSueno.toFixed(1);

        // Crear gráfico simple de pacientes por médico
        const chartContainer = document.getElementById('pacientes-medico-chart');
        chartContainer.innerHTML = '';

        for (const [medico, cantidad] of Object.entries(data.pacientesPorMedico)) {
            const barContainer = document.createElement('div');
            barContainer.style.marginBottom = '10px';

            const label = document.createElement('span');
            label.textContent = medico;
            label.style.display = 'inline-block';
            label.style.width = '200px';
            label.style.marginRight = '10px';

            const bar = document.createElement('div');
            bar.style.display = 'inline-block';
            bar.style.height = '20px';
            bar.style.backgroundColor = '#3498db';
            bar.style.width = `${cantidad * 20}px`;
            bar.style.borderRadius = '4px';

            const count = document.createElement('span');
            count.textContent = cantidad;
            count.style.marginLeft = '10px';

            barContainer.appendChild(label);
            barContainer.appendChild(bar);
            barContainer.appendChild(count);
            chartContainer.appendChild(barContainer);
        }
    }
}

// Actualizar dashboard cada 30 segundos
setInterval(loadDashboardData, 30000);