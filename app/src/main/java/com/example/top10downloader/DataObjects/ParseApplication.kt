package com.example.top10downloader.DataObjects

import android.util.Log
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory

class ParseApplication {

    private val TAG = "ParseApplication"
    public val applications = ArrayList<FeedEntry>();

    fun parse(xmlData:String) :Boolean{
        Log.d(TAG, "parse is called with ${xmlData}")
        var status = true
        var inEntry = false
        var textValue = ""

        try{
            var xmlFactory = XmlPullParserFactory.newInstance()
            xmlFactory.isNamespaceAware = true
            var xpp = xmlFactory.newPullParser()
            xpp.setInput(xmlData.reader())
            var eventType = xpp.eventType
            var currentRecord = FeedEntry()

            while(eventType != XmlPullParser.END_DOCUMENT){
                var tagName = xpp.name?.lowercase()
                when(eventType){
                    XmlPullParser.START_TAG ->{
                        if(tagName == "entry"){
                            inEntry = true
                        }
                    }

                    XmlPullParser.TEXT -> textValue = xpp.text
                    XmlPullParser.END_TAG ->{
                        if(inEntry){
                            when(tagName){
                                "entry" -> {
                                    applications.add(currentRecord)
                                    inEntry = false
                                    currentRecord = FeedEntry()
                                }
                                "name" -> currentRecord.name = textValue
                                "artist" -> currentRecord.artist = textValue
                                "releasedate" -> currentRecord.releaseDate  = textValue
                                "summary" -> currentRecord.summary = textValue
                                "image" -> currentRecord.imageURL = textValue
                            }
                        }
                    }
                }
                eventType = xpp.next()
            }

            Log.d(TAG, "=========================All Data==========================")
            for(feedEntry in applications){
                Log.d(TAG, feedEntry.toString())
            }
        }catch(e:Exception){
            e.printStackTrace()
            status = false
        }

        return status
    }

}