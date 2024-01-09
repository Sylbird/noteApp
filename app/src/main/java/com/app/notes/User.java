package com.app.notes;

public class User {

    private String id;
    private String nombre;
    private String contraseña;

    public User(){

    }
    public User(String id, String nombre, String contraseña) {
        this.id = id;
        this.nombre = nombre;
        this.contraseña = contraseña;
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

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }



    // Método para verificar la contraseña
    public boolean verificarContraseña(String contraseña) {
        return this.contraseña.equals(contraseña);
    }


}
