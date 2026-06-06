package model.kamar;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class ModelTableKamar extends AbstractTableModel {

    private List<ModelKamar> data;
    private final String[] namaKolom = {"ID", "No. Kamar", "Tipe", "Harga/Bulan", "Status", "Fasilitas"};

    public ModelTableKamar(List<ModelKamar> data) {
        this.data = data;
    }

    public void setData(List<ModelKamar> data) {
        this.data = data;
        fireTableDataChanged();
    }

    public ModelKamar getKamarAt(int row) {
        return data.get(row);
    }

    @Override
    public int getRowCount() { return data.size(); }

    @Override
    public int getColumnCount() { return namaKolom.length; }

    @Override
    public String getColumnName(int col) { return namaKolom[col]; }

    @Override
    public Object getValueAt(int row, int col) {
        ModelKamar k = data.get(row);
        switch (col) {
            case 0: return k.getIdKamar();
            case 1: return k.getNomorKamar();
            case 2: return k.getTipe();
            case 3: return "Rp " + String.format("%,.0f", k.getHargaPerBulan());
            case 4: return k.getStatus();
            case 5: return k.getFasilitas();
            default: return "";
        }
    }
}
