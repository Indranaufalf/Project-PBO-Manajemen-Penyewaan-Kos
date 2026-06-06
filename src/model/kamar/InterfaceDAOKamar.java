package model.kamar;

import java.util.List;

public interface InterfaceDAOKamar {
    boolean tambah(ModelKamar kamar);
    boolean update(ModelKamar kamar);
    boolean hapus(int idKamar);
    ModelKamar getById(int idKamar);
    List<ModelKamar> getAll();
    List<ModelKamar> getByStatus(String status);
    boolean updateStatus(int idKamar, String status);
}
