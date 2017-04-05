package com.example.spenc.uoitstudyroom;

import java.util.ArrayList;

/**
 * BookARoom
 * <p>
 * Created by Lachlan Johnston on 3/12/2017.
 */

class Parser {
    private String html;

    Parser(char[] html) {
        this.html = new String(html);
    }

    /**
     * Generates a list of elements given a tag name to find.
     *
     * @param elemFind Element to find within the HTML
     * @return a list of elements matching the tag name
     */
    ArrayList<Element> getElements(String elemFind) {
        String elemName = elemFind;
        elemFind = "<" + elemFind + ">";
        String elemClose = "</" + elemName ;
        int elemNameLen = elemFind.length();
        int elemCloseLen = elemClose.length();
        int elementStart = 0;
        Boolean found = false;
        ArrayList<Element> elements = new ArrayList<>();

        for (int  i = 0; i < html.length(); i++) {
            String elemString;
            if (i + elemNameLen < html.length())
                elemString = html.substring(i, i + elemNameLen);
            else
                break;
            Element elem = new Element(elemName);

            if (elemString.equals(elemFind)) {
                elementStart = i;
                found = true;
            }

            if (Character.toString(html.charAt(i)).equals(">") && found) {
                elemString = html.substring(elementStart, i + 1);
                elem.addAttributes(elemString);
                elements.add(elem);
                int end = 0;
                int depth = 0;

                if (!elemString.contains("/>")) {

                    for (int j = i + 1; j < html.length() - elemNameLen; j++) {

                        if (html.substring(j, j + elemNameLen).equals(elemName))
                            depth += 1;

                        if (html.substring(j, j + elemCloseLen).equals(elemClose)) {
                            if (depth > 0)
                                depth -= 1;
                            else {
                                end = j;
                                break;
                            }
                        }
                    }

                    elem.setContent(html.substring(i + 1, end));
                } else {
                    elem.setContent("");
                }

                found = false;
            }
        }

        return elements;
    }

    ArrayList<Element> getElements(String elemFind, String... attribs) {
        String elemName = elemFind;
        elemFind = "<" + elemFind;
        String elemClose = "</" + elemName;
        int elemNameLen = elemFind.length();
        int elementStart = 0;
        Boolean found = false;
        ArrayList<Element> elements = new ArrayList<>();

        for (int  i = 0; i < html.length(); i++) {
            String elemString;
            Boolean valid = true;

            if (i + elemNameLen < html.length())
                elemString = html.substring(i, i + elemNameLen);
            else
                break;



            Element elem = new Element(elemName);

            if (elemString.equals(elemFind)) {
                elementStart = i;
                found = true;
            }

            if (Character.toString(html.charAt(i)).equals(">") && found) {
                for (String attrib : attribs)
                    if (!elemString.contains(attrib))
                        valid = false;

                //if (!valid) continue;
                elemString = html.substring(elementStart, i + 1);
                elem.addAttributes(elemString);
                elements.add(elem);
                int endContent = 0;

                if (!elemString.contains("/>")) {
                    for (int j = i + 1; j < html.length(); j++)
                        if (html.substring(j, j + elemClose.length()).equals(elemClose)) {
                            endContent = j;
                            break;
                        }

                    elem.setContent(html.substring(i, endContent));
                } else {
                    elem.setContent("");
                }

                found = false;
            }
        }

        return elements;
    }

    ArrayList<Element> getElementsFromString(String html) {
        ArrayList<Element> elements = new ArrayList<>();
        int start = 0;
        int end = 0;
        int depth = 0;

//        System.out.println("Starting");

        for (int  i = 0; i < html.length(); i++) {
            Element element = new Element("");
            Boolean beginning = true;

            if (html.charAt(i) == '<') {
                for (int j = i; j < html.length(); j++) {
                    if (html.charAt(j) == ' ' && beginning) {
                        start = i;
                        element = new Element(html.substring(i + 1, j));
                        beginning = false;
                    }

                    if (html.charAt(j) == '>' || (html.substring(j, j + 1)).equals("/>")) {
                        element.addAttributes(html.substring(start, j));
                        break;
                    }
                }
            }

            elements.add(element);
        }

//        System.out.println("Done");
        return elements;
    }

    ArrayList<Element> getElements(ArrayList<Element> elems,
                                   String elemFind, String attrib) {
        ArrayList<Element> matchingElems = new ArrayList<>();

        for (Element elem : elems)
            if (elem.getName().equals(elemFind) && elem.contains(attrib))
                matchingElems.add(elem);


        return matchingElems;
    }

    ArrayList<Element> select(String elementName, String attrib, String val) {
        int depth = 0;
        String attribFind = attrib + "=" + "\"" + val + "\"";
        int attribLength = attribFind.length();
        Boolean elemFound = false;
        String elementNameEnd = "</" + elementName;
        elementName = "<" + elementName;
        int elementNameLength = elementName.length();
        int elementEndLength = elementNameEnd.length();
        int start = 0;
        int end = 0;


        for (int i = 0; i < html.length() - attribLength; i++) {
            if (html.substring(i, i + attribLength).equals(attribFind)) {
                for (int j = i; j > 0; j--) {
                    if (html.charAt(j) == '<') {
                        start = j;
                        elemFound = true;
                        break;
                    }
                }
                break;
            }
        }

        Boolean tagEnded = false;

        for (int i = start + 1; i < html.length() - elementNameLength; i++) {
            if (!tagEnded && html.charAt(i) == '>')
                tagEnded = true;

            if (!tagEnded) {
                if (html.substring(i, i + 2).equals("/>")) {
                    end = i + 2;
                    break;
                }
            }

            if (html.substring(i, i + elementNameLength).equals(elementName))
                depth += 1;

            if (html.substring(i, i + elementEndLength).equals(elementNameEnd)) {
                if (depth > 0)
                    depth -= 1;
                else {
                    end = i;
                    break;
                }
            }
        }

        if (elemFound) {
            return getElementsFromString(html.substring(start, end));
        } else {
            System.out.print("Element not found. ");
            return null;
        }
    }
}

