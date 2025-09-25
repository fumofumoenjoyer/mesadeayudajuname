package mesadeayudajuname.model;

/**
 * Enumerador que representa los niveles de prioridad de un ticket.
 * 
 * @author juanulb
 */
public enum Prioridad {
    /**
     * Prioridad baja - no crítica, puede esperar
     */
    BAJA,
    
    /**
     * Prioridad media - importante pero no urgente
     */
    MEDIA,
    
    /**
     * Prioridad alta - crítica, requiere atención inmediata
     */
    ALTA;
    
    /**
     * Devuelve el valor numérico de la prioridad para comparación.
     * ALTA = 3, MEDIA = 2, BAJA = 1
     * 
     * @return valor numérico de la prioridad
     */
    public int getValor() {
        switch(this) {
            case ALTA: return 3;
            case MEDIA: return 2;
            case BAJA: return 1;
            default: return 0;
        }
    }
}