package com.asghar.adapterpractice;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DataProcessor {
    private static final String TAG = "DataProcessor";

    interface ProcessorCallBack{
        void onDataAvailable(List<News> newsList);
    }
    ProcessorCallBack callBack;

    private final Executor executor = Executors.newSingleThreadExecutor();
    private final Handler handler = new Handler(Looper.getMainLooper());
    String xmlData = "";



    List<News> newsList = new ArrayList<>();

    public void execute(String hyperlink, ProcessorCallBack callBack){
        this.callBack = callBack;
        executor.execute(() -> {
            //doInBackground
            URL url;
            HttpURLConnection connection;
            String readLine;
            
            try {
                url = new URL(hyperlink);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                int responseCode = connection.getResponseCode();
                
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    StringBuilder tempXML = new StringBuilder();
                    while ((readLine = reader.readLine()) != null) {
                        tempXML.append(readLine);
                    }
                    xmlData = tempXML.toString();
                }
                reader.close();
                
            }catch (MalformedURLException e){
                Log.e(TAG, "execute: Error in URL: " + e.getMessage());
            }catch (IOException e){
                Log.e(TAG, "execute: Error while downloading data: " + e.getMessage());
            }

            handler.post(() -> {
                //onPostExecute
//                Log.d(TAG, "execute: XML DATA:" + xmlData);
                News currentNews = null;
                String textValue = "";
                boolean inItem = false;

                try {
                    XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                    factory.setNamespaceAware(true);
                    XmlPullParser parser = factory.newPullParser();
                    parser.setInput(new StringReader(xmlData));

                    int eventType = parser.getEventType();

                    while (eventType != XmlPullParser.END_DOCUMENT){
                        String tagName = parser.getName();
                        switch (eventType){
                            case XmlPullParser.START_TAG:
                                if("item".equalsIgnoreCase(tagName)){
                                    inItem = true;
                                    currentNews = new News();
                                }
                                break;
                            case XmlPullParser.TEXT:
                                textValue = parser.getText();
//                                Log.d(TAG, "execute: TAGNAME: " + tagName + ": " + parser.getText());
                                break;
                            case XmlPullParser.END_TAG:
                                if (inItem) {
                                    if ("title".equalsIgnoreCase(tagName)) {
//                                        Log.d(TAG, "execute: TITLE: " + textValue);
                                        currentNews.setTitle(textValue);
                                    }else if ("guid".equalsIgnoreCase(tagName)){
                                        currentNews.setLink(textValue);
                                    }else if ("description".equalsIgnoreCase(tagName)){
                                        currentNews.setDesc(textValue);
                                    }else if ("item".equalsIgnoreCase(tagName)){
                                        newsList.add(currentNews);
                                    }
                                }
                                break;
                        }
                        eventType = parser.next();
                    }

                    //giving data to main
                    callBack.onDataAvailable(newsList);

                } catch (XmlPullParserException | IOException e) {
                    Log.e(TAG, "execute: Error while parsing XML: " + e.getMessage());
                }
            });
        });
    }
}
