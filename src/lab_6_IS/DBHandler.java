package lab_6_IS;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

import javax.swing.JTable;

public class DBHandler {
	
	private static Connection conn = null;
	private static Statement stmt = null;
	private static ResultSet rs = null;
	private static String query = "SELECT * FROM test";
	private static String DB_URL;
	
	public static void openDB(String dbPath, JTable table){
		DB_URL = "jdbc:ucanaccess://" + dbPath.replaceAll("\\\\", "/");
		try {
			Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
			conn = DriverManager.getConnection(DB_URL);
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = stmt.executeQuery(query);
			int i = 0;
			ResultSetMetaData md = rs.getMetaData();
			int colCount = md.getColumnCount();
			if(rs != null){
				int rowCount = getRowCount(rs);
				Object[][] rowData = new Object[rowCount][colCount];
				String[] colNames = new String[colCount];
				while(rs.next()){
					for(int j = 1; j <= colCount; j++){
						if(j == 1){
							rowData[i][j-1] = rs.getInt(j);
							colNames[j-1] = md.getColumnLabel(j);
						}
						rowData[i][j-1] = rs.getObject(j);
						colNames[j-1] = md.getColumnLabel(j);
					}
					i++;	
				}
				rs.close();
				table.setModel(new ExtendedTableModel(rowData, colNames));
			}
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}finally {
			if(stmt != null)
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			if(conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
	}
	
	public static int getRowCount(ResultSet rs){
		int count = 0;
		try {
			rs.last();
			count = rs.getRow();
			rs.beforeFirst();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}
	
	public static void addToDB(Integer[] integers, JTable tab, List<Changes> list){
		ExtendedTableModel model = (ExtendedTableModel) tab.getModel();
		if(integers.length != 0 | list.isEmpty() == false){
			try {
				Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
				conn = DriverManager.getConnection(DB_URL);
				stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				System.out.println("connection opened");
			} catch (ClassNotFoundException | SQLException e1) {
				e1.printStackTrace();
			}
		}
		if(integers.length != 0){
			String colLabels = "";
			for(int i = 0; i < integers.length; i++){
				String data = "";
				for(int j = 1; j < tab.getColumnCount() - 2; j++){
					data += "'" + tab.getValueAt(integers[i], j) + "', ";
					if(i == 0){
						colLabels += tab.getColumnName(j) + ", ";
					}
				}
				data+= "'" + tab.getValueAt(integers[i], tab.getColumnCount() - 1) + "'";
				if(i == 0)colLabels += tab.getColumnName(tab.getColumnCount() - 1);
				String addQuery = "INSERT INTO test (" + colLabels + ") "
						+ "VALUES (" + data + ")";
				System.out.println(addQuery);
				try {
					stmt.executeUpdate(addQuery);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		if(list.isEmpty() == false){
			for(Changes ch : list){
				if(Arrays.asList(integers).contains(ch.getID())){
					continue;
				}
				String updateQuery = "UPDATE test SET " + model.getColumnName(ch.getColumn()) + "=" + ch.getValue() + " WHERE id=" + ch.getID();
				try {
					stmt.executeUpdate(updateQuery);
					
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}else{
			System.out.println("list is empty");
		}
		
		if(Main.getRemovedRows().isEmpty() == false){
			for(Integer i : Main.getRemovedRows()){
				String deleteQuery = "DELETE FROM test WHERE id = " + i;
				try {
					stmt.executeUpdate(deleteQuery);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
		try{
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			if(stmt != null)
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			if(conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
	}
}
