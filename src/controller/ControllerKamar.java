package controller;

import model.kamar.DAOKamar;
import model.kamar.InterfaceDAOKamar;
import model.kamar.ModelKamar;
import java.util.List;

public class ControllerKamar {

    // Polymorphism
    private final InterfaceDAOKamar dao;

    public ControllerKamar() {
        this.dao = new DAOKamar();
    }

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
