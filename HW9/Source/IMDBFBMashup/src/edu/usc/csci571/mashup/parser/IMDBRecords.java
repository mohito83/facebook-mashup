/**
 * 
 */
package edu.usc.csci571.mashup.parser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import edu.usc.csci571.mashup.utilities.AsyncData;

/**
 * @author mohit.aggarwal
 * 
 */
public class IMDBRecords implements Serializable, AsyncData{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int size;
	private List<IMDBRecord> recordsList;
	private String errorMsg;
	private int status;

	public IMDBRecords() {
		size = -1;
		recordsList = new ArrayList<IMDBRecord>();
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
	public List<IMDBRecord> getRecordsList() {
		return recordsList;
	}

	/**
	 * @param recordsList
	 *            the recordsList to set
	 */
	public void setRecordsList(List<IMDBRecord> recordsList) {
		this.recordsList = recordsList;
	}

	public void setRecord(IMDBRecord record) {
		recordsList.add(record);
	}

	public IMDBRecord getRecord(int index) {
		return recordsList.get(index);
	}

	/**
	 * @return the errorMsg
	 */
	public String getErrorMsg() {
		return errorMsg;
	}

	/**
	 * @param errorMsg the errorMsg to set
	 */
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}

}
