package model;

public class Jugador {

    private int id;
    private String nombre;
    private int puntajeMax;

    public Jugador() {
    }

    public Jugador(String nombre, int puntajeMax) {
        this.nombre = nombre;
        this.puntajeMax = puntajeMax;
    }

    public Jugador(int id, String nombre, int puntajeMax) {
        this.id = id;
        this.nombre = nombre;
        this.puntajeMax = puntajeMax;
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

    public int getPuntajeMax() {
        return puntajeMax;
    }

    public void setPuntajeMax(int puntajeMax) {
        this.puntajeMax = puntajeMax;
    }

    @Override
    public String toString() {
        return "Jugador{"
                + "id=" + id
                + ", nombre='" + nombre + '\''
                + ", puntajeMax=" + puntajeMax
                + '}';
    }
}
