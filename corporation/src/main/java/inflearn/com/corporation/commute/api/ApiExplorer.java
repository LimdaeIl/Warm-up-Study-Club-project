package inflearn.com.corporation.commute.api;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ApiExplorer {

    public static List<String> resultHoliday() throws IOException {
        List<HashMap<String, String>> hashMapList = new ArrayList<>();

        for (int i = 1; i <= 12; i++) {
            synchronized (hashMapList) {
                HashMap<String, String> holiday = findHoliday(i);
                hashMapList.add(holiday);
            }
        }

        List<String> dateList = new ArrayList<>();

        for (HashMap<String, String> stringStringHashMap : hashMapList) {
            for (String value : stringStringHashMap.values()) {
                if (value.matches("\\d{8}")) { // 8자리 숫자인지 확인
                    dateList.add(value); // 8자리 숫자면 리스트에 추가
                }
            }
        }

        System.out.println("8자리 숫자 리스트: " + dateList);

        return dateList;
    }

    private static HashMap<String, String> findHoliday(int i) throws IOException {
        String findMonth = "" + i;
        if (i < 10) {
            findMonth = "0" + i;
        }

        StringBuilder urlBuilder = new StringBuilder("https://apis.data.go.kr/B090041/openapi/service/SpcdeInfoService/getRestDeInfo"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "키는 숨기기"); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("solYear", "UTF-8") + "=" + URLEncoder.encode("2024", "UTF-8")); /*연*/
        urlBuilder.append("&" + URLEncoder.encode("solMonth", "UTF-8") + "=" + URLEncoder.encode(findMonth, "UTF-8")); /*월*/

        URL url = new URL(urlBuilder.toString());

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");

//        System.out.println("Response code: " + conn.getResponseCode());
        BufferedReader rd;
        if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }

        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();
//        System.out.println(sb.toString());

        HashMap<String, String> hashMap = parseXMLToHashMap(sb.toString());
//        System.out.println(hashMap);

        return hashMap;
    }

    private static HashMap<String, String> parseXMLToHashMap(String xmlData) {
        HashMap<String, String> resultMap = new HashMap<>();

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new ByteArrayInputStream(xmlData.getBytes()));

            NodeList itemList = document.getElementsByTagName("item");

            for (int i = 0; i < itemList.getLength(); i++) {
                Element item = (Element) itemList.item(i);
                String isHoliday = item.getElementsByTagName("isHoliday").item(0).getTextContent();

                if (isHoliday.equals("Y")) {
                    String dateKind = item.getElementsByTagName("dateKind").item(0).getTextContent();
                    String dateName = item.getElementsByTagName("dateName").item(0).getTextContent();
                    String locdate = item.getElementsByTagName("locdate").item(0).getTextContent();

                    resultMap.put("dateKind" + (i + 1), dateKind);
                    resultMap.put("dateName" + (i + 1), dateName);
                    resultMap.put("isHoliday" + (i + 1), isHoliday);
                    resultMap.put("locdate" + (i + 1), locdate);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultMap;
    }
}

