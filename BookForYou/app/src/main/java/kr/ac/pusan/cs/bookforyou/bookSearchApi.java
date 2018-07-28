package kr.ac.pusan.cs.bookforyou;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class bookSearchApi {
    public static ArrayList bookSearch(String keyword) {
        ArrayList<Obj_book> bookList = new ArrayList<>();
        String clientId = "pRMf5iQoP9H8PqkOkhI5";
        String clientSecret = "_BuFY_iYqD";
        try {

            String text = URLEncoder.encode(keyword, "UTF-8");
            //String apiURL = "https://openapi.naver.com/v1/search/book?query="+ text; // json 결과
            String apiURL = "https://openapi.naver.com/v1/search/book.xml?query=" + text; // xml 결과
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("X-Naver-Client-Id", clientId);
            con.setRequestProperty("X-Naver-Client-Secret", clientSecret);
            int responseCode = con.getResponseCode();

            if(responseCode==200) { // 정상 호출

            } else {  // 에러 발생
                //return bookList;
            }
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(con.getInputStream(), null);

            String tag;
            xpp.next();

            int eventType = xpp.getEventType();
            StringBuffer response = new StringBuffer();
            Obj_book book = null;
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String title, pubdate, author, price, publisher, image;

                switch (eventType) {

                    case XmlPullParser.START_DOCUMENT:

                        break;

                    case XmlPullParser.START_TAG:
                        tag = xpp.getName();    //테그 이름 얻어오기
                        if (tag.equals("item")) {
                            book = new Obj_book();
                        }// 첫번째 검색결과

                        else if (tag.equals("title")) {

                            xpp.next();

                            if (xpp.getText().contains("Naver Open API")) {
                                break;
                            } else {
                                title = xpp.getText().replace("<b>", "").replace("</b>", "");
                                //response.append("제목: " + title + "\n");
                                book.title = title;
                            }
                        } else if (tag.equals("pubdate")) {

                            xpp.next();
                            pubdate = xpp.getText();
                            //response.append("출판일: " + pubdate + "\n");
                            book.pubdate = pubdate;
                        } else if (tag.equals("author")) {

                            xpp.next();
                            author = xpp.getText();
                            //response.append("저자: " + author + "\n");
                            book.author = author;

                        } else if (tag.equals("price")) { // getmapx value

                            xpp.next();
                            //price = Integer.parseInt(xpp.getText());
                            price = xpp.getText();
                            //response.append("가격: " + price + "\n");
                            book.price = price;

                        } else if (tag.equals("publisher")) { // getmapy valye

                            xpp.next();
                            publisher = xpp.getText();
                            //response.append("출판사: " + publisher + "\n");
                            book.publisher = publisher;
                        } else if (tag.equals("image")) { // getmapy valye

                            xpp.next();
                            image = xpp.getText();
                            //response.append("이미지: " + image + "\n");
                            book.image = image;

                        }

                        break;

                    case XmlPullParser.TEXT:

                        break;

                    case XmlPullParser.END_TAG:

                        tag = xpp.getName();    //테그 이름 얻어오기

                        if (tag.equals("item")) {
                            bookList.add(book);
                            //response.append("\n\n\n");
                            break;
                        }

                }

                eventType = xpp.next();

            }

            return bookList;
            //return response.toString();

        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();

        }
        return bookList;
        //return "No";
    }
}
