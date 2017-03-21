package com.example.spenc.uoitstudyroom;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * BookARoom
 * <p>
 * Created by Lachlan Johnston & Spencer Bryson on 3/12/2017.
 */
class Element {

    private String name;
    private HashMap<String, String> attribs = new HashMap<>();
    private String content;

    String getName() {
        return name;
    }

    String getAttribute(String attrib) {
        return attribs.get(attrib);
    }

    HashMap<String, String> getAttribs() {
        return attribs;
    }

    String getContent() {
        return content;
    }

    String getID(String s) {
        String[] ids = s.split(",");

        String[] id = ids[1].split("'");

        String found = id[1];

        return found;
    }

    void setContent(String content) {
        this.content = content;
    }

    Element(String name) {
        this.name = name;
    }

    Boolean contains(String attrib) {
        return attribs.containsKey(attrib);
    }

    void addAttributes(String element) {

        String regex = "[A-Za-z]+=\"([^\"]*)\"";
        String nameRegex = "[^=]*";
        String valRegex = "\"([^\"]*)\"";
        Pattern p = Pattern.compile(regex);
        Pattern pName = Pattern.compile(nameRegex);
        Pattern pVal = Pattern.compile(valRegex);
        Matcher m = p.matcher(element);
        Matcher m2;

        while (m.find()) {
            String attrib = m.group();

            m2 = pName.matcher(attrib);
            m2.find();

            String attribName = m2.group();

            m2 = pVal.matcher(attrib);
            m2.find();

            String attribVal = m2.group(1);

            attribs.put(attribName, attribVal);
        }
    }
}
