package contact;

public class ContactPhone {

	public static final String homeType = "home";
	public static final String workType = "work";
	public static final String mobileType = "mobile";

	private String number;
	private String type;

	public ContactPhone() {}

	public ContactPhone(String num, String ptype) {
		this.number = num;
		this.type = ptype;
	}

	public void setNumber(String pnumber) {
		this.number = pnumber;
	}

	public String getNumber() {
		return number;
	}

	public void setType(String ptype) {
		this.type = ptype;
	}

	public String getType() {
		return type;
	}

	public String toString() {
		return "phone {" +
				"number='" + number + '\'' +
				", type='" + type + '\'' +
				'}';
	}

}
