# Sistema de Mesa de Ayuda - Java SE

## Descripción del Proyecto

Este proyecto implementa un sistema completo de Mesa de Ayuda (Help Desk) desarrollado en Java SE utilizando el formato de proyecto Ant. Es una aplicación de consola diseñada para gestionar tickets de incidencias o reclamos siguiendo un flujo de trabajo profesional.

## Características Principales

### Funcionalidades Core
- **Gestión Completa de Tickets**: Crear, asignar, iniciar y cerrar tickets
- **Sistema de Técnicos**: Gestión de personal técnico con habilidades especializadas
- **Clasificación Automática**: IA básica para categorizar y priorizar tickets automáticamente
- **Métricas y Reportes**: Estadísticas detalladas del rendimiento del sistema
- **Persistencia en CSV**: Almacenamiento de datos sin dependencias externas
- **Búsqueda y Filtros**: Múltiples opciones de búsqueda y filtrado

### Arquitectura del Sistema
```
mesadeayudajuname/
├── model/          # Modelos de datos (Ticket, Tecnico, enums)
├── service/        # Lógica de negocio (TableroService, Clasificador)
├── util/           # Utilidades (CSVRepository, DatosEjemplo)
├── app/            # Interfaz de usuario (MesaDeAyudaApp)
└── Mesadeayudajuname.java  # Clase principal
```

## Instalación y Configuración

### Prerrequisitos
- Java SE 8 o superior
- Apache Ant (incluido con NetBeans)

### Compilación y Ejecución

#### Usando Ant desde línea de comandos:
```bash
# Compilar el proyecto
ant compile

# Ejecutar la aplicación
ant run

# Generar JAR ejecutable
ant jar

# Ejecutar desde JAR
java -jar dist/mesadeayudajuname.jar

# Limpiar archivos compilados
ant clean
```

#### Usando NetBeans:
1. Abrir el proyecto en NetBeans
2. Click derecho en el proyecto → "Clean and Build"
3. Click derecho en el proyecto → "Run"

## Guía de Uso

### Flujo de Trabajo Recomendado

1. **Configuración Inicial**
   - Agregar técnicos al sistema con sus habilidades
   - El sistema incluye datos de ejemplo al inicio

2. **Gestión de Tickets**
   - Crear tickets (manual o con clasificación automática)
   - Asignar tickets a técnicos apropiados
   - Iniciar trabajo en tickets asignados
   - Cerrar tickets una vez resueltos

3. **Monitoreo**
   - Ver tickets pendientes organizados por prioridad
   - Consultar métricas del sistema
   - Usar filtros para análisis específicos

### Menú Principal
```
┌─────────────────── MENÚ PRINCIPAL ───────────────────┐
│  1. Crear nuevo ticket                               │
│  2. Ver tickets pendientes                           │
│  3. Asignar ticket a técnico                         │
│  4. Cambiar estado de ticket                         │
│  5. Buscar y filtrar tickets                         │
│  6. Ver métricas del sistema                         │
│  7. Gestionar técnicos                               │
│  8. Ayuda                                            │
│  0. Salir                                            │
└──────────────────────────────────────────────────────┘
```

## Modelo de Datos

### Estados de Ticket
- **ABIERTO**: Ticket recién creado, sin asignar
- **EN_CURSO**: Ticket asignado y en proceso de resolución
- **CERRADO**: Ticket resuelto y finalizado

### Prioridades
- **ALTA**: Crítica, requiere atención inmediata
- **MEDIA**: Importante pero no urgente
- **BAJA**: Puede esperar, no crítica

### Categorías por Defecto
- **Redes**: Problemas de conectividad, servidores, infraestructura
- **Hardware**: Equipos físicos, impresoras, componentes
- **Software**: Aplicaciones, sistemas operativos, programas
- **Seguridad**: Contraseñas, accesos, virus, permisos
- **General**: Otros tipos de solicitudes

## Clasificación Automática

El sistema incluye un clasificador inteligente que analiza el título y descripción de los tickets para sugerir automáticamente:

### Detección de Prioridad ALTA
- Palabras clave: "urgente", "crítico", "emergencia", "caído", "no funciona"

### Detección de Categorías
- **Redes**: "red", "conexión", "internet", "router", "servidor"
- **Hardware**: "disco", "memoria", "monitor", "impresora", "teclado"
- **Software**: "aplicación", "programa", "windows", "office", "navegador"
- **Seguridad**: "virus", "contraseña", "login", "acceso", "hack"

## Métricas Disponibles

- **Total de tickets** en el sistema
- **Porcentaje de tickets cerrados**
- **Tiempo promedio de resolución**
- **Distribución por estados**
- **Distribución por prioridades**

## Persistencia de Datos

Los datos se almacenan automáticamente en archivos CSV:
- `tickets.csv`: Información completa de todos los tickets
- `tecnicos.csv`: Datos de técnicos y sus habilidades

### Formato de Archivos CSV

#### tickets.csv
```csv
id;titulo;descripion;categoria;estado;prioridad;tecnicoId;fechaCreacion;fechaInicio;fechaCierre
```

#### tecnicos.csv
```csv
id;nombre;skills
```

## Casos de Prueba

### Casos de Creación de Tickets

1. **Caso 1**: Crear ticket con prioridad ALTA
   - **Entrada**: Título "Servidor caído", Prioridad ALTA
   - **Esperado**: Ticket aparece primero en lista de pendientes

2. **Caso 2**: Clasificación automática con palabra "urgente"
   - **Entrada**: Descripción conteniendo "urgente"
   - **Esperado**: Sistema asigna prioridad ALTA automáticamente

3. **Caso 3**: Crear ticket sin título
   - **Entrada**: Título vacío
   - **Esperado**: Error "El título del ticket no puede estar vacío"

### Casos de Asignación

4. **Caso 4**: Asignar ticket a técnico con habilidad correcta
   - **Entrada**: Ticket de "Redes" a técnico con skill "Redes"
   - **Esperado**: Asignación exitosa, estado cambia a EN_CURSO

5. **Caso 5**: Asignar ticket a técnico sin habilidad
   - **Entrada**: Ticket de "Hardware" a técnico solo con skills de "Software"
   - **Esperado**: Warning pero permite asignación

6. **Caso 6**: Asignar ticket inexistente
   - **Entrada**: ID de ticket que no existe
   - **Esperado**: Error "Ticket con ID X no encontrado"

### Casos de Cambio de Estado

7. **Caso 7**: Iniciar ticket sin técnico asignado
   - **Entrada**: Intentar iniciar ticket ABIERTO sin técnico
   - **Esperado**: Error "No se puede iniciar un ticket sin técnico asignado"

8. **Caso 8**: Cerrar ticket que no está EN_CURSO
   - **Entrada**: Intentar cerrar ticket ABIERTO
   - **Esperado**: Error "Solo se pueden cerrar tickets que están en curso"

9. **Caso 9**: Cerrar ticket correctamente
   - **Entrada**: Ticket EN_CURSO
   - **Esperado**: Estado cambia a CERRADO, se registra fecha de cierre

### Casos de Búsqueda y Filtros

10. **Caso 10**: Filtrar por técnico específico
    - **Entrada**: ID de técnico válido
    - **Esperado**: Solo tickets asignados a ese técnico

11. **Caso 11**: Buscar por categoría "Redes"
    - **Entrada**: Categoría "Redes"
    - **Esperado**: Solo tickets de esa categoría

12. **Caso 12**: Filtrar por estado CERRADO
    - **Entrada**: Estado CERRADO
    - **Esperado**: Solo tickets finalizados

### Casos de Métricas

13. **Caso 13**: Calcular porcentaje con tickets cerrados
    - **Entrada**: Sistema con 10 tickets, 3 cerrados
    - **Esperado**: Porcentaje cerrados = 30%

14. **Caso 14**: Tiempo promedio con un ticket cerrado
    - **Entrada**: Ticket cerrado con duración conocida
    - **Esperado**: Tiempo promedio igual a la duración del ticket

15. **Caso 15**: Métricas con sistema vacío
    - **Entrada**: Sin tickets en el sistema
    - **Esperado**: Todas las métricas en 0, sin errores

## Decisiones de Diseño

### Persistencia
- **CSV elegido sobre base de datos**: Simplicidad, portabilidad, no requiere instalación adicional
- **Separación de archivos**: tickets.csv y tecnicos.csv para mejor organización

### Arquitectura
- **Patrón Service**: TableroService centraliza la lógica de negocio
- **Separación de capas**: Modelo, servicio, utilidades y aplicación claramente separados
- **Repository Pattern**: CSVRepository abstrae el acceso a datos

### Validaciones
- **Validación en tiempo real**: Verificaciones inmediatas al crear/modificar datos
- **Mensajes de error claros**: Información específica sobre problemas
- **Estados consistentes**: Transiciones de estado validadas

### Interfaz de Usuario
- **Menú intuitivo**: Opciones numeradas con iconos descriptivos
- **Feedback visual**: Colores y símbolos para diferentes estados
- **Ayuda integrada**: Información de uso accesible desde el menú

## Mejoras Futuras Sugeridas

1. **Funcionalidades**
   - Histórico de cambios en tickets
   - Comentarios y notas en tickets
   - Notificaciones por email
   - Reportes en PDF
   - Import/Export de datos

2. **Técnicas**
   - Interfaz gráfica (Swing/JavaFX)
   - Base de datos real (H2, PostgreSQL)
   - API REST para integración
   - Pruebas automatizadas (JUnit)
   - Logging estructurado

3. **Operacionales**
   - Backup automático de datos
   - Configuración externa
   - Multi-idioma
   - Temas personalizables

## Estructura de Archivos del Proyecto

```
mesadeayudajuname/
├── build.xml                   # Configuración de Ant
├── manifest.mf                 # Manifiesto del JAR
├── README.md                   # Este archivo
├── nbproject/                  # Configuración NetBeans
│   ├── build-impl.xml
│   ├── project.properties
│   └── project.xml
├── src/
│   └── mesadeayudajuname/
│       ├── Mesadeayudajuname.java    # Clase principal
│       ├── model/                     # Modelos de datos
│       │   ├── Estado.java
│       │   ├── Prioridad.java
│       │   ├── Tecnico.java
│       │   └── Ticket.java
│       ├── service/                   # Lógica de negocio
│       │   ├── Clasificador.java
│       │   └── TableroService.java
│       ├── util/                      # Utilidades
│       │   ├── CSVRepository.java
│       │   └── DatosEjemplo.java
│       └── app/                       # Interfaz de usuario
│           └── MesaDeAyudaApp.java
├── tickets.csv                 # Datos de tickets (generado)
├── tecnicos.csv               # Datos de técnicos (generado)
└── dist/                      # JAR ejecutable (generado)
    └── mesadeayudajuname.jar
```

## Contacto y Soporte

Este proyecto fue desarrollado como parte de un ejercicio de aprendizaje en Programación Orientada a Objetos con Java SE.

Para preguntas o mejoras, consulte la documentación del código fuente que incluye Javadoc completo para todas las clases públicas.

---

**Versión**: 1.0  
**Autor**: juanulb  
**Fecha**: 2025  
**Licencia**: Educativo