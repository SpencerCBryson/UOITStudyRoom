package com.example.spenc.uoitstudyroom;

import android.os.AsyncTask;
import java.io.IOException;

/**
 * Scrapes raw HTML data from website, needing to be parsed
 */

class ScrapeBookingsTask extends AsyncTask<DataScraper, Void, Integer> {

    @Override
    protected Integer doInBackground(DataScraper... param) {
        //TODO: Do we really need to have the scraper exist outside of a task?
        DataScraper dataScraper = param[0];

        String[] formData = new String[4];

        formData[0] = "ctl00$ContentPlaceHolder1$Calendar1";
        formData[1] = "QI916RYBbfQD6nuE3P2gUrvDb1BDJVQtoegI6o9TQULjip291oACQAghC9JgAqkrAer78ueTRqk4Okz0QU77V557+IiU/xKFi73fvkLyGACRqDhgbxAAdBxfePQYalNCXAV3QweGJdAtRy1aLFkvqIAJmPEWC9HhWtziqyN8XgOZb6jvRHUQyjsvqsahMKGGWE9kcxuOThDTg1IaNlo7PYXwQd0TkifIrEg4L6/4iis01RMmAZKPFGO+MHE+LR6HfxSSSfVih0ygC2aBNQDAu5uasnLD3lj3Pl7X8NEg4mKOjo29/xcptjMoGRWlS0Hu7ZjS/KMjvKr9k/+OiWHNu25cez40rszFn4d2y6KPWGc/kwwHAt/egUzGv4DdLJNn6o/K2Iz3TPxmFHHuTBEujcSD1fC4LdrWpfHHJg+9n70ZRd86npeSPD1DY990zOSr/EZY0ZuQApmW3PqYlAVwxolybHrsMgmuTXEsO/B2u1/amSK5H641g8pHv2i80zf3djGrozbmp1ZLWm6M8MP32sJGCSmlTA5jBQdFZKputn1Fzlk0LHdUoKm7Ty1NYA7lTrXGc5sRe4nfAI7QAbDUmGiSX3mJnNuK0p038ahoOTQ4zCXCEGvN4tKndRmOXSTb3no+f2IyY5uFsKJL0WQQjC0NHurmpNvdMTFhd2vux201D7IvIHI8SYtRZztzwqKaSmiipdKZQTsOC903ErpPrNPIy3Mw/RHP0brwuzEJ3ZmjPlLMsxszbV65esBpwSS8DztH3aWkU52CG7aZSHtKN2XS9Mm7C3iF/W30ddEk3PzMC7gsWAQ+v6jClEz9yUMahcdkZJq1zgzuq2mKyEDkEzhBvSxUvHyJ7ggwWQneEXFa1LxuC2T1jiuunCYODBtdhZbg0yBjJAECSHEfx0AbdfhdJ2lfFXJPATS466uPeKIqtVOdrwRWisBl+lOHdRjKPPmW8XazcclO13ap44v+S1GOlxXsEBTxTVnzxA+XTTs1yOELlzOkOqiteY5mmy0tZ4CK2ACU/JMVrpIDbtxZ+CMEPjwqQln+mooX0NVrdAe6CHu+VxrnL/CKPy5n8Jr0xEpbmVEaPU1Cjx1ejeUme92eNzdzZ8MBMCPrxQ/AsH2VaFozPXYQb0P4WBSb9Kedp06Gl/HbwzhWiknrBlXZLL8TpqdoB11G9tERr7CUAullEBpdFdCrQBvEFZmL99un90w497c1s5lc0SVJJpSSfg==";
        formData[2] = "80536060";
        formData[3] = "Ptb1rYxaXnc9TOf07fS94ZNi36RioOLkPv4yPSbSlWAd+frDycynM1TTgeg/t7qTfG7qieF3+iiuJk9EBcrpTUDvcYAOQMvrIyWljNR+kZOz/AZ5SL2jysacl1DG3hR7yiLR2dVx4bQ94jhmKmJ8mUk7VBEoM7/RVxt22iMk0N6hgGnZyURlHrhDsineyzjsQxk14FmXMF1H6vNwenHuZFNdFbu1G+n9FrQs/ZyK8WmiIJWkNmcai6b9VtJ8KOEqgaZdm2U/61G7N7Ft8lncqXFj4CPyQ0EmRYXKCfYM2LlQTSGEOidAK8kdh/e8yPRQkpiHez/irAfS8s9ayR7FNapCozalAGNynoTe7v/i5mp4Mz1eyyr700BIRexByc/QoCgy2D9qaE4f772hgndOEnFx401T1T5ybqf1p3vzSJ4Zhi2iM1WJqtiniGD7c5e7QOUHRUF42HSQaFjkw2En3Hn4VkEVHNN6/BHmQEDNFcrrMyleoWNwQnY0TtyfIPUGQOZzPPwmBtuSt3xrfAVaBKAaJZqAj0m2a9bID88klQuKB/xJZolKu4XWKdDUk3cl9HPNXcHgIcUFz9kiUBgprh3NACRoE1avpEns2BRL9Qd1Q4KppVXbq+3Mcy2mr2hndjFh7rTK/Dj27jyeuROkHG4scJEF5bbaNezt+AWUVwG1kWwzl6/LQKRq0FZP1vkxjBOzX+ZruQL/mzDbc0JPXLZxgwi86/x+9jC8YWBxpfGRMzvh+xn9HjOUG3/JZWplaLeq1tTGn5M0VRCkc2+uMQi5q4pLf3W60+MNByahgiQuSg0o2hmqah8ucRQtCnpXtWzwGkvWzrxGXBPnQpWjYa/XUWx7SBB36yk67K9CLCFQg1wiklKj2TzggNb26rRFFrP5HxU0XKii6jwG4cTM7xK76uWZkDn+OeCyBiMBo/3IXmph5Zjw9bsuotFf6bCmujIsf9ua7lAPiyascmFHFuSBbC5a9C0NOmrUwb278p588xX4QaurTNDPIz+aIA2TtOL7j/jHhBBvv9GIF5W3WlGsN8RZkoJ3SerRhp5tcaX0YzG6ukbJuELTDHN4LrnrN/E94haYvDam9QWbg6CQ8tt6wY4gVcl1kaWrb2eMNcbmGuKrBhyvZpvadQInCDLqFow/lE3q6o7ePqdRo9pEzrchGcmzIB6C3QemFiFpk4gyGedvoGUXY2ooTe6oZWYYk1ongZJj7MDQiokk6qSGiDGH4rqOrW5FoxM2WSwQIqo/zilBrvplVfZObw18vT+HPbfcrxteR+HxKgydwgw05h1lsK40sFP2TxLn2WnS7W7JnJ6f/b4jzDrBiEJNoT0z4QGv6KB2EZtcQAQs/Dk6M01SiHLxo6vG4B28izY8PowGzf9JZkN6Pmhz7QaqLXfQscb3OaIfv2oRzWiCPdYWr20AZ2zkNFQQ4SmclTwWc/UXk68hPbKyWtW5ZkM8pGP9NzKMQbrdS6inHJFMQWHWoIv6CqdDFsyIUI2q4iPVdNlIj5kotE3RoT6Ya0sGLposzwn3Lzyb1pPBiQFuMHtDT0ykuwA9c/+1wPitI7LLg0wd/T1bh/Y4N5ZL1zExa0Kf1+F7DMgOnHNg0y2CAHFKgxMSWsKG6dWvHXvNpitJ9uK/x2Kcy2AhZh7U7wGAqQ3LunFu2Oyc/0gFGjmN+iElqS7NKtSX+tpV4+l2dxbpfS3Kxoj/AX+wBIff5rkrrcs56Mf1oCjZfezFYQCgdyguOC26f24T0sgnePkEtb2eBqk0ggxIdDb6sGSiRmAWVue1HhBIBkXU/FixAXhmmjbiBDEjds+oTV/qJM6DruRUvxmeNPBKfWCiPHqyJGn0v7PX2JiY74aQJrnsU36vzcjQsukgZldMGfxq6wiMhu3Gsys=";

        char[] cbuf = dataScraper.postDate(6273,formData);

        //System.out.println(new String(cbuf));

        // Raw HTML in cbuf will be passed to another class to parse it

        return cbuf.length;
    }

    protected void onPostExecute(Integer result)
    {
        System.out.println("Bytes Read: " + result);
    }

}