package csci498.lunchlist;

import java.util.Date;

/**
 * Restaurant models a restaurant by holding information about the
 * restaurant's name, address, and establishment type (e.g. sit-down)
 */
public class Restaurant {

	private String name;
	private String address;
	private String type;
	private Date   date;
	
	public Restaurant() {
		name    = "";
		address = "";
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(final String name) {
		this.name = name;
	}
	
	public String getAddress() {
		return address;
	}
	
	public void setAddress(final String address) {
		this.address = address;
	}

	public String getType() {
		return type;
	}
	
	public void setType(final String type) {
		this.type = type;
	}
	
	public void setDate(final Date date) {
		this.date = date;
	}
	
	public Date getDate() {
		return date;
	}
	
	@Override
	public String toString() {
		return getName();
	}
}
