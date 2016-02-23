package lab_6_IS;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import javax.swing.DropMode;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;

import webservice.endpoint.DataHelperPublisher;


public class Main {
	
	private static String[] colNames;
	private static Object[][] rowData;
	private static ExtendedTableModel model;
	private static JTable table;
	
	private static List<Changes> changesList = new ArrayList<Changes>();
	private static List<Integer> removedRows = new ArrayList<Integer>();
	
	private static File file;
	
	private static boolean isExited = false, shiftPressed = false;
	
	public static void main(String[] args){
		File def = new File("file2.txt");
		readFile(def);
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	try {
					GlobalScreen.registerNativeHook();
				} catch (NativeHookException e) {
					e.printStackTrace();
				}
            	NativeMouseListenerEventsHandler handler = new NativeMouseListenerEventsHandler();
            	GlobalScreen.addNativeMouseListener(handler);
            	GlobalScreen.addNativeMouseMotionListener(handler);
                initUI();
                publishEndpoint();
            }
        });
	}
	
	private static void initUI() {
		JFrame frame = new JFrame("lab_6");
		
		model = new ExtendedTableModel(rowData, colNames);
		table = new JTable(model);
		JPanel butPanel = new JPanel();
		
		
		
		GridLayout gl = new GridLayout(2, 1);
		frame.setLayout(gl);
		frame.addMouseListener(new MouseListener(){
			
			@Override
			public void mouseClicked(MouseEvent arg0) {
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				setExited(false);
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				setExited(true);
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
			}
			
		});
		
		table.addKeyListener(new KeyListener(){

			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_SHIFT)
					setShiftPressed(true);
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_SHIFT)
					setShiftPressed(false);
			}

			@Override
			public void keyTyped(KeyEvent e) {
			}
			
		});
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		table.setPreferredScrollableViewportSize(new Dimension(500, 150));
		table.setFillsViewportHeight(true);
		table.setAutoCreateRowSorter(true);
		table.setDragEnabled(true);
		table.setDropMode(DropMode.INSERT_ROWS);
		table.setTransferHandler(new TableRowTransferHandler(table));
		
		JScrollPane scrollPane = new JScrollPane(table);
		
		JFileChooser fc = new JFileChooser("C:\\Users\\User\\workspace\\lab_6_IS");
		
		JButton openBut = new JButton("open..");
		openBut.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				 int returnVal = fc.showOpenDialog(frame);
				 if (returnVal == JFileChooser.APPROVE_OPTION) {
		               file = fc.getSelectedFile();
		               model = new ExtendedTableModel(rowData, colNames);
		               table.setModel(model);
		               readFile(file);
		               changeModel((ExtendedTableModel) table.getModel());
		               model.fireTableDataChanged();
		         }
			}
			
		});
		
		JButton saveBut = new JButton("save");
		saveBut.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				int returnVal = fc.showSaveDialog(frame);
				if (returnVal == JFileChooser.APPROVE_OPTION){
					file = fc.getSelectedFile();
					saveFile(file, table);
					readFile(file);
					model = new ExtendedTableModel(rowData, colNames);
		            table.setModel(model);
					changeModel((ExtendedTableModel) table.getModel());
		            model.fireTableDataChanged();
				}
			}
		});
		
		JButton dbBut = new JButton("open db..");
		dbBut.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				int returnVal = fc.showOpenDialog(frame);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					DBHandler.openDB(fc.getSelectedFile().getAbsolutePath(), table);
					model = (ExtendedTableModel) table.getModel();
					model.fireTableDataChanged();
		            model.addTableModelListener(new TableModelListener(){
		       			@Override
		       			public void tableChanged(TableModelEvent e) {
		       				if(e.getColumn() != -1){
		       					Changes ch = new Changes(e.getFirstRow(), e.getColumn(), model);
		       					changesList.add(ch);
		       				}
		       			}
		       		});
		         }
			}
			
		});
		
		JButton dbSaveBut = new JButton("save db..");
		dbSaveBut.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				DBHandler.addToDB(findMaxIDs(table), table, changesList);
			}
			
		});
		
		JButton readXMLBut = new JButton("read XML..");
		readXMLBut.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				int returnVal = fc.showOpenDialog(frame);
				if (returnVal == JFileChooser.APPROVE_OPTION){
					file = fc.getSelectedFile();
					List<BookBean> bookList = XMLHandler.readXML(file);
					rowData = new Object[bookList.size()][];
					for(int i = 0; i < bookList.size(); i++){
						rowData[i] = bookList.get(i).toObjectArray();
					};
					colNames = BookBean.getTableHeaders();
					model = new ExtendedTableModel(rowData, colNames);
					table.setModel(model);
					changeModel((ExtendedTableModel) table.getModel());
		            model.fireTableDataChanged();
		        }
			}
		});
		
		JButton saveAsXML = new JButton("save as XML");
		saveAsXML.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				int returnVal = fc.showSaveDialog(frame);
				if (returnVal == JFileChooser.APPROVE_OPTION){
					file = fc.getSelectedFile();
					XMLHandler.writeXML(file, table, colNames);
				}
			}
		});
		
		JButton readJSON = new JButton("read JSON..");
		readJSON.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				int returnVal = fc.showOpenDialog(frame);
				if (returnVal == JFileChooser.APPROVE_OPTION){
					file = fc.getSelectedFile();
					model = new ExtendedTableModel(JSONHandler.getData(JSONHandler.readJSON(file)), BookBean.getTableHeaders());
					table.setModel(model);
					model.fireTableDataChanged();
				}
			}
			
		});
		
		JButton saveAsJSON = new JButton("save as JSON");
		saveAsJSON.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				int returnVal = fc.showSaveDialog(frame);
				if (returnVal == JFileChooser.APPROVE_OPTION){
					file = fc.getSelectedFile();
					JSONHandler.writeJSON(file, JSONHandler.createJSON(table));
				}
			}
			
		});
		
		JButton addBut = new JButton("add row");
		addBut.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				model.addRow(new Object[colNames.length], model);
				model.fireTableDataChanged();
			}
			
		});
		
		JButton delBut = new JButton("delete row");
		delBut.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				getRemovedRows().add(Integer.parseInt(table.getValueAt(table.getSelectedRow(), 0).toString()));
				model.removeRow(table.getSelectedRow());
			}
		});
		
		butPanel.setLayout(new GridLayout(5, 1));
		
		butPanel.add(addBut);
		butPanel.add(delBut);
		butPanel.add(openBut);
		butPanel.add(saveBut);
		butPanel.add(dbBut);
		butPanel.add(dbSaveBut);
		butPanel.add(readXMLBut);
		butPanel.add(saveAsXML);
		butPanel.add(readJSON);
		butPanel.add(saveAsJSON);

		
		frame.add(scrollPane);
		frame.add(butPanel);
		frame.pack();
		frame.setVisible(true);
		
	}
	
	public static void readFile(File file){
		try(FileInputStream fis = new FileInputStream(file)){
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
			String currLine = "";
			int i = 0, j;
			colNames = new String[10];
			rowData = new Object[numberOflines(br)][colNames.length];
			fis.getChannel().position(0);
			br = new BufferedReader(new InputStreamReader(fis));
			for(Object[] temp : rowData){
				for (int k = 0; k < temp.length; k++) {
					temp[k] = "";
				}
			}
			while((currLine = br.readLine()) != null){
				if(currLine.contains("PRD|")){
					currLine = currLine.substring(currLine.indexOf("PRD|") + 3, currLine.length());
					String pattern = "\\|";
					Scanner s = extracted(currLine).useDelimiter(pattern);
					j = 0;
					while(s.hasNext()){
						String temp = s.next();
						if(temp.isEmpty()) temp = "";
						rowData[i][j] = temp;
						j++;
					}
					i++;
				}
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		};
	}

	private static Scanner extracted(String currLine) {
		return new Scanner(currLine);
	}
	
	public static void saveFile(File file, JTable table){
		try {
			PrintWriter wr = new PrintWriter(file, "UTF-8");
			
			int [] columnIntInd = {table.getColumnModel().getColumnIndex("year"), table.getColumnModel().getColumnIndex("left")};
			boolean a = false;
			for(int col : columnIntInd){
				for(int row = 0; row < table.getRowCount(); row++){
					if(table.getValueAt(row, col).toString().matches("\\d+?") == false){
						JOptionPane.showMessageDialog(table, "not a number at" + row + ", " + col, "Error checking values", JOptionPane.ERROR_MESSAGE);
						a = false;
						break;
					}else a = true;
				}
			}
			for(int row = 0; row < table.getRowCount(); row++){
				if(table.getValueAt(row, 7).toString().matches("\\d+(\\.\\d+)?") == false){
					JOptionPane.showMessageDialog(table, "not a number at" + row + ", " + 7, "Error checking values", JOptionPane.ERROR_MESSAGE);
					a = false;
					break;
				}else a = true;
			}
			if(a == true){
				for(int row = 0; row < table.getRowCount(); row++){
					wr.write("PRD|");
					for(int col = 0; col < table.getColumnCount(); col++){
						String temp;
						if(table.getValueAt(row, col) == null) temp = "";
						else{
							temp = table.getValueAt(row, col).toString();
							if(temp == "-") temp = "";
						}
						wr.write(temp + "|");
					}
					wr.write("\r\n");
				}
				wr.close();	
			}
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
	}
	
	public static Integer[] findMaxIDs(JTable tab){
		int numOfAddedRows = tab.getRowCount() - rowData.length;
		Integer [] addRowsIndexes = new Integer[numOfAddedRows], indexes = new Integer [tab.getRowCount()], temp = new Integer[tab.getRowCount()];
		int colIDIndex = tab.getColumnModel().getColumnIndex("id");
		for(int i = 0; i < indexes.length; i++){
			indexes[i] = Integer.parseInt(tab.getValueAt(i, colIDIndex).toString());
			temp[i] = indexes[i];
		}
		Arrays.sort(temp);
		for(int i = 0; i < addRowsIndexes.length; i++){
			addRowsIndexes[i] = Arrays.asList(indexes).indexOf(Arrays.asList(temp).get(temp.length - i - 1));
		}
		return addRowsIndexes;
	}
	
	public static void changeModel(ExtendedTableModel m){
		m.setRowCount(0);
		for(Object[] temp : rowData){
			m.addRow(temp);
		}
		m.setColumnIdentifiers(colNames);
	}
		
	public static int numberOflines(BufferedReader br){
		int lines = 0;
		try {
			while(br.readLine() != null) lines++;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return lines;		
	}
	 
	public static void publishEndpoint(){
		 new DataHelperPublisher(getTable());
	}
	

	public static JTable getTable() {
		return table;
	}

	public static void setTable(JTable table) {
		Main.table = table;
	}

	public static boolean isExited() {
		return isExited;
	}

	public static void setExited(boolean isExited) {
		Main.isExited = isExited;
	}

	public static boolean isShiftPressed() {
		return shiftPressed;
	}

	public static void setShiftPressed(boolean shiftPressed) {
		Main.shiftPressed = shiftPressed;
	}

	public static List<Integer> getRemovedRows() {
		return removedRows;
	}

	public static void setRemovedRows(List<Integer> removedRows) {
		Main.removedRows = removedRows;
	}
	
	
	
}