/**
 * 
 */
package edu.usc.csci571.data;

/**
 * This class holds the data for an IMDB record.
 * 
 * @author mohit.aggarwal
 * 
 */
public class IMDBRecord {

	private String cover;
	private String title;
	private String year;
	private String directors;
	private String ratings;
	private String details;

	/**
	 * @return the cover
	 */
	public String getCover() {
		return cover;
	}

	/**
	 * @param cover
	 *            the cover to set
	 */
	public void setCover(String cover) {
		this.cover = cover;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the year
	 */
	public String getYear() {
		return year;
	}

	/**
	 * @param year
	 *            the year to set
	 */
	public void setYear(String year) {
		this.year = year;
	}

	/**
	 * @return the directors
	 */
	public String getDirectors() {
		return directors;
	}

	/**
	 * @param directors
	 *            the directors to set
	 */
	public void setDirectors(String directors) {
		if(directors.equalsIgnoreCase("NA")){
			this.directors = directors;
		}else{
			this.directors = directors.substring(0, directors.length()-1);
		}
	}

	/**
	 * @return the ratings
	 */
	public String getRatings() {
		return ratings;
	}

	/**
	 * @param ratings
	 *            the ratings to set
	 */
	public void setRatings(String ratings) {
		this.ratings = ratings;
	}

	/**
	 * @return the details
	 */
	public String getDetails() {
		return details;
	}

	/**
	 * @param details
	 *            the details to set
	 */
	public void setDetails(String details) {
		this.details = details;
	}

	public String toString() {
		StringBuffer tostrBuffer = new StringBuffer();
		tostrBuffer.append("cover:" + cover + "title:" + title + "year:" + year
				+ "directors:" + directors + "rating:" + ratings + "details:"
				+ details + "\n");

		return tostrBuffer.toString();
	}
}
