package main;

import view.ViewDashboard;
import view.kamar.ViewDataKamar;
import view.penyewa.ViewDataPenyewa;
import view.transaksi.ViewDataTransaksi;

import javax.swing.*;
import java.awt.*;

public class Main extends JFrame {

    private ViewDashboard     dashboard;
    private ViewDataKamar     viewKamar;
    private ViewDataPenyewa   viewPenyewa;
    private ViewDataTransaksi viewTransaksi;

    private CardLayout cardLayout;
    private JPanel     panelKonten;

    public Main() {
        setTitle("Sistem Manajemen Penyewaan Kos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 700);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(900, 600));
        initComponents();
    }

    private void initComponents() {
        dashboard     = new ViewDashboard();
        viewKamar     = new ViewDataKamar();
        viewPenyewa   = new ViewDataPenyewa();
        viewTransaksi = new ViewDataTransaksi();

        cardLayout  = new CardLayout();
        panelKonten = new JPanel(cardLayout);
        panelKonten.add(dashboard,     "dashboard");
        panelKonten.add(viewKamar,     "kamar");
        panelKonten.add(viewPenyewa,   "penyewa");
        panelKonten.add(viewTransaksi, "transaksi");

        viewPenyewa.setOnPenyewaChanged(() -> {
            viewTransaksi.loadData();
            viewTransaksi.refreshKamar();
            viewTransaksi.refreshPenyewa();
            dashboard.refresh();
            viewKamar.loadData(); 
        });

        dashboard.setOnKamar(() -> {
            cardLayout.show(panelKonten, "kamar");
            viewKamar.loadData();
        });

        dashboard.setOnPenyewa(() -> {
            cardLayout.show(panelKonten, "penyewa");
            viewPenyewa.refreshKamar();
            viewPenyewa.loadData();
        });

        dashboard.setOnTransaksi(() -> {
            cardLayout.show(panelKonten, "transaksi");
            viewTransaksi.loadData();
            viewTransaksi.refreshKamar();
            viewTransaksi.refreshPenyewa();
        });

        viewKamar.setOnBack(() -> {
            cardLayout.show(panelKonten, "dashboard");
            dashboard.refresh();
        });

        viewPenyewa.setOnBack(() -> {
            cardLayout.show(panelKonten, "dashboard");
            dashboard.refresh();
        });

        viewTransaksi.setOnBack(() -> {
            cardLayout.show(panelKonten, "dashboard");
            dashboard.refresh();
            viewKamar.loadData();
            viewPenyewa.loadData();
        });

        setLayout(new BorderLayout());
        add(panelKonten, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main().setVisible(true));
    }
}
