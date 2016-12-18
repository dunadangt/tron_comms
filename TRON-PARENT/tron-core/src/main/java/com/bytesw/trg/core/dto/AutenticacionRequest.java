package com.bytesw.trg.core.dto;

import com.bytesw.trg.core.bo.Usuario;

/**
 *
 * @author lvasquez
 */
public class AutenticacionRequest {

        private Usuario usuario;

        public Usuario getUsuario() {
                return usuario;
        }

        public void setUsuario(Usuario usuario) {
                this.usuario = usuario;
        }

}
