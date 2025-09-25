package mesadeayudajuname.model;

import java.util.List;
import java.util.ArrayList;

/**
 * Representa un técnico del sistema de mesa de ayuda.
 * Cada técnico tiene un ID único, nombre y una lista de habilidades
 * que determinan qué categorías de tickets puede atender.
 * 
 * @author juanulb
 */
public class Tecnico {
    private int id;
    private String nombre;
    private List<String> skills;
    
    /**
     * Constructor para crear un nuevo técnico.
     * 
     * @param id ID único del técnico
     * @param nombre Nombre completo del técnico
     * @param skills Lista de habilidades/categorías que puede atender
     */
    public Tecnico(int id, String nombre, List<String> skills) {
        this.id = id;
        this.nombre = nombre;
        this.skills = new ArrayList<>(skills);
    }
    
    /**
     * Constructor sobrecargado para crear técnico sin habilidades iniciales.
     * 
     * @param id ID único del técnico
     * @param nombre Nombre completo del técnico
     */
    public Tecnico(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
        this.skills = new ArrayList<>();
    }
    
    /**
     * Verifica si el técnico puede atender una categoría específica.
     * 
     * @param categoria Categoría del ticket a verificar
     * @return true si el técnico puede atender esta categoría, false en caso contrario
     */
    public boolean puedeAtender(String categoria) {
        if (categoria == null || categoria.trim().isEmpty()) {
            return false;
        }
        
        // Búsqueda insensible a mayúsculas/minúsculas
        for (String skill : skills) {
            if (skill.equalsIgnoreCase(categoria.trim())) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Agrega una nueva habilidad al técnico.
     * 
     * @param skill Nueva habilidad a agregar
     */
    public void agregarSkill(String skill) {
        if (skill != null && !skill.trim().isEmpty() && !skills.contains(skill.trim())) {
            skills.add(skill.trim());
        }
    }
    
    /**
     * Remueve una habilidad del técnico.
     * 
     * @param skill Habilidad a remover
     */
    public void removerSkill(String skill) {
        skills.remove(skill);
    }
    
    // Getters y Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public List<String> getSkills() {
        return new ArrayList<>(skills);
    }
    
    public void setSkills(List<String> skills) {
        this.skills = new ArrayList<>(skills);
    }
    
    @Override
    public String toString() {
        return String.format("Técnico{id=%d, nombre='%s', skills=%s}", 
                           id, nombre, skills);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Tecnico tecnico = (Tecnico) obj;
        return id == tecnico.id;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}