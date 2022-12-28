/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package carwash.serviceImpl;

import carwash.pojo.Member;
import carwash.service.MemberService;
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
public class MemberServiceImpl implements MemberService{
    private ConnectionManager conMan;
    private Connection conn;
    Statement stmt;
    ResultSet rs;

    @Override
    public List<Member> findAll() {
        List<Member> listMember = new ArrayList<>();
        String sql = "SELECT * FROM member";
        
        conMan = new ConnectionManager();
        conn = conMan.connect();
        
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                Member member = new Member();
                member.setId(rs.getInt("id"));
                member.setNama(rs.getString("nama"));
                member.setAlamat(rs.getString("alamat"));
                member.setNoHp(rs.getString("noHp"));
                member.setTanggalDaftar(rs.getString("tanggal_daftar"));
                member.setMasaAktif(rs.getString("masa_aktif"));
                
                listMember.add(member);
            }
            conMan.disconnect();
        } catch (SQLException e) {
            Logger.getLogger(MemberServiceImpl.class.getName())
                    .log(Level.SEVERE, null, e);
        }
        
        return listMember;
    }

    @Override
    public Integer create(Member object) {
        int result = 0;
        String sql = "INSERT INTO member VALUES"
                + "('" + object.getNama() + "', "
                + "'" + object.getAlamat() + "', "
                + "'" + object.getNoHp() + "', "
                + "'" + object.getTanggalDaftar() + "', "
                + "'" + object.getMasaAktif() + "')";
        
        conMan = new ConnectionManager();
        conn = conMan.connect();
        
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
            conMan.disconnect();
        } catch (SQLException e) {
            Logger.getLogger(MemberServiceImpl.class.getName())
                    .log(Level.SEVERE, null, e);
        }
        return result;
    }

    @Override
    public Integer update(Member object) {
        int result = 0;
        String sql = "UPDATE member SET nama ='" + object.getNama() + "', "
                + "alamat='" + object.getAlamat() + "', "
                + "noHp='" + object.getNoHp()+ "', "
                + "tanggal_daftar='" + object.getTanggalDaftar()+ "', "
                + "masa_aktif='" + object.getMasaAktif()+ "'"
                + "WHERE id=" + object.getId() + "";
        
        conMan = new ConnectionManager();
        conn = conMan.connect();
        
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
            conMan.disconnect();
        } catch (SQLException e) {
            Logger.getLogger(MemberServiceImpl.class.getName())
                    .log(Level.SEVERE, null, e);
        }
        return result;
    }

    @Override
    public Member findById(int id) {
        Member member = null;
        String sql = "SELECT * FROM member WHERE id=" + id + "";
        
        conMan = new ConnectionManager();
        conn = conMan.connect();
        
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                member = new Member();
                member.setId(rs.getInt("id"));
                member.setNama(rs.getString("nama"));
                member.setAlamat(rs.getString("alamat"));
                member.setNoHp(rs.getString("noHp"));
                member.setTanggalDaftar(rs.getString("tanggal_daftar"));
                member.setMasaAktif(rs.getString("masa_aktif"));
            }
            conMan.disconnect();
        } catch (SQLException e) {
            Logger.getLogger(MemberServiceImpl.class.getName())
                    .log(Level.SEVERE, null, e);
        }
        return member;
    }

    @Override
    public Integer delete(int id) {
        int result = 0;
        String sql = "DELETE FROM member WHERE id=" + id + "";
        
        conMan = new ConnectionManager();
        conn = conMan.connect();
        
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
            conMan.disconnect();
        } catch (SQLException e) {
            Logger.getLogger(MemberServiceImpl.class.getName())
                    .log(Level.SEVERE, null, e);
        }
        return result;
    }
}
