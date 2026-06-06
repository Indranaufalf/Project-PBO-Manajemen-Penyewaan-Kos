package view.kamar;

import controller.*;
import model.kamar.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class ViewDataKamar extends JPanel {

    private ControllerKamar controller;
    private ModelTableKamar tableModel;
    private JTable tabel;
    private JTextField txtNomor, txtHarga, txtFasilitas, txtKeterangan;
    private JComboBox<String> cbTipe, cbStatus;
    private JButton btnTambah, btnUpdate, btnHapus, btnBersih;
    private int idTerpilih = -1;
    private Runnable onBack;

    private final Color PRIMARY = new Color(30, 41, 59);
    private final Color BG      = new Color(248, 250, 252);
    private final Color BORDER  = new Color(203, 213, 225);

    public ViewDataKamar() {
        controller = new ControllerKamar();
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

        JLabel lblHeader = new JLabel("Data Kamar Kos");
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

        gbc.gridx = 0; gbc.gridy = 0; formPanel.add(lbl("No. Kamar:"), gbc);
        gbc.gridx = 1; txtNomor = field(); formPanel.add(txtNomor, gbc);
        gbc.gridx = 2; formPanel.add(lbl("Tipe Kamar:"), gbc);
        gbc.gridx = 3; cbTipe = combo("Standard", "Deluxe", "VIP"); formPanel.add(cbTipe, gbc);

        gbc.gridx = 0; gbc.gridy = 1; formPanel.add(lbl("Harga/Bulan (Rp):"), gbc);
        gbc.gridx = 1; txtHarga = field(); formPanel.add(txtHarga, gbc);
        gbc.gridx = 2; formPanel.add(lbl("Status:"), gbc);
        gbc.gridx = 3; cbStatus = combo("Tersedia", "Terisi"); formPanel.add(cbStatus, gbc);

        gbc.gridx = 0; gbc.gridy = 2; formPanel.add(lbl("Fasilitas:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3; txtFasilitas = fieldWide(); formPanel.add(txtFasilitas, gbc); gbc.gridwidth = 1;

        gbc.gridx = 0; gbc.gridy = 3; formPanel.add(lbl("Keterangan:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3; txtKeterangan = fieldWide(); formPanel.add(txtKeterangan, gbc); gbc.gridwidth = 1;

        JPanel panelBtn = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panelBtn.setBackground(Color.WHITE);
        btnTambah = btn("Tambah"); btnUpdate = btn("Update");
        btnHapus  = btn("Hapus");  btnBersih = btn("Bersih");
        panelBtn.add(btnTambah); panelBtn.add(btnUpdate);
        panelBtn.add(btnHapus);  panelBtn.add(btnBersih);
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 4; formPanel.add(panelBtn, gbc);

        JPanel topPanel = new JPanel(new BorderLayout(0, 8));
        topPanel.setBackground(BG);
        topPanel.add(header, BorderLayout.NORTH);
        topPanel.add(formPanel, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);

        tableModel = new ModelTableKamar(new ArrayList<>());
        tabel = new JTable(tableModel);
        tabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabel.setRowHeight(28);
        tabel.setSelectionBackground(PRIMARY); tabel.setSelectionForeground(Color.WHITE);
        tabel.setGridColor(BORDER); tabel.setShowHorizontalLines(true); tabel.setShowVerticalLines(true);
        tabel.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabel.getTableHeader().setBackground(new Color(241, 245, 249));
        tabel.getTableHeader().setForeground(PRIMARY);
        tabel.getTableHeader().setPreferredSize(new Dimension(0, 32));
        tabel.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, BORDER));

        tabel.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val, boolean sel, boolean foc, int r, int c) {
                JLabel l = (JLabel) super.getTableCellRendererComponent(t, val, sel, foc, r, c);
                l.setBackground(sel ? PRIMARY : Color.WHITE);
                l.setForeground(sel ? Color.WHITE : PRIMARY);
                l.setFont(l.getFont().deriveFont(Font.BOLD));
                return l;
            }
        });

        add(new JScrollPane(tabel), BorderLayout.CENTER);

        btnTambah.addActionListener(e -> tambahData());
        btnUpdate.addActionListener(e -> updateData());
        btnHapus.addActionListener(e  -> hapusData());
        btnBersih.addActionListener(e -> bersihForm());

        tabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tabel.getSelectedRow();
                if (row < 0) return;
                ModelKamar k = tableModel.getKamarAt(row);
                idTerpilih = k.getIdKamar();
                txtNomor.setText(k.getNomorKamar()); cbTipe.setSelectedItem(k.getTipe());
                txtHarga.setText(String.valueOf((int) k.getHargaPerBulan()));
                cbStatus.setSelectedItem(k.getStatus());
                txtFasilitas.setText(k.getFasilitas()); txtKeterangan.setText(k.getKeterangan());
            }
        });
    }

    private void tambahData() {
        try {
            boolean ok = controller.tambahKamar(txtNomor.getText().trim(), cbTipe.getSelectedItem().toString(),
                Double.parseDouble(txtHarga.getText().trim()), txtFasilitas.getText().trim(), txtKeterangan.getText().trim());
            if (ok) { JOptionPane.showMessageDialog(this, "Kamar berhasil ditambahkan!"); loadData(); bersihForm(); }
            else JOptionPane.showMessageDialog(this, "Gagal menambahkan kamar.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException ex) { JOptionPane.showMessageDialog(this, "Harga harus berupa angka!", "Error", JOptionPane.ERROR_MESSAGE); }
    }

    private void updateData() {
        if (idTerpilih == -1) { JOptionPane.showMessageDialog(this, "Pilih baris dulu!", "Peringatan", JOptionPane.WARNING_MESSAGE); return; }
        try {
            boolean ok = controller.updateKamar(idTerpilih, txtNomor.getText().trim(), cbTipe.getSelectedItem().toString(),
                Double.parseDouble(txtHarga.getText().trim()), cbStatus.getSelectedItem().toString(),
                txtFasilitas.getText().trim(), txtKeterangan.getText().trim());
            if (ok) { JOptionPane.showMessageDialog(this, "Data berhasil diperbarui!"); loadData(); bersihForm(); }
        } catch (NumberFormatException ex) { JOptionPane.showMessageDialog(this, "Harga harus berupa angka!", "Error", JOptionPane.ERROR_MESSAGE); }
    }

    private void hapusData() {
        if (idTerpilih == -1) { JOptionPane.showMessageDialog(this, "Pilih baris dulu!", "Peringatan", JOptionPane.WARNING_MESSAGE); return; }
        if (JOptionPane.showConfirmDialog(this, "Yakin hapus kamar ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
            if (controller.hapusKamar(idTerpilih)) { JOptionPane.showMessageDialog(this, "Kamar berhasil dihapus!"); loadData(); bersihForm(); }
    }

    private void bersihForm() {
        idTerpilih = -1;
        txtNomor.setText(""); txtHarga.setText(""); txtFasilitas.setText(""); txtKeterangan.setText("");
        cbTipe.setSelectedIndex(0); cbStatus.setSelectedIndex(0); tabel.clearSelection();
    }

    public void loadData() { tableModel.setData(controller.getAllKamar()); }

    private JLabel lbl(String t) { JLabel l = new JLabel(t); l.setFont(new Font("Segoe UI", Font.PLAIN, 13)); l.setForeground(PRIMARY); return l; }
    private JTextField field() { JTextField tf = new JTextField(); tf.setPreferredSize(new Dimension(160, 28)); tf.setFont(new Font("Segoe UI", Font.PLAIN, 13)); return tf; }
    private JTextField fieldWide() { JTextField tf = new JTextField(); tf.setPreferredSize(new Dimension(350, 28)); tf.setFont(new Font("Segoe UI", Font.PLAIN, 13)); return tf; }
    private JComboBox<String> combo(String... items) { JComboBox<String> cb = new JComboBox<>(items); cb.setPreferredSize(new Dimension(150, 28)); return cb; }
    private JButton btn(String t) {
        JButton b = new JButton(t); b.setFont(new Font("Segoe UI", Font.BOLD, 13));
        b.setBackground(PRIMARY); b.setForeground(Color.WHITE);
        b.setFocusPainted(false); b.setBorderPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); b.setPreferredSize(new Dimension(100, 32)); return b;
    }
}