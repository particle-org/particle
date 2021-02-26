package com.particle.util.xml;

import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class XmlParseUtils {

    /**
     * 解析xml数据
     */
    public static ViewNode parse(InputStream in) throws Exception {
        if (in == null) {
            return null;
        }
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(in);
        doc.getDocumentElement().normalize();
        Element rootElement = doc.getDocumentElement();
        return parseNode(rootElement);
    }

    /**
     * 解析node
     *
     * @param node
     * @return
     */
    private static ViewNode parseNode(Node node) {
        if (node.getNodeType() != Element.ELEMENT_NODE) {
            return null;
        }

        ViewNode viewNode = new ViewNode();
        viewNode.setViewName(node.getNodeName());
        viewNode.setValue(node.getNodeValue());
        NamedNodeMap attributes = node.getAttributes();

        HashMap<String, String> attributeMap = new HashMap<>();

        for (int j = 0; j < attributes.getLength(); j++) {
            Node item = attributes.item(j);
            attributeMap.put(item.getNodeName(), item.getNodeValue());
        }

        viewNode.setAttributes(attributeMap);

        NodeList childNodes = node.getChildNodes();
        if (childNodes == null || childNodes.getLength() == 0) {
            return viewNode;
        }
        viewNode.setChildren(parseNodeList(childNodes));

        return viewNode;
    }

    /**
     * 解析子node
     *
     * @param nList
     * @return
     */
    private static List<ViewNode> parseNodeList(NodeList nList) {
        List<ViewNode> childNodes = new ArrayList<>();
        for (int i = 0; i < nList.getLength(); i++) {
            Node node = nList.item(i);
            ViewNode viewNode = parseNode(node);
            if (viewNode != null) {
                childNodes.add(viewNode);
            }
        }
        return childNodes;
    }
}
