/**
 * Restaurant models a restaurant by holding information about the
 * restaurant's name, address, and establishment type (e.g. sit-down)
 */

package csci498.lunchlist;

public class Restaurant {

	private String name;
	private String address;
	private String type;
	
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
	
	@Override
	public String toString() {
		return getName();
	}
}
