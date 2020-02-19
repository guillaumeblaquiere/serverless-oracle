package dev.gblaquiere.serverlessoracle.java.function;

import com.google.cloud.functions.HttpFunction;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;
import dev.gblaquiere.serverlessoracle.java.Helpers;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class OracleConnection extends HttpServlet implements HttpFunction { //extends only usefull for Cloud Run/AppEngine

    public void service(HttpServletRequest request,
                      HttpServletResponse response) throws IOException {
        service(Helpers.createHttpRequest(request), Helpers.createHttpResponse(response));
    }

    //With function, the name can be different. Not with Cloud Run. Here a GET request
    @Override
    public void service(HttpRequest request, HttpResponse response) {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");

            String dbIp = System.getenv("ORACLE_IP");
            String dbSchema = System.getenv("ORACLE_SCHEMA");
            String dbUser = System.getenv("ORACLE_USER");
            String dbPassword = System.getenv("ORACLE_PASSWORD");

            Connection con = DriverManager.getConnection(
                    "jdbc:oracle:thin:@" + dbIp + ":1521:" + dbSchema, dbUser, dbPassword);

            Statement stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery("select 'Great!' from dual");
            while (rs.next()) {
                response.getOutputStream().write(rs.getString(1).getBytes());
                //FIXME  This line write nothing when use wrapped servlet response
//                response.getWriter().write(rs.getString(1));
            }

            con.close();
            response.setStatusCode(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            System.out.println(e);
            response.setStatusCode(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            try {
                response.getWriter().write(e.getMessage());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}