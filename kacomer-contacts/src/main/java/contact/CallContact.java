package contact;

public class CallContact {
	private ContactName name;
	private String phone;
	
	public CallContact() {}
	
	public void setName(ContactName cname) {
		this.name = cname;
	}

	public ContactName getName() {
		return name;
	}
	
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	@Override
	public String toString() {

		return "name = " + name.toString() +
				"\nphone = " + getPhone() +
				"\n";				
	} 
}
