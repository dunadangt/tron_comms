package com.bytesw.trg.core.dto;

/**
 *
 * @author lvasquez
 */
public class Evento {

        private Integer tipo;
        private String username;
        private Integer x;
        private Integer y;

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

        @Override
        public String toString() {
                return "Evento{" + "tipo=" + tipo + ", username=" + username + ", x=" + x + ", y=" + y + '}';
        }

        
}
