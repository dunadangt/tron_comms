package com.bytesw.trg.core.dto;

import com.bytesw.trg.core.bo.Usuario;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author lvasquez
 */
public class IniciarPartidaResponse {

        private List<Usuario> usuarios = new ArrayList<>();

        public List<Usuario> getUsuarios() {
                return usuarios;
        }

        public void setUsuarios(List<Usuario> usuarios) {
                this.usuarios = usuarios;
        }

        @Override
        public String toString() {
                return "IniciarPartidaResponse{" + "usuarios=" + usuarios + '}';
        }

}
