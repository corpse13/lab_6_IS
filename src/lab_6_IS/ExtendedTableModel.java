package lab_6_IS;

import java.util.Vector;

import javax.swing.table.DefaultTableModel;

//Custom table model

public class ExtendedTableModel extends DefaultTableModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ExtendedTableModel(Object rowData[][], Object columnNames[]) {
        super(rowData, columnNames);
     }
   
   public ExtendedTableModel() {
		
	}

   @Override
     public Class<?> getColumnClass(int col) {
       if (col == 0)       //second column accepts only Integer values
           return Integer.class;
       else return String.class;  //other columns accept String values
   }
   
   @Override
   public boolean isCellEditable(int row, int col) {
        switch (col) {
            case 0: return false;
            default: return true;
         }
   }
   
   public void addRow(Object[] rowData, ExtendedTableModel model){
        addRow(convertToVector(rowData, model));
   }
   
   protected Vector<Object> convertToVector(Object[] data, ExtendedTableModel model){
        if (data == null)
          return null;
        Vector<Object> vector = new Vector<Object>(data.length);
        for (int i = 0; i < data.length; i++){
        	if(i == 0){
        		data[i] = model.getRowCount()+1;
        		vector.add(data[i]);
        	}else{
        		vector.add(data[i]);
        	}
        }
        
        return vector;          
   }
}
