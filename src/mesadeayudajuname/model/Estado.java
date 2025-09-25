package mesadeayudajuname.model;

/**
 * Enumerador que representa los posibles estados de un ticket.
 * 
 * @author juanulb
 */
public enum Estado {
    /**
     * Ticket recién creado, aún no asignado o sin iniciar trabajo
     */
    ABIERTO,
    
    /**
     * Ticket asignado a un técnico y en proceso de resolución
     */
    EN_CURSO,
    
    /**
     * Ticket resuelto y cerrado
     */
    CERRADO
}