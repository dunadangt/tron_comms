package com.bytesw.trg.core.dto;

/**
 *
 * @author lvasquez
 */
public class NotificacionServidor {

        private NotifyServerLocation notifyServerLocation;
        private AbandonarPartidaResponse abandonarPartidaResponse;
        private AutenticacionResponse autenticacionResponse;
        private CrearPartidaResponse crearPartidaResponse;
        private IniciarPartidaResponse iniciarPartidaResponse;
        private ListarPartidasResponse listarPartidasResponse;
        private UnirsePartidaResponse unirsePartidaResponse;

        public NotifyServerLocation getNotifyServerLocation() {
                return notifyServerLocation;
        }

        public void setNotifyServerLocation(NotifyServerLocation notifyServerLocation) {
                this.notifyServerLocation = notifyServerLocation;
        }

        public AbandonarPartidaResponse getAbandonarPartidaResponse() {
                return abandonarPartidaResponse;
        }

        public void setAbandonarPartidaResponse(AbandonarPartidaResponse abandonarPartidaResponse) {
                this.abandonarPartidaResponse = abandonarPartidaResponse;
        }

        public AutenticacionResponse getAutenticacionResponse() {
                return autenticacionResponse;
        }

        public void setAutenticacionResponse(AutenticacionResponse autenticacionResponse) {
                this.autenticacionResponse = autenticacionResponse;
        }

        public CrearPartidaResponse getCrearPartidaResponse() {
                return crearPartidaResponse;
        }

        public void setCrearPartidaResponse(CrearPartidaResponse crearPartidaResponse) {
                this.crearPartidaResponse = crearPartidaResponse;
        }

        public IniciarPartidaResponse getIniciarPartidaResponse() {
                return iniciarPartidaResponse;
        }

        public void setIniciarPartidaResponse(IniciarPartidaResponse iniciarPartidaResponse) {
                this.iniciarPartidaResponse = iniciarPartidaResponse;
        }

        public ListarPartidasResponse getListarPartidasResponse() {
                return listarPartidasResponse;
        }

        public void setListarPartidasResponse(ListarPartidasResponse listarPartidasResponse) {
                this.listarPartidasResponse = listarPartidasResponse;
        }

        public UnirsePartidaResponse getUnirsePartidaResponse() {
                return unirsePartidaResponse;
        }

        public void setUnirsePartidaResponse(UnirsePartidaResponse unirsePartidaResponse) {
                this.unirsePartidaResponse = unirsePartidaResponse;
        }

}
