/**
 * 
 */
package edu.usc.csci571.parser;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import edu.usc.csci571.data.IMDBRecord;
import edu.usc.csci571.data.IMDBRecords;

/**
 * This class parses the IMDB Record XML file and populate corresponding data
 * structures
 * 
 * @author mohit.aggarwal
 * 
 */
public class IMDBRecordsParser {

	/**
	 * This methd parses the XML file
	 * 
	 * @param is
	 * @return
	 * @throws ParserConfigurationException 
	 * @throws IOException 
	 * @throws SAXException 
	 */
	public static IMDBRecords parseIMDBRecords(InputStream is) throws ParserConfigurationException, SAXException, IOException {
		IMDBRecords records = new IMDBRecords();

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		Document root = null;

		// create instance of document builder
		DocumentBuilder builder = factory.newDocumentBuilder();

		// parse the xml data using the document builder. The document
		// builder will return the DOM representation of the xml file.
		root = builder.parse(new BufferedInputStream(is));
		
		records = getIMDBRecords(root);

		return records;
	}

	private static IMDBRecords getIMDBRecords(Document root) {
		IMDBRecords records = new IMDBRecords();

		Element docElement = root.getDocumentElement();

		String val = docElement.getAttribute("stat");

		if (val.equalsIgnoreCase("ok")) {
			NodeList recNode = docElement.getElementsByTagName("result");
			records.setSize(recNode.getLength());
			for (int i = 0; i < recNode.getLength(); i++) {
				Element n = (Element) recNode.item(i);
				IMDBRecord record = new IMDBRecord();
				record.setCover(n.getAttribute("cover"));
				record.setDetails(n.getAttribute("details"));
				record.setDirectors(n.getAttribute("director"));
				record.setRatings(n.getAttribute("rating"));
				record.setTitle(n.getAttribute("title"));
				record.setYear(n.getAttribute("year"));
				records.setRecord(record);
			}
		} else {
			records.setSize(0);
		}

		return records;
	}

	/**
	 * This method takes the imdb records and convert them in JSON format.
	 * 
	 * @param records
	 * @return
	 */
	public static String getJSONString(IMDBRecords records) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("{\"results\":{");

		buffer.append("\"result\":[");
		if (records.getSize() > 0) {
			for (int i = 0; i < records.getSize(); i++) {
				IMDBRecord record = records.getRecord(i);
				buffer.append("{\"cover\":\"" + record.getCover()
						+ "\", \"title\":\"" + record.getTitle()
						+ "\", \"year\":\"" + record.getYear()
						+ "\", \"director\":\"" + record.getDirectors()
						+ "\", \"rating\":\"" + record.getRatings()
						+ "\", \"details\":\"" + record.getDetails() + "\"}");

				if (i != records.getSize() - 1) {
					buffer.append(",");
				}
			}

		}
		buffer.append("]");
		buffer.append("}}");
		return buffer.toString();
	}
}
