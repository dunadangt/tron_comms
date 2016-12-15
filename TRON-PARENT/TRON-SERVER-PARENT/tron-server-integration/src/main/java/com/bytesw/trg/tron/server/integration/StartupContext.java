package com.bytesw.trg.tron.server.integration;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author lvasquez
 */
public class StartupContext {

        static org.slf4j.Logger logger = LoggerFactory.getLogger(StartupContext.class);

        public static void main(String[] args) {

                Properties properties = new Properties();
                logger.info("Tron Server V 1.0");
                try {
                        if (args != null && args.length > 0) {
                                logger.info("Iniciando carga de propiedades");

                                properties.load(new FileInputStream(new File(args[0])));
                                for (Object object : properties.keySet()) {
                                        String key = object.toString();
                                        String value = properties.getProperty(key);
                                        logger.info("Propiedad [" + key + "]: [" + value + "] ");
                                        System.setProperty(key, value);
                                }
                        } else {
                                logger.info("No existen propiedades externas, se utilizarán las defecto.");
                        }
                } catch (Exception ex) {
                        logger.error("Error cargando propiedades externas, se utilizarán las defecto.", ex);
                }

                //        "classpath:/com/bytesw/ocs/rpi/ri/bs/spring/applicationContext-ocs-ri-persistence.xml"
                ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:/com/bytesw/trg/tron/server/services/bs/spring/applicationContext-services.xml");
        }

}
