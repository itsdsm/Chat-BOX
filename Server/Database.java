package com.socket;

/**
 *
 * @author Deepsagar Mandal
 */
import java.io.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.*;

public class Database {
    
    public String filePath;
    
    public Database(String filePath){
        this.filePath = filePath;
    }
    
    public boolean userExists(String username){
        
        try{
            File db = new File(filePath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(db);
            doc.getDocumentElement().normalize();
            
            NodeList nList = doc.getElementsByTagName("user");
            
            for (int i=0; i<nList.getLength();i++) {
                Node nNode = nList.item(i);
                if (nNode.getNodeType()==Node.ELEMENT_NODE) {
                    Element eElement = (Element)nNode;
                    if(getTagValue("username", eElement).equals(username)){
                        return true;
                    }
                }
            }
            return false;
        }
        catch(Exception e){
            System.out.println("user already exists");
            return false;
        }
    }
    
    public boolean checkLogin(String username, String password){
        
        if(!userExists(username)){ 
            return false; 
        }
        
        try{
            File db = new File(filePath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(db);
            doc.getDocumentElement().normalize();
            
            NodeList nList = doc.getElementsByTagName("user");
            
            for (int i=0;i<nList.getLength();i++) {
                Node nNode = nList.item(i);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element)nNode;
                    if(getTagValue("username",eElement).equals(username)&&getTagValue("password",eElement).equals(password)){
                        return true;
                    }
                }
            }
            return false;
        }
        catch(Exception e){
            System.out.println("user already exists");
            return false;
        }
    }
    
    public void addUser(String username, String password){
        
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(filePath);
            Node data = doc.getFirstChild();
            Element newuser = doc.createElement("user");
            Element newusername = doc.createElement("username"); newusername.setTextContent(username);
            Element newpassword = doc.createElement("password"); newpassword.setTextContent(password);
            newuser.appendChild(newusername); newuser.appendChild(newpassword); data.appendChild(newuser);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(filePath));
            transformer.transform(source, result);
	   } 
           catch(Exception e){
		System.out.println(e.getMessage());
	   }
	}
    
    public static String getTagValue(String sTag, Element eElement) {
	NodeList nlList = eElement.getElementsByTagName(sTag).item(0).getChildNodes();
        Node nValue = (Node) nlList.item(0);
	return nValue.getNodeValue();
  }
}