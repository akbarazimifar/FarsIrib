package ir.farsirib.utils;


import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


public class PrXml {
    public String  html ;
    public String  Html ;
    public String[] date_array={"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};

    public List<Rss> parser() {

            List<Rss> data=new ArrayList<>();

        int checker_img=0;
        int j;

            URL url = null;
        try {
            url = new URL("http://fars.irib.ir/%D8%AE%D8%A8%D8%B1/-/asset_publisher/L62kfRcvHTtz/rss?p_p_cacheability=cacheLevelPage&_101_INSTANCE_L62kfRcvHTtz_currentURL=%2Fweb%2Firib-1-theme%2F%25D8%25AE%25D8%25A8%25D8%25B1&_101_INSTANCE_L62kfRcvHTtz_portletAjaxable=1");

        }
        catch(Exception e) {
            Log.e("Xml", "error url");
        }
        try {

                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc = null;


            doc = db.parse(new InputSource(url.openStream()));
                NodeList nl = doc.getElementsByTagName("entry");

                 for (int i = 0; i <10; i++) {
                    NodeList cur = nl.item(i).getChildNodes();


                     Rss rss = new Rss();
                     checker_img=0;
                    for ( j = 1; j <cur.getLength(); j++) {
                        if (cur.item(j).getNodeName().equals("title")) {
                            rss.setTitle(cur.item(j).getFirstChild().getNodeValue());

                            Log.e("title", cur.item(j).getFirstChild().getNodeValue());
                        }

                        else if ((cur.item(j).getNodeName().equals("link")) && (cur.item(j).getAttributes().item(0).getTextContent().equals("alternate"))) {

                            rss.setLink(cur.item(j).getAttributes().item(1).getTextContent());
                            Log.e("link", cur.item(j).getAttributes().item(1).getTextContent());

                        }
                        else if ((cur.item(j).getNodeName().equals("link")) && (cur.item(j).getAttributes().item(0).getTextContent().equals("enclosure"))){
                            String Img=cur.item(j).getAttributes().item(1).getTextContent();
                            rss.setImg(cur.item(j).getAttributes().item(1).getTextContent());

                            Log.e("image", cur.item(j).getAttributes().item(1).getTextContent());
                            checker_img++;

                        }

                    }
                    if( checker_img==0)
                        rss.setImg("http://fars.irib.ir/image/layout_set_logo?img_id=750534&t=1506117034454");


                    data.add(rss);
                }

        }
            catch (Exception e) {
            Log.e("Xml", "xml errorrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr");
        }

        return data;

    }







}

