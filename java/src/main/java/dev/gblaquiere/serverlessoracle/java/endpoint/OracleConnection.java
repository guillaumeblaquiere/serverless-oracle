package dev.gblaquiere.serverlessoracle.java.endpoint;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class OracleConnection extends HttpServlet { //extends only usefull for Cloud Run

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws IOException {
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
            while (rs.next())
                response.getWriter().println(rs.getString(1));

            con.close();
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            System.out.println(e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println(e.getMessage());
        }
    }
}