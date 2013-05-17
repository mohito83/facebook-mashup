/**
 * 
 */
package edu.usc.csci571.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import edu.usc.csci571.data.IMDBRecords;
import edu.usc.csci571.parser.IMDBRecordsParser;

/**
 * @author mohit.aggarwal
 * 
 */
public class FacebookIMDBMashupServlet extends HttpServlet {

	public static String PARSE_ERROR_TXT = "Unable to parse the response";
	public static String GENERIC_ERROR_TXT = "Unable to fetch the response";
	public static String URL_ERROR_TXT = "Unable to parse the URL";
	
	/**
	 * 
	 */
	public FacebookIMDBMashupServlet() {
		// TODO Auto-generated constructor stub
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter writer = response.getWriter();

		URL url;
		URLConnection connection = null;
		try {
			String title = (String)request.getParameter("Title");
			String mediaType = (String)request.getParameter("mediaType");
			mediaType = URLEncoder.encode(mediaType, "UTF-8");
			title = URLEncoder.encode(title, "UTF-8");
			
			//System.out.println("Inside the FacebookIMDBMashupServlet, parameters passed:"+title+" == "+mediaType);
			
			String phpQueryString = "http://cs-server.usc.edu:10137/hw8.php?Title="
					+ title + "&mediaType=" + mediaType;
			
			// Create connection
			url = new URL(phpQueryString);
			connection = url.openConnection();
			connection.setAllowUserInteraction(false);

			InputStream inputStream = url.openStream();

			// Get Response
			IMDBRecords records = IMDBRecordsParser.parseIMDBRecords(inputStream);
			
			//TO get the JSON string
			//if(records.getSize()>0){
				response.setStatus(200);
				writer.write(IMDBRecordsParser.getJSONString(records));
			/*}else{
				response.setStatus(200);
				
			}*/
			
		} catch ( UnsupportedEncodingException ue){
			response.setStatus(400);
			writer.write(URL_ERROR_TXT);
		} catch (MalformedURLException mfe){
			response.setStatus(400);
			writer.write(GENERIC_ERROR_TXT);
		} catch (ParserConfigurationException e) {
			response.setStatus(400);
			writer.write(PARSE_ERROR_TXT);
		} catch (SAXException e) {
			response.setStatus(400);
			writer.write(PARSE_ERROR_TXT);
		} catch (IOException e) {
			response.setStatus(400);
			writer.write(GENERIC_ERROR_TXT);
		} 

	}
	
	/*private String getErrorJSONString(Class exceptionType){
		int errorCode = -1;
		if(exceptionType == MalformedURLException.class){
			errorCode = 1000;
		}
		
		if(exceptionType == ParserConfigurationException.class){
			errorCode = 1001;
		}
		
		if(exceptionType == SAXException.class){
			errorCode = 1002;
		}
		
		if(exceptionType == IOException.class){
			errorCode = 1003;
		}
		return errorCode+"";
	}*/
}
