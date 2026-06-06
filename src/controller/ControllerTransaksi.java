package controller;

import model.transaksi.DAOTransaksi;
import model.transaksi.InterfaceDAOTransaksi;
import model.transaksi.ModelTransaksi;
import model.kamar.ModelKamar;
import java.util.Date;
import java.util.List;

public class ControllerTransaksi {

    private final InterfaceDAOTransaksi dao;
    private final ControllerKamar       ctrlKamar;

    public ControllerTransaksi() {
        this.dao       = new DAOTransaksi();
        this.ctrlKamar = new ControllerKamar();
    }

    public ControllerTransaksi(InterfaceDAOTransaksi dao, ControllerKamar ctrlKamar) {
        this.dao       = dao;
        this.ctrlKamar = ctrlKamar;
    }

    public boolean tambahTransaksi(int idKamar, int idPenyewa, Date tglMasuk,
                                    Date tglKeluar, int durasi, double total,
                                    String status, String ket) {
        if (idKamar <= 0 || idPenyewa <= 0)        return false;
        if (tglMasuk == null || tglKeluar == null) return false;
        if (tglKeluar.before(tglMasuk))            return false;
        if (durasi <= 0 || total <= 0)             return false;

        ModelTransaksi t = new ModelTransaksi(0, idKamar, idPenyewa, "", "",
                                              tglMasuk, tglKeluar, durasi, total, status, ket);
        boolean berhasil = dao.tambah(t);
        if (berhasil) {
            ctrlKamar.updateStatusKamar(idKamar, "Terisi");
        }
        return berhasil;
    }

    public boolean updateTransaksi(int id, int idKamar, int idPenyewa, Date tglMasuk,
                                    Date tglKeluar, int durasi, double total,
                                    String status, String ket) {
        if (tglMasuk == null || tglKeluar == null) return false;
        if (tglKeluar.before(tglMasuk))            return false;
        if (durasi <= 0 || total <= 0)             return false;

        ModelTransaksi t = new ModelTransaksi(id, idKamar, idPenyewa, "", "",
                                              tglMasuk, tglKeluar, durasi, total, status, ket);
        return dao.update(t);
    }

    public boolean hapusTransaksi(int idTransaksi) {
        ModelTransaksi t = dao.getById(idTransaksi);
        boolean berhasil = dao.hapus(idTransaksi);
        if (berhasil && t != null) {
            ctrlKamar.updateStatusKamar(t.getIdKamar(), "Tersedia");
        }
        return berhasil;
    }

    public List<ModelTransaksi> getAllTransaksi() {
        return dao.getAll();
    }

    public List<ModelTransaksi> getTransaksiByStatus(String status) {
        return dao.getByStatus(status);
    }

    public ModelTransaksi getTransaksiById(int id) {
        return dao.getById(id);
    }

    public boolean updateStatusPembayaran(int id, String status) {
        return dao.updateStatus(id, status);
    }

    public double hitungTotal(double hargaPerBulan, int durasiBulan) {
        if (hargaPerBulan <= 0 || durasiBulan <= 0) return 0;
        return hargaPerBulan * durasiBulan;
    }

    public List<ModelKamar> getKamarTersedia() {
        return ctrlKamar.getKamarTersedia();
    }

    public void cekDanUpdateStatusKamarKadaluarsa() {
        Date sekarang = new Date();
        List<ModelTransaksi> semua = dao.getAll();
        for (ModelTransaksi t : semua) {
            if (t.getTanggalKeluar() != null && t.getTanggalKeluar().before(sekarang)) {
                ModelKamar kamar = ctrlKamar.getKamarById(t.getIdKamar());
                if (kamar != null && "Terisi".equals(kamar.getStatus())) {
                    ctrlKamar.updateStatusKamar(t.getIdKamar(), "Tersedia");
                }
            }
        }
    }
}
