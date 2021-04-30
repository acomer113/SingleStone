package contact;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "id", "name", "address", "phone", "email" })
public class Contact {

	private int id;
	private ContactName name;
	private ContactAddress address;
	private List<ContactPhone> phone;
	private String email;

	public Contact( ) {}

	public Contact(String fname, String mname, String lname, 
			String street, String city, String state, String zip, 
			String hphone, String wphone, String mphone, 
			String email) {

		this.name = new ContactName(fname, mname, lname);
		this.address = new ContactAddress(street, city, state, zip);
		if (!hphone.isBlank()) {
			this.phone.add(new ContactPhone(hphone, ContactPhone.homeType));
		}
		if(!wphone.isBlank()) {
			this.phone.add(new ContactPhone(hphone, ContactPhone.workType));
		}
		if(!mphone.isBlank()) {
			this.phone.add(new ContactPhone(hphone, ContactPhone.mobileType));
		}
		this.email = email;

	}

	public void setID(int id) {
		this.id = id;
	}

	public int getID() {
		return id;
	}

	public void setName(ContactName cname) {
		this.name = cname;
	}

	public ContactName getName() {
		return name;
	}

	public void setAddress(ContactAddress caddr) {
		this.address = caddr;
	}

	public ContactAddress getAddress() {
		return address;
	}

	public void setPhone(List<ContactPhone> cphone) {
		this.phone = cphone;
	}

	public List<ContactPhone> getPhone() {
		return phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	@Override
	public String toString() {

		return "name = " + name.toString() +
				"\naddress = " + address.toString() + 
				"\nphone = " +phone.toString() +
				"\nemail = " + getEmail() + 
				"\n";				
	} 

}
