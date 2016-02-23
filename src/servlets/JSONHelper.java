package servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import webservice.client.DataClient;

/**
 * Servlet implementation class JSONHelper
 */
@WebServlet("/JSONHelper")
public class JSONHelper extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public JSONHelper() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Object dataOb = DataClient.getData();
		JSONArray data = (JSONArray) dataOb;
//		request.setAttribute("array", data);
		response.setContentType("text/html");
		PrintWriter pw = response.getWriter();
		String out = "<table><tr><td>id</td><td>english title</td><td>original title</td><td>author</td><td>year</td><td>country</td><td>genre</td><td>price</td><td>left</td></tr>";
		for(Object ob : data){
			JSONObject book = (JSONObject) ob;
			out+="<tr>";
			out+="<td>"+book.get("id")+"</td>";
			out+="<td>"+book.get("englishTitle")+"</td>";
			out+="<td>"+book.get("originalTitle")+"</td>";
			out+="<td>"+book.get("author")+"</td>";
			out+="<td>"+book.get("year")+"</td>";
			out+="<td>"+book.get("country")+"</td>";
			out+="<td>"+book.get("genre")+"</td>";
			out+="<td>"+book.get("price")+"</td>";
			out+="<td>"+book.get("left")+"</td>";
			out+="</tr>";
		}
		out += "</table>";
		pw.print(out);
	}
	

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//doGet(request, response);
		throw new UnavailableException("");
		//return;
	}

}
