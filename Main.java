import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        File file = new File("./src/doc.kml");
        try (InputStream inputStream = new FileInputStream(file)) {
            Document docKml = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(inputStream);
            NodeList placemarkList = docKml.getElementsByTagName("Placemark");
            Node n = docKml.getFirstChild();

            String xpathExpression = "/Document/[name]";
            XPathFactory xpf = XPathFactory.newInstance();
            XPath xpath = xpf.newXPath();
            Element userElement = (Element) xpath.evaluate("kml/Document/name", docKml,
                    XPathConstants.NODE);
            String fileName = userElement == null ? "no_name" : userElement.getTextContent();
            System.out.println(fileName);

            List<String> stringList = new ArrayList<>();
            for (int i = 0; i < placemarkList.getLength(); i++) {
                Node nNode = placemarkList.item(i);
                System.out.println("\nCurrent Element :");
                System.out.print(nNode.getNodeName() + "\n");
                NodeList childNodeList = nNode.getChildNodes();
                System.out.println("child " + nNode.hasChildNodes());
                for (int j = 0; j < childNodeList.getLength(); j++) {
                    Node currentChildNode = childNodeList.item(j);
                    if (currentChildNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element element = (Element) currentChildNode;
                        if (element.getTagName().equalsIgnoreCase("name")) {
                            System.out.println("name "+element.getTextContent());
                        }
                        if (element.getTagName().equalsIgnoreCase("Polygon")){
                            System.out.println("Polygon....");
                            StringWriter writer = new StringWriter();
                            Transformer transformer = TransformerFactory.newInstance().newTransformer();
                            transformer.transform(new DOMSource(element), new StreamResult(writer));
                            String xmlStr = writer.toString().;
                            System.out.println(xmlStr);
                        }
                    }
                }
                /*StringWriter writer = new StringWriter();
                Transformer transformer = TransformerFactory.newInstance().newTransformer();
                transformer.transform(new DOMSource(nNode), new StreamResult(writer));
                String xmlStr = writer.toString();
                System.out.println(xmlStr);*/
                System.out.println("--------");
                System.out.println(placemarkList.getLength());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
    }
}
