package com.demo;

import java.io.*;

import com.itextpdf.kernel.pdf.*;
import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.xfa.XfaForm;
import com.itextpdf.licensekey.LicenseKey;
import com.itextpdf.tool.xml.xtra.xfa.XFAFlattener;

import org.w3c.dom.Document;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class App {
    public static void main(String[] args) throws Exception {
        String pathLicenceKey = System.getProperty("user.dir") + "/licence.xml";
        String pathLicenceKeyXFA = System.getProperty("user.dir") + "/licence-xfa.xml";
        LicenseKey.loadLicenseFile(pathLicenceKey);
        LicenseKey.loadLicenseFile(pathLicenceKeyXFA);

        // 1. Extract XFA XML for inspection
        PdfDocument pdfDoc = new PdfDocument(new PdfReader("acta_xfa.pdf"));
        PdfAcroForm acroForm = PdfAcroForm.getAcroForm(pdfDoc, false);
        XfaForm xfa = acroForm.getXfaForm();
        Document xfaDom = xfa.getDomDocument();
        pdfDoc.close();

        // Write full XFA XML to file
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        transformer.transform(
            new DOMSource(xfaDom),
            new StreamResult(new FileOutputStream("xfa_dump.xml"))
        );
        System.out.println("XFA XML dumped to xfa_dump.xml");

        // 2. Flatten as before
        // XFAFlattener xfaFlattener = new XFAFlattener();
        // xfaFlattener.flatten(
        //     new FileInputStream("acta_xfa.pdf"),
        //     new FileOutputStream("output.pdf")
        // );
    }
}