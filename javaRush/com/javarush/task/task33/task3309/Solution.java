package com.javarush.task.task33.task3309;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.StringWriter;

/*
Комментарий внутри xml
*/
public class Solution {
    public static String toXmlWithComment(Object obj, String tagName, String comment) {
        String stringOut = null;
        StringWriter writer = new StringWriter();
        try {
            //создаем дерево, состоящее из тэгов
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();
            document.setXmlStandalone(false);

            JAXBContext context = JAXBContext.newInstance(obj.getClass());
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            //делаем маршалинг в объект document
            marshaller.marshal(obj, document);

            //делаем CDATA, если нужно
            changeTextToCDATA(document, document);

            //добавляем комменты
            NodeList nodeList = document.getElementsByTagName(tagName);
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node element = nodeList.item(i);
                element.getParentNode().insertBefore(document.createComment(comment), element);
            }

            /**
             * DOMSource(document) - источник xml - дерева
             * new StreamResult(writer) - поток, который запишет xml-дерево во writer
             */
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(new DOMSource(document), new StreamResult(writer));

            stringOut = writer.toString();
        }
        catch (Exception e){}
        finally {
            try {
                writer.close();
            }
            catch (IOException e){}
        }

        return stringOut;

    }

    private static void changeTextToCDATA(Node mainNode, Document document){
        if (mainNode.hasChildNodes()){
            NodeList children = mainNode.getChildNodes();
            for (int i = 0; i < children.getLength(); i++) {
                Node node = children.item(i);
                if (node.getNodeType() == Node.TEXT_NODE){
                    if (node.getTextContent().matches(".*[<>&'].*")) {

                        Node newNode = document.createCDATASection(node.getTextContent());
                        node.getParentNode().replaceChild(newNode, node);
                    }
                }
                else
                    changeTextToCDATA(node, document);
            }
        }
    }

    @XmlType(name = "first")
    @XmlRootElement
    public static class First {
        public String[] second = new String[]{
                "123 /' ",
                ""};
    }

    public static void main(String[] args) {
        String tagName = "1second";
        String comment = "it's a comment";
        String text = toXmlWithComment(new First(), tagName, comment);
        System.out.println(text);

    }
}
