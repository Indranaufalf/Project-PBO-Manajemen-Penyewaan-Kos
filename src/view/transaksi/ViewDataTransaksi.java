package view.transaksi;

import controller.*;
import model.kamar.*;
import model.penyewa.*;
import model.transaksi.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.text.*;
import java.util.*;

public class ViewDataTransaksi extends JPanel {

    private ControllerTransaksi ctrlTransaksi;
    private ControllerKamar     ctrlKamar;
    private ControllerPenyewa   ctrlPenyewa;
    private ModelTableTransaksi tableModel;
    private JTable              tabel;

    private JComboBox<ModelKamar>   cbKamar;
    private JComboBox<ModelPenyewa> cbPenyewa;
    private JSpinner   spTglMasuk, spDurasi;
    private JLabel     lblTglKeluar, lblTotal;
    private JComboBox<String> cbStatus;
    private JTextField txtKeterangan;
    private JButton    btnTambah, btnUpdate, btnHapus, btnBersih;
    private JLabel     lblNamaPenyewa;
    private int        idTerpilih = -1;
    private Runnable   onBack;

    private final Color PRIMARY = new Color(30, 41, 59);
    private final Color BG      = new Color(248, 250, 252);
    private final Color BORDER  = new Color(203, 213, 225);
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    public ViewDataTransaksi() {
        ctrlTransaksi = new ControllerTransaksi();
        ctrlKamar     = new ControllerKamar();
        ctrlPenyewa   = new ControllerPenyewa();
        setLayout(new BorderLayout(10, 10));
        setBackground(BG);
        setBorder(new EmptyBorder(15, 15, 15, 15));
        initComponents();
        loadData();
    }

    public void setOnBack(Runnable r) { this.onBack = r; }

    private void initComponents() {
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

        JLabel lblHeader = new JLabel("Data Transaksi Penyewaan");
        lblHeader.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblHeader.setForeground(PRIMARY);
        header.add(btnBack, BorderLayout.WEST);
        header.add(lblHeader, BorderLayout.CENTER);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER), new EmptyBorder(12, 15, 12, 15)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0; formPanel.add(lbl("Pilih Kamar:"), gbc);
        gbc.gridx = 1; cbKamar = new JComboBox<>(); cbKamar.setPreferredSize(new Dimension(200, 28));
        muatKamar(); formPanel.add(cbKamar, gbc);
        gbc.gridx = 2; formPanel.add(lbl("Penyewa:"), gbc);
        gbc.gridx = 3; cbPenyewa = new JComboBox<>(); cbPenyewa.setPreferredSize(new Dimension(200, 28));
        muatPenyewa(); formPanel.add(cbPenyewa, gbc);

        gbc.gridx = 2; gbc.gridy = 1; gbc.gridwidth = 2;
        lblNamaPenyewa = new JLabel(" ");
        lblNamaPenyewa.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblNamaPenyewa.setForeground(new Color(15, 118, 110));
        formPanel.add(lblNamaPenyewa, gbc);
        gbc.gridwidth = 1;

        gbc.gridx = 0; gbc.gridy = 2; formPanel.add(lbl("Tgl Masuk:"), gbc);
        gbc.gridx = 1; spTglMasuk = new JSpinner(new SpinnerDateModel());
        spTglMasuk.setEditor(new JSpinner.DateEditor(spTglMasuk, "dd/MM/yyyy"));
        spTglMasuk.setPreferredSize(new Dimension(150, 28)); formPanel.add(spTglMasuk, gbc);
        gbc.gridx = 2; formPanel.add(lbl("Durasi (Bulan):"), gbc);
        gbc.gridx = 3; spDurasi = new JSpinner(new SpinnerNumberModel(1, 1, 60, 1));
        spDurasi.setPreferredSize(new Dimension(80, 28)); formPanel.add(spDurasi, gbc);

        gbc.gridx = 0; gbc.gridy = 3; formPanel.add(lbl("Tgl Keluar:"), gbc);
        gbc.gridx = 1; lblTglKeluar = new JLabel("-");
        lblTglKeluar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTglKeluar.setForeground(PRIMARY); formPanel.add(lblTglKeluar, gbc);
        gbc.gridx = 2; formPanel.add(lbl("Total Bayar:"), gbc);
        gbc.gridx = 3; lblTotal = new JLabel("Rp 0");
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTotal.setForeground(PRIMARY); formPanel.add(lblTotal, gbc);

        gbc.gridx = 0; gbc.gridy = 4; formPanel.add(lbl("Status:"), gbc);
        gbc.gridx = 1; cbStatus = new JComboBox<>(new String[]{"Belum Lunas", "Lunas"});
        cbStatus.setPreferredSize(new Dimension(150, 28)); formPanel.add(cbStatus, gbc);
        gbc.gridx = 2; formPanel.add(lbl("Keterangan:"), gbc);
        gbc.gridx = 3; txtKeterangan = new JTextField();
        txtKeterangan.setPreferredSize(new Dimension(200, 28)); formPanel.add(txtKeterangan, gbc);

        JPanel panelBtn = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panelBtn.setBackground(Color.WHITE);
        btnTambah = btn("Tambah"); btnUpdate = btn("Update");
        btnHapus  = btn("Hapus");  btnBersih = btn("Bersih");
        panelBtn.add(btnTambah); panelBtn.add(btnUpdate);
        panelBtn.add(btnHapus);  panelBtn.add(btnBersih);
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 4; formPanel.add(panelBtn, gbc);

        spDurasi.addChangeListener(e   -> hitungOtomatis());
        cbKamar.addActionListener(e    -> hitungOtomatis());
        spTglMasuk.addChangeListener(e -> hitungOtomatis());
        cbPenyewa.addActionListener(e  -> updateNamaPenyewa());

        JPanel topPanel = new JPanel(new BorderLayout(0, 8));
        topPanel.setBackground(BG);
        topPanel.add(header, BorderLayout.NORTH);
        topPanel.add(formPanel, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);

        tableModel = new ModelTableTransaksi(new ArrayList<>());
        tabel = new JTable(tableModel);
        tabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabel.setRowHeight(28);
        tabel.setSelectionBackground(PRIMARY); tabel.setSelectionForeground(Color.WHITE);
        tabel.setGridColor(BORDER); tabel.setShowHorizontalLines(true); tabel.setShowVerticalLines(true);
        tabel.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabel.getTableHeader().setBackground(new Color(241, 245, 249));
        tabel.getTableHeader().setForeground(PRIMARY);
        tabel.getTableHeader().setPreferredSize(new Dimension(0, 32));
        tabel.getTableHeader().setOpaque(true);
        tabel.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, BORDER));

        tabel.getColumnModel().getColumn(7).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val, boolean sel,
                                                           boolean foc, int r, int c) {
                JLabel l = (JLabel) super.getTableCellRendererComponent(t, val, sel, foc, r, c);
                l.setBackground(sel ? PRIMARY : Color.WHITE);
                l.setForeground(sel ? Color.WHITE : PRIMARY);
                l.setFont(l.getFont().deriveFont(Font.BOLD));
                return l;
            }
        });

        JScrollPane scroll = new JScrollPane(tabel);
        scroll.setBorder(BorderFactory.createLineBorder(BORDER));
        add(scroll, BorderLayout.CENTER);

        btnTambah.addActionListener(e -> tambahData());
        btnUpdate.addActionListener(e -> updateData());
        btnHapus.addActionListener(e  -> hapusData());
        btnBersih.addActionListener(e -> bersihForm());

        tabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tabel.getSelectedRow();
                if (row < 0) return;
                ModelTransaksi t = tableModel.getTransaksiAt(row);
                idTerpilih = t.getIdTransaksi();
                for (int i = 0; i < cbKamar.getItemCount(); i++)
                    if (cbKamar.getItemAt(i).getIdKamar() == t.getIdKamar()) {
                        cbKamar.setSelectedIndex(i); break;
                    }
                for (int i = 0; i < cbPenyewa.getItemCount(); i++)
                    if (cbPenyewa.getItemAt(i).getIdPenyewa() == t.getIdPenyewa()) {
                        cbPenyewa.setSelectedIndex(i); break;
                    }
                spTglMasuk.setValue(t.getTanggalMasuk());
                spDurasi.setValue(t.getDurasiBulan());
                cbStatus.setSelectedItem(t.getStatusPembayaran());
                txtKeterangan.setText(t.getKeterangan());
                hitungOtomatis();
            }
        });
    }

    private void updateNamaPenyewa() {
        ModelPenyewa p = (ModelPenyewa) cbPenyewa.getSelectedItem();
        lblNamaPenyewa.setText(p != null ? "Nama: " + p.getNama() : " ");
    }

    private void hitungOtomatis() {
        Date tglMasuk = (Date) spTglMasuk.getValue();
        int  durasi   = (int)  spDurasi.getValue();
        Calendar cal  = Calendar.getInstance();
        cal.setTime(tglMasuk); cal.add(Calendar.MONTH, durasi);
        lblTglKeluar.setText(sdf.format(cal.getTime()));
        if (cbKamar.getSelectedItem() != null) {
            double total = ctrlTransaksi.hitungTotal(
                ((ModelKamar) cbKamar.getSelectedItem()).getHargaPerBulan(), durasi);
            lblTotal.setText("Rp " + String.format("%,.0f", total));
        }
    }

    private void tambahData() {
        if (cbKamar.getSelectedItem() == null || cbPenyewa.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Pilih kamar dan penyewa terlebih dahulu!",
                "Peringatan", JOptionPane.WARNING_MESSAGE); return;
        }
        ModelKamar   kamar   = (ModelKamar)   cbKamar.getSelectedItem();
        ModelPenyewa penyewa = (ModelPenyewa) cbPenyewa.getSelectedItem();
        Date tglMasuk = (Date) spTglMasuk.getValue();
        int  durasi   = (int)  spDurasi.getValue();
        Calendar cal  = Calendar.getInstance();
        cal.setTime(tglMasuk); cal.add(Calendar.MONTH, durasi);
        double total  = ctrlTransaksi.hitungTotal(kamar.getHargaPerBulan(), durasi);

        boolean ok = ctrlTransaksi.tambahTransaksi(
            kamar.getIdKamar(), penyewa.getIdPenyewa(),
            tglMasuk, cal.getTime(), durasi, total,
            cbStatus.getSelectedItem().toString(), txtKeterangan.getText().trim());

        if (ok) {
            JOptionPane.showMessageDialog(this, "Transaksi berhasil disimpan!");
            loadData();
            bersihForm();
            muatKamar(); // refresh dropdown kamar supaya status Terisi langsung hilang
        } else {
            JOptionPane.showMessageDialog(this, "Gagal menyimpan transaksi.",
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateData() {
        if (idTerpilih == -1) {
            JOptionPane.showMessageDialog(this, "Pilih transaksi di tabel dulu!",
                "Peringatan", JOptionPane.WARNING_MESSAGE); return;
        }
        ModelKamar   kamar   = (ModelKamar)   cbKamar.getSelectedItem();
        ModelPenyewa penyewa = (ModelPenyewa) cbPenyewa.getSelectedItem();
        Date tglMasuk = (Date) spTglMasuk.getValue();
        int  durasi   = (int)  spDurasi.getValue();
        Calendar cal  = Calendar.getInstance();
        cal.setTime(tglMasuk); cal.add(Calendar.MONTH, durasi);
        double total  = ctrlTransaksi.hitungTotal(kamar.getHargaPerBulan(), durasi);

        boolean ok = ctrlTransaksi.updateTransaksi(
            idTerpilih, kamar.getIdKamar(), penyewa.getIdPenyewa(),
            tglMasuk, cal.getTime(), durasi, total,
            cbStatus.getSelectedItem().toString(), txtKeterangan.getText().trim());

        if (ok) {
            JOptionPane.showMessageDialog(this, "Transaksi berhasil diperbarui!");
            loadData(); bersihForm(); muatKamar();
        }
    }

    private void hapusData() {
        if (idTerpilih == -1) {
            JOptionPane.showMessageDialog(this, "Pilih transaksi di tabel dulu!",
                "Peringatan", JOptionPane.WARNING_MESSAGE); return;
        }
        int konfirm = JOptionPane.showConfirmDialog(this,
            "Yakin hapus transaksi ini?\nStatus kamar akan kembali jadi Tersedia.",
            "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (konfirm == JOptionPane.YES_OPTION) {
            if (ctrlTransaksi.hapusTransaksi(idTerpilih)) {
                JOptionPane.showMessageDialog(this, "Transaksi berhasil dihapus!");
                loadData(); bersihForm();
                muatKamar(); 
            }
        }
    }

    private void bersihForm() {
        idTerpilih = -1;
        spTglMasuk.setValue(new Date()); spDurasi.setValue(1);
        cbStatus.setSelectedIndex(0); txtKeterangan.setText("");
        lblTglKeluar.setText("-"); lblTotal.setText("Rp 0");
        lblNamaPenyewa.setText(" ");
        tabel.clearSelection();
    }

    public void loadData() {
        ctrlTransaksi.cekDanUpdateStatusKamarKadaluarsa();
        tableModel.setData(ctrlTransaksi.getAllTransaksi());
    }

    public void refreshPenyewa() { muatPenyewa(); }

    public void refreshKamar() { muatKamar(); }

    private void muatKamar() {
        cbKamar.removeAllItems();
        for (ModelKamar k : ctrlKamar.getAllKamar()) cbKamar.addItem(k);
    }

    private void muatPenyewa() {
        cbPenyewa.removeAllItems();
        for (ModelPenyewa p : ctrlPenyewa.getAllPenyewa()) cbPenyewa.addItem(p);
    }

    private JLabel lbl(String t) {
        JLabel l = new JLabel(t);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        l.setForeground(PRIMARY); return l;
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
