package com.bytesw.trg.core.dto;

import com.bytesw.trg.core.bo.Usuario;
import java.net.InetAddress;

/**
 *
 * @author lvasquez
 */
public class AutenticacionRequest {

        private Usuario usuario;

        private InetAddress answerAddress;
        private Integer answerPort;
        private Integer gamePort;

        public Usuario getUsuario() {
                return usuario;
        }

        public void setUsuario(Usuario usuario) {
                this.usuario = usuario;
        }

        public InetAddress getAnswerAddress() {
                return answerAddress;
        }

        public void setAnswerAddress(InetAddress answerAddress) {
                this.answerAddress = answerAddress;
        }

        public Integer getAnswerPort() {
                return answerPort;
        }

        public void setAnswerPort(Integer answerPort) {
                this.answerPort = answerPort;
        }

        public Integer getGamePort() {
                return gamePort;
        }

        public void setGamePort(Integer gamePort) {
                this.gamePort = gamePort;
        }

}
