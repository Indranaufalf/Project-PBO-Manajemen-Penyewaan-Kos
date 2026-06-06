package model.penyewa;

import java.util.List;

public interface InterfaceDAOPenyewa {
    boolean tambah(ModelPenyewa penyewa);
    boolean update(ModelPenyewa penyewa);
    boolean hapus(int idPenyewa);
    ModelPenyewa getById(int idPenyewa);
    List<ModelPenyewa> getAll();
    List<ModelPenyewa> cariByNama(String nama);
}
