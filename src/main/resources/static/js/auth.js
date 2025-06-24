// Estado global del usuario
let currentUser = null;

// Función para manejar el login
async function handleLogin(event) {
    event.preventDefault();
    
    const username = document.getElementById('login-username').value;
    const password = document.getElementById('login-password').value;
    
    try {
        const response = await api.login({ username, password });
        
        if (response.success) {
            currentUser = response.usuario;
            localStorage.setItem('currentUser', JSON.stringify(currentUser));
            
            // Mostrar información del usuario
            showUserInfo();
            
            // Mostrar navegación según el rol
            showNavigationByRole();
            
            // Ir al dashboard
            showSection('dashboard');
            
            // Cargar datos del dashboard
            loadDashboardData();
            
        } else {
            alert('Error de login: ' + response.message);
        }
    } catch (error) {
        alert('Error de conexión: ' + error.message);
    }
}

// Función para mostrar información del usuario
function showUserInfo() {
    if (currentUser) {
        document.getElementById('user-name').textContent = currentUser.nombreCompleto;
        document.getElementById('user-role').textContent = `(${currentUser.rol})`;
        document.getElementById('user-info').style.display = 'block';
    }
}

// Función para mostrar navegación según el rol
function showNavigationByRole() {
    const nav = document.getElementById('main-nav');
    nav.style.display = 'block';
    
    // Ocultar todas las secciones primero
    document.querySelectorAll('section').forEach(section => {
        section.classList.remove('active');
    });
    
    // Mostrar secciones según el rol
    if (currentUser.rol === 'ADMIN') {
        // Admin puede ver todo
        showSection('dashboard');
    } else if (currentUser.rol === 'MEDICO') {
        // Médico ve dashboard, pacientes y turnos
        showSection('dashboard');
        // Ocultar sección de médicos
        document.querySelector('nav a[onclick="showSection(\'medicos\')"]').parentElement.style.display = 'none';
    } else if (currentUser.rol === 'PACIENTE') {
        // Paciente solo ve dashboard y sus propios datos
        showSection('dashboard');
        // Ocultar secciones de gestión
        document.querySelector('nav a[onclick="showSection(\'pacientes\')"]').parentElement.style.display = 'none';
        document.querySelector('nav a[onclick="showSection(\'medicos\')"]').parentElement.style.display = 'none';
        document.querySelector('nav a[onclick="showSection(\'turnos\')"]').parentElement.style.display = 'none';
    }
}

// Función para logout
function logout() {
    currentUser = null;
    localStorage.removeItem('currentUser');
    
    // Ocultar información del usuario y navegación
    document.getElementById('user-info').style.display = 'none';
    document.getElementById('main-nav').style.display = 'none';
    
    // Mostrar login
    document.querySelectorAll('section').forEach(section => {
        section.classList.remove('active');
    });
    document.getElementById('login').classList.add('active');
    
    // Limpiar formulario
    document.getElementById('login-form').reset();
}

// Función para verificar si hay usuario logueado al cargar la página
function checkAuthStatus() {
    const savedUser = localStorage.getItem('currentUser');
    if (savedUser) {
        currentUser = JSON.parse(savedUser);
        showUserInfo();
        showNavigationByRole();
        loadDashboardData();
    }
}

// Event listeners
document.addEventListener('DOMContentLoaded', () => {
    // Verificar estado de autenticación
    checkAuthStatus();
    
    // Manejar formulario de login
    document.getElementById('login-form').addEventListener('submit', handleLogin);
});

// Función para verificar permisos según el rol
function hasPermission(requiredRole) {
    if (!currentUser) return false;
    
    if (requiredRole === 'ADMIN') {
        return currentUser.rol === 'ADMIN';
    } else if (requiredRole === 'MEDICO') {
        return currentUser.rol === 'ADMIN' || currentUser.rol === 'MEDICO';
    } else if (requiredRole === 'PACIENTE') {
        return currentUser.rol === 'ADMIN' || currentUser.rol === 'MEDICO' || currentUser.rol === 'PACIENTE';
    }
    
    return false;
} 