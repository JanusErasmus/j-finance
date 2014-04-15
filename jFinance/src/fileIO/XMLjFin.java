package fileIO;


import gui.parseUtils;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import content.Category;
import content.Content;
import content.Transaction;

public class XMLjFin
{
	public boolean newFile(String fileName, Content budget)
	{
//		Content simple = new Content();
//		Category een = new Category("Een",100,111);
//		Category subEen = new Category("subEen",1001,11101);
//		Category twee = new Category("Twee",200,222);
//		Category drie = new Category("drie",300,333);
//		
//		een.subCatagories.add(subEen);
//		
//		simple.categories.add(een);
//		simple.categories.add(twee);
//		simple.categories.add(drie);
		
		try {

			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			// root elements
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("Budget");
			doc.appendChild(rootElement);	
			
			//Income
			Element incomeElement = doc.createElement("Income");
			rootElement.appendChild(incomeElement);
			Element bankIncome = doc.createElement("bank");
			bankIncome.appendChild(doc.createTextNode(Double.toString(budget.income.bank)));
			incomeElement.appendChild(bankIncome);
			
			Element walletIncome = doc.createElement("wallet");
			walletIncome.appendChild(doc.createTextNode(Double.toString(budget.income.wallet)));
			incomeElement.appendChild(walletIncome);
			
			//Current Income
			Element currentElement = doc.createElement("Current");	
			rootElement.appendChild(currentElement);
			Element bankCurr = doc.createElement("bank");
			bankCurr.appendChild(doc.createTextNode(Double.toString(budget.currIncome.bank)));
			currentElement.appendChild(bankCurr);
			
			Element walletCurr = doc.createElement("wallet");
			walletCurr.appendChild(doc.createTextNode(Double.toString(budget.currIncome.wallet)));
			currentElement.appendChild(walletCurr);
			
			//Previous Income
			Element prevElement = doc.createElement("Previous");
			rootElement.appendChild(prevElement);
			Element bankPrev = doc.createElement("bank");
			bankPrev.appendChild(doc.createTextNode(Double.toString(budget.prevIncome.bank)));
			prevElement.appendChild(bankPrev);
			
			Element walletPrev = doc.createElement("wallet");
			walletPrev.appendChild(doc.createTextNode(Double.toString(budget.prevIncome.wallet)));
			prevElement.appendChild(walletPrev);
			
			
			//Categories of Budget			
			for(int k = 0 ; k < budget.categories.size(); k++)
			{
				rootElement.appendChild(categoryElement(doc, budget.categories.get(k)));
			}
			
			//entries in budget
			for(int k = 0; k < budget.entries.size(); k++)
			{
				rootElement.appendChild(transactionElement(doc, budget.entries.get(k)));
			}
			
			
//			rootElement.appendChild(categoryElement(doc, een));
//			rootElement.appendChild(categoryElement(doc, twee));
//			rootElement.appendChild(categoryElement(doc, drie));			
			
			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File("C:\\" + fileName + ".xml"));

			// Output to console for testing
			// StreamResult result = new StreamResult(System.out);

			transformer.transform(source, result);

			System.out.println("File saved!");

		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		}

		return false;
	}
	
	public boolean readFile(String fileName)
	{
		try {
			 
			File fXmlFile = new File("C:\\file.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();
	 
			System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
			NodeList nList = doc.getElementsByTagName("Staff");
			System.out.println("-----------------------");
	 
			for (int temp = 0; temp < nList.getLength(); temp++) {
	 
			   Node nNode = nList.item(temp);
			   if (nNode.getNodeType() == Node.ELEMENT_NODE) {
	 
			      Element eElement = (Element) nNode;
	 
			      System.out.println("First Name : " + getTagValue("firstname", eElement));
			      System.out.println("Last Name : " + getTagValue("lastname", eElement));
		          System.out.println("Nick Name : " + getTagValue("nickname", eElement));
			      System.out.println("Salary : " + getTagValue("salary", eElement));
	 
			   }
			}
		  } catch (Exception e) {
			return false;
		  }
		
		return true;
	}
	
	 private static String getTagValue(String sTag, Element eElement) {
			NodeList nlList = eElement.getElementsByTagName(sTag).item(0).getChildNodes();
		 
		        Node nValue = (Node) nlList.item(0);
		 
			return nValue.getNodeValue();
		  }
	 
	 private Element categoryElement(Document doc, Category cat)
	 {
		 Element e = doc.createElement("Category");

		 // Label
		 Element name = doc.createElement("name");
		 name.appendChild(doc.createTextNode(cat.name));
		 e.appendChild(name);

		 // Amount
		 Element amount = doc.createElement("amount");
		 amount.appendChild(doc.createTextNode(Double.toString(cat.amount)));
		 e.appendChild(amount);

		 //prevAmount
		 Element prevAmount = doc.createElement("prevAmount");
		 prevAmount.appendChild(doc.createTextNode(Double.toString(cat.prevAmount)));
		 e.appendChild(prevAmount);
		 
		 if(cat.subCatagories.size() > 0)
		 {
		 
			 for(int k = 0 ; k < cat.subCatagories.size(); k++)
			 {
				 //subCats
				 Element subCat = doc.createElement("subCategory");
				 
				 Category catS = cat.subCatagories.get(k);
				 
				 //Append this subcats values
				// Label
				 Element subName = doc.createElement("name");
				 subName.appendChild(doc.createTextNode(catS.name));
				 subCat.appendChild(subName);

				 // Amount
				 Element subAmount = doc.createElement("amount");
				 subAmount.appendChild(doc.createTextNode(Double.toString(catS.amount)));
				 subCat.appendChild(subAmount);

				 //prevAmount
				 Element subPrevAmount = doc.createElement("prevAmount");
				 subPrevAmount.appendChild(doc.createTextNode(Double.toString(catS.prevAmount)));
				 subCat.appendChild(subPrevAmount);
				 
				 e.appendChild(subCat);
			 }
		 
		 }

		 return e;
	 }
	 
	 private Element transactionElement(Document doc, Transaction tran)
	 {
		 Element e = doc.createElement("Entry");
		 
		 // Date
		 Element date = doc.createElement("date");
		 date.appendChild(doc.createTextNode(tran.date));
		 e.appendChild(date);
		 
		// description
		 Element desc = doc.createElement("desc");
		 desc.appendChild(doc.createTextNode(tran.description));
		 e.appendChild(desc);
		 
		// From
		 Element from = doc.createElement("from");
		 from.appendChild(doc.createTextNode(tran.from));
		 e.appendChild(from);
		 
		// Category
		 Element cat = doc.createElement("category");
		 cat.appendChild(doc.createTextNode(tran.category));
		 e.appendChild(cat);
		 
		// Category
		 Element amount = doc.createElement("amount");
		 amount.appendChild(doc.createTextNode(Double.toString(tran.amount)));
		 e.appendChild(amount);
		 
		 return e;
	 }
}
