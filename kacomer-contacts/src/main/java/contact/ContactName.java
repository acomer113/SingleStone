package contact;

public class ContactName {
	private String first = "";
	private String middle = "";
	private String last = "";
	
	public ContactName() {};
	
	public ContactName(String fname, String mname, String lname) {
		this.first = fname;
		this.middle = mname;
		this.last = lname;
	}
	
	public String getFirst() {
		return first;
	}

	public void setFirst(String first) {
		this.first = first;
	}

	public String getMiddle() {
		return middle;
	}

	public void setMiddle(String middle) {
		this.middle = middle;
	}

	public String getLast() {
		return last;
	}

	public void setLast(String last) {
		this.last = last;
	}
	
    @Override
	public String toString() {
		return "name {" +
				"first='" + first + '\'' +
	            ", middle='" + middle + '\'' +
	            ", last=" + last + '\'' +
	            '}';
	}
}
