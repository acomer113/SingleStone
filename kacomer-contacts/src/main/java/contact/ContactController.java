package contact;

import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import db.DBHelper;
import spark.Request;
import spark.Response;

public class ContactController {

	static DBHelper dbHelper = new DBHelper();
	static Logger logger = LoggerFactory.getLogger(ContactController.class);

	private static final String CONTACT_SUCCESS = "Contact Successfully Created";
	private static final String CONTACT_REMOVE = "Contact Successfully Removed";
	private static final String CONTACT_UPDATED = "Contact Successfully Updated";
	private static final String NO_RESULTS = "{\n}";
	private static final String CONT_TYPE = "Content-Type";
	private static final String TEXT_HTML = "text/html; charset=utf-8";
	private static final String APP_JSON = "application/json; charset=utf-8";

	public ContactController() { }

	public static String createNewContact(Request req, Response res) {
		Contact contact = new Contact();

		try {
			logger.info("Parsed and Escaped data passed to new Contact = \n" + req.body());
			ObjectMapper objectMapper = new ObjectMapper();
			contact = objectMapper.readValue(req.body(), Contact.class);
		} catch(Exception e) {
			e.printStackTrace();
			res.status(500);
		}

		try {
			DBHelper.createNewContact(contact);
		} catch (SQLException e) {
			DBHelper.printSQLException(e);
		}

		res.status(201);
		res.header(CONT_TYPE, TEXT_HTML);
		res.body(CONTACT_SUCCESS);
		return res.body();

	}

	public static String listContacts(Request req, Response res) {

		List<Contact> clist = null;
		String jsonString = "";
		String respBody = "";

		try {
			clist = DBHelper.listContacts();
		} catch (SQLException e) {
			DBHelper.printSQLException(e);
		}

		try {
			ObjectMapper objectMapper = new ObjectMapper();
			StringBuffer sb = new StringBuffer();
			sb.append("[");

			for (Contact contact : clist) {
				JsonNode jsonResp = objectMapper.valueToTree(contact);
				jsonString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonResp);

				logger.info("Contact String" + jsonString);
				sb.append(jsonString);
				sb.append(",");
			}

			sb.deleteCharAt(sb.length()-1);
			sb.append("]");
			respBody = sb.toString();

			logger.info("Response String" + respBody);
		} catch (Exception e) {
			res.status(500);
			e.printStackTrace();
		}
		res.status(200);
		res.header(CONT_TYPE, APP_JSON);
		res.body(respBody);
		return respBody;
	}

	public static String getContactID(Request req, Response res) {
		
		String id = req.params("id");
		Contact contact = new Contact();

		String jsonString = "";

		try {
			contact = DBHelper.getContactID(id);
		} catch (SQLException e) {
			DBHelper.printSQLException(e);
		}

		if (contact == null) {
			return NO_RESULTS;
		}
		
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode jsonResp = objectMapper.valueToTree(contact);
			jsonString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonResp);
		} catch (Exception e) {
			res.status(500);
			e.printStackTrace();
		}
		res.status(200);
		res.header(CONT_TYPE, APP_JSON);
		res.body(jsonString);
		return jsonString;
	}

	public static String delContactID(Request req, Response res) {
		String id = req.params("id");
		int recordsRemoved = 0;

		try {
			recordsRemoved = DBHelper.delContactID(id);
		} catch (SQLException e) {
			res.status(500);
			DBHelper.printSQLException(e);
		}

		if (recordsRemoved == 0) {
			res.status(204);
			return "Record (ID=" + id + ") Does Not Exist";
		} else {
			res.status(204);
			return CONTACT_REMOVE;
		}
	}
	
	public static String getCallList(Request req, Response res) {
		List<CallContact> clist = null;
		String jsonString = "";
		String respBody = "";

		try {
			clist = DBHelper.getCallList();
		} catch (SQLException e) {
			res.status(500);
			DBHelper.printSQLException(e);
		}

		try {
			ObjectMapper objectMapper = new ObjectMapper();
			StringBuffer sb = new StringBuffer();
			sb.append("[");

			for (CallContact callContact : clist) {
				JsonNode jsonResp = objectMapper.valueToTree(callContact);
				jsonString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonResp);

				logger.info("Contact String" + jsonString);
				sb.append(jsonString);
				sb.append(",");
			}

			sb.deleteCharAt(sb.length()-1);
			sb.append("]");
			respBody = sb.toString();

			logger.info("Response String" + respBody);
		} catch (Exception e) {
			e.printStackTrace();
		}
		res.status(200);
		res.header(CONT_TYPE, APP_JSON);
		res.body(respBody);
		return respBody;
	}	
	
	public static String updateContact(Request req, Response res) {
		String id = req.params("id");
		int recordsUpdated = 0;
		Contact contact = new Contact();

		try {

			logger.info("Parsed and Escaped data passed to updateContact = \n" + req.body());

			ObjectMapper objectMapper = new ObjectMapper();

			contact = objectMapper.readValue(req.body(), Contact.class);

		} catch(Exception e) {
			e.printStackTrace();
			res.status(500);
		}

		try {
			recordsUpdated = DBHelper.updateContact(contact,id);
		} catch (SQLException e) {
			DBHelper.printSQLException(e);
		}

		if (recordsUpdated == 0) {
			res.status(204);
			return "Contact (ID=" + id + ") Does Not Exist";
		} else {
			res.status(200);
			res.header(CONT_TYPE, TEXT_HTML);
			StringBuffer sb = new StringBuffer();
			sb.append(CONTACT_UPDATED);
			sb.append(" (ID=" + id + ")");
			String respBody = sb.toString();
			res.body(respBody);
			return respBody;
		}
	}

}
