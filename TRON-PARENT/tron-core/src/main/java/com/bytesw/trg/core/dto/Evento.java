package com.bytesw.trg.core.dto;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author lvasquez
 */
public class Evento {

        private Integer tipo;
        private String username;
        private Integer x;
        private Integer y;
        private List<Punto> puntos = new ArrayList<>();
        private Long sequence;

        public Integer getTipo() {
                return tipo;
        }

        public void setTipo(Integer tipo) {
                this.tipo = tipo;
        }

        public String getUsername() {
                return username;
        }

        public void setUsername(String username) {
                this.username = username;
        }

        public Integer getX() {
                return x;
        }

        public void setX(Integer x) {
                this.x = x;
        }

        public Integer getY() {
                return y;
        }

        public void setY(Integer y) {
                this.y = y;
        }

        public List<Punto> getPuntos() {
                return puntos;
        }

        public void setPuntos(List<Punto> puntos) {
                this.puntos = puntos;
        }

        public Long getSequence() {
                return sequence;
        }

        public void setSequence(Long sequence) {
                this.sequence = sequence;
        }

        @Override
        public String toString() {
                return "Evento{" + "tipo=" + tipo + ", username=" + username + ", x=" + x + ", y=" + y + ", puntos=" + puntos + ", sequence=" + sequence + '}';
        }

}
