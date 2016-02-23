package webservice.endpoint;

import javax.swing.JTable;
import javax.xml.ws.Endpoint;

public class DataHelperPublisher {
	public DataHelperPublisher(JTable table){
		System.out.println("Start publishing");
		Endpoint.publish("http://localhost:9900/endpoint.webservice/books", new DataHelperImpl(table));
		System.out.println("Published");
	}
}
