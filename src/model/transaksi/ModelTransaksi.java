package model.transaksi;

import model.BaseModel;
import java.util.Date;

public class ModelTransaksi extends BaseModel {

    private int    idKamar;
    private int    idPenyewa;
    private String nomorKamar;
    private String namaPenyewa;
    private Date   tanggalMasuk;
    private Date   tanggalKeluar;
    private int    durasiBulan;      
    private double totalBayar;
    private String statusPembayaran;
    private String keterangan;

    public ModelTransaksi() {
        super();
    }

    public ModelTransaksi(int idTransaksi, int idKamar, int idPenyewa,
                          String nomorKamar, String namaPenyewa,
                          Date tanggalMasuk, Date tanggalKeluar,
                          int durasiBulan, double totalBayar,
                          String statusPembayaran, String keterangan) {
        super(idTransaksi);
        this.idKamar          = idKamar;
        this.idPenyewa        = idPenyewa;
        this.nomorKamar       = nomorKamar;
        this.namaPenyewa      = namaPenyewa;
        this.tanggalMasuk     = tanggalMasuk;
        this.tanggalKeluar    = tanggalKeluar;
        this.durasiBulan      = durasiBulan;
        this.totalBayar       = totalBayar;
        this.statusPembayaran = statusPembayaran;
        this.keterangan       = keterangan;
    }

    public int  getIdTransaksi()      { return getId(); }
    public void setIdTransaksi(int i) { setId(i); }

    public int    getIdKamar()               { return idKamar; }
    public void   setIdKamar(int i)          { this.idKamar = i; }

    public int    getIdPenyewa()             { return idPenyewa; }
    public void   setIdPenyewa(int i)        { this.idPenyewa = i; }

    public String getNomorKamar()            { return nomorKamar; }
    public void   setNomorKamar(String s)    { this.nomorKamar = s; }

    public String getNamaPenyewa()           { return namaPenyewa; }
    public void   setNamaPenyewa(String s)   { this.namaPenyewa = s; }

    public Date   getTanggalMasuk()          { return tanggalMasuk; }
    public void   setTanggalMasuk(Date d)    { this.tanggalMasuk = d; }

    public Date   getTanggalKeluar()         { return tanggalKeluar; }
    public void   setTanggalKeluar(Date d)   { this.tanggalKeluar = d; }

    public int    getDurasiBulan()           { return durasiBulan; }
    public void   setDurasiBulan(int n)      { this.durasiBulan = n; }

    /** Alias untuk backward-compat dengan DAOTransaksi lama. */
    public int    getDurasiBuilan()          { return durasiBulan; }
    public void   setDurasiBuilan(int n)     { this.durasiBulan = n; }

    public double getTotalBayar()            { return totalBayar; }
    public void   setTotalBayar(double d)    { this.totalBayar = d; }

    public String getStatusPembayaran()          { return statusPembayaran; }
    public void   setStatusPembayaran(String s)  { this.statusPembayaran = s; }

    public String getKeterangan()            { return keterangan; }
    public void   setKeterangan(String s)    { this.keterangan = s; }

    // --- implementasi method abstract dari BaseModel (polymorphism) ---

    @Override
    public String getInfoSingkat() {
        return "Transaksi #" + getId()
             + " | Kamar: " + nomorKamar
             + " | Penyewa: " + namaPenyewa
             + " | " + durasiBulan + " bulan"
             + " | " + statusPembayaran;
    }
}
