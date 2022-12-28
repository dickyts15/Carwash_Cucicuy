/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package carwash.serviceImpl;

import carwash.pojo.Pencucian;
import carwash.service.PencucianService;
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
public class PencucianServiceImpl implements PencucianService {

    private ConnectionManager conMan;
    private Connection conn;
    Statement stmt;
    ResultSet rs;

    @Override
    public List<Pencucian> findAll() {
        List<Pencucian> listPencucian = new ArrayList<>();
        String sql = "SELECT * FROM pencucian";

        conMan = new ConnectionManager();
        conn = conMan.connect();

        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Pencucian pencucian = new Pencucian();
                pencucian.setId(rs.getInt("id"));
                pencucian.setJenis(rs.getString("jenis"));
                pencucian.setHarga(rs.getInt("harga"));

                listPencucian.add(pencucian);
            }
            conMan.disconnect();
        } catch (SQLException e) {
            Logger.getLogger(PencucianServiceImpl.class.getName())
                    .log(Level.SEVERE, null, e);
        }

        return listPencucian;
    }

    @Override
    public Integer create(Pencucian object) {
        int result = 0;
        String sql = "INSERT INTO pencucian VALUES"
                + "('" + object.getJenis() + "', "
                + "" + object.getHarga() + ")";

        conMan = new ConnectionManager();
        conn = conMan.connect();

        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
            conMan.disconnect();
        } catch (SQLException e) {
            Logger.getLogger(PencucianServiceImpl.class.getName())
                    .log(Level.SEVERE, null, e);
        }
        return result;
    }

    @Override
    public Integer update(Pencucian object) {
        int result = 0;
        String sql = "UPDATE pencucian SET jenis ='" + object.getJenis() + "', "
                + "harga=" + object.getHarga() + ""
                + "WHERE id=" + object.getId() + "";

        conMan = new ConnectionManager();
        conn = conMan.connect();

        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
            conMan.disconnect();
        } catch (SQLException e) {
            Logger.getLogger(PencucianServiceImpl.class.getName())
                    .log(Level.SEVERE, null, e);
        }
        return result;
    }

    @Override
    public Pencucian findById(int id) {
        Pencucian pencucian = null;
        String sql = "SELECT * FROM pencucian WHERE id=" + id + "";

        conMan = new ConnectionManager();
        conn = conMan.connect();

        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                pencucian = new Pencucian();
                pencucian.setId(rs.getInt("id"));
                pencucian.setJenis(rs.getString("jenis"));
                pencucian.setHarga(rs.getInt("harga"));

            }
            conMan.disconnect();
        } catch (SQLException e) {
            Logger.getLogger(PencucianServiceImpl.class.getName())
                    .log(Level.SEVERE, null, e);
        }
        return pencucian;
    }

    @Override
    public Integer delete(int id) {
        int result = 0;
        String sql = "DELETE FROM pencucian WHERE id=" + id + "";

        conMan = new ConnectionManager();
        conn = conMan.connect();

        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
            conMan.disconnect();
        } catch (SQLException e) {
            Logger.getLogger(PencucianServiceImpl.class.getName())
                    .log(Level.SEVERE, null, e);
        }
        return result;
    }

}
