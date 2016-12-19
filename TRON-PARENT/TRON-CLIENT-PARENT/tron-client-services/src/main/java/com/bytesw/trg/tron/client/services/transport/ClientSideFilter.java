package com.bytesw.trg.tron.client.services.transport;

import com.bytesw.trg.core.bo.Usuario;
import com.bytesw.trg.core.dto.ClientServerRequest;
import com.bytesw.trg.core.dto.Match;
import com.bytesw.trg.core.dto.NotificacionServidor;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import java.io.IOException;
import java.util.Queue;
import org.glassfish.grizzly.filterchain.BaseFilter;
import org.glassfish.grizzly.filterchain.FilterChainContext;
import org.glassfish.grizzly.filterchain.NextAction;
import org.slf4j.LoggerFactory;

/**
 *
 * @author lvasquez
 */
public class ClientSideFilter extends BaseFilter {

        static org.slf4j.Logger logger = LoggerFactory.getLogger(ClientSideFilter.class);
        private Queue<NotificacionServidor> answerQueue;
        private UDPSocketThread ust;
        private OutgoingUDPSocketThread oust;

        @Override
        public NextAction handleWrite(FilterChainContext ctx) throws IOException {
                logger.info("Sending [" + ctx.getMessage() + "]");
                return ctx.getInvokeAction();
        }

        @Override
        public NextAction handleRead(FilterChainContext ctx) throws IOException {
                String jsonString = ctx.getMessage();
                if (jsonString == null) {
                        logger.info("Error en lectura: mensaje vacio");
                }
                Gson json = new Gson();
                ClientServerRequest response = null;
                try {
                        response = json.fromJson(jsonString, ClientServerRequest.class);
                        if (response != null) {
                                logger.info("Received [" + response + "]");
                                if (response.getIniciarPartidaResponse() != null) {
                                        Match match = new Match();
                                        match.setJugadores(response.getIniciarPartidaResponse().getUsuarios());
                                        oust.setUsuarios(response.getIniciarPartidaResponse().getUsuarios());
                                        oust.setMatch(match);
                                        Thread t = new Thread(oust);
                                        t.start();
                                        ust.setUsuarios(response.getIniciarPartidaResponse().getUsuarios());
                                        ust.setMatch(match);

                                        boolean owner = true;
                                        for (Usuario usuario : response.getIniciarPartidaResponse().getUsuarios()) {
                                                if (usuario.getPosition() == 1) {
                                                        owner = false;
                                                        break;
                                                }
                                        }
                                        if (owner) {
                                                ust.testRoundTrip();
                                                ust.notifyStart();
                                        } else {
                                                Thread t2 = new Thread(ust);
                                                t2.start();
                                        }
                                }
                        }
                } catch (JsonSyntaxException jsonSyntaxException) {
                        logger.error("Error en parseo de mensaje [" + jsonString + "]", jsonSyntaxException);
                } catch (Exception ex) {
                        logger.error("Error procesando mensaje [" + response + "]", ex);
                }
                return ctx.getStopAction();
        }

        public Queue<NotificacionServidor> getAnswerQueue() {
                return answerQueue;
        }

        public void setAnswerQueue(Queue<NotificacionServidor> answerQueue) {
                this.answerQueue = answerQueue;
        }

        public void setUst(UDPSocketThread ust) {
                this.ust = ust;
        }

        public void setOust(OutgoingUDPSocketThread oust) {
                this.oust = oust;
        }

}
