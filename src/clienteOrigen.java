public class clienteOrigen {
    private String nombre;
    private String email;
    private String telefono;

    public clienteOrigen(String nombre, String email, String telefono) {
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;

    }
    
    public String getNombre() {
        return nombre;
    }

    public setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getEmail() {
        return email;
    }

    public setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
