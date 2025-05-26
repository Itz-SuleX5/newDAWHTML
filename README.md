# Auth0 Full Stack Application

Este proyecto incluye una aplicación completa con autenticación Auth0:
- **Backend**: Spring Boot con Spring Security y OAuth2
- **Frontend**: React con Auth0 SDK
- **Containerización**: Docker y Docker Compose
- **Despliegue**: Configurado para GitHub

## 🚀 Características

### Backend (Spring Boot)
- ✅ **Protección completa de rutas**: Todas las rutas requieren autenticación
- ✅ **Redirección automática a Auth0**: Si no estás logueado, te redirige automáticamente
- ✅ **API REST**: Endpoints protegidos
- ✅ **Interfaz web**: UI con Thymeleaf y Bootstrap

### Frontend (React)
- ✅ **Dashboard moderno**: Interfaz responsive con React
- ✅ **Autenticación Auth0**: Login/logout integrado
- ✅ **Información del usuario**: Muestra datos del perfil
- ✅ **Diseño responsive**: Compatible con móviles y desktop

## 📋 Configuración de Auth0

### Credenciales configuradas:
- **Domain**: `dev-6a8gx4jqe8ymcodi.us.auth0.com`
- **Client ID**: `LeECmGtmibebqZVG80hUoUUl7ZefIr7a`
- **Client Secret**: `pgRctA7QdN4fUe2nAQsK9h-1bmh8DA8qRToGFHXSZ7MVFDY33lbVR6J63Ku7Orxo`

### URLs configuradas en Auth0:
- **Allowed Callback URLs**: 
  - `http://localhost:8080/login/oauth2/code/auth0` (Spring Boot)
  - `http://localhost:3000` (React)
- **Allowed Logout URLs**: 
  - `http://localhost:8080/`
  - `http://localhost:3000`
- **Allowed Web Origins**: 
  - `http://localhost:8080`
  - `http://localhost:3000`

## 🐳 Ejecución con Docker

### Prerrequisitos
- Docker
- Docker Compose

### Ejecutar toda la aplicación

```bash
# Clonar el repositorio
git clone https://github.com/Itz-SuleX5/newDAW.git
cd newDAW

# Ejecutar con Docker Compose
docker-compose up --build
```

### Acceder a las aplicaciones
- **React Frontend**: http://localhost:3000
- **Spring Boot Backend**: http://localhost:8080

### Ejecutar servicios individualmente

```bash
# Solo el backend Spring Boot
docker-compose up auth0-backend

# Solo el frontend React
docker-compose up auth0-frontend
```

## 💻 Desarrollo Local

### Backend (Spring Boot)

```bash
# Navegar al directorio raíz
cd proyectoNuevo

# Ejecutar con Maven
mvn spring-boot:run

# O compilar y ejecutar
mvn clean package
java -jar target/auth0-springboot-0.0.1-SNAPSHOT.jar
```

### Frontend (React)

```bash
# Navegar al directorio de React
cd react-dashboard

# Instalar dependencias
npm install

# Ejecutar en modo desarrollo
npm start
```

## 📁 Estructura del Proyecto

```
proyectoNuevo/
├── src/                                    # Código fuente Spring Boot
│   ├── main/java/com/example/auth0springboot/
│   │   ├── Auth0SpringbootApplication.java
│   │   ├── config/SecurityConfig.java
│   │   └── controller/HomeController.java
│   └── main/resources/
│       ├── application.yml
│       └── templates/                      # Plantillas Thymeleaf
├── react-dashboard/                        # Aplicación React
│   ├── src/
│   │   ├── App.js                         # Componente principal
│   │   ├── App.css                        # Estilos
│   │   └── index.js                       # Configuración Auth0
│   ├── public/
│   ├── Dockerfile                         # Docker para React
│   └── package.json
├── Dockerfile                              # Docker para Spring Boot
├── docker-compose.yml                     # Orquestación completa
├── pom.xml                                # Dependencias Maven
└── README.md
```

## 🌐 Rutas Disponibles

### Backend (Spring Boot) - http://localhost:8080
- `/` - Redirige a `/home`
- `/home` - Página principal (requiere autenticación)
- `/profile` - Perfil del usuario (requiere autenticación)
- `/dashboard` - Dashboard con estadísticas (requiere autenticación)
- `/logout` - Cerrar sesión

### Frontend (React) - http://localhost:3000
- `/` - Dashboard principal con autenticación Auth0

## 🔧 Tecnologías Utilizadas

### Backend
- **Spring Boot 3.2.0**
- **Spring Security 6**
- **OAuth2 Client**
- **Thymeleaf**
- **Maven**

### Frontend
- **React 18**
- **Auth0 React SDK**
- **CSS3 con Flexbox/Grid**
- **Responsive Design**

### DevOps
- **Docker**
- **Docker Compose**
- **Nginx** (para servir React en producción)

## 🚀 Despliegue en GitHub

### Subir al repositorio

```bash
# Inicializar git (si no está inicializado)
git init

# Agregar remote
git remote add origin https://github.com/Itz-SuleX5/newDAW.git

# Agregar archivos
git add .

# Commit
git commit -m "Initial commit: Auth0 Full Stack Application"

# Push
git push -u origin main
```

### GitHub Actions (Opcional)

Puedes agregar un workflow de GitHub Actions para CI/CD:

```yaml
# .github/workflows/docker.yml
name: Docker Build and Push

on:
  push:
    branches: [ main ]

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
    - uses: actions/checkout@v2
    - name: Build and run with Docker Compose
      run: |
        docker-compose up --build -d
        docker-compose down
```

## 🔒 Seguridad

- Todas las rutas están protegidas por Auth0
- Tokens JWT manejados automáticamente
- Logout seguro que limpia sesiones
- Variables de entorno para credenciales sensibles

## 🐛 Solución de Problemas

### Error: "Invalid client_id"
- Verifica que las credenciales de Auth0 sean correctas
- Asegúrate de que la aplicación esté habilitada en Auth0

### Error: "Callback URL mismatch"
- Verifica que las URLs estén configuradas correctamente en Auth0
- Para desarrollo local: `http://localhost:3000` y `http://localhost:8080/login/oauth2/code/auth0`

### Error de Docker
```bash
# Limpiar contenedores y volúmenes
docker-compose down -v
docker system prune -f

# Reconstruir
docker-compose up --build
```

## 📝 Licencia

Este proyecto es de código abierto y está disponible bajo la licencia MIT.

## 👥 Contribución

1. Fork el proyecto
2. Crea una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abre un Pull Request

---

**¡Listo para usar!** 🎉

Ejecuta `docker-compose up --build` y accede a:
- **React App**: http://localhost:3000
- **Spring Boot App**: http://localhost:8080