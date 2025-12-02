package model;

import java.time.LocalDateTime;

public class Puntaje {

    private int id;
    private int idJugador;
    private int puntaje;
    private LocalDateTime fechaHora;

    public Puntaje() {
    }

    public Puntaje(int idJugador, int puntaje, LocalDateTime fechaHora) {
        this.idJugador = idJugador;
        this.puntaje = puntaje;
        this.fechaHora = fechaHora;
    }

}
