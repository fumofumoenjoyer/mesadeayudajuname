/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package mesadeayudajuname;

import mesadeayudajuname.app.MesaDeAyudaApp;

/**
 * Clase principal del sistema de Mesa de Ayuda.
 * Punto de entrada de la aplicación.
 * 
 * @author juanulb
 */
public class Mesadeayudajuname {

    /**
     * Método principal que inicia la aplicación.
     * 
     * @param args argumentos de línea de comandos (no utilizados)
     */
    public static void main(String[] args) {
        System.out.println("Iniciando Sistema de Mesa de Ayuda...");
        
        try {
            MesaDeAyudaApp app = new MesaDeAyudaApp();
            app.ejecutar();
        } catch (Exception e) {
            System.err.println("Error fatal en la aplicación: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
