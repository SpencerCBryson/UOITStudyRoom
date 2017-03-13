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
        elemFind = "<" + elemFind;
        String elemClose = "</" + elemName;
        System.out.println(elemFind);
        int elemNameLen = elemFind.length();
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
                System.out.println("here");
                elementStart = i;
                found = true;
            }

            if (Character.toString(html.charAt(i)).equals(">") && found) {
                elemString = html.substring(elementStart, i + 1);
                elem.addAttributes(elemString);
                elements.add(elem);
                int endContent = 0;

                if (!elemString.contains("/>")) {
                    for (int j = i + 1; j < html.length(); j++)
                        if (html.substring(j, j + elemClose.length()).equals(elemClose)) {
                            System.out.println("here");
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
}

