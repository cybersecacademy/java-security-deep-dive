package edu.otib.lab_xxe;

import edu.otib.lab_xxe.model.Product;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class BillPrintingController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @GetMapping("/bill")
    public String billPrintingForm() {
        return "bill_printing_form";
    }

    @PostMapping("/bill")
    public String billProcesing(
            @RequestParam("bill_xml") MultipartFile billXmlFile,
            RedirectAttributes redirectAttributes,
            Model model) {
        if (billXmlFile.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Please select XML-file to upload");
            return "redirect:bill";
        }
        try {
            model.addAttribute("bill_name", billXmlFile.getName());
            // Process XML
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(billXmlFile.getInputStream());
            NodeList nList = doc.getElementsByTagName("product");
            double totalPrice = 0.0;
            List<Product> products = new ArrayList<>();
            for (int currNodeIndex = 0; currNodeIndex < nList.getLength(); currNodeIndex++) {
                Node currNode = nList.item(currNodeIndex);
                if (currNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element currElement = (Element) currNode;
                    String currElementName = currElement.getElementsByTagName("name").item(0).getTextContent();
                    Integer currElementQuantity = Integer.parseInt(currElement.getElementsByTagName("quantity")
                            .item(0).getTextContent());
                    Double currElementPrice = Double.parseDouble(currElement.getElementsByTagName("price")
                            .item(0).getTextContent());
                    products.add(new Product(currElementName, currElementQuantity, currElementPrice));
                    totalPrice += currElementQuantity * currElementPrice;
                }
            }
            model.addAttribute("products", products);
            model.addAttribute("total_price", totalPrice);
        } catch (ParserConfigurationException ex) {
            log.error("ParserConfigurationException was thrown: " + ex.getMessage());
        } catch (IOException ex) {
            log.error("IOException was thrown: " + ex.getMessage());
        }catch (SAXException ex) {
            log.error("SAXException was thrown: " + ex.getMessage());
        }
        return "bill_print";
    }
}
