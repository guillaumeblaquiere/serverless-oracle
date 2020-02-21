package dev.gblaquiere.serverlessoracle.java;

import com.google.cloud.functions.invoker.NewHttpFunctionExecutor;
import dev.gblaquiere.serverlessoracle.java.function.OracleConnection;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import static org.eclipse.jetty.servlet.ServletContextHandler.NO_SESSIONS;

public class ServletApplication {
    public static void main(String[] args) {


        String portEnvVar = System.getenv().get("PORT");
        Integer port = 8080;
        if (portEnvVar != null && !portEnvVar.equals("")) {
            //Assume that the port is correctly set
            port = Integer.parseInt(portEnvVar);
        }

        Server server = new Server(port);

        ServletContextHandler servletHandler = new ServletContextHandler(NO_SESSIONS);
        servletHandler.addServlet(new ServletHolder(NewHttpFunctionExecutor.forClass(OracleConnection.class)), "/");
        server.setHandler(servletHandler);

        try {
            server.start();
            server.join();
        } catch (Exception ex) {
            System.exit(1);
        } finally {
            server.destroy();
        }
    }
}

