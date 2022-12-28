/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package carwash.serviceImpl;

import carwash.pojo.Pegawai;
import carwash.service.PegawaiService;
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
public class PegawaiServiceImpl implements PegawaiService {

    private ConnectionManager conMan;
    private Connection conn;
    Statement stmt;
    ResultSet rs;

    @Override
    public List<Pegawai> findAll() {
        List<Pegawai> listPegawai = new ArrayList<>();
        String sql = "SELECT * FROM pegawai";

        conMan = new ConnectionManager();
        conn = conMan.connect();

        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Pegawai pegawai = new Pegawai();
                pegawai.setId(rs.getInt("id"));
                pegawai.setNama(rs.getString("nama"));
                pegawai.setAlamat(rs.getString("alamat"));
                pegawai.setNoHp(rs.getString("noHp"));
                pegawai.setStatus(rs.getString("status"));
                pegawai.setGaji(rs.getInt("gaji"));

                listPegawai.add(pegawai);
            }
            conMan.disconnect();
        } catch (SQLException e) {
            Logger.getLogger(AdminServiceImpl.class.getName())
                    .log(Level.SEVERE, null, e);
        }

        return listPegawai;
    }

    @Override
    public Integer create(Pegawai object) {
        int result = 0;
        String sql = "INSERT INTO pegawai VALUES"
                + "('" + object.getNama() + "', "
                + "'" + object.getAlamat() + "', "
                + "'" + object.getNoHp() + "', "
                + "'" + object.getStatus() + "', "
                + "" + object.getGaji() + ")";

        conMan = new ConnectionManager();
        conn = conMan.connect();

        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
            conMan.disconnect();
        } catch (SQLException e) {
            Logger.getLogger(PegawaiServiceImpl.class.getName())
                    .log(Level.SEVERE, null, e);
        }
        return result;
    }

    @Override
    public Integer update(Pegawai object) {
        int result = 0;
        String sql = "UPDATE pegawai SET nama ='" + object.getNama() + "', "
                + "alamat='" + object.getAlamat() + "', "
                + "noHp='" + object.getNoHp() + "', "
                + "status='" + object.getStatus() + "', "
                + "gaji=" + object.getGaji() + ""
                + "WHERE id=" + object.getId() + "";

        conMan = new ConnectionManager();
        conn = conMan.connect();

        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
            conMan.disconnect();
        } catch (SQLException e) {
            Logger.getLogger(PegawaiServiceImpl.class.getName())
                    .log(Level.SEVERE, null, e);
        }
        return result;
    }

    @Override
    public Pegawai findById(int id) {
        Pegawai pegawai = null;
        String sql = "SELECT * FROM pegawai WHERE id=" + id + "";

        conMan = new ConnectionManager();
        conn = conMan.connect();

        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                pegawai = new Pegawai();
                pegawai.setId(rs.getInt("id"));
                pegawai.setNama(rs.getString("nama"));
                pegawai.setAlamat(rs.getString("alamat"));
                pegawai.setNoHp(rs.getString("noHp"));
                pegawai.setStatus(rs.getString("status"));
                pegawai.setGaji(rs.getInt("gaji"));
            }
            conMan.disconnect();
        } catch (SQLException e) {
            Logger.getLogger(PegawaiServiceImpl.class.getName())
                    .log(Level.SEVERE, null, e);
        }
        return pegawai;
    }

    @Override
    public Integer delete(int id) {
        int result = 0;
        String sql = "DELETE FROM pegawai WHERE id=" + id + "";

        conMan = new ConnectionManager();
        conn = conMan.connect();

        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
            conMan.disconnect();
        } catch (SQLException e) {
            Logger.getLogger(PegawaiServiceImpl.class.getName())
                    .log(Level.SEVERE, null, e);
        }
        return result;
    }

}
