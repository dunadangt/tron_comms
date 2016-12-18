package com.bytesw.trg.tron.server.services.bs;

import com.bytesw.trg.core.dto.AbandonarPartidaRequest;
import com.bytesw.trg.core.dto.ActualizarEstadoRequest;
import com.bytesw.trg.core.dto.AutenticacionRequest;
import com.bytesw.trg.core.dto.CrearPartidaRequest;
import com.bytesw.trg.core.dto.IniciarPartidaRequest;
import com.bytesw.trg.core.dto.ListarPartidasRequest;
import com.bytesw.trg.core.dto.UnirsePartidaRequest;

/**
 *
 * @author lvasquez
 */
public interface TronService {

        public void abandonarPartidaRequest(AbandonarPartidaRequest request);
        public void actualizarEstadoRequest(ActualizarEstadoRequest request);
        public void autenticacionRequest(AutenticacionRequest request);
        public void crearPartidaRequest(CrearPartidaRequest request);
        public void listarPartidasRequest(ListarPartidasRequest request);
        public void unirsePartidaRequest(UnirsePartidaRequest request);
        public void iniciarPartidaRequest(IniciarPartidaRequest request);
        
}
