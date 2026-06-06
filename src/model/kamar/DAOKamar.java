package model.kamar;

import model.Connector;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DAOKamar implements InterfaceDAOKamar {

    private Connection getConn() {
        return Connector.getConnection();
    }

    @Override
    public boolean tambah(ModelKamar kamar) {
        String sql = "INSERT INTO kamar (nomor_kamar, tipe, harga_per_bulan, status, fasilitas, keterangan) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, kamar.getNomorKamar());
            ps.setString(2, kamar.getTipe());
            ps.setDouble(3, kamar.getHargaPerBulan());
            ps.setString(4, kamar.getStatus());
            ps.setString(5, kamar.getFasilitas());
            ps.setString(6, kamar.getKeterangan());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("tambah kamar error: " + e.getMessage()); return false;
        }
    }

    @Override
    public boolean update(ModelKamar kamar) {
        String sql = "UPDATE kamar SET nomor_kamar=?, tipe=?, harga_per_bulan=?, status=?, fasilitas=?, keterangan=? WHERE id_kamar=?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, kamar.getNomorKamar());
            ps.setString(2, kamar.getTipe());
            ps.setDouble(3, kamar.getHargaPerBulan());
            ps.setString(4, kamar.getStatus());
            ps.setString(5, kamar.getFasilitas());
            ps.setString(6, kamar.getKeterangan());
            ps.setInt(7, kamar.getIdKamar());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("update kamar error: " + e.getMessage()); return false;
        }
    }

    @Override
    public boolean hapus(int idKamar) {
        String sql = "DELETE FROM kamar WHERE id_kamar=?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, idKamar);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("hapus kamar error: " + e.getMessage()); return false;
        }
    }

    @Override
    public ModelKamar getById(int idKamar) {
        String sql = "SELECT * FROM kamar WHERE id_kamar=?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, idKamar);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
        } catch (SQLException e) {
            System.err.println("getById kamar error: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<ModelKamar> getAll() {
        List<ModelKamar> list = new ArrayList<>();
        String sql = "SELECT * FROM kamar ORDER BY nomor_kamar";
        try (Statement st = getConn().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.err.println("getAll kamar error: " + e.getMessage());
        }
        return list;
    }

    @Override
    public List<ModelKamar> getByStatus(String status) {
        List<ModelKamar> list = new ArrayList<>();
        String sql = "SELECT * FROM kamar WHERE status=?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, status);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.err.println("getByStatus kamar error: " + e.getMessage());
        }
        return list;
    }

    @Override
    public boolean updateStatus(int idKamar, String status) {
        String sql = "UPDATE kamar SET status=? WHERE id_kamar=?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, idKamar);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("updateStatus kamar error: " + e.getMessage()); return false;
        }
    }

    private ModelKamar mapRow(ResultSet rs) throws SQLException {
        return new ModelKamar(
            rs.getInt("id_kamar"), rs.getString("nomor_kamar"), rs.getString("tipe"),
            rs.getDouble("harga_per_bulan"), rs.getString("status"),
            rs.getString("fasilitas"), rs.getString("keterangan"));
    }
}
