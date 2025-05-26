import React, { useEffect } from 'react';
import { useAuth0 } from '@auth0/auth0-react';
import './App.css';

function App() {
  // Redirección automática si la URL contiene :8080
  useEffect(() => {
    if (window.location.host.includes(':8080')) {
      const cleanUrl = window.location.href.replace(':8080', '');
      window.location.replace(cleanUrl);
    }
  }, []);

  const { user, isAuthenticated, isLoading, loginWithRedirect, logout } = useAuth0();

  if (isLoading) {
    return (
      <div className="loading-container">
        <div className="spinner"></div>
        <p>Cargando...</p>
      </div>
    );
  }

  if (!isAuthenticated) {
    return (
      <div className="login-container">
        <div className="login-card">
          <h1>Bienvenido</h1>
          <p>Por favor, inicia sesión para acceder al dashboard</p>
          <button 
            className="login-button"
            onClick={() => loginWithRedirect()}
          >
            Iniciar Sesión con Auth0
          </button>
        </div>
      </div>
    );
  }

  return (
    <div className="App">
      <header className="app-header">
        <div className="header-content">
          <h1>Dashboard</h1>
          <div className="user-info">
            {user?.picture && (
              <img 
                src={user.picture} 
                alt="Profile" 
                className="profile-picture"
              />
            )}
            <span className="username">
              Hola, {user?.name || user?.email || 'Usuario'}
            </span>
            <button 
              className="logout-button"
              onClick={() => logout({ returnTo: window.location.origin })}
            >
              Cerrar Sesión
            </button>
          </div>
        </div>
      </header>
      
      <main className="main-content">
        <div className="dashboard-container">
          <div className="welcome-section">
            <h2>Dashboard</h2>
            <p>Bienvenido al dashboard, <strong>{user?.name || user?.email}</strong></p>
          </div>
          
          <div className="stats-grid">
            <div className="stat-card">
              <h3>Usuarios Activos</h3>
              <div className="stat-number">1,234</div>
            </div>
            <div className="stat-card">
              <h3>Sesiones Hoy</h3>
              <div className="stat-number">89</div>
            </div>
            <div className="stat-card">
              <h3>Nuevos Registros</h3>
              <div className="stat-number">24</div>
            </div>
            <div className="stat-card">
              <h3>Uptime</h3>
              <div className="stat-number">99.9%</div>
            </div>
          </div>
          
          <div className="user-details">
            <h3>Información del Usuario</h3>
            <div className="user-detail-grid">
              <div className="detail-item">
                <strong>Nombre:</strong> {user?.name || 'No disponible'}
              </div>
              <div className="detail-item">
                <strong>Email:</strong> {user?.email || 'No disponible'}
              </div>
              <div className="detail-item">
                <strong>ID:</strong> {user?.sub || 'No disponible'}
              </div>
              <div className="detail-item">
                <strong>Última actualización:</strong> {user?.updated_at || 'No disponible'}
              </div>
            </div>
          </div>
        </div>
      </main>
    </div>
  );
}

export default App;