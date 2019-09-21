package modelos;

public class Usuario {

    //declara los datos que contiene la clase con private para encapsularlos
    //agregar id para el manejo de datos
    private int id;
    private String nombre;
    private String usuario;
    private String correo;
    private String direccion;
    private String contraseña;

    public Usuario() {
        //cunado no conocemos los datos completos
    }

    //para consultas

    public Usuario(int id, String nombre, String usuario, String correo, String direccion, String contraseña) {
        this.id = id;
        this.nombre = nombre;
        this.usuario = usuario;
        this.correo = correo;
        this.direccion = direccion;
        this.contraseña = contraseña;
    }

    //para guardar datos
    public Usuario(String nombre, String usuario, String correo, String direccion, String contraseña) {
        this.nombre = nombre;
        this.usuario = usuario;
        this.correo = correo;
        this.direccion = direccion;
        this.contraseña = contraseña;
    }

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

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }
}
