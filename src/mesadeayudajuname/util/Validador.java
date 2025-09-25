package mesadeayudajuname.util;

import java.util.List;
import java.util.ArrayList;

/**
 * Clase de utilidades para validación de datos.
 * 
 * @author juanulb
 */
public class Validador {
    
    /**
     * Valida que una cadena no sea nula ni esté vacía.
     * 
     * @param valor Valor a validar
     * @param nombreCampo Nombre del campo para mensajes de error
     * @throws IllegalArgumentException si la validación falla
     */
    public static void validarTextoRequerido(String valor, String nombreCampo) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException("El campo '" + nombreCampo + "' no puede estar vacío");
        }
    }
    
    /**
     * Valida que un ID sea positivo.
     * 
     * @param id ID a validar
     * @param nombreCampo Nombre del campo para mensajes de error
     * @throws IllegalArgumentException si la validación falla
     */
    public static void validarIdPositivo(int id, String nombreCampo) {
        if (id <= 0) {
            throw new IllegalArgumentException("El " + nombreCampo + " debe ser un número positivo");
        }
    }
    
    /**
     * Valida que un objeto no sea nulo.
     * 
     * @param objeto Objeto a validar
     * @param nombreCampo Nombre del campo para mensajes de error
     * @throws IllegalArgumentException si la validación falla
     */
    public static void validarNoNulo(Object objeto, String nombreCampo) {
        if (objeto == null) {
            throw new IllegalArgumentException("El campo '" + nombreCampo + "' no puede ser nulo");
        }
    }
    
    /**
     * Normaliza una cadena de texto (trim y primera letra mayúscula).
     * 
     * @param texto Texto a normalizar
     * @return Texto normalizado
     */
    public static String normalizarTexto(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return "";
        }
        
        String textoLimpio = texto.trim();
        if (textoLimpio.length() == 1) {
            return textoLimpio.toUpperCase();
        }
        
        return textoLimpio.substring(0, 1).toUpperCase() + 
               textoLimpio.substring(1).toLowerCase();
    }
    
    /**
     * Valida y normaliza una lista de habilidades.
     * 
     * @param skills Lista de habilidades a validar
     * @return Lista de habilidades normalizadas
     */
    public static List<String> validarYNormalizarSkills(List<String> skills) {
        List<String> skillsNormalizadas = new ArrayList<>();
        
        if (skills != null) {
            for (String skill : skills) {
                if (skill != null && !skill.trim().isEmpty()) {
                    String skillNormalizada = normalizarTexto(skill);
                    if (!skillsNormalizadas.contains(skillNormalizada)) {
                        skillsNormalizadas.add(skillNormalizada);
                    }
                }
            }
        }
        
        return skillsNormalizadas;
    }
    
    /**
     * Valida que la longitud de un texto esté dentro de los límites.
     * 
     * @param texto Texto a validar
     * @param nombreCampo Nombre del campo para mensajes de error
     * @param longitudMinima Longitud mínima permitida
     * @param longitudMaxima Longitud máxima permitida
     * @throws IllegalArgumentException si la validación falla
     */
    public static void validarLongitudTexto(String texto, String nombreCampo, 
                                          int longitudMinima, int longitudMaxima) {
        if (texto == null) texto = "";
        
        if (texto.length() < longitudMinima) {
            throw new IllegalArgumentException(
                "El campo '" + nombreCampo + "' debe tener al menos " + 
                longitudMinima + " caracteres"
            );
        }
        
        if (texto.length() > longitudMaxima) {
            throw new IllegalArgumentException(
                "El campo '" + nombreCampo + "' no puede tener más de " + 
                longitudMaxima + " caracteres"
            );
        }
    }
}