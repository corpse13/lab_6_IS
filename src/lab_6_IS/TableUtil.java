package lab_6_IS;

import java.util.ArrayList;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class TableUtil {
	
	public static ArrayList<Object[]> getSelectedList(JTable table){
		ArrayList<Object[]> list = new ArrayList<Object[]>();
		int[] indxs = table.getSelectedRows();
		for(int i : indxs){
			Object obj[] = new Object[table.getColumnCount()];
			for(int j = 0; j < table.getColumnCount(); j++){
				obj[j] = table.getValueAt(i, j);
			}
			list.add(obj);
		}
		return list;
	}
	
	public static void removeSelected(JTable table){
		int[] indxs = table.getSelectedRows();
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		for(int i : indxs){
			Main.getRemovedRows().add(Integer.parseInt(table.getValueAt(i, 0).toString()));
			model.removeRow(i);
		}
		model.fireTableDataChanged();
	}
	
	public static void addRowAt(JTable table, Object[] obj, int index){
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		model.insertRow(index, obj);
		model.fireTableDataChanged();
	}
	
	public static void selectMultipleRow(JTable table, ArrayList<Integer> sels){
		table.setRowSelectionInterval(sels.get(0), sels.get(sels.size()-1));
	}

}
