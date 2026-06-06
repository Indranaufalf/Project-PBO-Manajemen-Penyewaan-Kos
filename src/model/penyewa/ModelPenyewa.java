package model.penyewa;

import model.BaseModel;

/**
 * ModelPenyewa — merepresentasikan data satu penyewa kos.
 *
 * Ditambahkan: idKamar dan nomorKamar agar penyewa terhubung langsung ke kamar.
 */
public class ModelPenyewa extends BaseModel {

    private String nama;
    private String nik;
    private String noHp;
    private String email;
    private String alamatAsal;
    private String pekerjaan;
    private int    idKamar;      // relasi ke kamar yang dipilih
    private String nomorKamar;   // untuk tampilan langsung di tabel

    public ModelPenyewa() {
        super();
    }

    public ModelPenyewa(int idPenyewa, String nama, String nik, String noHp,
                        String email, String alamatAsal, String pekerjaan,
                        int idKamar, String nomorKamar) {
        super(idPenyewa);
        this.nama       = nama;
        this.nik        = nik;
        this.noHp       = noHp;
        this.email      = email;
        this.alamatAsal = alamatAsal;
        this.pekerjaan  = pekerjaan;
        this.idKamar    = idKamar;
        this.nomorKamar = nomorKamar;
    }

    // Konstruktor lama untuk backward-compat
    public ModelPenyewa(int idPenyewa, String nama, String nik, String noHp,
                        String email, String alamatAsal, String pekerjaan) {
        this(idPenyewa, nama, nik, noHp, email, alamatAsal, pekerjaan, 0, "-");
    }

    /** @deprecated Gunakan getId() dari BaseModel */
    public int  getIdPenyewa()      { return getId(); }
    /** @deprecated Gunakan setId() dari BaseModel */
    public void setIdPenyewa(int i) { setId(i); }

    public String getNama()             { return nama; }
    public void   setNama(String n)     { this.nama = n; }

    public String getNik()              { return nik; }
    public void   setNik(String n)      { this.nik = n; }

    public String getNoHp()             { return noHp; }
    public void   setNoHp(String n)     { this.noHp = n; }

    public String getEmail()            { return email; }
    public void   setEmail(String e)    { this.email = e; }

    public String getAlamatAsal()           { return alamatAsal; }
    public void   setAlamatAsal(String a)   { this.alamatAsal = a; }

    public String getPekerjaan()            { return pekerjaan; }
    public void   setPekerjaan(String p)    { this.pekerjaan = p; }

    public int    getIdKamar()              { return idKamar; }
    public void   setIdKamar(int i)         { this.idKamar = i; }

    public String getNomorKamar()           { return nomorKamar; }
    public void   setNomorKamar(String s)   { this.nomorKamar = s; }

    @Override
    public String getInfoSingkat() {
        return nama + " | NIK: " + nik + " | HP: " + noHp
             + " | Kamar: " + (nomorKamar != null && !nomorKamar.isEmpty() ? nomorKamar : "-");
    }

    @Override
    public String toString() {
        return nama + " (" + nik + ")";
    }
}
