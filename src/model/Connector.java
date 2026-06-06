package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connector {
    private static final String URL      = "jdbc:mysql://localhost:3306/manajemen_kos?useSSL=false&serverTimezone=Asia/Jakarta&allowPublicKeyRetrieval=true";
    private static final String USER     = "root";
    private static final String PASSWORD = "";

    private static Connection connection = null;

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Koneksi database berhasil.");
            }
        } catch (ClassNotFoundException e) {
            System.err.println("ERROR: Driver MySQL tidak ditemukan.");
            System.err.println("Solusi: tambahkan mysql-connector-j.jar ke Libraries project.");
            showErrorDialog("Driver MySQL tidak ditemukan!\n\nSolusi: Klik kanan Libraries → Add JAR → pilih mysql-connector-j.jar");
        } catch (SQLException e) {
            System.err.println("ERROR: Gagal koneksi ke database.");
            System.err.println("Detail: " + e.getMessage());
            showErrorDialog("Gagal koneksi ke database!\n\nDetail: " + e.getMessage()
                + "\n\nPastikan:\n1. MySQL/XAMPP sudah aktif\n2. Database 'manajemen_kos' sudah dibuat\n3. Password di Connector.java sudah benar");
        }
        return connection;
    }

    private static void showErrorDialog(String pesan) {
        // Tampilkan dialog error supaya tidak diam-diam gagal
        javax.swing.SwingUtilities.invokeLater(() ->
            javax.swing.JOptionPane.showMessageDialog(null, pesan, "Error Koneksi Database",
                javax.swing.JOptionPane.ERROR_MESSAGE));
    }

    public static void closeConnection() {
        if (connection != null) {
            try { connection.close(); connection = null; }
            catch (SQLException e) { System.err.println("Error menutup koneksi: " + e.getMessage()); }
        }
    }
}
