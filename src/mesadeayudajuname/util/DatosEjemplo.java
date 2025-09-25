package mesadeayudajuname.util;

import mesadeayudajuname.model.*;
import mesadeayudajuname.service.TableroService;
import java.util.Arrays;

/**
 * Clase para inicializar datos de ejemplo en el sistema.
 * Útil para demostraciones y pruebas.
 * 
 * @author juanulb
 */
public class DatosEjemplo {
    
    /**
     * Inicializa el sistema con datos de ejemplo.
     * 
     * @param tableroService Servicio del tablero a inicializar
     */
    public static void inicializarDatosEjemplo(TableroService tableroService) {
        // Verificar si ya hay datos
        if (!tableroService.getTecnicos().isEmpty() || !tableroService.getTickets().isEmpty()) {
            System.out.println("El sistema ya contiene datos. No se inicializarán datos de ejemplo.");
            return;
        }
        
        System.out.println("Inicializando datos de ejemplo...");
        
        // Crear técnicos de ejemplo
        int tecnico1 = tableroService.agregarTecnico("Ana García", 
            Arrays.asList("Redes", "Hardware", "Seguridad"));
        
        int tecnico2 = tableroService.agregarTecnico("Carlos López", 
            Arrays.asList("Software", "Hardware"));
        
        int tecnico3 = tableroService.agregarTecnico("María Rodríguez", 
            Arrays.asList("Redes", "Seguridad", "Software"));
        
        int tecnico4 = tableroService.agregarTecnico("José Martínez", 
            Arrays.asList("Hardware", "General"));
        
        // Crear tickets de ejemplo
        
        // Ticket crítico de red
        int ticket1 = tableroService.crearTicket(
            "Servidor principal sin conexión",
            "El servidor principal de la empresa ha perdido completamente la conexión de red. " +
            "Ningún empleado puede acceder a los sistemas internos. Es urgente resolver este problema.",
            "Redes",
            Prioridad.ALTA
        );
        
        // Ticket de hardware
        int ticket2 = tableroService.crearTicket(
            "Impresora oficina 2 no funciona",
            "La impresora multifuncional de la oficina 2 no responde. Los empleados no pueden imprmir documentos importantes.",
            "Hardware",
            Prioridad.MEDIA
        );
        
        // Ticket de software
        int ticket3 = tableroService.crearTicket(
            "Problema con Microsoft Office",
            "Excel se cierra inesperadamente al abrir archivos grandes. Afecta a varios usuarios del departamento de contabilidad.",
            "Software",
            Prioridad.MEDIA
        );
        
        // Ticket de seguridad
        int ticket4 = tableroService.crearTicket(
            "Usuario bloqueado en sistema",
            "El usuario jperez no puede acceder al sistema. Su contraseña parece haber expirado pero el sistema no le permite cambiarla.",
            "Seguridad",
            Prioridad.BAJA
        );
        
        // Ticket con clasificación automática
        int ticket5 = tableroService.crearTicketConClasificacion(
            "Virus detectado en equipo",
            "El antivirus ha detectado múltiples amenazas críticas en el equipo del director. Necesitamos limpieza completa urgente."
        );
        
        // Ticket general
        int ticket6 = tableroService.crearTicket(
            "Solicitud nuevo software",
            "El departamento de diseño requiere licencias adicionales de Adobe Creative Suite para nuevos empleados.",
            "General",
            Prioridad.BAJA
        );
        
        // Asignar algunos tickets
        try {
            tableroService.asignarTicket(ticket1, tecnico3); // María - experta en redes
            tableroService.asignarTicket(ticket2, tecnico4); // José - hardware
            tableroService.asignarTicket(ticket3, tecnico2); // Carlos - software
            
            // Los tickets se inician automáticamente al ser asignados
            // Cerrar un ticket que ya está en curso
            tableroService.cerrarTicket(ticket2);
            
        } catch (Exception e) {
            System.err.println("Error al asignar tickets de ejemplo: " + e.getMessage());
        }
        
        System.out.println("✅ Datos de ejemplo inicializados:");
        System.out.println("   - 4 técnicos creados");
        System.out.println("   - 6 tickets de ejemplo creados");
        System.out.println("   - Algunos tickets asignados y en diferentes estados");
        System.out.println("   - Un ticket completado para demostrar métricas");
    }
}