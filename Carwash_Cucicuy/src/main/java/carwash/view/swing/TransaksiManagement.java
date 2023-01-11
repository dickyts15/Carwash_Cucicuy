/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package carwash.view.swing;

import carwash.pojo.Additional;
import carwash.pojo.Admin;
import carwash.pojo.Member;
import carwash.pojo.Pegawai;
import carwash.pojo.Pencucian;
import carwash.pojo.Transaksi;
import carwash.service.AdditionalService;
import carwash.service.MemberService;
import carwash.service.PegawaiService;
import carwash.service.PencucianService;
import carwash.service.TransaksiService;
import carwash.serviceImpl.AdditionalServiceImpl;
import carwash.serviceImpl.PencucianServiceImpl;
import carwash.serviceImpl.TransaksiServiceImpl;
import carwash.utilities.ConnectionManager;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.print.PrinterException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTable;

/**
 *
 * @author ASUS
 */
public class TransaksiManagement extends javax.swing.JFrame {

    private ConnectionManager conMan;
    private Connection conn;
    Statement stmt;
    ResultSet rs;
    /**
     * Creates new form TransaksiManagement
     */
    TransaksiService transaksiService;
    PencucianService pencucianService;
    AdditionalService additionalService;
    MemberService memberService;
    PegawaiService pegawaiService;

    public TransaksiManagement() {
        initComponents();
        this.setLocationRelativeTo(null);
        listJenisPencucian();
        listJenisAdditional();
        loadData();
    }

    public void close() {
        WindowEvent we = new WindowEvent(this, WindowEvent.WINDOW_CLOSING);
        Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(we);
    }

    private void emptyField() {
        txtIdTransaksi.setText("");
        txtIdPegawai.setText("");
        txtIdMember.setText("");
        CBoxPencucian.setSelectedIndex(0);
        CBoxAdditional.setSelectedIndex(0);
        tanggalChooser.setCalendar(null);
        txtJenisMobil.setText("");
        txtPlatNomor.setText("");
        txtHarga.setText("");
    }

    private void loadData() {
        transaksiService = new TransaksiServiceImpl();
        List<Transaksi> listTransaksi;
        listTransaksi = transaksiService.findAll();
        Object[][] objectTransaksi = new Object[listTransaksi.size()][9];

        int counter = 0;

        for (Transaksi transaksi : listTransaksi) {
            objectTransaksi[counter][0] = transaksi.getId();
            objectTransaksi[counter][1] = transaksi.getPegawai().getNama();
            objectTransaksi[counter][2] = transaksi.getMember().getNama();
            objectTransaksi[counter][3] = transaksi.getTanggalTransaksi();
            objectTransaksi[counter][4] = transaksi.getPencucian().getJenis();
            objectTransaksi[counter][5] = transaksi.getAdditional().getNamaAdd();
            objectTransaksi[counter][6] = transaksi.getJenisMobil();
            objectTransaksi[counter][7] = transaksi.getPlatNomor();
            objectTransaksi[counter][8] = transaksi.getTotalHarga();
            counter++;
        }

        tblTransaksi.setModel(new javax.swing.table.DefaultTableModel(
                objectTransaksi,
                new String[]{
                    "ID Transaksi", "Nama Pegawai", "Nama Member", "Tanggal", "Jenis Pencucian", "Nama Additional", "Jenis Mobil", "Plat Nomor", "Total Harga"
                }
        ));
    }

    private void loadData(Transaksi transaksi) {
        Object[][] objectTransaksi = new Object[1][9];

        objectTransaksi[0][0] = transaksi.getId();
        objectTransaksi[0][1] = transaksi.getPegawai().getNama();
        objectTransaksi[0][2] = transaksi.getMember().getNama();
        objectTransaksi[0][3] = transaksi.getTanggalTransaksi();
        objectTransaksi[0][4] = transaksi.getPencucian().getJenis();
        objectTransaksi[0][5] = transaksi.getAdditional().getNamaAdd();
        objectTransaksi[0][6] = transaksi.getJenisMobil();
        objectTransaksi[0][7] = transaksi.getPlatNomor();
        objectTransaksi[0][8] = transaksi.getTotalHarga();

        tblTransaksi.setModel(new javax.swing.table.DefaultTableModel(
                objectTransaksi,
                new String[]{
                    "ID Transaksi", "Nama Pegawai", "Nama Member", "Tanggal", "Jenis Pencucian", "Nama Additional", "Jenis Mobil", "Plat Nomor", "Total Harga"
                }
        ));
    }

    private Transaksi findTransaksi(int id) {
        Transaksi transaksi = new Transaksi();
        transaksiService = new TransaksiServiceImpl();
        transaksi = transaksiService.findById(id);

        return transaksi;
    }

    public double totalharga(int idTransaksi, int idPen, int idAdd) {
        Member member = null;
        Pencucian pencucian = null;
        Additional additional = null;
        int result = 0;
        double totalHarga = 0;
        String sql = "SELECT mem.id_member, pen.harga_pencucian, addi.harga_additional "
                + "FROM transaksi t, member mem, pencucian pen, additional addi "
                + "WHERE t.id_transaksi = " + idTransaksi + " "
                + "AND t.id_member = mem.id_member "
                + "AND pen.id_pencucian =" + idPen + " "
                + "AND addi.id_additional = " + idAdd + "";

        conMan = new ConnectionManager();
        conn = conMan.connect();

        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                member = new Member();
                member.setId(rs.getInt("id_member"));

                if (member.getId() == 0) {
                    pencucian = new Pencucian();
                    pencucian.setHarga(rs.getInt("harga_pencucian"));

                    additional = new Additional();
                    additional.setHarga(rs.getInt("harga_additional"));
                } else {
                    pencucian = new Pencucian();
                    pencucian.setHarga(rs.getDouble("harga_pencucian") * 0.9);

                    additional = new Additional();
                    additional.setHarga(rs.getDouble("harga_additional") * 0.9);
                }

            }

            conMan.disconnect();
        } catch (SQLException ex) {
            Logger.getLogger(TransaksiServiceImpl.class.getName())
                    .log(Level.SEVERE, null, ex);
        }

        totalHarga = pencucian.getHarga() + additional.getHarga();
        return totalHarga;
    }

    public void listJenisPencucian() {
        pencucianService = new PencucianServiceImpl();
        List<Pencucian> listJenis = new ArrayList<>();
        listJenis = pencucianService.findAll();
        CBoxPencucian.removeAllItems();

        for (Pencucian pencucian : listJenis) {
            CBoxPencucian.addItem(pencucian.getJenis());
        }
    }

    public void listJenisAdditional() {
        additionalService = new AdditionalServiceImpl();
        List<Additional> listNamaAdd = new ArrayList<>();
        listNamaAdd = additionalService.findAll();
        CBoxAdditional.removeAllItems();

        for (Additional additional : listNamaAdd) {
            CBoxAdditional.addItem(additional.getNamaAdd());
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblTransaksi = new javax.swing.JTable();
        lbl_idPegawai = new javax.swing.JLabel();
        txtIdPegawai = new javax.swing.JTextField();
        lbl_idMember = new javax.swing.JLabel();
        txtIdMember = new javax.swing.JTextField();
        lbl_Pencucian = new javax.swing.JLabel();
        lbl_Additional = new javax.swing.JLabel();
        CBoxPencucian = new javax.swing.JComboBox<>();
        CBoxAdditional = new javax.swing.JComboBox<>();
        lbl_Tanggal = new javax.swing.JLabel();
        lbl_jenisMobil = new javax.swing.JLabel();
        lbl_platNomor = new javax.swing.JLabel();
        txtJenisMobil = new javax.swing.JTextField();
        txtPlatNomor = new javax.swing.JTextField();
        btnClear = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();
        txtSearchById = new javax.swing.JTextField();
        btnSearch = new javax.swing.JButton();
        btnRefresh = new javax.swing.JButton();
        lbl_idPegawai1 = new javax.swing.JLabel();
        txtIdTransaksi = new javax.swing.JTextField();
        lbl_platNomor1 = new javax.swing.JLabel();
        txtHarga = new javax.swing.JTextField();
        tanggalChooser = new com.toedter.calendar.JDateChooser();
        printButton = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        lbl_Transaksi = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        backToMenuButton = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("TransaksiManagement");

        jPanel1.setBackground(new java.awt.Color(153, 204, 255));

        tblTransaksi.setAutoCreateRowSorter(true);
        tblTransaksi.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID Transaksi", "Nama Pegawai", "Nama Member", "Tanggal", "Jenis Pencucian", "Nama Additional", "Jenis Mobil", "Plat Nomor", "Total Harga"
            }
        ));
        tblTransaksi.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblTransaksiMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblTransaksi);

        lbl_idPegawai.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        lbl_idPegawai.setForeground(new java.awt.Color(0, 0, 0));
        lbl_idPegawai.setText("ID Pegawai :");

        txtIdPegawai.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        txtIdPegawai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIdPegawaiActionPerformed(evt);
            }
        });

        lbl_idMember.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        lbl_idMember.setForeground(new java.awt.Color(0, 0, 0));
        lbl_idMember.setText("ID Member :");

        txtIdMember.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        txtIdMember.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIdMemberActionPerformed(evt);
            }
        });

        lbl_Pencucian.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        lbl_Pencucian.setForeground(new java.awt.Color(0, 0, 0));
        lbl_Pencucian.setText("Pencucian :");

        lbl_Additional.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        lbl_Additional.setForeground(new java.awt.Color(0, 0, 0));
        lbl_Additional.setText("Additional :");

        CBoxPencucian.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N

        CBoxAdditional.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N

        lbl_Tanggal.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        lbl_Tanggal.setForeground(new java.awt.Color(0, 0, 0));
        lbl_Tanggal.setText("Tanggal :");

        lbl_jenisMobil.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        lbl_jenisMobil.setForeground(new java.awt.Color(0, 0, 0));
        lbl_jenisMobil.setText("Jenis Mobil :");

        lbl_platNomor.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        lbl_platNomor.setForeground(new java.awt.Color(0, 0, 0));
        lbl_platNomor.setText("Plat Nomor :");

        txtJenisMobil.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        txtJenisMobil.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtJenisMobilMouseClicked(evt);
            }
        });
        txtJenisMobil.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtJenisMobilActionPerformed(evt);
            }
        });

        txtPlatNomor.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        txtPlatNomor.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtPlatNomorMouseClicked(evt);
            }
        });
        txtPlatNomor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPlatNomorActionPerformed(evt);
            }
        });

        btnClear.setBackground(new java.awt.Color(204, 204, 204));
        btnClear.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        btnClear.setForeground(new java.awt.Color(0, 0, 0));
        btnClear.setText("Clear");
        btnClear.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });

        btnDelete.setBackground(new java.awt.Color(204, 204, 204));
        btnDelete.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        btnDelete.setForeground(new java.awt.Color(0, 0, 0));
        btnDelete.setText("Delete");
        btnDelete.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        btnUpdate.setBackground(new java.awt.Color(204, 204, 204));
        btnUpdate.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        btnUpdate.setForeground(new java.awt.Color(0, 0, 0));
        btnUpdate.setText("Update");
        btnUpdate.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });

        btnSave.setBackground(new java.awt.Color(204, 204, 204));
        btnSave.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        btnSave.setForeground(new java.awt.Color(0, 0, 0));
        btnSave.setText("Save");
        btnSave.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        txtSearchById.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        txtSearchById.setText("Search by ID");
        txtSearchById.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtSearchByIdMouseClicked(evt);
            }
        });
        txtSearchById.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSearchByIdActionPerformed(evt);
            }
        });

        btnSearch.setBackground(new java.awt.Color(204, 204, 204));
        btnSearch.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        btnSearch.setForeground(new java.awt.Color(0, 0, 0));
        btnSearch.setText("Search");
        btnSearch.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });

        btnRefresh.setBackground(new java.awt.Color(204, 204, 204));
        btnRefresh.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        btnRefresh.setForeground(new java.awt.Color(0, 0, 0));
        btnRefresh.setText("Refresh");
        btnRefresh.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshActionPerformed(evt);
            }
        });

        lbl_idPegawai1.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        lbl_idPegawai1.setForeground(new java.awt.Color(0, 0, 0));
        lbl_idPegawai1.setText("ID Transaksi :");

        txtIdTransaksi.setEditable(false);
        txtIdTransaksi.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        txtIdTransaksi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIdTransaksiActionPerformed(evt);
            }
        });

        lbl_platNomor1.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        lbl_platNomor1.setForeground(new java.awt.Color(0, 0, 0));
        lbl_platNomor1.setText("Total Harga :");

        txtHarga.setEditable(false);
        txtHarga.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        txtHarga.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtHargaMouseClicked(evt);
            }
        });
        txtHarga.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtHargaActionPerformed(evt);
            }
        });

        tanggalChooser.setDateFormatString("y-MM-dd");

        printButton.setBackground(new java.awt.Color(204, 204, 204));
        printButton.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        printButton.setForeground(new java.awt.Color(0, 0, 0));
        printButton.setText("PRINT");
        printButton.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        printButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                    .addComponent(lbl_idPegawai)
                                    .addGap(29, 29, 29))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(lbl_Additional)
                                    .addGap(39, 39, 39)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(lbl_Pencucian)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(CBoxPencucian, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(CBoxAdditional, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(109, 109, 109))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(txtIdPegawai)
                                .addGap(108, 108, 108)))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(lbl_Tanggal)
                                    .addComponent(lbl_platNomor))
                                .addGap(64, 64, 64)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtPlatNomor, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
                                    .addComponent(tanggalChooser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(lbl_platNomor1)
                                .addGap(64, 64, 64)
                                .addComponent(txtHarga, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(lbl_idPegawai1)
                        .addGap(21, 21, 21)
                        .addComponent(txtIdTransaksi, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 556, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(lbl_idMember)
                        .addGap(29, 29, 29)
                        .addComponent(txtIdMember)
                        .addGap(109, 109, 109)
                        .addComponent(lbl_jenisMobil)
                        .addGap(65, 65, 65)
                        .addComponent(txtJenisMobil, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(txtSearchById, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(29, 29, 29)
                        .addComponent(btnRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnClear, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(printButton, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_idPegawai1)
                    .addComponent(txtIdTransaksi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(lbl_idPegawai)
                                .addComponent(txtIdPegawai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lbl_Tanggal))
                            .addComponent(tanggalChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lbl_idMember)
                            .addComponent(txtIdMember, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbl_jenisMobil)
                            .addComponent(txtJenisMobil, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(72, 72, 72)
                                .addComponent(lbl_Additional))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(CBoxPencucian, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lbl_Pencucian)
                                    .addComponent(lbl_platNomor)
                                    .addComponent(txtPlatNomor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(63, 63, 63)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lbl_platNomor1)
                                        .addComponent(txtHarga, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(CBoxAdditional, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(21, 21, 21)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtSearchById, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btnRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(printButton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                        .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnClear, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel2.setBackground(new java.awt.Color(130, 170, 227));

        lbl_Transaksi.setFont(new java.awt.Font("Times New Roman", 1, 36)); // NOI18N
        lbl_Transaksi.setForeground(new java.awt.Color(0, 0, 0));
        lbl_Transaksi.setText("MANAGE TRANSAKSI");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(160, 160, 160)
                .addComponent(lbl_Transaksi)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbl_Transaksi)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jMenu1.setText("File");

        backToMenuButton.setText("Back to Dashboard");
        backToMenuButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backToMenuButtonActionPerformed(evt);
            }
        });
        jMenu1.add(backToMenuButton);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtIdPegawaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIdPegawaiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIdPegawaiActionPerformed

    private void txtIdMemberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIdMemberActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIdMemberActionPerformed

    private void txtJenisMobilMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtJenisMobilMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_txtJenisMobilMouseClicked

    private void txtJenisMobilActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtJenisMobilActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtJenisMobilActionPerformed

    private void txtPlatNomorMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtPlatNomorMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPlatNomorMouseClicked

    private void txtPlatNomorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPlatNomorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPlatNomorActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        String tanggalTransaksi, jenisMobil, platNomor;
        int idPegawai, idMember, idPencucian, idAdditional;
        transaksiService = new TransaksiServiceImpl();
        pencucianService = new PencucianServiceImpl();
        idPegawai = Integer.parseInt(txtIdPegawai.getText());
        idMember = Integer.parseInt(txtIdMember.getText());
        idPencucian = CBoxPencucian.getSelectedIndex() + 1;
        idAdditional = CBoxAdditional.getSelectedIndex();
        Date date = tanggalChooser.getDate();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        tanggalTransaksi = dateFormat.format(date);
        jenisMobil = txtJenisMobil.getText();
        platNomor = txtPlatNomor.getText();

        Transaksi transaksi = new Transaksi();
        Pegawai pegawai = new Pegawai();
        Member member = new Member();
        Pencucian pencucian = new Pencucian();
        Additional additional = new Additional();
        transaksi.setTanggalTransaksi(tanggalTransaksi);
        transaksi.setJenisMobil(jenisMobil);
        transaksi.setPlatNomor(platNomor);
        transaksi.setTotalHarga(totalharga(transaksi.getId(), idPencucian, idAdditional));
        pegawai.setId(idPegawai);
        member.setId(idMember);
        pencucian.setId(idPencucian);
        additional.setId(idAdditional);

        transaksi.setPegawai(pegawai);
        transaksi.setMember(member);
        transaksi.setPencucian(pencucian);
        transaksi.setAdditional(additional);

        transaksiService.create(transaksi);
        JOptionPane.showMessageDialog(null, "Data Transaksi berhasil ditambahkan ...");
        loadData();
        emptyField();

    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        String tanggalTransaksi, jenisMobil, platNomor;
        int idTransaksi, idPegawai, idMember, idPencucian, idAdditional;
        transaksiService = new TransaksiServiceImpl();
        pencucianService = new PencucianServiceImpl();
        idTransaksi = Integer.parseInt(txtIdTransaksi.getText());
        idPegawai = Integer.parseInt(txtIdPegawai.getText());
        idMember = Integer.parseInt(txtIdMember.getText());
        idPencucian = CBoxPencucian.getSelectedIndex() + 1;
        idAdditional = CBoxAdditional.getSelectedIndex();
        Date date = tanggalChooser.getDate();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        tanggalTransaksi = dateFormat.format(date);
        jenisMobil = txtJenisMobil.getText();
        platNomor = txtPlatNomor.getText();

        Transaksi transaksi = new Transaksi();
        Pegawai pegawai = new Pegawai();
        Member member = new Member();
        Pencucian pencucian = new Pencucian();
        Additional additional = new Additional();
        transaksi.setId(idTransaksi);
        transaksi.setTanggalTransaksi(tanggalTransaksi);
        transaksi.setJenisMobil(jenisMobil);
        transaksi.setPlatNomor(platNomor);
        transaksi.setTotalHarga(totalharga(idTransaksi, idPencucian, idAdditional));
        pegawai.setId(idPegawai);
        member.setId(idMember);
        pencucian.setId(idPencucian);
        additional.setId(idAdditional);

        transaksi.setPegawai(pegawai);
        transaksi.setMember(member);
        transaksi.setPencucian(pencucian);
        transaksi.setAdditional(additional);

        transaksiService.update(transaksi);
        JOptionPane.showMessageDialog(null, "Data Transaksi berhasil diupdate ...");
        loadData();
        emptyField();
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        int id;
        transaksiService = new TransaksiServiceImpl();
        int dialogButton = JOptionPane.YES_NO_OPTION;

        id = Integer.parseInt(txtIdTransaksi.getText());

        int dialogResult = JOptionPane.showConfirmDialog(null, "Yakin ingin menghapus?", "Warning", dialogButton);
        if (dialogResult == JOptionPane.YES_OPTION) {
            transaksiService.delete(id);
            loadData();
            emptyField();
        }
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        emptyField();
    }//GEN-LAST:event_btnClearActionPerformed

    private void txtSearchByIdMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtSearchByIdMouseClicked
        txtSearchById.setText("");
    }//GEN-LAST:event_txtSearchByIdMouseClicked

    private void txtSearchByIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSearchByIdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSearchByIdActionPerformed

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        int id;
        Transaksi searchedTransaksi = new Transaksi();

        id = Integer.parseInt(txtSearchById.getText());
        searchedTransaksi = findTransaksi(id);
        if (searchedTransaksi != null) {
            loadData(searchedTransaksi);
        } else {
            JOptionPane.showMessageDialog(null, "Data Tidak Ditemukan.");
        }
    }//GEN-LAST:event_btnSearchActionPerformed

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
        loadData();
    }//GEN-LAST:event_btnRefreshActionPerformed

    private void txtIdTransaksiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIdTransaksiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIdTransaksiActionPerformed

    private void txtHargaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtHargaMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_txtHargaMouseClicked

    private void txtHargaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtHargaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtHargaActionPerformed

    private void tblTransaksiMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblTransaksiMouseClicked

        Pegawai pegawai = null;
        Member member = null;
        Pencucian pencucian = null;
        Additional additional = null;

        String tanggal, jenisMobil, platNomor;
        int idTransaksi;
        double totalHarga = 0;
        Date date = null;
        int row = tblTransaksi.getSelectedRow();
        idTransaksi = Integer.parseInt(tblTransaksi.getValueAt(row, 0).toString());
        String query = "SELECT peg.id_pegawai, mem.id_member, pen.id_pencucian, addi.id_additional "
                + "FROM transaksi t, pegawai peg, member mem, pencucian pen, additional addi "
                + "WHERE t.id_pegawai = peg.id_pegawai AND t.id_member = mem.id_member "
                + "AND t.id_pencucian = pen.id_pencucian AND t.id_additional = addi.id_additional "
                + "AND t.id_transaksi=" + idTransaksi + "";

        conMan = new ConnectionManager();
        conn = conMan.connect();

        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);

            while (rs.next()) {
                pegawai = new Pegawai();
                pegawai.setId(rs.getInt("id_pegawai"));

                member = new Member();
                member.setId(rs.getInt("id_member"));

                pencucian = new Pencucian();
                pencucian.setId(rs.getInt("id_pencucian"));

                additional = new Additional();
                additional.setId(rs.getInt("id_additional"));
            }

            conMan.disconnect();
        } catch (SQLException ex) {
            Logger.getLogger(TransaksiServiceImpl.class.getName())
                    .log(Level.SEVERE, null, ex);
        }

        tanggal = tblTransaksi.getValueAt(row, 3).toString();
        try {
            date = new SimpleDateFormat("y-MM-dd").parse(tanggal);
        } catch (ParseException e) {
            Logger.getLogger(TransaksiManagement.class.getName())
                    .log(Level.SEVERE, null, e);
        }
        jenisMobil = tblTransaksi.getValueAt(row, 6).toString();
        platNomor = tblTransaksi.getValueAt(row, 7).toString();
        totalHarga = Double.parseDouble(tblTransaksi.getValueAt(row, 8).toString());

        txtIdTransaksi.setText(idTransaksi + "");
        txtIdPegawai.setText(pegawai.getId() + "");
        txtIdMember.setText(member.getId() + "");
        tanggalChooser.setDate(date);
        CBoxPencucian.setSelectedIndex(pencucian.getId() - 1);
        CBoxAdditional.setSelectedIndex(additional.getId());
        txtJenisMobil.setText(jenisMobil);
        txtPlatNomor.setText(platNomor);
        txtHarga.setText(totalHarga + "");

    }//GEN-LAST:event_tblTransaksiMouseClicked

    private void backToMenuButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backToMenuButtonActionPerformed
        int dialogButton = JOptionPane.YES_NO_OPTION;

        int dialogResult = JOptionPane.showConfirmDialog(null, "Yakin Kembali Ke Dashboard?", "Pesan", dialogButton);
        if (dialogResult == JOptionPane.YES_OPTION) {
            Dashboard dashboard = new Dashboard();
            dashboard.setVisible(true);
            close();
        }
    }//GEN-LAST:event_backToMenuButtonActionPerformed

    private void printButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printButtonActionPerformed
        MessageFormat header = new MessageFormat("Daftar Transaksi");
        MessageFormat footer = new MessageFormat("Page {0, number, integer}");
        try {
            tblTransaksi.print(JTable.PrintMode.FIT_WIDTH, header, footer);
        } catch (PrinterException e) {
            System.out.println("Error: " + e);
        }
    }//GEN-LAST:event_printButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(TransaksiManagement.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TransaksiManagement.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TransaksiManagement.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TransaksiManagement.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TransaksiManagement().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> CBoxAdditional;
    private javax.swing.JComboBox<String> CBoxPencucian;
    private javax.swing.JMenuItem backToMenuButton;
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton btnSearch;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lbl_Additional;
    private javax.swing.JLabel lbl_Pencucian;
    private javax.swing.JLabel lbl_Tanggal;
    private javax.swing.JLabel lbl_Transaksi;
    private javax.swing.JLabel lbl_idMember;
    private javax.swing.JLabel lbl_idPegawai;
    private javax.swing.JLabel lbl_idPegawai1;
    private javax.swing.JLabel lbl_jenisMobil;
    private javax.swing.JLabel lbl_platNomor;
    private javax.swing.JLabel lbl_platNomor1;
    private javax.swing.JButton printButton;
    private com.toedter.calendar.JDateChooser tanggalChooser;
    private javax.swing.JTable tblTransaksi;
    private javax.swing.JTextField txtHarga;
    private javax.swing.JTextField txtIdMember;
    private javax.swing.JTextField txtIdPegawai;
    private javax.swing.JTextField txtIdTransaksi;
    private javax.swing.JTextField txtJenisMobil;
    private javax.swing.JTextField txtPlatNomor;
    private javax.swing.JTextField txtSearchById;
    // End of variables declaration//GEN-END:variables
}
