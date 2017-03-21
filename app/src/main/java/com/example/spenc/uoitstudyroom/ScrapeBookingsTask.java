package com.example.spenc.uoitstudyroom;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Scrapes raw HTML data from website, needing to be parsed
 */

class ScrapeBookingsTask extends AsyncTask<DataScraper, Void, Integer> {

    private ProgressDialog dialog;
    ArrayList<Booking> bookingList = new ArrayList<>();
    DataScraper dataScraper = new DataScraper();

    ScrapeBookingsTask(MainActivity activity) {
        dialog = new ProgressDialog(activity);
    }

    @Override
    protected void onPreExecute() {
        dialog.setMessage("Loading bookings... please wait.");
        dialog.show();
    }

    @Override
    protected void onPostExecute(Integer result)
    {
        System.out.println("Bytes Read: " + result);
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    protected Integer doInBackground(DataScraper... param) {
        DataScraper dataScraper = param[0];
        String[] formData = new String[4];

        long startTime = System.currentTimeMillis();

        //TODO: Scrape with getRawHTML first
        char[] cbuf = dataScraper.getRawHtml();

        //TODO: Parse scraped data and retrieve required form data
        //TODO: Remove hardcoded driver code after parser is ready
        formData[0] = "ctl00$ContentPlaceHolder1$Calendar1";
        formData[1] = "Qy7rZS46beQUdQ2WhcURff79MxZ/IW+qpvpO5jmp4oWTCoLgsZRFIWzzEH8W+8T+vfBYrLkDPsKxFb/wxf3gDBXVIqzucr+y23qrhakK7KesSRMw1M6lf0etHU96Ov4tokE74U5wQ05LvWO5VmVHY3/wlzmZsEdRU06z1XTLYNK4BGg78hwi4HNml3puxAqYltzmwKp2NasjRZa3bgF9BNBkEej1JJg1Z1V3En1D27L8m5VeNuKdV62mTKnAJrABdPSgEjR8MGHAO0k/Q/1o+VR/f1HpFvgoJbOMxBJ8Gb6F0VUx11G+zAUs3KwiAiGb2cRIebBtzw7PfjXHnP3rDJ/DSogy36JbBEnnxGiEiV8JV0YZYbqryYHqtXEolrZSmsC4azRtYtFpZsVm/ea5X2W59Ddm4WJMzBrRU8gjYoUo6HzQuqmU80rwNSc2yVYVj1iZv21GfhexB5FIr82vmq1qA7GI0LgM/wDjiDKddwRn2gtJtVNBwN6bBSzD9wU4O/lyX4K7eOQNVBWSi7VKVbm39aCjTi7hpLdcLGr4qukQsnKcI0a26a9hgjD9Ee0FGqyis69uLAhWql08Zn503bSVHzYFv70uhJn5mJaQDJJDYxougEDMp+g1IbJXVNIe1p6iQWkTGJoF/tyRi4JYUYHvxavaYryTe+E5AU7GYEY6sQRYWBpg3+QXWnG5UrDiWIC9vl4QJSoSa3kO/+Z9U+awYwiVMFtQvgZVvBzSjD3UeRVxx6vaWTl8HXQ9SrA7vvmtP39W00aXPANOeUC9CxaCmQ9RRdUPV2QG8OTQoCmO/rlCQq8ehsW2L68HPLCyfJ7PwxfwwNpfDKow1qwnb5fFoOloLqKKN4MaXklDm3fEoUQbFRWXvUKujqZzpdS815JZ+4OoMnVdO4sO6vRgdJJ0mi7UG9fkeANJ343jv+T5Kg759rABliAz0DKNbOqNyODnY8KwPRMqFVzMJOL5t8ryx616twhEMTct70lG+Xd/hvq4Nrc1HYLx9WLuyDaS7Hr8aey73Uzscuabi9Q1GFWR7yiP0nMo5ypKcmq3idL9n3zqV1hcZy7mV+rQcomV+lGUvINQv7tuABArX8cnH/LYC5gct5AMvc5nkIa/1Xeh6pFn2opilNrHorped7yKMajRY45AmSgFB+kqGgJR/5meN94EnaOVniT21LSDl7TecfBKDB5o3FizLkMGuoxDxqSr2UVIE52w6cCQkEYt+A==";
        formData[2] = "80536060";
        formData[3] = "W+TINosvcLHyv0ZQcvzJLDSpJ2vOo2GgUinr+yWi8BLTO1bHov+3pWgjq0+g0gXk3HhMJm7XnxL/6OqcuHHcmrEkvM4Yerh0wizYlVd8mycapVcR0jSkAYX/XAvUg9jD2YGWXe//uyRPIkZs/VbEGSUOEe7gifhm1CjA2mdabk2Pcd7JcrKPM6zmlI453ZkwOIA7mMKqCT447TCBqFq5TBKCDfSZ2uLOg9t2168mMwZMoxRawydEXusTExH+OqNnniUOT4LfpFuKmNiK/87s6gR5JaNtfxqRm0pFckkvIUFCO025PcM+pFRDUs5Aj1l+D5OeSAjSp5kn6Hc/6H5HrThCPeyTTsWxo1H4ojrYgs6NT2VATpKVzCtmZUi/x36d5kF7UgbIYiWC8W3MIoJxLel+Qifj5w6gL/TWJhCSx7nmzIDx4TSmh8GIAQZEZz8nViqVfCmVr/e/0PuOg5Id+6f9JEQoApek/JL5aPqnDnzahZchP8Dg9uDSfcTuKzD6Tn4jI7jOiR8ixP8PoTjczGTTbCWH5S7shraXIvlXZDIoC5wvD17tpEpj/c+VV87zyRBYs1x5X0IUgEjBQuaJPSlkvYx6knRl0pMNb6LJehIjfVEaYkEZFoZgt3dST2l6LB7n634ssP8gmb2IX/vF/VDdKUd4OeKUXrzk6wGt0vS2IT4Hx9YJEwGTGzkIJyGe6EYjPt80F4UiYUmYf2HR2f9NBcYSC4zja5ghtZcaC0LqUFwr/2k9sodGwlnLA/2LTT0JwdmdpjRRBwKYyx7cHd+911C0Lx2EzF7s3yCFdXdADhJ7eQI5KD+4ZemqwFBV0MF1GI4bjgunjGFejGcwjQC+6mDvcC14MfBtqSBVcorOzlcf/vL/PgAcfMmNe7Lk+kfDnDbrUeNXaJbrJSStSaOQ27tOcUhVA1e0WSGFR42T93NnHuzvYyXlugVJlic6Y6sllp20vzVTU5ux3oN9/MDPg4/NX2qEz6e5uJPXjP9qhFHGq9o3wEvbJ/PCNb4U8KqPrbdMW7omJzXAsNKUHbJeqiXBCBlF9CpPhed/CwF72PtRzxwaSRdnUFLoutdqN/EfqgonraBZnskZCRzDPXrNrTZGvvjZGJG3U280W//tO6YDklyg6njuwy5q0klzChNJcZvYOmZhWR5r/bs0HuV9UkdeJIcsJEbPA10LXNYURK/s3mp0SqW3O5VVkPdLlyfnGqFIah+83rSL3I4PZWpHtPXqNEZcYsESlqJU23Z6eDQEBLp3aCoJzyKVkqNzP+pqu/blP9ZCCg8RGgLUYllcw4ozoA1tS5XBgaKsyqwo1XIS0+w9i/0NKZz4Ra2L2eFxjum4O6PCuH48GS2NZYTKLHiWKluccdJgBWEtdnhLTw0nqIMRa1c3Lutc1x0FGNR+izjCxKKG3ojzCF00zz/fFyFvOuOus9O0wW9dvLUPzZ9JehOjGjblBOKktplFtFu/10lck9gVaHqjDHkc5wucuX2kjEtOvN5/bAcH0/FijxGLJCiv9EQl+1r9BKfKCcWzrxffqBqj4ct1+W95Wmcb33y7k0d1V71U/cypMhLN51sqz8RG9v5xRthsGSKboED6i42K9RSwu82o5NeBMMsW2+n5dquFuuOEi7n1AWks0onvLF0kVkQSHFCZRsr5G2vcg4IYTWMk8+WF5CoVhCZK+BfeK5Qp8GkJwBTUFG+bZkKgM1KZbh1j9J4bnBMvM4DO6l8xzfRo/Q671OsMyHCGp5TE74yKMyDoIN+gkVYPEvArhiYw2y9TKzvOVKVTXYLxIQZUcCo3R3srQRO23von5w/zZ+9B7r1uA+XFxTg7GcZEyX6RSXzMNtJw79lNKUcV1SX2v3hunthOhuju/TFvP45TL/HEPnT60LjwroM=";

        // Get dates

        Parser parser = new Parser(cbuf);
        ArrayList<Element> elems = parser.select("table", "id", "ContentPlaceHolder1_Calendar1");
        ArrayList<Element> aElems = parser.getElements(elems, "a", "href");
        ArrayList<String> ids = new ArrayList<>();

        for (Element e : aElems) {
            String link = e.getAttribute("href");
            ids.add(e.getID(link));
        }

        // Get all postings by date
        for (String id : ids) {
            int idInt = Integer.parseInt(id);

            cbuf = dataScraper.postDate(idInt, formData);
            parser = new Parser(cbuf);
            elems = parser.select("table", "id", "ContentPlaceHolder1_Table1");
            ArrayList<Element> bookingElems = parser.getElements(elems, "a", "href");
            System.out.println(bookingElems.size());

            for (Element elem : bookingElems) {
                Booking booking = parseBookingData(elem.getAttribute("href"));

                bookingList.add(booking);
            }
        }

//        for (Booking b : bookingList)
//                System.out.println(b.getRoom() + " " + b.getDate());


        dataScraper.postDate(6289,formData);

        //TODO: Scrape updated eventvalidation and viewstate for posting a certain booking
        cbuf = dataScraper.selectBooking("LIB305","8:30 PM",0);

        //TODO: Post booking with retrieved data
        dataScraper.postBooking(formData);

        long totalTime = System.currentTimeMillis() - startTime;

        //System.out.println(new String(cbuf));
        System.out.println("Scraped bookings in " + totalTime + " ms.");

        return cbuf.length;
    }

    Booking parseBookingData(String data) {
        String reg = "\\d{1,2}:..\\s[A|P]M";
        String reg_ = "LIB[a-zA-Z0-9]*";
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(data);
        Pattern p_ = Pattern.compile(reg_);
        Matcher m_ = p_.matcher(data);

        m.find();
        m_.find();

        Booking booking = new Booking(m.group(), m_.group());
        return booking;
    }
}