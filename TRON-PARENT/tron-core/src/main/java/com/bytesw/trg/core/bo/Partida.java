package com.bytesw.trg.core.bo;

import java.util.List;

/**
 *
 * @author lvasquez
 */
public class Partida {

        private Long id;
        private Usuario creador;
        private List<Usuario> jugadores;

        public Long getId() {
                return id;
        }

        public void setId(Long id) {
                this.id = id;
        }

        public Usuario getCreador() {
                return creador;
        }

        public void setCreador(Usuario creador) {
                this.creador = creador;
        }

        public List<Usuario> getJugadores() {
                return jugadores;
        }

        public void setJugadores(List<Usuario> jugadores) {
                this.jugadores = jugadores;
        }

}
