package util;

public class Path {

	//Define 
	public static String NEW = "/contacts/"; //create new contact - POST
	public static String LIST = "/contacts/";  //get all contacts - GET
	public static String GETID = "/contacts/:id"; //get contact with {id} - GET
	public static String DELETE = "/contacts/:id"; //delete contact with {id} - DELETE
	public static String UPDATE = "/contacts/:id"; //update contact with {id} - PUT
	public static String CALLLIST = "/contacts/call-list"; //get call list - GET

}
