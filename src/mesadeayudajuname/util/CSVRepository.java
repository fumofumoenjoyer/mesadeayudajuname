package mesadeayudajuname.util;

import mesadeayudajuname.model.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Repositorio para persistir datos en archivos CSV.
 * Maneja la lectura y escritura de tickets y técnicos.
 * 
 * @author juanulb
 */
public class CSVRepository {
    private static final String TICKETS_FILE = "tickets.csv";
    private static final String TECNICOS_FILE = "tecnicos.csv";
    private static final String CSV_SEPARATOR = ";";
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    /**
     * Guarda la lista de tickets en el archivo CSV.
     * 
     * @param tickets Lista de tickets a guardar
     * @throws IOException si hay error en la escritura del archivo
     */
    public void guardarTickets(List<Ticket> tickets) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(TICKETS_FILE))) {
            // Escribir encabezado
            writer.println("id;titulo;descripcion;categoria;estado;prioridad;tecnicoId;fechaCreacion;fechaInicio;fechaCierre");
            
            for (Ticket ticket : tickets) {
                StringBuilder line = new StringBuilder();
                line.append(ticket.getId()).append(CSV_SEPARATOR);
                line.append(escaparCSV(ticket.getTitulo())).append(CSV_SEPARATOR);
                line.append(escaparCSV(ticket.getDescripcion())).append(CSV_SEPARATOR);
                line.append(escaparCSV(ticket.getCategoria())).append(CSV_SEPARATOR);
                line.append(ticket.getEstado().name()).append(CSV_SEPARATOR);
                line.append(ticket.getPrioridad().name()).append(CSV_SEPARATOR);
                
                // Técnico asignado
                if (ticket.getTecnicoAsignado() != null) {
                    line.append(ticket.getTecnicoAsignado().getId());
                }
                line.append(CSV_SEPARATOR);
                
                // Fechas
                line.append(ticket.getFechaCreacion().format(DATE_FORMAT)).append(CSV_SEPARATOR);
                
                if (ticket.getFechaInicio() != null) {
                    line.append(ticket.getFechaInicio().format(DATE_FORMAT));
                }
                line.append(CSV_SEPARATOR);
                
                if (ticket.getFechaCierre() != null) {
                    line.append(ticket.getFechaCierre().format(DATE_FORMAT));
                }
                
                writer.println(line.toString());
            }
        }
    }
    
    /**
     * Carga la lista de tickets desde el archivo CSV.
     * 
     * @param tecnicos Mapa de técnicos para realizar las asignaciones
     * @return Lista de tickets cargados
     * @throws IOException si hay error en la lectura del archivo
     */
    public List<Ticket> cargarTickets(Map<Integer, Tecnico> tecnicos) throws IOException {
        List<Ticket> tickets = new ArrayList<>();
        File file = new File(TICKETS_FILE);
        
        if (!file.exists()) {
            return tickets; // Devolver lista vacía si no existe el archivo
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine(); // Saltar encabezado
            
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(CSV_SEPARATOR, -1);
                if (parts.length >= 6) {
                    try {
                        int id = Integer.parseInt(parts[0]);
                        String titulo = desescaparCSV(parts[1]);
                        String descripcion = desescaparCSV(parts[2]);
                        String categoria = desescaparCSV(parts[3]);
                        Estado estado = Estado.valueOf(parts[4]);
                        Prioridad prioridad = Prioridad.valueOf(parts[5]);
                        
                        // Crear ticket
                        Ticket ticket = new Ticket(id, titulo, descripcion, categoria, prioridad);
                        
                        // Asignar técnico si existe
                        if (parts.length > 6 && !parts[6].isEmpty()) {
                            int tecnicoId = Integer.parseInt(parts[6]);
                            Tecnico tecnico = tecnicos.get(tecnicoId);
                            if (tecnico != null) {
                                // Usar reflection para establecer el técnico sin validaciones
                                java.lang.reflect.Field tecnicoField = Ticket.class.getDeclaredField("tecnicoAsignado");
                                tecnicoField.setAccessible(true);
                                tecnicoField.set(ticket, tecnico);
                            }
                        }
                        
                        // Establecer fechas y estado usando reflection para evitar validaciones
                        if (parts.length > 7 && !parts[7].isEmpty()) {
                            LocalDateTime fechaCreacion = LocalDateTime.parse(parts[7], DATE_FORMAT);
                            java.lang.reflect.Field fechaCreacionField = Ticket.class.getDeclaredField("fechaCreacion");
                            fechaCreacionField.setAccessible(true);
                            fechaCreacionField.set(ticket, fechaCreacion);
                        }
                        
                        if (parts.length > 8 && !parts[8].isEmpty()) {
                            LocalDateTime fechaInicio = LocalDateTime.parse(parts[8], DATE_FORMAT);
                            java.lang.reflect.Field fechaInicioField = Ticket.class.getDeclaredField("fechaInicio");
                            fechaInicioField.setAccessible(true);
                            fechaInicioField.set(ticket, fechaInicio);
                        }
                        
                        if (parts.length > 9 && !parts[9].isEmpty()) {
                            LocalDateTime fechaCierre = LocalDateTime.parse(parts[9], DATE_FORMAT);
                            java.lang.reflect.Field fechaCierreField = Ticket.class.getDeclaredField("fechaCierre");
                            fechaCierreField.setAccessible(true);
                            fechaCierreField.set(ticket, fechaCierre);
                        }
                        
                        // Establecer estado
                        java.lang.reflect.Field estadoField = Ticket.class.getDeclaredField("estado");
                        estadoField.setAccessible(true);
                        estadoField.set(ticket, estado);
                        
                        tickets.add(ticket);
                        
                    } catch (Exception e) {
                        System.err.println("Error al procesar línea del CSV: " + line);
                        e.printStackTrace();
                    }
                }
            }
        }
        
        return tickets;
    }
    
    /**
     * Guarda la lista de técnicos en el archivo CSV.
     * 
     * @param tecnicos Lista de técnicos a guardar
     * @throws IOException si hay error en la escritura del archivo
     */
    public void guardarTecnicos(List<Tecnico> tecnicos) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(TECNICOS_FILE))) {
            // Escribir encabezado
            writer.println("id;nombre;skills");
            
            for (Tecnico tecnico : tecnicos) {
                StringBuilder line = new StringBuilder();
                line.append(tecnico.getId()).append(CSV_SEPARATOR);
                line.append(escaparCSV(tecnico.getNombre())).append(CSV_SEPARATOR);
                
                // Skills separadas por comas
                String skills = String.join(",", tecnico.getSkills());
                line.append(escaparCSV(skills));
                
                writer.println(line.toString());
            }
        }
    }
    
    /**
     * Carga la lista de técnicos desde el archivo CSV.
     * 
     * @return Lista de técnicos cargados
     * @throws IOException si hay error en la lectura del archivo
     */
    public List<Tecnico> cargarTecnicos() throws IOException {
        List<Tecnico> tecnicos = new ArrayList<>();
        File file = new File(TECNICOS_FILE);
        
        if (!file.exists()) {
            return tecnicos; // Devolver lista vacía si no existe el archivo
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine(); // Saltar encabezado
            
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(CSV_SEPARATOR, -1);
                if (parts.length >= 3) {
                    try {
                        int id = Integer.parseInt(parts[0]);
                        String nombre = desescaparCSV(parts[1]);
                        String skillsStr = desescaparCSV(parts[2]);
                        
                        List<String> skills = new ArrayList<>();
                        if (!skillsStr.isEmpty()) {
                            skills.addAll(Arrays.asList(skillsStr.split(",")));
                        }
                        
                        Tecnico tecnico = new Tecnico(id, nombre, skills);
                        tecnicos.add(tecnico);
                        
                    } catch (NumberFormatException e) {
                        System.err.println("Error al procesar línea del CSV de técnicos: " + line);
                    }
                }
            }
        }
        
        return tecnicos;
    }
    
    /**
     * Escapa caracteres especiales para CSV.
     * 
     * @param value Valor a escapar
     * @return Valor escapado
     */
    private String escaparCSV(String value) {
        if (value == null) return "";
        if (value.contains(CSV_SEPARATOR) || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
    
    /**
     * Desescapa caracteres especiales de CSV.
     * 
     * @param value Valor a desescapar
     * @return Valor desescapado
     */
    private String desescaparCSV(String value) {
        if (value == null) return "";
        if (value.startsWith("\"") && value.endsWith("\"")) {
            return value.substring(1, value.length() - 1).replace("\"\"", "\"");
        }
        return value;
    }
}