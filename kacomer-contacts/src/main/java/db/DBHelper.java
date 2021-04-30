package db;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import contact.CallContact;
import contact.Contact;
import contact.ContactAddress;
import contact.ContactName;
import contact.ContactPhone;

public class DBHelper {
	private static String jdbcURL = "jdbc:h2:~/test";
	private static String jdbcUsername = "contactUser";
	private static String jdbcPassword = "";
	
	private static final String SQLSEP = "\',\'"; 
	private static final String QUOTE = "\'"; 

	private static final String dropSQL = "DROP TABLE CONTACTS";

	private static final String createSQL = "CREATE TABLE IF NOT EXISTS CONTACTS (\r\n" + "  ID INT AUTO_INCREMENT PRIMARY KEY,\r\n" +
			"  FNAME VARCHAR(20),\r\n" + "  MNAME VARCHAR(20),\r\n" + " LNAME VARCHAR(20),\r\n" + 
			"  STREET VARCHAR(50),\r\n" + " CITY VARCHAR(30),\r\n" + "STATE VARCHAR(30),\r\n" + "ZIP VARCHAR(10),\r\n" +
			"  HOMEPHONE VARCHAR(12),\r\n" + "  WORKPHONE VARCHAR(12),\r\n" + "  MOBILEPHONE VARCHAR(12),\r\n" +
			"  EMAIL VARCHAR(50)\r\n" + "  );";

	private static String createContactInsertSQL = "INSERT INTO CONTACTS(FNAME, MNAME, LNAME, STREET, CITY, STATE, ZIP, HOMEPHONE, WORKPHONE, MOBILEPHONE, EMAIL) VALUES ";

	private static String listContactsSQL = "SELECT * FROM CONTACTS";

	private static String getContactIDSQL = "SELECT * FROM CONTACTS WHERE ID = ?";
	
	private static String delContactIDSQL = "DELETE FROM CONTACTS WHERE ID = ?";
	
	private static String getCallListSQL = "SELECT FNAME, MNAME, LNAME, HOMEPHONE FROM CONTACTS ORDER BY LNAME, FNAME";
	
	private static String updateContactIDSQL = "UPDATE CONTACTS SET FNAME=?, MNAME=?, LNAME=?, STREET=?, CITY=?, STATE=?, ZIP=?, HOMEPHONE=?, WORKPHONE=?, MOBILEPHONE=?, EMAIL=? WHERE ID = ?";


	private static final String grantAccessSQL = "GRANT SELECT, INSERT, UPDATE, DELETE ON CONTACTS TO contactUser;";

	private static Connection getConnection() {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return connection;
	}

	public static void printSQLException(SQLException ex) {
		for (Throwable e: ex) {
			if (e instanceof SQLException) {
				e.printStackTrace();
				System.out.println("SQLState: " + ((SQLException) e).getSQLState());
				System.out.println("Error Code: " + ((SQLException) e).getErrorCode());
				System.out.println("Message: " + e.getMessage());
				Throwable t = ex.getCause();
				while (t != null) {
					System.out.println("Cause: " + t);
					t = t.getCause();
				}
			}
		}
	}

	public static void dropTable() throws SQLException {
		System.out.println(dropSQL);

		try {
			Connection connection = getConnection();
			Statement statement = connection.createStatement();
			statement.execute(dropSQL);
		} catch (SQLException e) {
			printSQLException(e);
		}
	}

	public static void createTable() throws SQLException {
		System.out.println(createSQL);

		try {
			Connection connection = getConnection();
			Statement statement = connection.createStatement();
			statement.execute(createSQL);
		} catch (SQLException e) {
			printSQLException(e);
		}
	}

	public static void grantTableAccess() throws SQLException {
		System.out.println(grantAccessSQL);

		try {
			Connection connection = getConnection();
			Statement statement = connection.createStatement();
			statement.execute(grantAccessSQL);
		} catch (SQLException e) {
			printSQLException(e);
		}
	}

	public static void createNewContact(Contact c) throws SQLException {
		String createContactSelect = "";
		StringBuffer sb = new StringBuffer();
		
		String homeph = "";
		String workph = "";
		String mobileph = "";

		for (ContactPhone ph : c.getPhone()) {
			if (ph.getType().equalsIgnoreCase(ContactPhone.homeType)) {
				homeph = ph.getNumber();
			} else if (ph.getType().equalsIgnoreCase(ContactPhone.workType)) {
				workph = ph.getNumber();
			} else if (ph.getType().equalsIgnoreCase(ContactPhone.mobileType)) {
				mobileph = ph.getNumber();
			} else {
				//ignore
			}
		}

		String selectPhone = homeph + SQLSEP + workph + SQLSEP + mobileph + SQLSEP;
		String selectPrePhone = "(" + QUOTE + c.getName().getFirst() + SQLSEP + c.getName().getMiddle() + SQLSEP + c.getName().getLast() + SQLSEP +
				c.getAddress().getStreet() + SQLSEP + c.getAddress().getCity() + SQLSEP + c.getAddress().getState() + SQLSEP + c.getAddress().getZip() + SQLSEP;
		String selectPostPhone = c.getEmail() + QUOTE +")";

		sb.append(selectPrePhone);
		sb.append(selectPhone);
		sb.append(selectPostPhone);
		createContactSelect = sb.toString();
		
		StringBuffer sqlStmt = new StringBuffer();
		sqlStmt.append(createContactInsertSQL);
		sqlStmt.append(createContactSelect);
		String createContactSQL = sqlStmt.toString();
		
		try {
			Connection connection = getConnection();
			Statement statement = connection.createStatement();
			statement.execute(createContactSQL);
		} catch (SQLException e) {
			printSQLException(e);
		}
	}

	public static List<Contact> listContacts() throws SQLException {
		List<Contact> contactList = new ArrayList<Contact>();
		try {
			Connection connection = getConnection();

			Statement statement = connection.createStatement(); 
			ResultSet rs = statement.executeQuery(listContactsSQL);
			while(rs.next()) {
				Contact contact = new Contact();
				contact.setID(rs.getInt(1));
				ContactName name = new ContactName(rs.getString(2), rs.getString(3), rs.getString(4));
				contact.setName(name);
				ContactAddress address = new ContactAddress(rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8));
				contact.setAddress(address);
				List<ContactPhone> phones = new ArrayList<ContactPhone>();
				String homephone = rs.getString(9);
				if (!homephone.isBlank()) {
					phones.add(new ContactPhone(homephone, ContactPhone.homeType));
				}
				String workphone = rs.getString(10);
				if (!workphone.isBlank()) {
					phones.add(new ContactPhone(workphone, ContactPhone.workType));
				}
				String mobilephone = rs.getString(11);
				if (!mobilephone.isBlank()) {
					phones.add(new ContactPhone(mobilephone, ContactPhone.mobileType));
				}
				contact.setPhone(phones);
				contact.setEmail(rs.getString(12));
				contactList.add(contact);
			};
			return contactList;
		} catch (SQLException e) {
			printSQLException(e);
		}

		return null;
	}

	public static Contact getContactID(String id) throws SQLException {
		Contact contact = new Contact();
		try {
			Connection connection = getConnection();
			PreparedStatement statement = connection.prepareStatement(getContactIDSQL);    
			statement.setString(1, id);
			ResultSet rs = statement.executeQuery();
			if (!rs.isBeforeFirst()) {
				return null;
			}
			while(rs.next()) {
				contact.setID(rs.getInt(1));
				ContactName name = new ContactName(rs.getString(2), rs.getString(3), rs.getString(4));
				contact.setName(name);
				ContactAddress address = new ContactAddress(rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8));
				contact.setAddress(address);
				List<ContactPhone> phones = new ArrayList<ContactPhone>();
				String homephone = rs.getString(9);
				if (!homephone.isBlank()) {
					phones.add(new ContactPhone(homephone, ContactPhone.homeType));
				}
				String workphone = rs.getString(10);
				if (!workphone.isBlank()) {
					phones.add(new ContactPhone(workphone, ContactPhone.workType));
				}
				String mobilephone = rs.getString(11);
				if (!mobilephone.isBlank()) {
					phones.add(new ContactPhone(mobilephone, ContactPhone.mobileType));
				}
				contact.setPhone(phones);
				contact.setEmail(rs.getString(12));
			};
			return contact;
		} catch (SQLException e) {
			printSQLException(e);
		}
		return null;
	}
		
	public static int delContactID(String id) throws SQLException {
		int count = 0;
		try {
			Connection connection = getConnection();
			PreparedStatement statement = connection.prepareStatement(delContactIDSQL);    
			statement.setString(1, id);
			count = statement.executeUpdate();
		} catch (SQLException e) {
			printSQLException(e);
		}
		return count;
	}
	
	public static List<CallContact> getCallList() throws SQLException {
		List<CallContact> callList = new ArrayList<CallContact>();
		try {
			Connection connection = getConnection();

			Statement statement = connection.createStatement(); 
			ResultSet rs = statement.executeQuery(getCallListSQL);
			while(rs.next()) {
				String num = rs.getString(4);
				System.out.println("number is: " + num);
				if(num.isBlank()) {
					continue;
				}
				CallContact callContact = new CallContact();
				ContactName name = new ContactName(rs.getString(1), rs.getString(2), rs.getString(3));
				callContact.setName(name);
				callContact.setPhone(num);
				callList.add(callContact);
			};
			return callList;
		} catch (SQLException e) {
			printSQLException(e);
		}

		return null;
	}
	
	public static int updateContact(Contact contact, String id) throws SQLException {
		int count=0;
		try {
			Connection connection = getConnection();
			PreparedStatement statement = connection.prepareStatement(updateContactIDSQL);    
			statement.setString(1, contact.getName().getFirst());
			statement.setString(2, contact.getName().getMiddle());
			statement.setString(3, contact.getName().getLast());
			statement.setString(4, contact.getAddress().getStreet());
			statement.setString(5, contact.getAddress().getCity());
			statement.setString(6, contact.getAddress().getState());
			statement.setString(7, contact.getAddress().getZip());
			String hphone = "";
			String wphone = "";
			String mphone = "";
			for (ContactPhone ph : contact.getPhone()) {
				if (ph.getType().equalsIgnoreCase(ContactPhone.homeType)) {
					hphone = ph.getNumber();
				} else if (ph.getType().equalsIgnoreCase(ContactPhone.workType)) {
					wphone = ph.getNumber();
				} else if (ph.getType().equalsIgnoreCase(ContactPhone.mobileType)) {
					mphone = ph.getNumber();
				} else {
					//ignore
				}
			}
			statement.setString(8, hphone);
			statement.setString(9, wphone);
			statement.setString(10, mphone);
			statement.setString(11, contact.getEmail());
			statement.setString(12, id);		
			count = statement.executeUpdate();
		} catch (SQLException e) {
			printSQLException(e);
		}
		return count;
		
	}

}

