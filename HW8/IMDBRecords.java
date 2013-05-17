/**
 * 
 */
package edu.usc.csci571.data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mohit.aggarwal
 * 
 */
public class IMDBRecords {

	private int size;
	private List recordsList;

	public IMDBRecords() {
		size = -1;
		recordsList = new ArrayList();
	}

	/**
	 * @return the size
	 */
	public int getSize() {
		return size;
	}

	/**
	 * @param size
	 *            the size to set
	 */
	public void setSize(int size) {
		this.size = size;
	}

	/**
	 * @return the recordsList
	 */
	public List getRecordsList() {
		return recordsList;
	}

	/**
	 * @param recordsList
	 *            the recordsList to set
	 */
	public void setRecordsList(List recordsList) {
		this.recordsList = recordsList;
	}

	public void setRecord(IMDBRecord record) {
		recordsList.add(record);
	}

	public IMDBRecord getRecord(int index) {
		return (IMDBRecord) recordsList.get(index);
	}

}
