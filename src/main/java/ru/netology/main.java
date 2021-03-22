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
import java.util.List;

public class main {
    public static void main(String[] args) {

        List<String[]> csvTexts = new ArrayList<>();
        csvTexts.add("1,John,Smith,USA,25".split(","));
        csvTexts.add("2,Inav,Petrov,RU,23".split(","));

        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileNameCSV = "data.csv";

        String fileNameXML = "data.xml";
        File fileXML = new File(fileNameXML);


        // Работа с XML
        // Создание файла
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
            root.appendChild(staff);

            Element employee1 = doc.createElement("employee");
            staff.appendChild(employee1);

            Element id = doc.createElement("id");
            id.appendChild(doc.createTextNode("1"));
            employee1.appendChild(id);


            Element firstname = doc.createElement("firstname");
            firstname.appendChild(doc.createTextNode("John"));
            employee1.appendChild(firstname);

            Element lastname = doc.createElement("lastname");
            lastname.appendChild(doc.createTextNode("Smith"));
            employee1.appendChild(lastname);

            Element country = doc.createElement("country");
            country.appendChild(doc.createTextNode("USA"));
            employee1.appendChild(country);

            Element age = doc.createElement("age");
            age.appendChild(doc.createTextNode("25"));
            employee1.appendChild(age);

// 22222222222222222222222222222


            Element employee2 = doc.createElement("employee");
            staff.appendChild(employee2);

            Element id2 = doc.createElement("id");
            id2.appendChild(doc.createTextNode("3"));
            employee2.appendChild(id2);


            Element firstname2 = doc.createElement("firstname");
            firstname2.appendChild(doc.createTextNode("Inav"));
            employee2.appendChild(firstname2);

            Element lastname2 = doc.createElement("lastname");
            lastname2.appendChild(doc.createTextNode("Petrov"));
            employee2.appendChild(lastname2);

            Element country2 = doc.createElement("country");
            country2.appendChild(doc.createTextNode("RU"));
            employee2.appendChild(country2);

            Element age2 = doc.createElement("age");
            age2.appendChild(doc.createTextNode("23"));
            employee2.appendChild(age2);


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


        // чтение XML

        List<Employee> listXML = parseXML(columnMapping, fileNameXML);
        String json1 = listToJson(listXML);
        writeString(json1,"data1.json");

        // Работа с CSV

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

    private static List<Employee> parseXML(String[] columnMapping, String fileNameXML) {
        List<Employee> list = new ArrayList<>();
        List<String> el = new ArrayList<>();

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
                                    el.add(node2.getTextContent());
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
        list.add(new Employee(Integer.parseInt(el.get(0)),el.get(1), el.get(2), el.get(3),Integer.parseInt(el.get(4))));
        list.add(new Employee(Integer.parseInt(el.get(5)),el.get(6), el.get(7), el.get(8),Integer.parseInt(el.get(9))));
        return list;

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