package com.alterbeef.DriveUploader;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

import com.google.gdata.client.authn.oauth.*;
import com.google.gdata.client.docs.*;
import com.google.gdata.data.*;
import com.google.gdata.data.batch.*;
import com.google.gdata.data.docs.*;
import com.google.gdata.util.*;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;

import org.xml.sax.*;
import org.w3c.dom.*;

@SuppressWarnings("unused")
public class Uploader {

	private String account = null;
	private String password = null;

	public Uploader(String _file) {
		testFile(_file);	// test declared file
		readXML();			// read config from xml
		//testLogin();		// test login
			// create doc
			// push doc
	}

	public static void main(String[] args){
		if(args.length != 1){
			System.out.println("\nUsage: uploader  <file> ");
			return;
		}
		new Uploader(args[0]);
	}

	private boolean testFile(String _file){
		File sourceFile = new File(_file);
		if(sourceFile.exists()){
			if(sourceFile.isFile()){
				if(sourceFile.canRead()){
					System.out.println("File test positive");
					return true;
				}else{
					System.out.println("Cannot read file: " + sourceFile.getAbsolutePath());
				}
			}else{
				System.out.println("Not a file: " + sourceFile.getAbsolutePath());
			}
		}else{
			System.out.println("File does not exist: " + sourceFile.getAbsolutePath());
		}
		return false;
	}
	
	private void testLogin(){
	    DocsService service = new DocsService("MyDocumentsListIntegration-v1");
	    try {
			service.setUserCredentials(account, password);
		} catch (AuthenticationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private boolean readXML(){
        Document dom;
        // Make an  instance of the DocumentBuilderFactory
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            // use the factory to take an instance of the document builder
            DocumentBuilder db = dbf.newDocumentBuilder();
            // parse using the builder to get the DOM mapping of the    
            // XML file
            dom = db.parse("Uploader.xml");

            Element doc = dom.getDocumentElement();

            account = getTextValue(account, doc, "account");
            if (account != null) {
                if (!account.isEmpty()){
                	System.out.println("Read account: " + account);
                }else{
                	System.out.println("Account name empty");
                	return false;
                }
            }else{
            	System.out.println("Could not read XML account");
            	return false;
            }

            password = getTextValue(password, doc, "password");
            if (password != null) {
                if (!password.isEmpty()){
                	System.out.println("Read password: length = " + password.length());
                	return true;
                }
                else
                	System.out.println("Password empty");
            }else{
            	System.out.println("Could not read XML password");
            }
        } catch (ParserConfigurationException pce) {
            System.out.println(pce.getMessage());
        } catch (SAXException se) {
            System.out.println(se.getMessage());
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }
        return false;
	}

	private String getTextValue(String def, Element doc, String tag) {
	    String value = def;
	    NodeList nl;
	    nl = doc.getElementsByTagName(tag);
	    if (nl.getLength() > 0 && nl.item(0).hasChildNodes()) {
	        value = nl.item(0).getFirstChild().getNodeValue();
	    }
	    return value;
	}
}
