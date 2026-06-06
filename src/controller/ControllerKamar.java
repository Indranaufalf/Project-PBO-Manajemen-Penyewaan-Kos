package controller;

import model.kamar.DAOKamar;
import model.kamar.InterfaceDAOKamar;
import model.kamar.ModelKamar;
import java.util.List;

/**
 * ControllerKamar — jembatan antara View dan lapisan data (DAO) untuk entitas Kamar.
 *
 * Polymorphism: field dao bertipe InterfaceDAOKamar (interface), bukan DAOKamar
 *               (kelas konkret). Artinya di masa depan implementasi bisa diganti
 *               (misal DAOKamarFake untuk testing) tanpa mengubah kode ini sama sekali.
 *
 * Validasi input diperkuat: harga tidak boleh nol/negatif.
 */
public class ControllerKamar {

    // Polymorphism: dideklarasikan sebagai interface, bukan kelas konkret
    private final InterfaceDAOKamar dao;

    /** Konstruktor default — memakai implementasi database sungguhan. */
    public ControllerKamar() {
        this.dao = new DAOKamar();
    }

    /**
     * Konstruktor dengan dependency injection.
     * Memungkinkan penggantian implementasi DAO dari luar (misal untuk unit test).
     *
     * @param dao implementasi InterfaceDAOKamar yang akan digunakan
     */
    public ControllerKamar(InterfaceDAOKamar dao) {
        this.dao = dao;
    }

    public boolean tambahKamar(String nomor, String tipe, double harga,
                               String fasilitas, String keterangan) {
        if (nomor == null || nomor.trim().isEmpty()) return false;
        if (tipe  == null || tipe.trim().isEmpty())  return false;
        if (harga <= 0)                              return false;

        ModelKamar kamar = new ModelKamar(0, nomor.trim(), tipe.trim(),
                                          harga, "Tersedia", fasilitas, keterangan);
        return dao.tambah(kamar);
    }

    public boolean updateKamar(int id, String nomor, String tipe, double harga,
                               String status, String fasilitas, String keterangan) {
        if (nomor == null || nomor.trim().isEmpty()) return false;
        if (tipe  == null || tipe.trim().isEmpty())  return false;
        if (harga <= 0)                              return false;

        ModelKamar kamar = new ModelKamar(id, nomor.trim(), tipe.trim(),
                                          harga, status, fasilitas, keterangan);
        return dao.update(kamar);
    }

    public boolean hapusKamar(int idKamar) {
        return dao.hapus(idKamar);
    }

    public List<ModelKamar> getAllKamar() {
        return dao.getAll();
    }

    public List<ModelKamar> getKamarTersedia() {
        return dao.getByStatus("Tersedia");
    }

    public ModelKamar getKamarById(int id) {
        return dao.getById(id);
    }

    public boolean updateStatusKamar(int id, String status) {
        return dao.updateStatus(id, status);
    }
}
