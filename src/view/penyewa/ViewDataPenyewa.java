package view.penyewa;

import controller.*;
import model.kamar.ModelKamar;
import model.penyewa.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class ViewDataPenyewa extends JPanel {

    private ControllerPenyewa controller;
    private ControllerKamar   ctrlKamar;
    private ModelTablePenyewa tableModel;
    private JTable tabel;

    private JTextField txtNama, txtNik, txtNoHp, txtEmail, txtAlamat, txtPekerjaan, txtCari;
    private JComboBox<ModelKamar> cbKamar;
    private JSpinner spDurasi;
    private JLabel lblHarga, lblTotal;
    private JButton btnTambah, btnUpdate, btnHapus, btnBersih, btnCari;
    private int idTerpilih = -1;
    private Runnable onBack;
    private Runnable onPenyewaChanged; // callback untuk refresh tab transaksi

    private final Color PRIMARY = new Color(30, 41, 59);
    private final Color ACCENT  = new Color(16, 185, 129);
    private final Color BG      = new Color(248, 250, 252);
    private final Color BORDER  = new Color(203, 213, 225);

    public ViewDataPenyewa() {
        controller = new ControllerPenyewa();
        ctrlKamar  = new ControllerKamar();
        setLayout(new BorderLayout(10, 10));
        setBackground(BG);
        setBorder(new EmptyBorder(15, 15, 15, 15));
        initComponents();
        loadData();
    }

    public void setOnBack(Runnable r) { this.onBack = r; }
    /** Dipanggil saat penyewa ditambah agar tab Transaksi ikut refresh */
    public void setOnPenyewaChanged(Runnable r) { this.onPenyewaChanged = r; }

    /** Refresh dropdown kamar (dipanggil dari Main saat navigasi ke halaman ini) */
    public void refreshKamar() { muatKamar(); }

    private void initComponents() {
        // ---- HEADER ----
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BG);
        header.setBorder(new EmptyBorder(0, 0, 4, 0));

        JButton btnBack = new JButton("←");
        btnBack.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnBack.setForeground(new Color(100, 116, 139));
        btnBack.setBackground(BG);
        btnBack.setBorderPainted(false); btnBack.setFocusPainted(false);
        btnBack.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnBack.addActionListener(e -> { if (onBack != null) onBack.run(); });

        JLabel lblHeader = new JLabel("Data Penyewa Kos");
        lblHeader.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblHeader.setForeground(PRIMARY);
        header.add(btnBack, BorderLayout.WEST);
        header.add(lblHeader, BorderLayout.CENTER);

        // ---- FORM ----
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER), new EmptyBorder(12, 15, 12, 15)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Row 0: Nama & NIK
        gbc.gridx = 0; gbc.gridy = 0; formPanel.add(lbl("Nama Lengkap:"), gbc);
        gbc.gridx = 1; txtNama = field(); formPanel.add(txtNama, gbc);
        gbc.gridx = 2; formPanel.add(lbl("NIK (KTP):"), gbc);
        gbc.gridx = 3; txtNik = field(); formPanel.add(txtNik, gbc);

        // Row 1: No HP & Email
        gbc.gridx = 0; gbc.gridy = 1; formPanel.add(lbl("No. HP:"), gbc);
        gbc.gridx = 1; txtNoHp = field(); formPanel.add(txtNoHp, gbc);
        gbc.gridx = 2; formPanel.add(lbl("Email:"), gbc);
        gbc.gridx = 3; txtEmail = field(); formPanel.add(txtEmail, gbc);

        // Row 2: Alamat & Pekerjaan
        gbc.gridx = 0; gbc.gridy = 2; formPanel.add(lbl("Alamat Asal:"), gbc);
        gbc.gridx = 1; txtAlamat = field(); formPanel.add(txtAlamat, gbc);
        gbc.gridx = 2; formPanel.add(lbl("Pekerjaan:"), gbc);
        gbc.gridx = 3; txtPekerjaan = field(); formPanel.add(txtPekerjaan, gbc);

        // Row 3: Pilih Kamar & Durasi Sewa (baru)
        gbc.gridx = 0; gbc.gridy = 3; formPanel.add(lbl("Pilih Kamar:"), gbc);
        gbc.gridx = 1;
        cbKamar = new JComboBox<>();
        cbKamar.setPreferredSize(new Dimension(160, 28));
        cbKamar.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        formPanel.add(cbKamar, gbc);

        gbc.gridx = 2; formPanel.add(lbl("Durasi Sewa (Bulan):"), gbc);
        gbc.gridx = 3;
        spDurasi = new JSpinner(new SpinnerNumberModel(1, 1, 60, 1));
        spDurasi.setPreferredSize(new Dimension(80, 28));
        formPanel.add(spDurasi, gbc);

        // Row 4: Info harga & total otomatis
        gbc.gridx = 0; gbc.gridy = 4; formPanel.add(lbl("Harga/Bulan:"), gbc);
        gbc.gridx = 1;
        lblHarga = new JLabel("Rp 0");
        lblHarga.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblHarga.setForeground(new Color(71, 85, 105));
        formPanel.add(lblHarga, gbc);

        gbc.gridx = 2; formPanel.add(lbl("Estimasi Total:"), gbc);
        gbc.gridx = 3;
        lblTotal = new JLabel("Rp 0");
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTotal.setForeground(ACCENT);
        formPanel.add(lblTotal, gbc);

        // Baru dipanggil di sini, setelah lblHarga & lblTotal sudah diinisialisasi
        muatKamar();

        // Row 5: Badge info transaksi otomatis
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 4;
//        JLabel lblInfo = new JLabel(
//            "ℹ  Saat klik Tambah, transaksi akan otomatis dibuat dengan status Belum Lunas");
//        lblInfo.setFont(new Font("Segoe UI", Font.ITALIC, 12));
//        lblInfo.setForeground(new Color(100, 116, 139));
//        formPanel.add(lblInfo, gbc);
//        gbc.gridwidth = 1;

        // Row 6: Tombol + Cari
        JPanel panelBtn = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panelBtn.setBackground(Color.WHITE);
        btnTambah = btn("Tambah"); btnUpdate = btn("Update");
        btnHapus  = btn("Hapus");  btnBersih = btn("Bersih");
        panelBtn.add(btnTambah); panelBtn.add(btnUpdate);
        panelBtn.add(btnHapus);  panelBtn.add(btnBersih);

        JPanel panelCari = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        panelCari.setBackground(Color.WHITE);
        txtCari = new JTextField(); txtCari.setPreferredSize(new Dimension(180, 28));
        btnCari = btn("Cari");
        panelCari.add(lbl("Cari nama:")); panelCari.add(txtCari); panelCari.add(btnCari);

        JPanel panelBawah = new JPanel(new BorderLayout());
        panelBawah.setBackground(Color.WHITE);
        panelBawah.add(panelBtn, BorderLayout.WEST);
        panelBawah.add(panelCari, BorderLayout.EAST);

        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 4;
        formPanel.add(panelBawah, gbc);

        JPanel topPanel = new JPanel(new BorderLayout(0, 8));
        topPanel.setBackground(BG);
        topPanel.add(header, BorderLayout.NORTH);
        topPanel.add(formPanel, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);

        // ---- TABEL ----
        tableModel = new ModelTablePenyewa(new ArrayList<>());
        tabel = new JTable(tableModel);
        tabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabel.setRowHeight(28);
        tabel.setSelectionBackground(PRIMARY);
        tabel.setSelectionForeground(Color.WHITE);
        tabel.setGridColor(BORDER);
        tabel.setShowHorizontalLines(true);
        tabel.setShowVerticalLines(true);
        tabel.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabel.getTableHeader().setBackground(new Color(241, 245, 249));
        tabel.getTableHeader().setForeground(PRIMARY);
        tabel.getTableHeader().setPreferredSize(new Dimension(0, 32));
        tabel.getTableHeader().setOpaque(true);
        tabel.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, BORDER));

        // Kolom Kamar (index 7) diberi warna berbeda untuk mudah dibaca
        tabel.getColumnModel().getColumn(7).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val, boolean sel,
                                                           boolean foc, int r, int c) {
                JLabel l = (JLabel) super.getTableCellRendererComponent(t, val, sel, foc, r, c);
                l.setBackground(sel ? PRIMARY : new Color(240, 253, 250));
                l.setForeground(sel ? Color.WHITE : new Color(6, 95, 70));
                l.setFont(l.getFont().deriveFont(Font.BOLD));
                return l;
            }
        });

        JScrollPane scroll = new JScrollPane(tabel);
        scroll.setBorder(BorderFactory.createLineBorder(BORDER));
        add(scroll, BorderLayout.CENTER);

        // ---- EVENT LISTENERS ----
        cbKamar.addActionListener(e  -> hitungTotal());
        spDurasi.addChangeListener(e -> hitungTotal());

        btnTambah.addActionListener(e -> tambahData());
        btnUpdate.addActionListener(e -> updateData());
        btnHapus.addActionListener(e  -> hapusData());
        btnBersih.addActionListener(e -> bersihForm());
        btnCari.addActionListener(e   -> cariData());

        tabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tabel.getSelectedRow();
                if (row < 0) return;
                ModelPenyewa p = tableModel.getPenyewaAt(row);
                idTerpilih = p.getIdPenyewa();
                txtNama.setText(p.getNama());
                txtNik.setText(p.getNik());
                txtNoHp.setText(p.getNoHp());
                txtEmail.setText(p.getEmail());
                txtAlamat.setText(p.getAlamatAsal());
                txtPekerjaan.setText(p.getPekerjaan());
                // Pilih kamar yang sesuai di ComboBox
                for (int i = 0; i < cbKamar.getItemCount(); i++) {
                    ModelKamar k = cbKamar.getItemAt(i);
                    if (k != null && k.getIdKamar() == p.getIdKamar()) {
                        cbKamar.setSelectedIndex(i); break;
                    }
                }
            }
        });
    }

    private void muatKamar() {
        cbKamar.removeAllItems();
        // Tambah opsi kosong
        cbKamar.addItem(null);
        for (ModelKamar k : ctrlKamar.getAllKamar()) cbKamar.addItem(k);
        // Custom renderer supaya null ditampilkan sebagai "-- Pilih Kamar --"
        cbKamar.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object val,
                    int idx, boolean sel, boolean focus) {
                super.getListCellRendererComponent(list, val, idx, sel, focus);
                if (val == null) setText("-- Pilih Kamar --");
                else {
                    ModelKamar k = (ModelKamar) val;
                    setText(k.getNomorKamar() + " - " + k.getTipe()
                          + "  (" + k.getStatus() + ")");
                }
                return this;
            }
        });
        hitungTotal();
    }

    private void hitungTotal() {
        ModelKamar k = (ModelKamar) cbKamar.getSelectedItem();
        if (k == null) {
            lblHarga.setText("Rp 0"); lblTotal.setText("Rp 0"); return;
        }
        int durasi = (int) spDurasi.getValue();
        lblHarga.setText("Rp " + String.format("%,.0f", k.getHargaPerBulan()));
        lblTotal.setText("Rp " + String.format("%,.0f", k.getHargaPerBulan() * durasi));
    }

    private void tambahData() {
        String nama = txtNama.getText().trim(), nik = txtNik.getText().trim();
        if (nama.isEmpty() || nik.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nama dan NIK wajib diisi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        ModelKamar kamar = (ModelKamar) cbKamar.getSelectedItem();
        if (kamar == null) {
            JOptionPane.showMessageDialog(this, "Pilih kamar terlebih dahulu!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int durasi = (int) spDurasi.getValue();
        boolean ok = controller.tambahPenyewa(
            nama, nik,
            txtNoHp.getText().trim(), txtEmail.getText().trim(),
            txtAlamat.getText().trim(), txtPekerjaan.getText().trim(),
            kamar.getIdKamar(), durasi);

        if (ok) {
            JOptionPane.showMessageDialog(this,
                "Penyewa berhasil ditambahkan!\nTransaksi sewa otomatis dibuat dengan status Belum Lunas.");
            loadData();
            bersihForm();
            muatKamar(); // refresh kamar supaya status Terisi terupdate
            if (onPenyewaChanged != null) onPenyewaChanged.run();
        } else {
            JOptionPane.showMessageDialog(this,
                "Gagal menyimpan data penyewa.\nPastikan NIK 16 digit dan kamar tersedia.",
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateData() {
        if (idTerpilih == -1) {
            JOptionPane.showMessageDialog(this, "Pilih penyewa di tabel dulu!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        ModelKamar kamar = (ModelKamar) cbKamar.getSelectedItem();
        int idKamar = (kamar != null) ? kamar.getIdKamar() : 0;

        boolean ok = controller.updatePenyewa(
            idTerpilih,
            txtNama.getText().trim(), txtNik.getText().trim(),
            txtNoHp.getText().trim(), txtEmail.getText().trim(),
            txtAlamat.getText().trim(), txtPekerjaan.getText().trim(),
            idKamar);

        if (ok) {
            JOptionPane.showMessageDialog(this, "Data berhasil diperbarui!");
            loadData(); bersihForm();
        }
    }

    private void hapusData() {
        if (idTerpilih == -1) {
            JOptionPane.showMessageDialog(this, "Pilih penyewa di tabel dulu!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (JOptionPane.showConfirmDialog(this, "Yakin hapus penyewa ini?",
                "Konfirmasi", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            if (controller.hapusPenyewa(idTerpilih)) {
                JOptionPane.showMessageDialog(this, "Penyewa berhasil dihapus!");
                loadData(); bersihForm();
                if (onPenyewaChanged != null) onPenyewaChanged.run();
            }
        }
    }

    private void cariData() {
        String kata = txtCari.getText().trim();
        if (kata.isEmpty()) { loadData(); return; }
        tableModel.setData(controller.cariPenyewa(kata));
    }

    private void bersihForm() {
        idTerpilih = -1;
        txtNama.setText(""); txtNik.setText(""); txtNoHp.setText("");
        txtEmail.setText(""); txtAlamat.setText(""); txtPekerjaan.setText("");
        txtCari.setText(""); cbKamar.setSelectedIndex(0);
        spDurasi.setValue(1); lblHarga.setText("Rp 0"); lblTotal.setText("Rp 0");
        tabel.clearSelection();
    }

    public void loadData() { tableModel.setData(controller.getAllPenyewa()); }

    private JLabel lbl(String t) {
        JLabel l = new JLabel(t);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        l.setForeground(PRIMARY); return l;
    }
    private JTextField field() {
        JTextField tf = new JTextField();
        tf.setPreferredSize(new Dimension(160, 28));
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 13)); return tf;
    }
    private JButton btn(String t) {
        JButton b = new JButton(t);
        b.setFont(new Font("Segoe UI", Font.BOLD, 13));
        b.setBackground(PRIMARY); b.setForeground(Color.WHITE);
        b.setFocusPainted(false); b.setBorderPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(new Dimension(100, 32)); return b;
    }
}
