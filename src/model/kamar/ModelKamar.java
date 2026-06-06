package model.kamar;

import model.BaseModel;

/**
 * ModelKamar — merepresentasikan data satu kamar kos.
 *
 * Inheritance : extends BaseModel, mewarisi field id dan method getId/setId.
 * Polymorphism: meng-override getInfoSingkat() sesuai konteks kamar.
 */
public class ModelKamar extends BaseModel {

    private String nomorKamar;
    private String tipe;
    private double hargaPerBulan;
    private String status;
    private String fasilitas;
    private String keterangan;

    public ModelKamar() {
        super();
    }

    public ModelKamar(int idKamar, String nomorKamar, String tipe,
                      double hargaPerBulan, String status, String fasilitas, String keterangan) {
        super(idKamar);
        this.nomorKamar   = nomorKamar;
        this.tipe         = tipe;
        this.hargaPerBulan = hargaPerBulan;
        this.status       = status;
        this.fasilitas    = fasilitas;
        this.keterangan   = keterangan;
    }

    // --- getter/setter field spesifik kamar ---

    /** @deprecated Gunakan getId() dari BaseModel */
    public int getIdKamar()              { return getId(); }
    /** @deprecated Gunakan setId() dari BaseModel */
    public void setIdKamar(int id)       { setId(id); }

    public String getNomorKamar()                  { return nomorKamar; }
    public void   setNomorKamar(String nomorKamar) { this.nomorKamar = nomorKamar; }

    public String getTipe()          { return tipe; }
    public void   setTipe(String t)  { this.tipe = t; }

    public double getHargaPerBulan()             { return hargaPerBulan; }
    public void   setHargaPerBulan(double harga) { this.hargaPerBulan = harga; }

    public String getStatus()           { return status; }
    public void   setStatus(String s)   { this.status = s; }

    public String getFasilitas()            { return fasilitas; }
    public void   setFasilitas(String f)    { this.fasilitas = f; }

    public String getKeterangan()           { return keterangan; }
    public void   setKeterangan(String k)   { this.keterangan = k; }

    // --- implementasi method abstract dari BaseModel (polymorphism) ---

    @Override
    public String getInfoSingkat() {
        return "Kamar " + nomorKamar + " - " + tipe
             + " (Rp " + String.format("%,.0f", hargaPerBulan) + "/bulan)"
             + " - " + status;
    }

    /** Dipakai ComboBox di View — tampilkan nomor & tipe saja. */
    @Override
    public String toString() {
        return nomorKamar + " - " + tipe;
    }
}
