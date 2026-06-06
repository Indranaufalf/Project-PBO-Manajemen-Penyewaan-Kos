package model.transaksi;

import model.Connector;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DAOTransaksi implements InterfaceDAOTransaksi {

    private Connection getConn() { return Connector.getConnection(); }

    @Override
    public boolean tambah(ModelTransaksi t) {
        String sql = "INSERT INTO transaksi (id_kamar, id_penyewa, tanggal_masuk, tanggal_keluar, " +
                     "durasi_bulan, total_bayar, status_pembayaran, keterangan) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, t.getIdKamar()); ps.setInt(2, t.getIdPenyewa());
            ps.setDate(3, new java.sql.Date(t.getTanggalMasuk().getTime()));
            ps.setDate(4, new java.sql.Date(t.getTanggalKeluar().getTime()));
            ps.setInt(5, t.getDurasiBulan()); ps.setDouble(6, t.getTotalBayar());
            ps.setString(7, t.getStatusPembayaran()); ps.setString(8, t.getKeterangan());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { System.err.println("tambah transaksi error: " + e.getMessage()); return false; }
    }

    @Override
    public boolean update(ModelTransaksi t) {
        String sql = "UPDATE transaksi SET id_kamar=?, id_penyewa=?, tanggal_masuk=?, tanggal_keluar=?, " +
                     "durasi_bulan=?, total_bayar=?, status_pembayaran=?, keterangan=? WHERE id_transaksi=?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, t.getIdKamar()); ps.setInt(2, t.getIdPenyewa());
            ps.setDate(3, new java.sql.Date(t.getTanggalMasuk().getTime()));
            ps.setDate(4, new java.sql.Date(t.getTanggalKeluar().getTime()));
            ps.setInt(5, t.getDurasiBulan()); ps.setDouble(6, t.getTotalBayar());
            ps.setString(7, t.getStatusPembayaran()); ps.setString(8, t.getKeterangan());
            ps.setInt(9, t.getIdTransaksi());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { System.err.println("update transaksi error: " + e.getMessage()); return false; }
    }

    @Override
    public boolean hapus(int idTransaksi) {
        String sql = "DELETE FROM transaksi WHERE id_transaksi=?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, idTransaksi); return ps.executeUpdate() > 0;
        } catch (SQLException e) { System.err.println("hapus transaksi error: " + e.getMessage()); return false; }
    }

    @Override
    public ModelTransaksi getById(int idTransaksi) {
        String sql = "SELECT t.*, k.nomor_kamar, p.nama as nama_penyewa FROM transaksi t " +
                     "JOIN kamar k ON t.id_kamar = k.id_kamar " +
                     "JOIN penyewa p ON t.id_penyewa = p.id_penyewa WHERE t.id_transaksi=?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, idTransaksi);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
        } catch (SQLException e) { System.err.println("getById transaksi error: " + e.getMessage()); }
        return null;
    }

    @Override
    public List<ModelTransaksi> getAll() {
        List<ModelTransaksi> list = new ArrayList<>();
        String sql = "SELECT t.*, k.nomor_kamar, p.nama as nama_penyewa FROM transaksi t " +
                     "JOIN kamar k ON t.id_kamar = k.id_kamar " +
                     "JOIN penyewa p ON t.id_penyewa = p.id_penyewa ORDER BY t.id_transaksi DESC";
        try (Statement st = getConn().createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) { System.err.println("getAll transaksi error: " + e.getMessage()); }
        return list;
    }

    @Override
    public List<ModelTransaksi> getByStatus(String status) {
        List<ModelTransaksi> list = new ArrayList<>();
        String sql = "SELECT t.*, k.nomor_kamar, p.nama as nama_penyewa FROM transaksi t " +
                     "JOIN kamar k ON t.id_kamar = k.id_kamar " +
                     "JOIN penyewa p ON t.id_penyewa = p.id_penyewa WHERE t.status_pembayaran=?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, status);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) { System.err.println("getByStatus transaksi error: " + e.getMessage()); }
        return list;
    }

    @Override
    public boolean updateStatus(int idTransaksi, String status) {
        String sql = "UPDATE transaksi SET status_pembayaran=? WHERE id_transaksi=?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, status); ps.setInt(2, idTransaksi);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { System.err.println("updateStatus transaksi error: " + e.getMessage()); return false; }
    }

    private ModelTransaksi mapRow(ResultSet rs) throws SQLException {
        return new ModelTransaksi(
            rs.getInt("id_transaksi"), rs.getInt("id_kamar"), rs.getInt("id_penyewa"),
            rs.getString("nomor_kamar"), rs.getString("nama_penyewa"),
            rs.getDate("tanggal_masuk"), rs.getDate("tanggal_keluar"),
            rs.getInt("durasi_bulan"), rs.getDouble("total_bayar"),
            rs.getString("status_pembayaran"), rs.getString("keterangan"));
    }
}
