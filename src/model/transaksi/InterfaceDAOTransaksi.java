package model.transaksi;

import java.util.List;

public interface InterfaceDAOTransaksi {
    boolean tambah(ModelTransaksi t);
    boolean update(ModelTransaksi t);
    boolean hapus(int idTransaksi);
    ModelTransaksi getById(int idTransaksi);
    List<ModelTransaksi> getAll();
    List<ModelTransaksi> getByStatus(String status);
    boolean updateStatus(int idTransaksi, String status);
}
