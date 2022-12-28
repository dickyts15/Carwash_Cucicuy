/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package carwash.serviceImpl;

import carwash.pojo.Additional;
import carwash.service.AdditionalService;
import carwash.utilities.ConnectionManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ASUS
 */
public class AdditionalServiceImpl implements AdditionalService {

    private ConnectionManager conMan;
    private Connection conn;
    Statement stmt;
    ResultSet rs;

    @Override
    public List<Additional> findAll() {
        List<Additional> listAdditional = new ArrayList<>();
        String sql = "SELECT * FROM additional";

        conMan = new ConnectionManager();
        conn = conMan.connect();

        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Additional additional = new Additional();
                additional.setId(rs.getInt("id"));
                additional.setNamaAdd(rs.getString("namaAdd"));
                additional.setHarga(rs.getInt("harga"));

                listAdditional.add(additional);
            }
            conMan.disconnect();
        } catch (SQLException e) {
            Logger.getLogger(AdditionalServiceImpl.class.getName())
                    .log(Level.SEVERE, null, e);
        }

        return listAdditional;
    }

    @Override
    public Integer create(Additional object) {
        int result = 0;
        String sql = "INSERT INTO additional VALUES"
                + "('" + object.getNamaAdd() + "', "
                + "" + object.getHarga() + ")";

        conMan = new ConnectionManager();
        conn = conMan.connect();

        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
            conMan.disconnect();
        } catch (SQLException e) {
            Logger.getLogger(AdditionalServiceImpl.class.getName())
                    .log(Level.SEVERE, null, e);
        }
        return result;
    }

    @Override
    public Integer update(Additional object) {
        int result = 0;
        String sql = "UPDATE additional SET namaAdd ='" + object.getNamaAdd() + "', "
                + "harga=" + object.getHarga() + ""
                + "WHERE id=" + object.getId() + "";

        conMan = new ConnectionManager();
        conn = conMan.connect();

        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
            conMan.disconnect();
        } catch (SQLException e) {
            Logger.getLogger(AdditionalServiceImpl.class.getName())
                    .log(Level.SEVERE, null, e);
        }
        return result;
    }

    @Override
    public Additional findById(int id) {
        Additional additional = null;
        String sql = "SELECT * FROM additional WHERE id=" + id + "";

        conMan = new ConnectionManager();
        conn = conMan.connect();

        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                additional = new Additional();
                additional.setId(rs.getInt("id"));
                additional.setNamaAdd(rs.getString("namaAdd"));
                additional.setHarga(rs.getInt("harga"));

            }
            conMan.disconnect();
        } catch (SQLException e) {
            Logger.getLogger(AdditionalServiceImpl.class.getName())
                    .log(Level.SEVERE, null, e);
        }
        return additional;
    }

    @Override
    public Integer delete(int id) {
        int result = 0;
        String sql = "DELETE FROM additional WHERE id=" + id + "";

        conMan = new ConnectionManager();
        conn = conMan.connect();

        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
            conMan.disconnect();
        } catch (SQLException e) {
            Logger.getLogger(AdditionalServiceImpl.class.getName())
                    .log(Level.SEVERE, null, e);
        }
        return result;
    }

}
