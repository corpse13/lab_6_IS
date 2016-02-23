package lab_6_IS;

//Class to save changes in cells

public class Changes {
	private int row, column, id;
	private Object value;
	
	public Changes(){
		
	};
	public Changes(int row, int col, ExtendedTableModel model){
		if(col != -1){
			setRow(row);
			setColumn(col);
			setValue(model.getValueAt(row, col));
			setID(Integer.parseInt(model.getValueAt(row, 0).toString()));
		}else{
			System.out.println("col = -1");
		}
	}
	
	public int getRow() {
		return row;
	}
	public void setRow(int row) {
		this.row = row;
	}
	public int getColumn() {
		return column;
	}
	public void setColumn(int column) {
		this.column = column;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	public int getID() {
		return id;
	}
	public void setID(int id) {
		this.id = id;
	}
}
