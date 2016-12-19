package com.bytesw.trg.core.dto;

import com.bytesw.trg.core.bo.Usuario;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 *
 * @author lvasquez
 */
public class Match {

        private final Queue<Evento> incomingEventQueue = new ConcurrentLinkedQueue<>();
        private final Queue<Evento> outgoingEventQueue = new ConcurrentLinkedQueue<>();
        private List<Usuario> jugadores;

        public Queue<Evento> getIncomingEventQueue() {
                return incomingEventQueue;
        }

        public Queue<Evento> getOutgoingEventQueue() {
                return outgoingEventQueue;
        }

        public List<Usuario> getJugadores() {
                return jugadores;
        }

        public void setJugadores(List<Usuario> jugadores) {
                this.jugadores = jugadores;
        }

}
