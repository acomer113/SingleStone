<h3>Introduction</h3>
This repository contains code to implement a contact management REST API.  The API is self-sufficient, requiring no external integration effort to run. 
The system is built upon the Spark (http://sparkjava.com/) web application micro framework and integrates with the H2 Java database (http://www.h2database.com/html/main.html)
for contact data management.  <br/><br/>

The project is built using the Apache Maven build and management tool (https://maven.apache.org/).  This tool is essential to build and run the 
REST API.

<h3>Building the Contact REST API</h3>
The packaging is a little unorthodox, but easy enough to navigate.<br/><br/>

Once the repository has been cloned or its archive downloaded and extracted, the following directory structure is seen:

<ul>
<li>SingleStone-Master</li>
<ul>
  <li>kacomer-contacts</li>
    <ul>
        <li>.settings</li>
        <li>src</li>
            <ul>
            <li>main</li>
                <ul>
                <li>java</li>
                    <ul>
                    <li>contact</li>
                    <li>db</li>
                    <li>util</li>
                    </ul>
                 </ul>
                <li>test</li>
                <ul>
                <li>resources</li>
                </ul>
             </ul>
     </ul>
</ul>
</ul>
            
 To build the contact management REST API system, navigate to the <em>\SingleStone-Master\kacomer-contacts</em> directory.  The system is built with Maven using the following
 command:
 
      mvn clean compile assembly:single
      
 Successful execution of this command will create a new <em>\target</em> directory as shown below:
 
<ul>
<li>SingleStone-Master</li>
<ul>
  <li>kacomer-contacts</li>
    <ul>
        <li>.settings</li>
        <li>src</li>
            <ul>
            <li>main</li>
                <ul>
                <li>java</li>
                    <ul>
                    <li>contact</li>
                    <li>db</li>
                    <li>util</li>
                    </ul>
                 </ul>
                <li>test</li>
                <ul>
                <li>resources</li>
                </ul>
             </ul>
        <li>target</li>     
     </ul>
</ul>
</ul>

<h3>Running the Contact REST API</h3>

Navigate to the new <em>\target</em> directory and verify that the following executable .jar file exists:  

  <em>ContactAPI-KAComer.jar</em>
  
The system is launched with the following command:

     java -jar ContactAPI-KAComer.jar
          
As the system starts, the database is created and the statements used to create the CONTACTS database are logged to the console.  Requests are received at the 
root endpoint: http://localhost:4567/.

As specified, the system is configured to accept JSON input for the following operations:
<ul>
<li>Create Contact - HTTP POST</li>
<li>Update Contact - HTTP PUT</li>
</ul>

URL path parameters are used in the remaining actions:
<ul>
<li>Get Contact List - HTTP GET</li>
<li>Get Contact Call List - HTTP GET</li>
<li>Get Specific Contact - HTTP GET</li>
<li>Delete Contact - HTTP DELETE</li>
</ul>

An example request for a contact list is shown below:

  http://localhost:4567/contacts/
  
<h3>Testing</h3>

Testing of the system was performed using the Postman utility (https://www.postman.com/).  The test suite used was exported from the Postman environment and is contained
in the <em>\src\test\resources</em> directory referenced earlier.  The cases in this file (<em>SingleStoneExercise_PostmanCollection_KAComer.json</em>) may be imported into the Postman tool
if available.

<h3>Notes and Remaining Work</h3>

There are known limitations to the current implementation of the Contact REST API to include:
<ul>
<li>The system will accept duplicate contact entries</li>
<li>Contact updates require that all fields, to include those that are not to be modified, be included in the request. Blank fields will overwrite existing data</li>
<li>While the functional test suite is fairly complete, there is a unit test deficiency.  Integrating unit tests into the Spark framework will require additional investigation</li>
</ul>







    
