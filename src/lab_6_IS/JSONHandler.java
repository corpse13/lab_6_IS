package lab_6_IS;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.swing.JTable;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JSONHandler {
	
	@SuppressWarnings("unchecked")
	public static JSONArray createJSON(JTable table){
		int rows = table.getRowCount();
		int cols = table.getColumnCount();
		JSONArray bookStore = new JSONArray();
		for(int row = 0; row < rows; row++){
			JSONObject book = new JSONObject();
			for(int col = 0; col < cols; col++){
				if(table.getValueAt(row, col) != null)
					book.put(table.getColumnName(col), table.getValueAt(row, col));
				else book.put(table.getColumnName(col), "");
			}
			bookStore.add(book);
		}
		return bookStore;
	}
	
	public static void writeJSON(File file, JSONArray arr){
		try {
			PrintWriter pw = new PrintWriter(file, "UTF-8");
			pw.write(arr.toJSONString());
			pw.flush();
			pw.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		} 
	}
	
	public static JSONArray readJSON(File file){
		JSONArray arr = new JSONArray();
		JSONParser parser = new JSONParser();
		try {
			Object obj = parser.parse(new FileReader(file));
			arr = (JSONArray) obj;
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
		return arr;
	}
	
	public static Object[][] getData(JSONArray arr){
		Object[][] data = new Object[arr.size()][];
		for(int ind = 0; ind < arr.size(); ind++){
			BookBean book = new BookBean();
			JSONObject obj = (JSONObject) arr.get(ind);
			book.setID(Integer.parseInt(obj.get("id").toString()));
			book.setEnglishTitle(obj.get("englishTitle").toString());
			book.setOriginalTitle(obj.get("originalTitle").toString());
			book.setAuthor(obj.get("author").toString());
			book.setYear(Integer.parseInt(obj.get("year").toString()));
			book.setCountry(obj.get("country").toString());
			book.setGenre(obj.get("genre").toString());
			book.setPrice(obj.get("price").toString());
			book.setLeft(Integer.parseInt(obj.get("left").toString()));
			data[ind] = book.toObjectArray();
		}
		return data;
	}
	
	
	
}
