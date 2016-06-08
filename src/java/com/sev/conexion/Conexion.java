/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sqlserver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author latorre
 */
public class Conexion {

    Connection cn = null;
   

    public Conexion() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            cn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=SEV", "sa", "maito2004021");

        } catch (SQLException|ClassNotFoundException e) {
            System.out.println(e);
        }
    }

    public static void main(String[] args) throws SQLException {
        Conexion obj = new Conexion();
        Connection cn = obj.getConnection();
        cn.close();
    }

    public Connection getConnection() {
        return cn;
    }

    public void desconectar() throws SQLException {
        cn.close();
    }
}
