package webservice.client;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import org.json.simple.JSONArray;
import org.json.simple.JSONValue;

import webservice.endpoint.DataHelper;

public class DataClient {
	public static Object getData() {
//		System.out.println("start client");
		JSONArray message = null;
		URL url;
		try {
			url = new URL("http://localhost:9900/endpoint.webservice/books?wsdl");
			QName qname = new QName("http://endpoint.webservice/", "DataHelperImplService");
			Service service = Service.create(url, qname);
			DataHelper helper = service.getPort(DataHelper.class);
//			System.out.println("get data");
			Object ms = JSONValue.parse(helper.getPriceList());
//			JSONObject jObj = (JSONObject) ms;
			message = (JSONArray) ms;
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
//		System.out.println("stop client");
		return message;
	}
}
