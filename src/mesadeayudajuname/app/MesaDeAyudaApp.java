package mesadeayudajuname.app;

import mesadeayudajuname.model.*;
import mesadeayudajuname.service.*;
import java.util.*;

/**
 * Aplicación principal de consola para el sistema de Mesa de Ayuda.
 * Proporciona un menú interactivo para gestionar tickets y técnicos.
 * 
 * @author juanulb
 */
public class MesaDeAyudaApp {
    private TableroService tableroService;
    private Scanner scanner;
    
    /**
     * Constructor de la aplicación.
     */
    public MesaDeAyudaApp() {
        this.tableroService = new TableroService();
        this.scanner = new Scanner(System.in);
        
        // Inicializar datos de ejemplo si es la primera ejecución
        if (tableroService.getTecnicos().isEmpty() && tableroService.getTickets().isEmpty()) {
            System.out.println("Primera ejecución detectada. Inicializando con datos de ejemplo...");
            mesadeayudajuname.util.DatosEjemplo.inicializarDatosEjemplo(tableroService);
            System.out.println();
        }
    }
    
    /**
     * Método principal para ejecutar la aplicación.
     */
    public void ejecutar() {
        mostrarBienvenida();
        
        boolean continuar = true;
        while (continuar) {
            mostrarMenu();
            int opcion = leerOpcion();
            
            try {
                switch (opcion) {
                    case 1:
                        crearTicket();
                        break;
                    case 2:
                        listarTicketsPendientes();
                        break;
                    case 3:
                        asignarTicket();
                        break;
                    case 4:
                        cambiarEstadoTicket();
                        break;
                    case 5:
                        buscarTickets();
                        break;
                    case 6:
                        mostrarMetricas();
                        break;
                    case 7:
                        gestionarTecnicos();
                        break;
                    case 8:
                        mostrarAyuda();
                        break;
                    case 0:
                        continuar = false;
                        despedida();
                        break;
                    default:
                        System.out.println("ERROR: Opción no válida. Por favor, intente de nuevo.");
                }
            } catch (Exception e) {
                System.out.println("ERROR: " + e.getMessage());
            }
            
            if (continuar) {
                System.out.println("\nPresione Enter para continuar...");
                scanner.nextLine();
            }
        }
    }
    
    /**
     * Muestra el mensaje de bienvenida.
     */
    private void mostrarBienvenida() {
        System.out.println("╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║                    SISTEMA MESA DE AYUDA                    ║");
        System.out.println("║                  Gestión de Tickets de Soporte              ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
        System.out.println();
    }
    
    /**
     * Muestra el menú principal.
     */
    private static void mostrarMenuPrincipal() {
        System.out.println("╔═══════════════════════════════════════╗");
        System.out.println("║            MESA DE AYUDA             ║");
        System.out.println("║                                       ║");
        System.out.println("║ 1. Crear nuevo ticket                ║");
        System.out.println("║ 2. Ver todos los tickets             ║");
        System.out.println("║ 3. Asignar ticket a técnico          ║");
        System.out.println("║ 4. Iniciar trabajo en ticket         ║");
        System.out.println("║ 5. Cerrar ticket                     ║");
        System.out.println("║ 6. Gestionar técnicos                ║");
        System.out.println("║ 7. Ver métricas                      ║");
        System.out.println("║ 8. Crear tickets automáticos         ║");
        System.out.println("║ 9. Ayuda                             ║");
        System.out.println("║ 0. Salir                             ║");
        System.out.println("╚═══════════════════════════════════════╝");
        System.out.print("Seleccione una opción: ");
    }
    
    /**
     * Lee una opción del menú.
     */
    private int leerOpcion() {
        try {
            String input = scanner.nextLine().trim();
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
    
    /**
     * Crea un nuevo ticket.
     */
    private void crearTicket() {
        System.out.println("\n┌──────────── CREAR NUEVO TICKET ────────────┐");
        
        System.out.print("Título del ticket: ");
        String titulo = scanner.nextLine().trim();
        
        System.out.print("Descripción detallada: ");
        String descripcion = scanner.nextLine().trim();
        
        System.out.println("\n¿Desea usar clasificación automática? (s/n): ");
        String respuesta = scanner.nextLine().trim().toLowerCase();
        
        int ticketId;
        if (respuesta.equals("s") || respuesta.equals("si")) {
            // Clasificación automática
            ticketId = tableroService.crearTicketConClasificacion(titulo, descripcion);
            
            Ticket ticket = tableroService.buscarTicketPorId(ticketId);
            System.out.println("\nClasificación automática aplicada:");
            System.out.println("   Categoría sugerida: " + ticket.getCategoria());
            System.out.println("   Prioridad sugerida: " + ticket.getPrioridad());
            
            String palabrasClave = Clasificador.obtenerPalabrasClave(titulo, descripcion);
            if (!palabrasClave.isEmpty()) {
                System.out.println("   Razón: " + palabrasClave);
            }
        } else {
            // Clasificación manual
            System.out.print("Categoría (Redes/Hardware/Software/Seguridad/General): ");
            String categoria = scanner.nextLine().trim();
            if (categoria.isEmpty()) categoria = "General";
            
            System.out.println("Prioridad:");
            System.out.println("1. BAJA");
            System.out.println("2. MEDIA");
            System.out.println("3. ALTA");
            System.out.print("Seleccione (1-3): ");
            
            Prioridad prioridad = Prioridad.BAJA;
            try {
                int opcionPrioridad = Integer.parseInt(scanner.nextLine().trim());
                switch (opcionPrioridad) {
                    case 2: prioridad = Prioridad.MEDIA; break;
                    case 3: prioridad = Prioridad.ALTA; break;
                    default: prioridad = Prioridad.BAJA; break;
                }
            } catch (NumberFormatException e) {
                // Usar prioridad por defecto
            }
            
            ticketId = tableroService.crearTicket(titulo, descripcion, categoria, prioridad);
        }
        
        System.out.println("\nTicket #" + ticketId + " creado exitosamente!");
    }
    
    /**
     * Lista los tickets pendientes ordenados por prioridad.
     */
    private void listarTicketsPendientes() {
        System.out.println("\n┌─────────── TICKETS PENDIENTES ───────────┐");
        
        List<Ticket> pendientes = tableroService.pendientesPorPrioridad();
        
        if (pendientes.isEmpty()) {
            System.out.println("No hay tickets pendientes. ¡Excelente trabajo!");
            return;
        }
        
        System.out.printf("%-4s %-20s %-12s %-10s %-15s %-8s%n", 
                         "ID", "TÍTULO", "CATEGORÍA", "ESTADO", "TÉCNICO", "PRIORIDAD");
        System.out.println("─".repeat(75));
        
        for (Ticket ticket : pendientes) {
            String tecnico = (ticket.getTecnicoAsignado() != null) ? 
                           ticket.getTecnicoAsignado().getNombre() : "Sin asignar";
            
            String prioridadIcon = "";
            switch (ticket.getPrioridad()) {
                case ALTA: prioridadIcon = "ALTA"; break;
                case MEDIA: prioridadIcon = "MEDIA"; break;
                case BAJA: prioridadIcon = "BAJA"; break;
            }
            
            System.out.printf("%-4d %-20s %-12s %-10s %-15s %s%n",
                             ticket.getId(),
                             truncarTexto(ticket.getTitulo(), 20),
                             ticket.getCategoria(),
                             ticket.getEstado(),
                             truncarTexto(tecnico, 15),
                             prioridadIcon);
        }
        
        System.out.println("\nTotal de tickets pendientes: " + pendientes.size());
    }
    
    /**
     * Asigna un ticket a un técnico.
     */
    private void asignarTicket() {
        System.out.println("\n┌───────── ASIGNAR TICKET A TÉCNICO ─────────┐");
        
        // Mostrar tickets sin asignar
        List<Ticket> sinAsignar = tableroService.getTickets().stream()
            .filter(t -> t.getEstado() == Estado.ABIERTO && t.getTecnicoAsignado() == null)
            .collect(java.util.stream.Collectors.toList());
        
        if (sinAsignar.isEmpty()) {
            System.out.println("No hay tickets sin asignar.");
            return;
        }
        
        System.out.println("Tickets sin asignar:");
        for (Ticket ticket : sinAsignar) {
            System.out.printf("#%d - %s [%s]\n", 
                             ticket.getId(), ticket.getTitulo(), ticket.getCategoria());
        }
        
        System.out.print("\nID del ticket a asignar: ");
        int ticketId = leerEntero();
        
        Ticket ticket = tableroService.buscarTicketPorId(ticketId);
        if (ticket == null) {
            System.out.println("ERROR: Ticket no encontrado.");
            return;
        }
        
        // Mostrar técnicos disponibles para la categoría
        List<Tecnico> tecnicosDisponibles = tableroService.buscarTecnicosPorCategoria(ticket.getCategoria());
        
        System.out.println("\nTécnicos disponibles para la categoría '" + ticket.getCategoria() + "':");
        if (tecnicosDisponibles.isEmpty()) {
            System.out.println("No hay técnicos especializados en esta categoría.");
            System.out.println("Mostrando todos los técnicos:");
            tecnicosDisponibles = tableroService.getTecnicos();
        }
        
        for (Tecnico tecnico : tecnicosDisponibles) {
            System.out.printf("#%d - %s %s\n", 
                             tecnico.getId(), tecnico.getNombre(), tecnico.getSkills());
        }
        
        System.out.print("\nID del técnico: ");
        int tecnicoId = leerEntero();
        
        tableroService.asignarTicket(ticketId, tecnicoId);
        System.out.println("Ticket asignado exitosamente!");
    }
    
    /**
     * Cambia el estado de un ticket.
     */
    private void cambiarEstadoTicket() {
        System.out.println("\n┌────────── CAMBIAR ESTADO DE TICKET ──────────┐");
        
        System.out.print("ID del ticket: ");
        int ticketId = leerEntero();
        
        Ticket ticket = tableroService.buscarTicketPorId(ticketId);
        if (ticket == null) {
            System.out.println("ERROR: Ticket no encontrado.");
            return;
        }
        
        System.out.println("Ticket actual: " + ticket);
        System.out.println("Estado actual: " + ticket.getEstado());
        
        System.out.println("\n¿Qué acción desea realizar?");
        
        switch (ticket.getEstado()) {
            case ABIERTO:
                if (ticket.getTecnicoAsignado() != null) {
                    System.out.println("1. Iniciar trabajo en el ticket");
                } else {
                    System.out.println("ERROR: No se puede iniciar un ticket sin técnico asignado.");
                    return;
                }
                break;
            case EN_CURSO:
                System.out.println("1. Cerrar ticket (marcar como resuelto)");
                break;
            case CERRADO:
                System.out.println("ERROR: El ticket ya está cerrado.");
                return;
        }
        
        System.out.print("Seleccione una opción: ");
        int opcion = leerEntero();
        
        if (opcion == 1) {
            switch (ticket.getEstado()) {
                case ABIERTO:
                    tableroService.iniciarTicket(ticketId);
                    System.out.println("Ticket iniciado exitosamente!");
                    break;
                case EN_CURSO:
                    tableroService.cerrarTicket(ticketId);
                    System.out.println("Ticket cerrado exitosamente!");
                    
                    // Mostrar duración de resolución
                    long duracion = ticket.calcularDuracionResolucion();
                    if (duracion > 0) {
                        System.out.printf("Tiempo de resolución: %d minutos\n", duracion);
                    }
                    break;
            }
        }
    }
    
    /**
     * Busca y filtra tickets.
     */
    private void buscarTickets() {
        System.out.println("\n┌────────── BUSCAR Y FILTRAR TICKETS ──────────┐");
        
        System.out.println("1. Buscar por categoría");
        System.out.println("2. Buscar por estado");
        System.out.println("3. Buscar por técnico asignado");
        System.out.print("Seleccione tipo de búsqueda: ");
        
        int tipo = leerEntero();
        List<Ticket> resultados = new ArrayList<>();
        
        switch (tipo) {
            case 1:
                System.out.print("Ingrese la categoría: ");
                String categoria = scanner.nextLine().trim();
                resultados = tableroService.buscarPorCategoria(categoria);
                break;
                
            case 2:
                System.out.println("Estados disponibles:");
                System.out.println("1. ABIERTO");
                System.out.println("2. EN_CURSO");
                System.out.println("3. CERRADO");
                System.out.print("Seleccione estado: ");
                int estadoOpcion = leerEntero();
                
                Estado estado = Estado.ABIERTO;
                switch (estadoOpcion) {
                    case 2: estado = Estado.EN_CURSO; break;
                    case 3: estado = Estado.CERRADO; break;
                }
                
                resultados = tableroService.buscarPorEstado(estado);
                break;
                
            case 3:
                System.out.println("Técnicos disponibles:");
                for (Tecnico tecnico : tableroService.getTecnicos()) {
                    System.out.printf("#%d - %s\n", tecnico.getId(), tecnico.getNombre());
                }
                System.out.print("ID del técnico: ");
                int tecnicoId = leerEntero();
                resultados = tableroService.buscarPorTecnico(tecnicoId);
                break;
                
            default:
                System.out.println("ERROR: Opción no válida.");
                return;
        }
        
        mostrarResultadosBusqueda(resultados);
    }
    
    /**
     * Muestra los resultados de una búsqueda.
     */
    private void mostrarResultadosBusqueda(List<Ticket> tickets) {
        System.out.println("\nResultados de la búsqueda:");
        
        if (tickets.isEmpty()) {
            System.out.println("No se encontraron tickets que coincidan con los criterios.");
            return;
        }
        
        System.out.printf("%-4s %-20s %-12s %-10s %-15s %-8s%n", 
                         "ID", "TÍTULO", "CATEGORÍA", "ESTADO", "TÉCNICO", "PRIORIDAD");
        System.out.println("─".repeat(75));
        
        for (Ticket ticket : tickets) {
            String tecnico = (ticket.getTecnicoAsignado() != null) ? 
                           ticket.getTecnicoAsignado().getNombre() : "Sin asignar";
            
            System.out.printf("%-4d %-20s %-12s %-10s %-15s %s%n",
                             ticket.getId(),
                             truncarTexto(ticket.getTitulo(), 20),
                             ticket.getCategoria(),
                             ticket.getEstado(),
                             truncarTexto(tecnico, 15),
                             ticket.getPrioridad());
        }
        
        System.out.println("\nTotal encontrados: " + tickets.size());
    }
    
    /**
     * Muestra las métricas del sistema.
     */
    private void mostrarMetricas() {
        System.out.println("\n┌─────────── MÉTRICAS DEL SISTEMA ───────────┐");
        
        Map<String, Object> metricas = tableroService.calcularMetricas();
        
        System.out.println("Resumen General:");
        System.out.println("   Total de tickets: " + metricas.get("totalTickets"));
        System.out.println("   Tickets cerrados: " + metricas.get("ticketsCerrados"));
        System.out.printf("   Porcentaje cerrados: %.1f%%\n", (Double) metricas.get("porcentajeCerrados"));
        System.out.printf("   Tiempo promedio resolución: %.1f minutos\n", (Double) metricas.get("tiempoPromedioResolucion"));
        
        @SuppressWarnings("unchecked")
        Map<Estado, Long> ticketsPorEstado = (Map<Estado, Long>) metricas.get("ticketsPorEstado");
        System.out.println("\nTickets por Estado:");
        for (Estado estado : Estado.values()) {
            long count = ticketsPorEstado.getOrDefault(estado, 0L);
            System.out.printf("   %s: %d\n", estado, count);
        }
        
        @SuppressWarnings("unchecked")
        Map<Prioridad, Long> ticketsPorPrioridad = (Map<Prioridad, Long>) metricas.get("ticketsPorPrioridad");
        System.out.println("\nTickets por Prioridad:");
        for (Prioridad prioridad : Prioridad.values()) {
            long count = ticketsPorPrioridad.getOrDefault(prioridad, 0L);
            System.out.printf("   %s: %d\n", prioridad, count);
        }
    }
    
    /**
     * Gestiona los técnicos del sistema.
     */
    private void gestionarTecnicos() {
        System.out.println("\n┌───────── GESTIÓN DE TÉCNICOS ─────────┐");
        
        System.out.println("1. Ver todos los técnicos");
        System.out.println("2. Agregar nuevo técnico");
        System.out.print("Seleccione una opción: ");
        
        int opcion = leerEntero();
        
        switch (opcion) {
            case 1:
                mostrarTecnicos();
                break;
            case 2:
                agregarTecnico();
                break;
            default:
                System.out.println("ERROR: Opción no válida.");
        }
    }
    
    /**
     * Muestra todos los técnicos.
     */
    private void mostrarTecnicos() {
        List<Tecnico> tecnicos = tableroService.getTecnicos();
        
        if (tecnicos.isEmpty()) {
            System.out.println("No hay técnicos registrados en el sistema.");
            return;
        }
        
        System.out.println("\nTécnicos Registrados:");
        System.out.printf("%-4s %-20s %-30s%n", "ID", "NOMBRE", "HABILIDADES");
        System.out.println("─".repeat(55));
        
        for (Tecnico tecnico : tecnicos) {
            String skills = String.join(", ", tecnico.getSkills());
            System.out.printf("%-4d %-20s %-30s%n",
                             tecnico.getId(),
                             truncarTexto(tecnico.getNombre(), 20),
                             truncarTexto(skills, 30));
        }
    }
    
    /**
     * Agrega un nuevo técnico.
     */
    private void agregarTecnico() {
        System.out.println("\n┌────── AGREGAR NUEVO TÉCNICO ──────┐");
        
        System.out.print("Nombre del técnico: ");
        String nombre = scanner.nextLine().trim();
        
        System.out.println("Habilidades (separadas por comas):");
        System.out.println("Ejemplos: Redes, Hardware, Software, Seguridad");
        System.out.print("Habilidades: ");
        String skillsInput = scanner.nextLine().trim();
        
        List<String> skills = new ArrayList<>();
        if (!skillsInput.isEmpty()) {
            for (String skill : skillsInput.split(",")) {
                String skillTrimmed = skill.trim();
                if (!skillTrimmed.isEmpty()) {
                    skills.add(skillTrimmed);
                }
            }
        }
        
        int tecnicoId = tableroService.agregarTecnico(nombre, skills);
        System.out.println("Técnico #" + tecnicoId + " agregado exitosamente!");
    }
    
    /**
     * Muestra información de ayuda.
     */
    private void mostrarAyuda() {
        System.out.println("\n┌──────────────── AYUDA ────────────────┐");
        System.out.println("│                                       │");
        System.out.println("│ FLUJO DE TRABAJO RECOMENDADO:         │");
        System.out.println("│                                       │");
        System.out.println("│ 1. Agregar técnicos al sistema       │");
        System.out.println("│ 2. Crear tickets (manual o auto)     │");
        System.out.println("│ 3. Asignar tickets a técnicos        │");
        System.out.println("│ 4. Iniciar trabajo en tickets        │");
        System.out.println("│ 5. Cerrar tickets resueltos          │");
        System.out.println("│                                       │");
        System.out.println("│ CONSEJOS:                             │");
        System.out.println("│                                       │");
        System.out.println("│ • Use clasificación automática       │");
        System.out.println("│ • Revise métricas regularmente       │");
        System.out.println("│ • Mantenga skills de técnicos        │");
        System.out.println("│   actualizadas                       │");
        System.out.println("│                                       │");
        System.out.println("│ Los datos se guardan                 │");
        System.out.println("│ automáticamente en CSV               │");
        System.out.println("│                                       │");
        System.out.println("└───────────────────────────────────────┘");
    }
    
    /**
     * Muestra el mensaje de despedida.
     */
    private void despedida() {
        tableroService.guardarDatos();
        System.out.println("\n╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║                     ¡HASTA LUEGO!                           ║");
        System.out.println("║              Datos guardados exitosamente                   ║");
        System.out.println("║            Gracias por usar Mesa de Ayuda                   ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
    }
    
    // Métodos de utilidad
    
    /**
     * Lee un número entero desde la consola.
     */
    private int leerEntero() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
    
    /**
     * Trunca un texto a una longitud máxima.
     */
    private String truncarTexto(String texto, int maxLength) {
        if (texto == null) return "";
        if (texto.length() <= maxLength) return texto;
        return texto.substring(0, maxLength - 3) + "...";
    }
}