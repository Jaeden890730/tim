package com.mxic.oiplus.util;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XML {

	public XML() {
	}
	public static void main(String[] args) {
		XML XML1 = new XML();
	}

	public static Document xmlToDocument(String xmlStr) throws Exception{
		DocumentBuilder db = null;
		Document doc = null;
//		try{
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setNamespaceAware(true);
			db = dbf.newDocumentBuilder();
			doc = db.parse(new StringStream(xmlStr));
//		}
//		catch(Exception e){
//			e.printStackTrace();
//		doc = null;
//		}
		return doc;
	}

	public static Node findElementNode(String elementName, Node root){

		Node matchingNode = null;

		//Check to see if root is the desired element. If so return root.
		String nodeName = root.getNodeName();

		if((nodeName != null) & (nodeName.equals(elementName)))
		return root;

	//Check to see if root has any children if not return null
	if(!(root.hasChildNodes()))
	 return null;

 //Root has children, so continue searching for them
 NodeList childNodes = root.getChildNodes();
 int noChildren = childNodes.getLength();
 for(int i = 0; i < noChildren; i++){
	 if(matchingNode == null){
		 Node child = childNodes.item(i);
		 matchingNode = findElementNode(elementName,child);
	 } else break;
 }
 return matchingNode;
	}


	public static String getNodeValue(Node xnode){
		if(xnode.getChildNodes().getLength() > 0){
			return xnode.getChildNodes().item(0).getNodeValue();
		}
		else{
			return null;
		}
	}

	public static String getNodeValueFromRoot(String nodename, Node rootnode){
		return getNodeValue(findElementNode(nodename, rootnode));
	}
}
