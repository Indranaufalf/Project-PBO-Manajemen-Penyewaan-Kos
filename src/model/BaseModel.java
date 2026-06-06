package model;

/**
 * BaseModel — abstract class yang menjadi parent semua model entitas.
 *
 * Pilar OOP yang diterapkan:
 *   - Inheritance : ModelKamar, ModelPenyewa, ModelTransaksi extends class ini
 *   - Abstraction : method getInfoSingkat() bersifat abstract — setiap subclass
 *                   wajib mengimplementasikan sendiri sesuai kebutuhan entitasnya
 *   - Encapsulation: field id disimpan private, diakses lewat getter/setter
 */
public abstract class BaseModel {

    private int id;

    public BaseModel() {}

    public BaseModel(int id) {
        this.id = id;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    /**
     * Setiap subclass wajib meng-override method ini untuk memberikan
     * ringkasan informasi yang sesuai dengan entitasnya masing-masing.
     * Ini adalah contoh polymorphism — satu nama method, banyak implementasi.
     */
    public abstract String getInfoSingkat();

    /**
     * toString() di-override di BaseModel agar subclass punya default yang
     * konsisten; subclass tetap bisa override lagi jika perlu.
     */
    @Override
    public String toString() {
        return getInfoSingkat();
    }
}
