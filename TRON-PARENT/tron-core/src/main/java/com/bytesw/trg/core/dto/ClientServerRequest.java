package com.bytesw.trg.core.dto;

/**
 *
 * @author lvasquez
 */
public class ClientServerRequest {

        private AbandonarPartidaRequest abandonarPartidaRequest;
        private ActualizarEstadoRequest actualizarEstadoRequest;
        private AutenticacionRequest autenticacionRequest;
        private CrearPartidaRequest crearPartidaRequest;
        private ListarPartidasRequest listarPartidasRequest;
        private UnirsePartidaRequest unirsePartidaRequest;
        private IniciarPartidaRequest iniciarPartidaRequest;

        public AbandonarPartidaRequest getAbandonarPartidaRequest() {
                return abandonarPartidaRequest;
        }

        public void setAbandonarPartidaRequest(AbandonarPartidaRequest abandonarPartidaRequest) {
                this.abandonarPartidaRequest = abandonarPartidaRequest;
        }

        public ActualizarEstadoRequest getActualizarEstadoRequest() {
                return actualizarEstadoRequest;
        }

        public void setActualizarEstadoRequest(ActualizarEstadoRequest actualizarEstadoRequest) {
                this.actualizarEstadoRequest = actualizarEstadoRequest;
        }

        public AutenticacionRequest getAutenticacionRequest() {
                return autenticacionRequest;
        }

        public void setAutenticacionRequest(AutenticacionRequest autenticacionRequest) {
                this.autenticacionRequest = autenticacionRequest;
        }

        public CrearPartidaRequest getCrearPartidaRequest() {
                return crearPartidaRequest;
        }

        public void setCrearPartidaRequest(CrearPartidaRequest crearPartidaRequest) {
                this.crearPartidaRequest = crearPartidaRequest;
        }

        public ListarPartidasRequest getListarPartidasRequest() {
                return listarPartidasRequest;
        }

        public void setListarPartidasRequest(ListarPartidasRequest listarPartidasRequest) {
                this.listarPartidasRequest = listarPartidasRequest;
        }

        public UnirsePartidaRequest getUnirsePartidaRequest() {
                return unirsePartidaRequest;
        }

        public void setUnirsePartidaRequest(UnirsePartidaRequest unirsePartidaRequest) {
                this.unirsePartidaRequest = unirsePartidaRequest;
        }

        public IniciarPartidaRequest getIniciarPartidaRequest() {
                return iniciarPartidaRequest;
        }

        public void setIniciarPartidaRequest(IniciarPartidaRequest iniciarPartidaRequest) {
                this.iniciarPartidaRequest = iniciarPartidaRequest;
        }

}
