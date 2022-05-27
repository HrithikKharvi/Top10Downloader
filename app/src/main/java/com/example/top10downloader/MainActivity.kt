package com.example.top10downloader

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.top10downloader.DataObjects.FeedEntry
import com.example.top10downloader.DataObjects.ParseApplication
import kotlinx.android.synthetic.main.activity_main.*
import java.net.URL
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {
    private var TAG = "MainActivity"
    private var maxNumber = 10
    private var baseUrl ="http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=%d/xml"
    private var fullUrl: String = baseUrl.format(maxNumber)
    private var feedLimitTag = "feedLimit"
    private var baseUrlTAG:String  = "baseURL"
    private var fullUrlTAG:String = "fullURL"
    private var downloadData: DownloadData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("testing","oncreate")
        super.onCreate(savedInstanceState)
        if(savedInstanceState != null){
            fullUrl = savedInstanceState.getString(fullUrlTAG).toString()
            baseUrl = savedInstanceState.getString(baseUrlTAG).toString()
            maxNumber = savedInstanceState.getInt(feedLimitTag)
        }

        setContentView(R.layout.activity_main)
        Log.d(TAG, "Executing onCreate() method")
//        val downloadData = DownloadData(this, xmlListView)
        downloadUrl(fullUrl);
        Log.d(TAG, "onCreate() method executed")
    }



    private fun downloadUrl(url: String) {
        downloadData = DownloadData(this, xmlListView)
        downloadData?.execute(url, "This will go into log")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(feedLimitTag, maxNumber)
        outState.putString(baseUrlTAG, baseUrl)
        outState.putString(fullUrlTAG, fullUrl)
        Log.d("testing","saving")
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.feeds_menu, menu)

        if (maxNumber == 10) {
            menu?.findItem(R.id.top10)?.isChecked = true
        } else {
            menu?.findItem(R.id.top25)?.isChecked = true
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var currentURL: String
        when (item.itemId) {
            R.id.freeApps -> {
                currentURL =
                    "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=%d/xml"
                baseUrl = currentURL
            }
            R.id.paidApps -> {
                currentURL =
                    "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/toppaidapplications/limit=%d/xml"
                baseUrl = currentURL
            }
            R.id.songs -> {
                currentURL =
                    "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topsongs/limit=%d/xml"
                baseUrl = currentURL
            }
            R.id.refresh ->{
                currentURL = baseUrl
            }
            R.id.top10, R.id.top25 -> {
                if (!item.isChecked) {
                    item.isChecked = true
                    maxNumber = 35 - maxNumber
                }
                currentURL = baseUrl
            }
            else -> return super.onOptionsItemSelected(item)
        }
        currentURL = currentURL.format(maxNumber)
        if(!fullUrl.equals(currentURL)){
            Log.d("SAME","True");
            fullUrl = currentURL
            downloadUrl(fullUrl)
        }


        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        downloadData?.cancel(true)
    }

    companion object {
        private class DownloadData(context: Context, listView: ListView) :
            AsyncTask<String, Void, String>() {
            private var TAG = "DownloadData"
            private var propContext: Context by Delegates.notNull()
            private var propListView: ListView by Delegates.notNull()

            init {
                propContext = context
                propListView = listView
            }

            override fun doInBackground(vararg url: String?): String {
                Log.d(TAG, "executing doInBackground() method with input ${url[1]}")
                val rssFeed = downloadXML(url[0])
                if (rssFeed.isEmpty()) {
                    Log.e(TAG, "Error downloading data")
                }
                return rssFeed
            }

            override fun onPostExecute(result: String) {
                super.onPostExecute(result)
                var parseApplication: ParseApplication = ParseApplication()
                parseApplication.parse(result)
                var listViewAdapter = ArrayAdapter<FeedEntry>(
                    propContext,
                    R.layout.list_item,
                    parseApplication.applications
                )
                var feedAdapter: FeedAdapter =
                    FeedAdapter(propContext, R.layout.list_record, parseApplication.applications)

                propListView.adapter = feedAdapter
            }

            private fun downloadXML(urlPath: String?): String {
                return URL(urlPath).readText()
            }
        }
    }

}
