package mesadeayudajuname.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.Duration;

/**
 * Representa un ticket de incidencia en el sistema de mesa de ayuda.
 * Cada ticket tiene un ciclo de vida: ABIERTO -> EN_CURSO -> CERRADO
 * 
 * @author juanulb
 */
public class Ticket {
    private int id;
    private String titulo;
    private String descripcion;
    private String categoria;
    private Estado estado;
    private Prioridad prioridad;
    private Tecnico tecnicoAsignado;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaCierre;
    
    /**
     * Constructor para crear un nuevo ticket.
     * 
     * @param id ID único del ticket
     * @param titulo Título descriptivo del ticket
     * @param descripcion Descripción detallada del problema
     * @param categoria Categoría del ticket (ej. "Redes", "Hardware")
     * @param prioridad Prioridad del ticket
     */
    public Ticket(int id, String titulo, String descripcion, String categoria, Prioridad prioridad) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.prioridad = prioridad;
        this.estado = Estado.ABIERTO;
        this.fechaCreacion = LocalDateTime.now();
        this.tecnicoAsignado = null;
        this.fechaInicio = null;
        this.fechaCierre = null;
    }
    
    /**
     * Asigna el ticket a un técnico y cambia el estado a EN_CURSO si es aplicable.
     * 
     * @param tecnico Técnico al que se asigna el ticket
     * @throws IllegalStateException si el ticket ya está cerrado
     * @throws IllegalArgumentException si el técnico no puede atender esta categoría
     */
    public void asignar(Tecnico tecnico) {
        if (estado == Estado.CERRADO) {
            throw new IllegalStateException("No se puede asignar un ticket cerrado");
        }
        
        if (tecnico != null && !tecnico.puedeAtender(this.categoria)) {
            throw new IllegalArgumentException("El técnico no tiene las habilidades para atender esta categoría: " + this.categoria);
        }
        
        this.tecnicoAsignado = tecnico;
        if (tecnico != null && estado == Estado.ABIERTO) {
            iniciar();
        }
    }
    
    /**
     * Inicia el trabajo en el ticket, cambiando el estado a EN_CURSO.
     * 
     * @throws IllegalStateException si el ticket no está abierto o no tiene técnico asignado
     */
    public void iniciar() {
        if (estado != Estado.ABIERTO) {
            throw new IllegalStateException("Solo se pueden iniciar tickets abiertos");
        }
        
        if (tecnicoAsignado == null) {
            throw new IllegalStateException("No se puede iniciar un ticket sin técnico asignado");
        }
        
        this.estado = Estado.EN_CURSO;
        this.fechaInicio = LocalDateTime.now();
    }
    
    /**
     * Cierra el ticket, cambiando el estado a CERRADO.
     * 
     * @throws IllegalStateException si el ticket no está en curso
     */
    public void cerrar() {
        if (estado != Estado.EN_CURSO) {
            throw new IllegalStateException("Solo se pueden cerrar tickets que están en curso");
        }
        
        this.estado = Estado.CERRADO;
        this.fechaCierre = LocalDateTime.now();
    }
    
    /**
     * Calcula la duración total de resolución del ticket.
     * 
     * @return duración en minutos, o -1 si el ticket no está cerrado
     */
    public long calcularDuracionResolucion() {
        if (fechaInicio == null || fechaCierre == null) {
            return -1;
        }
        
        return Duration.between(fechaInicio, fechaCierre).toMinutes();
    }
    
    /**
     * Calcula el tiempo transcurrido desde la creación del ticket.
     * 
     * @return duración en minutos desde la creación
     */
    public long calcularTiempoTranscurrido() {
        LocalDateTime fin = (fechaCierre != null) ? fechaCierre : LocalDateTime.now();
        return Duration.between(fechaCreacion, fin).toMinutes();
    }
    
    /**
     * Devuelve una representación formateada de las fechas del ticket.
     * 
     * @return string con las fechas formateadas
     */
    public String getFechasFormateadas() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        StringBuilder sb = new StringBuilder();
        
        sb.append("Creado: ").append(fechaCreacion.format(formatter));
        
        if (fechaInicio != null) {
            sb.append(" | Iniciado: ").append(fechaInicio.format(formatter));
        }
        
        if (fechaCierre != null) {
            sb.append(" | Cerrado: ").append(fechaCierre.format(formatter));
        }
        
        return sb.toString();
    }
    
    // Getters y Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getTitulo() {
        return titulo;
    }
    
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public String getCategoria() {
        return categoria;
    }
    
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
    
    public Estado getEstado() {
        return estado;
    }
    
    public Prioridad getPrioridad() {
        return prioridad;
    }
    
    public void setPrioridad(Prioridad prioridad) {
        this.prioridad = prioridad;
    }
    
    public Tecnico getTecnicoAsignado() {
        return tecnicoAsignado;
    }
    
    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }
    
    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }
    
    public LocalDateTime getFechaCierre() {
        return fechaCierre;
    }
    
    @Override
    public String toString() {
        String tecnicoNombre = (tecnicoAsignado != null) ? tecnicoAsignado.getNombre() : "Sin asignar";
        return String.format("[#%d] %s | %s | %s | Técnico: %s | %s", 
                           id, titulo, categoria, estado, tecnicoNombre, prioridad);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Ticket ticket = (Ticket) obj;
        return id == ticket.id;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}