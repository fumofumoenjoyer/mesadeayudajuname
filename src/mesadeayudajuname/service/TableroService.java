package mesadeayudajuname.service;

import mesadeayudajuname.model.*;
import mesadeayudajuname.util.CSVRepository;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Servicio principal para la gestión de la mesa de ayuda.
 * Centraliza toda la lógica de negocio del sistema.
 * 
 * @author juanulb
 */
public class TableroService {
    private List<Ticket> tickets;
    private List<Tecnico> tecnicos;
    private Map<Integer, Tecnico> mapaTecnicos;
    private CSVRepository repository;
    private int siguienteIdTicket;
    private int siguienteIdTecnico;
    
    /**
     * Constructor del servicio del tablero.
     */
    public TableroService() {
        this.tickets = new ArrayList<>();
        this.tecnicos = new ArrayList<>();
        this.mapaTecnicos = new HashMap<>();
        this.repository = new CSVRepository();
        this.siguienteIdTicket = 1;
        this.siguienteIdTecnico = 1;
        
        cargarDatos();
    }
    
    /**
     * Carga los datos desde los archivos CSV.
     */
    private void cargarDatos() {
        try {
            // Primero cargar técnicos
            this.tecnicos = repository.cargarTecnicos();
            actualizarMapaTecnicos();
            
            // Actualizar siguiente ID de técnico
            if (!tecnicos.isEmpty()) {
                this.siguienteIdTecnico = tecnicos.stream()
                    .mapToInt(Tecnico::getId)
                    .max()
                    .orElse(0) + 1;
            }
            
            // Luego cargar tickets
            this.tickets = repository.cargarTickets(mapaTecnicos);
            
            // Actualizar siguiente ID de ticket
            if (!tickets.isEmpty()) {
                this.siguienteIdTicket = tickets.stream()
                    .mapToInt(Ticket::getId)
                    .max()
                    .orElse(0) + 1;
            }
            
        } catch (IOException e) {
            System.err.println("Error al cargar datos: " + e.getMessage());
        }
    }
    
    /**
     * Actualiza el mapa de técnicos para búsquedas rápidas.
     */
    private void actualizarMapaTecnicos() {
        mapaTecnicos.clear();
        for (Tecnico tecnico : tecnicos) {
            mapaTecnicos.put(tecnico.getId(), tecnico);
        }
    }
    
    /**
     * Guarda todos los datos en archivos CSV.
     */
    public void guardarDatos() {
        try {
            repository.guardarTickets(tickets);
            repository.guardarTecnicos(tecnicos);
        } catch (IOException e) {
            System.err.println("Error al guardar datos: " + e.getMessage());
        }
    }
    
    /**
     * Crea un nuevo ticket.
     * 
     * @param titulo Título del ticket
     * @param descripcion Descripción del ticket
     * @param categoria Categoría del ticket
     * @param prioridad Prioridad del ticket
     * @return ID del ticket creado
     */
    public int crearTicket(String titulo, String descripcion, String categoria, Prioridad prioridad) {
        validarDatosTicket(titulo, descripcion, categoria);
        
        Ticket nuevoTicket = new Ticket(siguienteIdTicket++, titulo, descripcion, categoria, prioridad);
        tickets.add(nuevoTicket);
        guardarDatos();
        
        return nuevoTicket.getId();
    }
    
    /**
     * Crea un nuevo ticket con clasificación automática.
     * 
     * @param titulo Título del ticket
     * @param descripcion Descripción del ticket
     * @return ID del ticket creado
     */
    public int crearTicketConClasificacion(String titulo, String descripcion) {
        validarDatosTicket(titulo, descripcion, null);
        
        Object[] clasificacion = Clasificador.clasificarCompleto(titulo, descripcion);
        String categoria = (String) clasificacion[0];
        Prioridad prioridad = (Prioridad) clasificacion[1];
        
        return crearTicket(titulo, descripcion, categoria, prioridad);
    }
    
    /**
     * Asigna un ticket a un técnico.
     * 
     * @param ticketId ID del ticket
     * @param tecnicoId ID del técnico
     * @throws IllegalArgumentException si el ticket o técnico no existen
     * @throws IllegalStateException si el ticket no se puede asignar
     */
    public void asignarTicket(int ticketId, int tecnicoId) {
        Ticket ticket = buscarTicketPorId(ticketId);
        if (ticket == null) {
            throw new IllegalArgumentException("Ticket con ID " + ticketId + " no encontrado");
        }
        
        Tecnico tecnico = mapaTecnicos.get(tecnicoId);
        if (tecnico == null) {
            throw new IllegalArgumentException("Técnico con ID " + tecnicoId + " no encontrado");
        }
        
        ticket.asignar(tecnico);
        guardarDatos();
    }
    
    /**
     * Inicia el trabajo en un ticket.
     * 
     * @param ticketId ID del ticket
     * @throws IllegalArgumentException si el ticket no existe
     * @throws IllegalStateException si el ticket no se puede iniciar
     */
    public void iniciarTicket(int ticketId) {
        Ticket ticket = buscarTicketPorId(ticketId);
        if (ticket == null) {
            throw new IllegalArgumentException("Ticket con ID " + ticketId + " no encontrado");
        }
        
        ticket.iniciar();
        guardarDatos();
    }
    
    /**
     * Cierra un ticket.
     * 
     * @param ticketId ID del ticket
     * @throws IllegalArgumentException si el ticket no existe
     * @throws IllegalStateException si el ticket no se puede cerrar
     */
    public void cerrarTicket(int ticketId) {
        Ticket ticket = buscarTicketPorId(ticketId);
        if (ticket == null) {
            throw new IllegalArgumentException("Ticket con ID " + ticketId + " no encontrado");
        }
        
        ticket.cerrar();
        guardarDatos();
    }
    
    /**
     * Devuelve una lista de tickets pendientes ordenados por prioridad.
     * 
     * @return Lista de tickets ordenados por prioridad (ALTA -> BAJA)
     */
    public List<Ticket> pendientesPorPrioridad() {
        return tickets.stream()
            .filter(t -> t.getEstado() != Estado.CERRADO)
            .sorted((t1, t2) -> Integer.compare(t2.getPrioridad().getValor(), t1.getPrioridad().getValor()))
            .collect(Collectors.toList());
    }
    
    /**
     * Busca tickets por categoría.
     * 
     * @param categoria Categoría a buscar
     * @return Lista de tickets de la categoría especificada
     */
    public List<Ticket> buscarPorCategoria(String categoria) {
        return tickets.stream()
            .filter(t -> t.getCategoria().equalsIgnoreCase(categoria))
            .collect(Collectors.toList());
    }
    
    /**
     * Busca tickets por estado.
     * 
     * @param estado Estado a buscar
     * @return Lista de tickets con el estado especificado
     */
    public List<Ticket> buscarPorEstado(Estado estado) {
        return tickets.stream()
            .filter(t -> t.getEstado() == estado)
            .collect(Collectors.toList());
    }
    
    /**
     * Busca tickets asignados a un técnico específico.
     * 
     * @param tecnicoId ID del técnico
     * @return Lista de tickets asignados al técnico
     */
    public List<Ticket> buscarPorTecnico(int tecnicoId) {
        return tickets.stream()
            .filter(t -> t.getTecnicoAsignado() != null && t.getTecnicoAsignado().getId() == tecnicoId)
            .collect(Collectors.toList());
    }
    
    /**
     * Calcula métricas del sistema.
     * 
     * @return Mapa con las métricas calculadas
     */
    public Map<String, Object> calcularMetricas() {
        Map<String, Object> metricas = new HashMap<>();
        
        int totalTickets = tickets.size();
        if (totalTickets == 0) {
            metricas.put("totalTickets", 0);
            metricas.put("porcentajeCerrados", 0.0);
            metricas.put("tiempoPromedioResolucion", 0.0);
            return metricas;
        }
        
        // Tickets cerrados
        List<Ticket> ticketsCerrados = tickets.stream()
            .filter(t -> t.getEstado() == Estado.CERRADO)
            .collect(Collectors.toList());
        
        int ticketsCerradosCount = ticketsCerrados.size();
        double porcentajeCerrados = (ticketsCerradosCount * 100.0) / totalTickets;
        
        // Tiempo promedio de resolución
        double tiempoPromedio = ticketsCerrados.stream()
            .mapToLong(Ticket::calcularDuracionResolucion)
            .filter(duracion -> duracion > 0)
            .average()
            .orElse(0.0);
        
        // Tickets por estado
        Map<Estado, Long> ticketsPorEstado = tickets.stream()
            .collect(Collectors.groupingBy(Ticket::getEstado, Collectors.counting()));
        
        // Tickets por prioridad
        Map<Prioridad, Long> ticketsPorPrioridad = tickets.stream()
            .collect(Collectors.groupingBy(Ticket::getPrioridad, Collectors.counting()));
        
        metricas.put("totalTickets", totalTickets);
        metricas.put("ticketsCerrados", ticketsCerradosCount);
        metricas.put("porcentajeCerrados", porcentajeCerrados);
        metricas.put("tiempoPromedioResolucion", tiempoPromedio);
        metricas.put("ticketsPorEstado", ticketsPorEstado);
        metricas.put("ticketsPorPrioridad", ticketsPorPrioridad);
        
        return metricas;
    }
    
    /**
     * Agrega un nuevo técnico al sistema.
     * 
     * @param nombre Nombre del técnico
     * @param skills Lista de habilidades
     * @return ID del técnico creado
     */
    public int agregarTecnico(String nombre, List<String> skills) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del técnico no puede estar vacío");
        }
        
        Tecnico nuevoTecnico = new Tecnico(siguienteIdTecnico++, nombre.trim(), skills);
        tecnicos.add(nuevoTecnico);
        mapaTecnicos.put(nuevoTecnico.getId(), nuevoTecnico);
        guardarDatos();
        
        return nuevoTecnico.getId();
    }
    
    /**
     * Busca técnicos que pueden atender una categoría específica.
     * 
     * @param categoria Categoría a verificar
     * @return Lista de técnicos que pueden atender la categoría
     */
    public List<Tecnico> buscarTecnicosPorCategoria(String categoria) {
        return tecnicos.stream()
            .filter(t -> t.puedeAtender(categoria))
            .collect(Collectors.toList());
    }
    
    // Métodos de utilidad
    
    /**
     * Busca un ticket por su ID.
     * 
     * @param id ID del ticket
     * @return Ticket encontrado o null si no existe
     */
    public Ticket buscarTicketPorId(int id) {
        return tickets.stream()
            .filter(t -> t.getId() == id)
            .findFirst()
            .orElse(null);
    }
    
    /**
     * Busca un técnico por su ID.
     * 
     * @param id ID del técnico
     * @return Técnico encontrado o null si no existe
     */
    public Tecnico buscarTecnicoPorId(int id) {
        return mapaTecnicos.get(id);
    }
    
    /**
     * Valida los datos básicos de un ticket.
     * 
     * @param titulo Título del ticket
     * @param descripcion Descripción del ticket
     * @param categoria Categoría del ticket (puede ser null)
     */
    private void validarDatosTicket(String titulo, String descripcion, String categoria) {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("El título del ticket no puede estar vacío");
        }
        if (descripcion == null || descripcion.trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción del ticket no puede estar vacía");
        }
    }
    
    // Getters
    
    public List<Ticket> getTickets() {
        return new ArrayList<>(tickets);
    }
    
    public List<Tecnico> getTecnicos() {
        return new ArrayList<>(tecnicos);
    }
}