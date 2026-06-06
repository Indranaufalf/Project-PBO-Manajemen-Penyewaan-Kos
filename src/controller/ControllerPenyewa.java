package controller;

import model.penyewa.DAOPenyewa;
import model.penyewa.InterfaceDAOPenyewa;
import model.penyewa.ModelPenyewa;
import model.transaksi.DAOTransaksi;
import model.transaksi.InterfaceDAOTransaksi;
import model.transaksi.ModelTransaksi;
import model.kamar.ModelKamar;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class ControllerPenyewa {

    private final InterfaceDAOPenyewa    dao;
    private final InterfaceDAOTransaksi  daoTransaksi;
    private final ControllerKamar        ctrlKamar;

    public ControllerPenyewa() {
        this.dao          = new DAOPenyewa();
        this.daoTransaksi = new DAOTransaksi();
        this.ctrlKamar    = new ControllerKamar();
    }

    public ControllerPenyewa(InterfaceDAOPenyewa dao) {
        this.dao          = dao;
        this.daoTransaksi = new DAOTransaksi();
        this.ctrlKamar    = new ControllerKamar();
    }

    public boolean tambahPenyewa(String nama, String nik, String noHp,
                                  String email, String alamat, String pekerjaan,
                                  int idKamar, int durasiBulan) {
        if (nama == null || nama.trim().isEmpty()) return false;
        if (nik  == null || nik.trim().isEmpty())  return false;
        if (!nik.trim().matches("\\d{16}"))        return false;
        if (idKamar <= 0 || durasiBulan <= 0)      return false;

        ModelKamar kamar = ctrlKamar.getKamarById(idKamar);
        if (kamar == null) return false;

        ModelPenyewa p = new ModelPenyewa(0, nama.trim(), nik.trim(),
                                          noHp, email, alamat, pekerjaan,
                                          idKamar, kamar.getNomorKamar());
        boolean ok = dao.tambah(p);
        if (!ok) return false;

        ModelPenyewa baru = cariPenyewaByNik(nik.trim());
        if (baru == null) return true; 

        Date tglMasuk = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(tglMasuk);
        cal.add(Calendar.MONTH, durasiBulan);
        Date tglKeluar = cal.getTime();
        double total   = kamar.getHargaPerBulan() * durasiBulan;

        ModelTransaksi t = new ModelTransaksi(
            0, idKamar, baru.getIdPenyewa(),
            kamar.getNomorKamar(), baru.getNama(),
            tglMasuk, tglKeluar, durasiBulan,
            total, "Belum Lunas", "Transaksi otomatis saat pendaftaran");
        daoTransaksi.tambah(t);

        ctrlKamar.updateStatusKamar(idKamar, "Terisi");

        return true;
    }

    public boolean tambahPenyewa(String nama, String nik, String noHp,
                                  String email, String alamat, String pekerjaan) {
        if (nama == null || nama.trim().isEmpty()) return false;
        if (nik  == null || nik.trim().isEmpty())  return false;
        if (!nik.trim().matches("\\d{16}"))        return false;
        ModelPenyewa p = new ModelPenyewa(0, nama.trim(), nik.trim(),
                                          noHp, email, alamat, pekerjaan);
        return dao.tambah(p);
    }

    public boolean updatePenyewa(int id, String nama, String nik, String noHp,
                                  String email, String alamat, String pekerjaan,
                                  int idKamar) {
        if (nama == null || nama.trim().isEmpty()) return false;
        if (nik  == null || nik.trim().isEmpty())  return false;
        if (!nik.trim().matches("\\d{16}"))        return false;

        String nomorKamar = "-";
        if (idKamar > 0) {
            ModelKamar k = ctrlKamar.getKamarById(idKamar);
            if (k != null) nomorKamar = k.getNomorKamar();
        }

        ModelPenyewa p = new ModelPenyewa(id, nama.trim(), nik.trim(),
                                          noHp, email, alamat, pekerjaan,
                                          idKamar, nomorKamar);
        return dao.update(p);
    }

    public boolean updatePenyewa(int id, String nama, String nik, String noHp,
                                  String email, String alamat, String pekerjaan) {
        return updatePenyewa(id, nama, nik, noHp, email, alamat, pekerjaan, 0);
    }

    public boolean hapusPenyewa(int idPenyewa) {
        return dao.hapus(idPenyewa);
    }

    public List<ModelPenyewa> getAllPenyewa() {
        return dao.getAll();
    }

    public List<ModelPenyewa> cariPenyewa(String nama) {
        return dao.cariByNama(nama == null ? "" : nama);
    }

    public ModelPenyewa getPenyewaById(int id) {
        return dao.getById(id);
    }

    private ModelPenyewa cariPenyewaByNik(String nik) {
        for (ModelPenyewa p : dao.getAll())
            if (nik.equals(p.getNik())) return p;
        return null;
    }
}
