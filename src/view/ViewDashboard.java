package view;

import controller.ControllerKamar;
import controller.ControllerPenyewa;
import controller.ControllerTransaksi;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ViewDashboard extends JPanel {

    private ControllerKamar ctrlKamar;
    private ControllerPenyewa ctrlPenyewa;
    private ControllerTransaksi ctrlTransaksi;

    private JLabel lblTotalKamar, lblKamarTersedia, lblKamarTerisi;
    private JLabel lblTotalPenyewa, lblTotalTransaksi, lblTransaksiLunas;

    private Runnable onKamar, onPenyewa, onTransaksi;

    private static final Color BG     = new Color(245, 247, 250);
    private static final Color WHITE  = Color.WHITE;
    private static final Color BORDER = new Color(226, 232, 240);
    private static final Color MUTED  = new Color(100, 116, 139);
    private static final Color HINT   = new Color(148, 163, 184);
    private static final Color DARK   = new Color(15,  23,  42);

    public ViewDashboard() {
        ctrlKamar     = new ControllerKamar();
        ctrlPenyewa   = new ControllerPenyewa();
        ctrlTransaksi = new ControllerTransaksi();
        setLayout(new GridBagLayout());
        setBackground(BG);
        initComponents();
        refresh();
    }

    public void setOnKamar(Runnable r)      { this.onKamar = r; }
    public void setOnPenyewa(Runnable r)    { this.onPenyewa = r; }
    public void setOnTransaksi(Runnable r)  { this.onTransaksi = r; }

    private void initComponents() {
        JPanel konten = new JPanel();
        konten.setLayout(new BoxLayout(konten, BoxLayout.Y_AXIS));
        konten.setBackground(BG);
        konten.setBorder(new EmptyBorder(24, 24, 24, 24));

        JLabel judul = new JLabel("Sistem Manajemen Penyewaan Kos", SwingConstants.CENTER);
        judul.setFont(new Font("Segoe UI", Font.BOLD, 16));
        judul.setForeground(DARK);
        judul.setAlignmentX(CENTER_ALIGNMENT);
        judul.setBorder(new EmptyBorder(0, 0, 16, 0));
        konten.add(judul);

        lblTotalKamar     = angka();
        lblKamarTersedia  = angka();
        lblKamarTerisi    = angka();
        lblTotalPenyewa   = angka();
        lblTotalTransaksi = angka();
        lblTransaksiLunas = angka();

        JPanel gridStat = new JPanel(new GridLayout(2, 3, 10, 10));
        gridStat.setBackground(BG);
        gridStat.setMaximumSize(new Dimension(660, 180));
        gridStat.setAlignmentX(CENTER_ALIGNMENT);

        gridStat.add(kartuStat("Total Kamar",     lblTotalKamar,     DARK));
        gridStat.add(kartuStat("Kamar Tersedia",  lblKamarTersedia,  DARK));
        gridStat.add(kartuStat("Kamar Terisi",    lblKamarTerisi,    DARK));
        gridStat.add(kartuStat("Total Penyewa",   lblTotalPenyewa,   DARK));
        gridStat.add(kartuStat("Total Transaksi", lblTotalTransaksi, DARK));
        gridStat.add(kartuStat("Transaksi Lunas", lblTransaksiLunas, DARK));
        konten.add(gridStat);

        konten.add(Box.createVerticalStrut(20));

        JLabel lblMenu = new JLabel("Menu", SwingConstants.LEFT);
        lblMenu.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblMenu.setForeground(HINT);
        lblMenu.setAlignmentX(CENTER_ALIGNMENT);
        lblMenu.setMaximumSize(new Dimension(660, 20));
        konten.add(lblMenu);
        konten.add(Box.createVerticalStrut(8));

        JPanel gridNav = new JPanel(new GridLayout(1, 3, 10, 0));
        gridNav.setBackground(BG);
        gridNav.setMaximumSize(new Dimension(660, 90));
        gridNav.setAlignmentX(CENTER_ALIGNMENT);

        gridNav.add(kartuNav("Data Kamar",   "🛏",  () -> { if (onKamar     != null) onKamar.run(); }));
        gridNav.add(kartuNav("Data Penyewa", "👤",  () -> { if (onPenyewa   != null) onPenyewa.run(); }));
        gridNav.add(kartuNav("Transaksi",    "📋",  () -> { if (onTransaksi != null) onTransaksi.run(); }));
        konten.add(gridNav);

        add(konten, new GridBagConstraints());
    }

    private JLabel angka() {
        JLabel l = new JLabel("0", SwingConstants.CENTER);
        l.setFont(new Font("Segoe UI", Font.BOLD, 22));
        return l;
    }

    private JPanel kartuStat(String judul, JLabel lblAngka, Color aksen) {
        JPanel p = new JPanel(new BorderLayout(0, 2));
        p.setBackground(WHITE);
        p.setPreferredSize(new Dimension(140, 80));
        p.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER),
            new EmptyBorder(8, 8, 8, 8)
        ));

        lblAngka.setForeground(aksen);
        p.add(lblAngka, BorderLayout.CENTER);

        JLabel lbl = new JLabel(judul, SwingConstants.CENTER);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        lbl.setForeground(MUTED);
        p.add(lbl, BorderLayout.SOUTH);

        return p;
    }

    private JPanel kartuNav(String label, String emoji, Runnable aksi) {
        JPanel p = new JPanel(new BorderLayout(0, 6));
        p.setBackground(WHITE);
        p.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER),
            new EmptyBorder(14, 10, 14, 10)
        ));
        p.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel ico = new JLabel(emoji, SwingConstants.CENTER);
        ico.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        p.add(ico, BorderLayout.CENTER);

        JLabel lbl = new JLabel(label, SwingConstants.CENTER);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lbl.setForeground(MUTED);
        p.add(lbl, BorderLayout.SOUTH);

        p.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                p.setBackground(new Color(241, 245, 249));
                p.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(148, 163, 184)),
                    new EmptyBorder(14, 10, 14, 10)
                ));
            }
            public void mouseExited(MouseEvent e) {
                p.setBackground(WHITE);
                p.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDER),
                    new EmptyBorder(14, 10, 14, 10)
                ));
            }
            public void mouseClicked(MouseEvent e) { aksi.run(); }
        });

        return p;
    }

    public void refresh() {
        new SwingWorker<int[], Void>() {
            @Override
            protected int[] doInBackground() {
                int total    = ctrlKamar.getAllKamar().size();
                int tersedia = ctrlKamar.getKamarTersedia().size();
                return new int[]{
                    total, tersedia, total - tersedia,
                    ctrlPenyewa.getAllPenyewa().size(),
                    ctrlTransaksi.getAllTransaksi().size(),
                    ctrlTransaksi.getTransaksiByStatus("Lunas").size()
                };
            }
            @Override
            protected void done() {
                try {
                    int[] d = get();
                    lblTotalKamar.setText(String.valueOf(d[0]));
                    lblKamarTersedia.setText(String.valueOf(d[1]));
                    lblKamarTerisi.setText(String.valueOf(d[2]));
                    lblTotalPenyewa.setText(String.valueOf(d[3]));
                    lblTotalTransaksi.setText(String.valueOf(d[4]));
                    lblTransaksiLunas.setText(String.valueOf(d[5]));
                } catch (Exception ignored) {}
            }
        }.execute();
    }
}