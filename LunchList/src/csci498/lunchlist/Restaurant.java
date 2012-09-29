package csci498.lunchlist;

/**
 * Restaurant models a restaurant by holding information about the
 * restaurant's name, address, and establishment type (e.g. sit-down)
 */
public class Restaurant {

	private String         name;
	private String         address;
	private String         notes;
	private RestaurantType type;
	
	public Restaurant() {
		name    = "";
		address = "";
		notes   = "";
		type    = RestaurantType.SIT_DOWN;
	}
	
	public String getNotes() {
		return notes;
	}

	public void setNotes(final String notes) {
		this.notes = notes;
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

	public RestaurantType getType() {
		return type;
	}
	
	public void setType(final RestaurantType type) {
		this.type = type;
	}
	
	@Override
	public String toString() {
		return getName();
	}
}
