package com.example.labo2;

import android.net.LocalSocketAddress;

import org.jdom2.Attribute;
import org.jdom2.DocType;
import org.jdom2.output.Format;
import org.json.JSONObject;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.output.XMLOutputter;


import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FormatRequest {
    public static String jsonRequest(String request){
        JSONObject jsonData;
        Map<String, String> mapData = new HashMap<>();
        mapData.put("data",request);
        jsonData = new JSONObject(mapData);
        return jsonData.toString();
    }

    public static String xmlRequest(List<Personne> personnes){
       Document doc = new Document();
        final DocType docType = new DocType("directory", "http://sym.iict.ch/directory.dtd");
        doc.setDocType(docType);
        doc.setRootElement(new Element("directory"));
        for(Personne pers : personnes){
            Element person = new Element("person");
            person.addContent(new Element("name").setText(pers.getName()));
            person.addContent(new Element("firstname").setText(pers.getFirstname()));
            person.addContent(new Element("gender").setText(pers.getGender()));
            Element phone = new Element("phone");
            phone.setAttribute("type", pers.getPhoneType());
            phone.setText(pers.getPhone());
            person.addContent(phone);
            doc.getRootElement().addContent(person);
        }
        //JDOM document is ready now, lets write it to file now
        XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
        //output xml to console for debugging

       return xmlOutputter.outputString(doc);
    }
}
