package com.example.rss_read_asynctask;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RssFeedAdapter adapter;
    ReadRSSTask readRSSTask;
    private List<RssFeedItem> items;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnLoad = findViewById(R.id.btnLoad);
        btnLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readRSSTask = new ReadRSSTask();
                readRSSTask.execute();
            }
        });

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<RssFeedItem> rssFeedItems = new ArrayList<>();

        adapter = new RssFeedAdapter(rssFeedItems);
        recyclerView.setAdapter(adapter);
    }


    public class ReadRSSTask extends AsyncTask<URL, Integer, List<RssFeedItem>> {
        private List<RssFeedItem> items;

        public String getXMLFromUrl(String url) {
            BufferedReader br = null;
            try {
                HttpURLConnection conn = (HttpURLConnection)(new URL(url)).openConnection();
                br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                String line;
                final StringBuilder sb = new StringBuilder();
                while ((line = br.readLine()) != null) {
                    sb.append(line).append("\n");
                }

                return sb.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            } finally {
                try {
                    if (br != null) br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        protected List<RssFeedItem> doInBackground(URL... urls) {
            try {
                SAXParserFactory factory = SAXParserFactory.newInstance();
                SAXParser saxParser = null;
                saxParser = factory.newSAXParser();

                RssHandler rssHandler = new RssHandler();
                saxParser.parse("https://feeds.bbci.co.uk/news/world/rss.xml", rssHandler);
                items = rssHandler.getListItem();

            } catch (IOException | SAXException | ParserConfigurationException e) {
                throw new RuntimeException(e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(List<RssFeedItem> result) {
            for (RssFeedItem feedItem : items) {
                System.out.println(feedItem.getTitle());
            }
            adapter = new RssFeedAdapter(items);
            recyclerView.setAdapter(adapter);
        }
    }
}