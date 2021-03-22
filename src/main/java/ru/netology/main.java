package ru.netology;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.w3c.dom.*;
import org.xml.sax.SAXException;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Main {
    public static void main(String[] args) {

        List<String[]> csvTexts = new ArrayList<>();
        csvTexts.add("1,John,Smith,USA,25".split(","));
        csvTexts.add("2,Inav,Petrov,RU,23".split(","));

        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileNameCSV = "data.csv";

        String fileNameXML = "data.xml";
        File fileXML = new File(fileNameXML);
        Employee empl = new Employee(1, "John", "Smith", "USA", 25);
        Employee empl1 = new Employee(2, "Inav", "Petrov", "RU", 23);
        Employee empl2 = new Employee(3, "John", "Smith", "USA", 25);
        Employee empl3 = new Employee(4, "Inav", "Petrov", "RU", 23);

        creatureXML(fileXML);
        addFileXml(fileXML, empl);
        addFileXml(fileXML, empl1);
        addFileXml(fileXML, empl2);
        addFileXml(fileXML, empl3);

        List<Employee> listXML = parseXML(columnMapping, fileNameXML);
        String json1 = listToJson(listXML);
        writeString(json1, "data1.json");

        try (CSVWriter writer = new CSVWriter(new FileWriter(fileNameCSV))) {
            for (String[] text : csvTexts) {
                writer.writeNext(text);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<Employee> listCSV = parseCSV(columnMapping, fileNameCSV);

        String json = listToJson(listCSV);
        writeString(json, "data.json");


    }


    private static void addFileXml(File fileXML, Employee empl) {

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(fileXML);
            Node node = doc.getDocumentElement();
            NodeList nodeList = node.getChildNodes();
            Node node_ = nodeList.item(0);
            System.out.println(node_.getNodeName());

            Element employee = doc.createElement("employee");
            node_.appendChild(employee);

            Element id = doc.createElement("id");
            id.appendChild(doc.createTextNode(String.valueOf(empl.id)));
            employee.appendChild(id);


            Element firstname = doc.createElement("firstname");
            firstname.appendChild(doc.createTextNode(empl.firstName));
            employee.appendChild(firstname);

            Element lastname = doc.createElement("lastname");
            lastname.appendChild(doc.createTextNode(empl.lastName));
            employee.appendChild(lastname);

            Element country = doc.createElement("country");
            country.appendChild(doc.createTextNode(empl.country));
            employee.appendChild(country);

            Element age = doc.createElement("age");
            age.appendChild(doc.createTextNode(String.valueOf(empl.age)));
            employee.appendChild(age);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);

            StreamResult result = new StreamResult(fileXML);
            transformer.transform(source, result);


        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }


    private static List<Employee> parseXML(String[] columnMapping, String fileNameXML) {
        List<Employee> list = new ArrayList<>();
//        List<String> el = new ArrayList<>();
        Queue<String> collection = new LinkedList<>();

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(fileNameXML));
            Node node = doc.getDocumentElement();
            NodeList nodeList = node.getChildNodes();
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node_ = nodeList.item(i);
                if (Node.ELEMENT_NODE == node_.getNodeType()) {
                    NodeList nodeList1 = node_.getChildNodes();
                    for (int j = 0; j < nodeList1.getLength(); j++) {
                        Node node1 = nodeList1.item(j);
                        if (Node.ELEMENT_NODE == node1.getNodeType()) {
                            NodeList nodeList2 = node1.getChildNodes();
                            for (int q = 0; q < nodeList2.getLength(); q++) {
                                Node node2 = nodeList2.item(q);
                                if (Node.ELEMENT_NODE == node2.getNodeType()) {
                                    NodeList nodeList3 = node2.getChildNodes();
                                    collection.add(node2.getTextContent());
                                }
                            }
                        }
                    }
                }
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException n) {
            n.printStackTrace();
        } catch (SAXException m) {
            m.printStackTrace();
        }
        for (int i = 0; collection.size() / 5 > i; ) {
            list.add(new Employee(Integer.parseInt(collection.poll()), collection.poll(), collection.poll(), collection.poll(), Integer.parseInt(collection.poll())));


        }
        return list;

    }

    private static void creatureXML(File fileXML) {
        try {
            if (fileXML.createNewFile()) {
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();

            Element root = doc.createElement("root");
            doc.appendChild(root);

            Element staff = doc.createElement("staff");
            root.appendChild(staff)
            ;
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);

            StreamResult result = new StreamResult(fileXML);
            transformer.transform(source, result);


        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

    private static void writeString(String json, String fileName) {
        try (FileWriter file = new FileWriter(fileName)) {
            file.write(json);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String listToJson(List<Employee> list) {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Employee>>() {
        }.getType();
        String json = gson.toJson(list, listType);
        return json;
    }

    private static List<Employee> parseCSV(String[] columnMapping, String fileName) {
        List<Employee> list = new ArrayList<>();
        ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
        strategy.setType(Employee.class);
        strategy.setColumnMapping(columnMapping);

        try (CSVReader reader = new CSVReader(new FileReader(fileName))) {
            CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(reader).withMappingStrategy(strategy).build();
            list = csv.parse();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }
}