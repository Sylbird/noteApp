package com.app.notes;

public class User {

    private String id;
    private String nombre;
    private String contrasena;

    public User(){

    }
    public User(String id, String nombre, String contrasena) {
        this.id = id;
        this.nombre = nombre;
        this.contrasena = contrasena;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }



    // Método para verificar la contraseña
    public boolean verificarContrasena(String contrasena) {
        return this.contrasena.equals(contrasena);
    }


}
