package util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ReadXml {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private Element root = null;

    public ReadXml(File xml) {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();

            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xml);
            doc.getDocumentElement().normalize();

            this.root = doc.getDocumentElement();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<File> getInputFileList() {
        ArrayList<File> inputFiles = new ArrayList<>();
        NodeList nl = root.getElementsByTagName("input");
        for (int i = 0; i < nl.getLength(); i++) {
            Node n = nl.item(i);
            File f = new File(n.getTextContent().trim());
            if (f.isFile()) {
                inputFiles.add(f);
            } else {
                logger.error("Error | This is not a file. | " + f);
                return null;
            }
        }
        return inputFiles;
    }

    public File getOutputDirFile() {
        Node n = root.getElementsByTagName("output").item(0);
        File outputDir = new File(n.getTextContent().trim());
        if (outputDir.isDirectory()) {
            return outputDir;
        } else {
            logger.error("Error | This is not a Directory. | " + outputDir);
            return null;
        }
    }
}
