package com.bytesw.trg.tron.server.services.transport;

import com.bytesw.trg.core.dto.ClientServerRequest;
import com.bytesw.trg.tron.server.services.bs.TronService;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import java.io.IOException;
import org.glassfish.grizzly.filterchain.BaseFilter;
import org.glassfish.grizzly.filterchain.FilterChainContext;
import org.glassfish.grizzly.filterchain.NextAction;
import org.slf4j.LoggerFactory;

/**
 *
 * @author lvasquez
 */
public class ServerSideFilter extends BaseFilter {

        static org.slf4j.Logger logger = LoggerFactory.getLogger(ServerSideFilter.class);
        private TronService tronService;

        @Override
        public NextAction handleRead(FilterChainContext ctx) throws IOException {
                String jsonString = ctx.getMessage();
                if (jsonString == null) {
                        logger.info("Error en lectura: mensaje vacio");
                }
                Gson json = new Gson();
                ClientServerRequest request = null;
                try {
                        request = json.fromJson(jsonString, ClientServerRequest.class);
                        if (request != null) {
                                if (request.getAutenticacionRequest() != null) {
                                        tronService.autenticacionRequest(request.getAutenticacionRequest());
                                }
                        }
                } catch (JsonSyntaxException jsonSyntaxException) {
                        logger.error("Error en parseo de mensaje [" + jsonString + "]", jsonSyntaxException);
                } catch (Exception ex) {
                        logger.error("Error procesando mensaje [" + request + "]", ex);
                }
                return ctx.getStopAction();
        }

        @Override
        public NextAction handleWrite(FilterChainContext ctx) throws IOException {
                logger.info("Sending [" + ctx.getMessage() + "]");
                return ctx.getInvokeAction();
        }

        public TronService getTronService() {
                return tronService;
        }

        public void setTronService(TronService tronService) {
                this.tronService = tronService;
        }

}
