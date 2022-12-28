/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package carwash.serviceImpl;

import carwash.pojo.Admin;
import carwash.service.AdminService;
import carwash.utilities.ConnectionManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ASUS
 */
public class AdminServiceImpl implements AdminService {

    private ConnectionManager conMan;
    private Connection conn;
    Statement stmt;
    ResultSet rs;

    @Override
    public Admin login(String username, String password) {
        Admin admin = null;

        String sql = "SELECT id, nama, username, password "
                + "FROM admin "
                + "WHERE username = '" + username + "' "
                + "AND password = '" + password + "'";

        conMan = new ConnectionManager();
        conn = conMan.connect();

        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                admin = new Admin();
                admin.setId(rs.getInt("id"));
                admin.setNama(rs.getString("nama"));
                admin.setUsername(rs.getString("username"));
            }
            conMan.disconnect();
        } catch (SQLException e) {
            Logger.getLogger(AdminServiceImpl.class.getName())
                    .log(Level.SEVERE, null, e);
        }
        return admin;
    }

}
