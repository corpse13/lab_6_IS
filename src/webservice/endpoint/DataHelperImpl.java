package webservice.endpoint;

import javax.jws.WebService;
import javax.swing.JTable;

import lab_6_IS.JSONHandler;

@WebService(endpointInterface = "webservice.endpoint.DataHelper")
public class DataHelperImpl implements DataHelper {
	
	private static JTable table;
	
	public DataHelperImpl(JTable table){
		setTable(table);
	}
	
	@Override
	public String getPriceList() {
		String message = JSONHandler.createJSON(getTable()).toJSONString();
		return message;
	}

	public static JTable getTable() {
		return table;
	}

	public static void setTable(JTable table) {
		DataHelperImpl.table = table;
	}
}
