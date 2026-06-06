package model.penyewa;

import model.Connector;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DAOPenyewa implements InterfaceDAOPenyewa {

    private Connection getConn() { return Connector.getConnection(); }

    @Override
    public boolean tambah(ModelPenyewa p) {
        String sql = "INSERT INTO penyewa (nama, nik, no_hp, email, alamat_asal, pekerjaan, id_kamar) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, p.getNama()); ps.setString(2, p.getNik());
            ps.setString(3, p.getNoHp()); ps.setString(4, p.getEmail());
            ps.setString(5, p.getAlamatAsal()); ps.setString(6, p.getPekerjaan());
            if (p.getIdKamar() > 0) ps.setInt(7, p.getIdKamar());
            else ps.setNull(7, Types.INTEGER);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { System.err.println("tambah penyewa error: " + e.getMessage()); return false; }
    }

    @Override
    public boolean update(ModelPenyewa p) {
        String sql = "UPDATE penyewa SET nama=?, nik=?, no_hp=?, email=?, alamat_asal=?, pekerjaan=?, id_kamar=? WHERE id_penyewa=?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, p.getNama()); ps.setString(2, p.getNik());
            ps.setString(3, p.getNoHp()); ps.setString(4, p.getEmail());
            ps.setString(5, p.getAlamatAsal()); ps.setString(6, p.getPekerjaan());
            if (p.getIdKamar() > 0) ps.setInt(7, p.getIdKamar());
            else ps.setNull(7, Types.INTEGER);
            ps.setInt(8, p.getIdPenyewa());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { System.err.println("update penyewa error: " + e.getMessage()); return false; }
    }

    @Override
    public boolean hapus(int idPenyewa) {
        String sql = "DELETE FROM penyewa WHERE id_penyewa=?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, idPenyewa); return ps.executeUpdate() > 0;
        } catch (SQLException e) { System.err.println("hapus penyewa error: " + e.getMessage()); return false; }
    }

    @Override
    public ModelPenyewa getById(int idPenyewa) {
        String sql = "SELECT p.*, k.nomor_kamar FROM penyewa p "
                   + "LEFT JOIN kamar k ON p.id_kamar = k.id_kamar "
                   + "WHERE p.id_penyewa=?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, idPenyewa);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
        } catch (SQLException e) { System.err.println("getById penyewa error: " + e.getMessage()); }
        return null;
    }

    @Override
    public List<ModelPenyewa> getAll() {
        List<ModelPenyewa> list = new ArrayList<>();
        String sql = "SELECT p.*, k.nomor_kamar FROM penyewa p "
                   + "LEFT JOIN kamar k ON p.id_kamar = k.id_kamar "
                   + "ORDER BY p.nama";
        try (Statement st = getConn().createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) { System.err.println("getAll penyewa error: " + e.getMessage()); }
        return list;
    }

    @Override
    public List<ModelPenyewa> cariByNama(String nama) {
        List<ModelPenyewa> list = new ArrayList<>();
        String sql = "SELECT p.*, k.nomor_kamar FROM penyewa p "
                   + "LEFT JOIN kamar k ON p.id_kamar = k.id_kamar "
                   + "WHERE p.nama LIKE ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, "%" + nama + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) { System.err.println("cariByNama error: " + e.getMessage()); }
        return list;
    }

    private ModelPenyewa mapRow(ResultSet rs) throws SQLException {
        int idKamar = rs.getInt("id_kamar");
        String nomorKamar = rs.wasNull() ? "-" : rs.getString("nomor_kamar");
        return new ModelPenyewa(
            rs.getInt("id_penyewa"), rs.getString("nama"), rs.getString("nik"),
            rs.getString("no_hp"), rs.getString("email"),
            rs.getString("alamat_asal"), rs.getString("pekerjaan"),
            idKamar, nomorKamar);
    }
}
