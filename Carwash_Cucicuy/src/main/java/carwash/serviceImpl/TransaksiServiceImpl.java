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

        String sql = "SELECT t.id_transaksi,peg.id,peg.nama,mem.id,mem.nama,mem.alamat,mem.noHp,"
                + "t.tanggal_transaksi,t.jenis_mobil,t.plat_nomor,pen.id,pen.jenis,add.id,add.namaAdd,"
                + "t.total_harga "
                + "FROM transaksi t, pegawai peg, member mem, pencucian pen, additional add"
                + "WHERE t.id_pegawai = peg.id AND t.id_member = mem.id AND"
                + "t.id_pencucian = pen.id AND t.id_additional = add.id";

        conMan = new ConnectionManager();
        conn = conMan.connect();

        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                transaksi = new Transaksi();

                transaksi.setId(rs.getInt("id"));
                transaksi.setTanggalTransaksi(rs.getString("tanggal_transaksi"));
                transaksi.setJenisMobil(rs.getString("jenis_mobil"));
                transaksi.setPlatNomor(rs.getString("plat_nomor"));
                transaksi.setTotalHarga(totalharga(pencucian, additional));

                pegawai = new Pegawai();
                pegawai.setId(rs.getInt("id"));
                pegawai.setNama(rs.getString("nama"));

                member = new Member();
                member.setId(rs.getInt("id"));
                member.setNama(rs.getString("nama"));
                member.setAlamat(rs.getString("alamat"));
                member.setNoHp(rs.getString("noHp"));

                pencucian = new Pencucian();
                pencucian.setId(rs.getInt("id"));
                pencucian.setJenis(rs.getString("jenis"));

                additional = new Additional();
                additional.setId(rs.getInt("id"));
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
        String sql = "INSERT INTO transaksi VALUES"
                + "('" + object.getPegawai().getId() + "', "
                + "'" + object.getMember().getId() + "', "
                + "'" + object.getPencucian().getId() + "', "
                + "'" + object.getAdditional().getId() + "', "
                + "'" + object.getTanggalTransaksi() + "', "
                + "'" + object.getJenisMobil() + "', "
                + "'" + object.getPlatNomor() + "', "
                + "" + totalharga(pencucian, additional) + ")";

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
        String sql = "UPDATE transaksi SET id_pegawai='" + object.getPegawai().getId() + "', "
                + "id_member='" + object.getMember().getId() + "', "
                + "id_pencucian='" + object.getPencucian().getId() + "', "
                + "id_additional='" + object.getAdditional().getId() + "', "
                + "tanggal_transaksi='" + object.getTanggalTransaksi() + "', "
                + "jenis_mobil='" + object.getJenisMobil() + "', "
                + "plat_nomor='" + object.getPlatNomor() + "', "
                + "total_harga=" + totalharga(pencucian, additional) + " "
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

        String sql = "SELECT t.id_transaksi,peg.id,peg.nama,mem.id,mem.nama,mem.alamat,mem.noHp,"
                + "t.tanggal_transaksi,t.jenis_mobil,t.plat_nomor,pen.id,pen.jenis,add.id,add.namaAdd,"
                + "pen.harga + add.harga as t.total_harga "
                + "FROM transaksi t, pegawai peg, member mem, pencucian pen, additional add"
                + "WHERE t.id_pegawai = peg.id AND t.id_member = mem.id AND"
                + "t.id_pencucian = pen.id AND t.id_additional = add.id"
                + "AND t.id_transaksi=" + id + "";

        conMan = new ConnectionManager();
        conn = conMan.connect();

        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                transaksi = new Transaksi();

                transaksi.setId(rs.getInt("id"));
                transaksi.setTanggalTransaksi(rs.getString("tanggal_transaksi"));
                transaksi.setJenisMobil(rs.getString("jenis_mobil"));
                transaksi.setPlatNomor(rs.getString("plat_nomor"));
                transaksi.setTotalHarga(totalharga(pencucian, additional));

                pegawai = new Pegawai();
                pegawai.setId(rs.getInt("id"));
                pegawai.setNama(rs.getString("nama"));

                member = new Member();
                member.setId(rs.getInt("id"));
                member.setNama(rs.getString("nama"));
                member.setAlamat(rs.getString("alamat"));
                member.setNoHp(rs.getString("noHp"));

                pencucian = new Pencucian();
                pencucian.setId(rs.getInt("id"));
                pencucian.setJenis(rs.getString("jenis"));

                additional = new Additional();
                additional.setId(rs.getInt("id"));
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

    public int totalharga(Pencucian pencucian, Additional additional) {
        int totalHarga = 0;
        pencucian = new Pencucian();
        additional = new Additional();

        totalHarga = pencucian.getHarga() + additional.getHarga();

        return totalHarga;
    }
}
