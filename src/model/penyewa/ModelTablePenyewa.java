package model.penyewa;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class ModelTablePenyewa extends AbstractTableModel {

    private List<ModelPenyewa> data;
    private final String[] namaKolom = {"ID", "Nama", "NIK", "No. HP", "Email", "Alamat Asal", "Pekerjaan", "Kamar"};

    public ModelTablePenyewa(List<ModelPenyewa> data) {
        this.data = data;
    }

    public void setData(List<ModelPenyewa> data) {
        this.data = data;
        fireTableDataChanged();
    }

    public ModelPenyewa getPenyewaAt(int row) {
        return data.get(row);
    }

    @Override public int getRowCount()    { return data.size(); }
    @Override public int getColumnCount() { return namaKolom.length; }
    @Override public String getColumnName(int col) { return namaKolom[col]; }

    @Override
    public Object getValueAt(int row, int col) {
        ModelPenyewa p = data.get(row);
        switch (col) {
            case 0: return p.getIdPenyewa();
            case 1: return p.getNama();
            case 2: return p.getNik();
            case 3: return p.getNoHp();
            case 4: return p.getEmail();
            case 5: return p.getAlamatAsal();
            case 6: return p.getPekerjaan();
            case 7: return (p.getNomorKamar() != null && !p.getNomorKamar().isEmpty())
                           ? p.getNomorKamar() : "-";
            default: return "";
        }
    }
}
