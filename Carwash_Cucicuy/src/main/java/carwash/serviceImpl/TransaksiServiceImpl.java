/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package carwash.serviceImpl;

import carwash.pojo.Additional;
import carwash.pojo.Member;
import carwash.pojo.Pegawai;
import carwash.pojo.Pencucian;
import carwash.pojo.Transaksi;
import carwash.service.TransaksiService;
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
public class TransaksiServiceImpl implements TransaksiService {

    private ConnectionManager conMan;
    private Connection conn;
    Statement stmt;
    ResultSet rs;

    @Override
    public List<Transaksi> findAll() {
        List<Transaksi> listTransaksi = new ArrayList<>();
        Transaksi transaksi = null;
        Pegawai pegawai = null;
        Member member = null;
        Pencucian pencucian = null;
        Additional additional = null;

        String sql = "SELECT t.id_transaksi,peg.id_pegawai,peg.nama_pegawai,mem.id_member,mem.nama_member,"
                + "t.tanggal_transaksi,pen.id_pencucian,pen.jenis,addi.id_additional,addi.namaAdd,t.jenis_mobil,t.plat_nomor,"
                + "t.total_harga "
                + "FROM transaksi t, pegawai peg, member mem, pencucian pen, additional addi "
                + "WHERE t.id_pegawai = peg.id_pegawai AND t.id_member = mem.id_member AND "
                + "t.id_pencucian = pen.id_pencucian AND t.id_additional = addi.id_additional";

        conMan = new ConnectionManager();
        conn = conMan.connect();

        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                transaksi = new Transaksi();

                transaksi.setId(rs.getInt("id_transaksi"));
                transaksi.setTanggalTransaksi(rs.getString("tanggal_transaksi"));
                transaksi.setJenisMobil(rs.getString("jenis_mobil"));
                transaksi.setPlatNomor(rs.getString("plat_nomor"));
                transaksi.setTotalHarga(Double.parseDouble(rs.getString("total_harga")));

                pegawai = new Pegawai();
                pegawai.setId(rs.getInt("id_pegawai"));
                pegawai.setNama(rs.getString("nama_pegawai"));

                member = new Member();
                member.setId(rs.getInt("id_member"));
                member.setNama(rs.getString("nama_member"));

                pencucian = new Pencucian();
                pencucian.setId(rs.getInt("id_pencucian"));
                pencucian.setJenis(rs.getString("jenis"));

                additional = new Additional();
                additional.setId(rs.getInt("id_additional"));
                additional.setNamaAdd(rs.getString("namaAdd"));

                transaksi.setPegawai(pegawai);
                transaksi.setMember(member);
                transaksi.setAdditional(additional);
                transaksi.setPencucian(pencucian);

                listTransaksi.add(transaksi);
            }
            conMan.disconnect();
        } catch (SQLException e) {
            Logger.getLogger(TransaksiServiceImpl.class.getName())
                    .log(Level.SEVERE, null, e);
        }

        return listTransaksi;
    }

    @Override
    public Integer create(Transaksi object) {
        Pencucian pencucian = null;
        Additional additional = null;

        int result = 0;
        String sql = "INSERT INTO transaksi(id_pegawai,id_member,id_pencucian,id_additional,"
                + "tanggal_transaksi,jenis_mobil,plat_nomor,total_harga) VALUES"
                + "(" + object.getPegawai().getId() + ", "
                + "" + object.getMember().getId() + ", "
                + "" + object.getPencucian().getId() + ", "
                + "" + object.getAdditional().getId() + ", "
                + "'" + object.getTanggalTransaksi() + "', "
                + "'" + object.getJenisMobil() + "', "
                + "'" + object.getPlatNomor() + "', "
                + "" + object.getTotalHarga() + ")";

        conMan = new ConnectionManager();
        conn = conMan.connect();

        try {
            stmt = conn.createStatement();
            result = stmt.executeUpdate(sql);
            conMan.disconnect();
        } catch (SQLException e) {
            Logger.getLogger(TransaksiServiceImpl.class.getName())
                    .log(Level.SEVERE, null, e);
        }
        return result;
    }

    @Override
    public Integer update(Transaksi object) {
        Pencucian pencucian = null;
        Additional additional = null;
        int result = 0;
        String sql = "UPDATE transaksi SET id_pegawai=" + object.getPegawai().getId() + ", "
                + "id_member=" + object.getMember().getId() + ", "
                + "id_pencucian=" + object.getPencucian().getId() + ", "
                + "id_additional=" + object.getAdditional().getId() + ", "
                + "tanggal_transaksi='" + object.getTanggalTransaksi() + "', "
                + "jenis_mobil='" + object.getJenisMobil() + "', "
                + "plat_nomor='" + object.getPlatNomor() + "', "
                + "total_harga=" + object.getTotalHarga() + " "
                + "WHERE id_transaksi=" + object.getId() + "";
        conMan = new ConnectionManager();
        conn = conMan.connect();

        try {
            stmt = conn.createStatement();
            result = stmt.executeUpdate(sql);
            conMan.disconnect();
        } catch (SQLException ex) {
            Logger.getLogger(TransaksiServiceImpl.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public Transaksi findById(int id) {
        Transaksi transaksi = null;
        Pegawai pegawai = null;
        Member member = null;
        Pencucian pencucian = null;
        Additional additional = null;

        String sql = "SELECT t.id_transaksi,peg.id_pegawai,peg.nama_pegawai,mem.id_member,mem.nama_member,"
                + "t.tanggal_transaksi,pen.id_pencucian,pen.jenis,addi.id_additional,addi.namaAdd,t.jenis_mobil,t.plat_nomor,"
                + "t.total_harga "
                + "FROM transaksi t, pegawai peg, member mem, pencucian pen, additional addi "
                + "WHERE t.id_pegawai = peg.id_pegawai AND t.id_member = mem.id_member AND "
                + "t.id_pencucian = pen.id_pencucian AND t.id_additional = addi.id_additional "
                + "AND t.id_transaksi=" + id + "";

        conMan = new ConnectionManager();
        conn = conMan.connect();

        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                transaksi = new Transaksi();

                transaksi.setId(rs.getInt("id_transaksi"));
                transaksi.setTanggalTransaksi(rs.getString("tanggal_transaksi"));
                transaksi.setJenisMobil(rs.getString("jenis_mobil"));
                transaksi.setPlatNomor(rs.getString("plat_nomor"));
                transaksi.setTotalHarga(Double.parseDouble(rs.getString("total_harga")));

                pegawai = new Pegawai();
                pegawai.setId(rs.getInt("id_pegawai"));
                pegawai.setNama(rs.getString("nama_pegawai"));

                member = new Member();
                member.setId(rs.getInt("id_member"));
                member.setNama(rs.getString("nama_member"));

                pencucian = new Pencucian();
                pencucian.setId(rs.getInt("id_pencucian"));
                pencucian.setJenis(rs.getString("jenis"));

                additional = new Additional();
                additional.setId(rs.getInt("id_additional"));
                additional.setNamaAdd(rs.getString("namaAdd"));

                transaksi.setPegawai(pegawai);
                transaksi.setMember(member);
                transaksi.setAdditional(additional);
                transaksi.setPencucian(pencucian);

            }
            conMan.disconnect();
        } catch (SQLException e) {
            Logger.getLogger(TransaksiServiceImpl.class.getName())
                    .log(Level.SEVERE, null, e);
        }

        return transaksi;
    }

    @Override
    public Integer delete(int id) {
        int result = 0;
        String sql = "DELETE FROM transaksi WHERE id_transaksi=" + id + "";

        conMan = new ConnectionManager();
        conn = conMan.connect();

        try {
            stmt = conn.createStatement();
            result = stmt.executeUpdate(sql);
            conMan.disconnect();
        } catch (SQLException ex) {
            Logger.getLogger(TransaksiServiceImpl.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
        return result;
    }

}
