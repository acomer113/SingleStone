import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.delete;
import static spark.Spark.put;


import java.sql.SQLException;

import contact.ContactController;
import db.DBHelper;
import util.Path;

public class ContactManagement {

	public static void main(String[] args) {

		try {
			DBHelper.dropTable();
		} catch (SQLException e) {
			DBHelper.printSQLException(e);
		}

		try {
			DBHelper.createTable();
		} catch (SQLException e) {
			DBHelper.printSQLException(e);
		}

		try {
			DBHelper.grantTableAccess();
		} catch (SQLException e) {
			DBHelper.printSQLException(e);
		}

		//Map Routes For HTTP Request Types
		get(Path.LIST, (req, res) -> { return ContactController.listContacts(req, res);} );
		get(Path.CALLLIST, (req,res) -> {return ContactController.getCallList(req,res);} );
		post(Path.NEW, "application/json", (req, res) -> { return ContactController.createNewContact(req, res);} );
		put(Path.UPDATE, "application/json", (req, res) -> { return ContactController.updateContact(req, res);} );
		get(Path.GETID, (req, res) -> { return ContactController.getContactID(req, res);} );
		delete(Path.DELETE, (req, res) -> { return ContactController.delContactID(req, res);} );
		
	}
}