package model.kamar;

import model.BaseModel;

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

    public int getIdKamar()              { return getId(); }
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


    @Override
    public String getInfoSingkat() {
        return "Kamar " + nomorKamar + " - " + tipe
             + " (Rp " + String.format("%,.0f", hargaPerBulan) + "/bulan)"
             + " - " + status;
    }

    @Override
    public String toString() {
        return nomorKamar + " - " + tipe;
    }
}
