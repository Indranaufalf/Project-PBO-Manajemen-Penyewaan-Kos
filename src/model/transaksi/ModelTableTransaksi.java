package model.transaksi;

import javax.swing.table.AbstractTableModel;
import java.text.SimpleDateFormat;
import java.util.List;

public class ModelTableTransaksi extends AbstractTableModel {

    private List<ModelTransaksi> data;
    private final String[] namaKolom = {"ID", "No. Kamar", "Penyewa", "Tgl Masuk", "Tgl Keluar", "Durasi", "Total Bayar", "Status", "Keterangan"};
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    public ModelTableTransaksi(List<ModelTransaksi> data) {
        this.data = data;
    }

    public void setData(List<ModelTransaksi> data) {
        this.data = data;
        fireTableDataChanged();
    }

    public ModelTransaksi getTransaksiAt(int row) {
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
        ModelTransaksi t = data.get(row);
        switch (col) {
            case 0: return t.getIdTransaksi();
            case 1: return t.getNomorKamar();
            case 2: return t.getNamaPenyewa();
            case 3: return t.getTanggalMasuk() != null ? sdf.format(t.getTanggalMasuk()) : "-";
            case 4: return t.getTanggalKeluar() != null ? sdf.format(t.getTanggalKeluar()) : "-";
            case 5: return t.getDurasiBuilan() + " bulan";
            case 6: return "Rp " + String.format("%,.0f", t.getTotalBayar());
            case 7: return t.getStatusPembayaran();
            case 8: return t.getKeterangan() != null ? t.getKeterangan() : "-";
            default: return "";
        }
    }
}