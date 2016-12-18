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

}
