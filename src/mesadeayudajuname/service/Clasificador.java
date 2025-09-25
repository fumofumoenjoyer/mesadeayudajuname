package mesadeayudajuname.service;

import mesadeayudajuname.model.*;
import java.util.regex.Pattern;

/**
 * Clase de utilidad para clasificar automáticamente tickets
 * basándose en palabras clave en el título y descripción.
 * 
 * @author juanulb
 */
public class Clasificador {
    
    // Patrones para prioridad ALTA
    private static final Pattern PRIORIDAD_ALTA = Pattern.compile(
        "\\b(urgente|crítico|crítica|emergencia|emergency|down|caído|parado|" +
        "no funciona|error crítico|falla crítica|servidor caído)\\b",
        Pattern.CASE_INSENSITIVE
    );
    
    // Patrones para prioridad MEDIA
    private static final Pattern PRIORIDAD_MEDIA = Pattern.compile(
        "\\b(importante|problema|issue|lento|lenta|intermitente|" +
        "no responde|timeout|delay|retraso)\\b",
        Pattern.CASE_INSENSITIVE
    );
    
    // Patrones para categorías
    private static final Pattern CATEGORIA_REDES = Pattern.compile(
        "\\b(red|redes|network|conexión|internet|wifi|ethernet|" +
        "router|switch|ip|dns|firewall|vpn)\\b",
        Pattern.CASE_INSENSITIVE
    );
    
    private static final Pattern CATEGORIA_HARDWARE = Pattern.compile(
        "\\b(hardware|hw|disco|ram|memoria|cpu|procesador|monitor|" +
        "pantalla|teclado|mouse|impresora|scanner)\\b",
        Pattern.CASE_INSENSITIVE
    );
    
    private static final Pattern CATEGORIA_SOFTWARE = Pattern.compile(
        "\\b(software|sw|aplicación|aplicacion|programa|windows|linux|" +
        "office|excel|word|outlook|navegador|browser)\\b",
        Pattern.CASE_INSENSITIVE
    );
    
    private static final Pattern CATEGORIA_SEGURIDAD = Pattern.compile(
        "\\b(seguridad|security|virus|malware|antivirus|contraseña|" +
        "password|login|acceso|cuenta|usuario|hack)\\b",
        Pattern.CASE_INSENSITIVE
    );
    
    /**
     * Clasifica automáticamente la prioridad de un ticket basándose
     * en el contenido del título y descripción.
     * 
     * @param titulo Título del ticket
     * @param descripcion Descripción del ticket
     * @return Prioridad sugerida
     */
    public static Prioridad clasificarPrioridad(String titulo, String descripcion) {
        String contenido = (titulo + " " + descripcion).toLowerCase();
        
        if (PRIORIDAD_ALTA.matcher(contenido).find()) {
            return Prioridad.ALTA;
        } else if (PRIORIDAD_MEDIA.matcher(contenido).find()) {
            return Prioridad.MEDIA;
        } else {
            return Prioridad.BAJA;
        }
    }
    
    /**
     * Clasifica automáticamente la categoría de un ticket basándose
     * en el contenido del título y descripción.
     * 
     * @param titulo Título del ticket
     * @param descripcion Descripción del ticket
     * @return Categoría sugerida
     */
    public static String clasificarCategoria(String titulo, String descripcion) {
        String contenido = (titulo + " " + descripcion).toLowerCase();
        
        if (CATEGORIA_REDES.matcher(contenido).find()) {
            return "Redes";
        } else if (CATEGORIA_HARDWARE.matcher(contenido).find()) {
            return "Hardware";
        } else if (CATEGORIA_SOFTWARE.matcher(contenido).find()) {
            return "Software";
        } else if (CATEGORIA_SEGURIDAD.matcher(contenido).find()) {
            return "Seguridad";
        } else {
            return "General";
        }
    }
    
    /**
     * Realiza una clasificación completa del ticket.
     * 
     * @param titulo Título del ticket
     * @param descripcion Descripción del ticket
     * @return Array con [categoría, prioridad]
     */
    public static Object[] clasificarCompleto(String titulo, String descripcion) {
        String categoria = clasificarCategoria(titulo, descripcion);
        Prioridad prioridad = clasificarPrioridad(titulo, descripcion);
        return new Object[]{categoria, prioridad};
    }
    
    /**
     * Sugiere palabras clave encontradas en el contenido para ayudar
     * al usuario a entender por qué se clasificó de cierta manera.
     * 
     * @param titulo Título del ticket
     * @param descripcion Descripción del ticket
     * @return String con las palabras clave encontradas
     */
    public static String obtenerPalabrasClave(String titulo, String descripcion) {
        String contenido = (titulo + " " + descripcion).toLowerCase();
        StringBuilder palabrasClave = new StringBuilder();
        
        if (PRIORIDAD_ALTA.matcher(contenido).find()) {
            palabrasClave.append("Prioridad ALTA detectada. ");
        } else if (PRIORIDAD_MEDIA.matcher(contenido).find()) {
            palabrasClave.append("Prioridad MEDIA detectada. ");
        }
        
        if (CATEGORIA_REDES.matcher(contenido).find()) {
            palabrasClave.append("Categoría REDES detectada. ");
        } else if (CATEGORIA_HARDWARE.matcher(contenido).find()) {
            palabrasClave.append("Categoría HARDWARE detectada. ");
        } else if (CATEGORIA_SOFTWARE.matcher(contenido).find()) {
            palabrasClave.append("Categoría SOFTWARE detectada. ");
        } else if (CATEGORIA_SEGURIDAD.matcher(contenido).find()) {
            palabrasClave.append("Categoría SEGURIDAD detectada. ");
        }
        
        return palabrasClave.toString().trim();
    }
}