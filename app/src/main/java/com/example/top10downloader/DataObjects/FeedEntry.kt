package com.example.top10downloader.DataObjects

class FeedEntry {

    var name: String = ""
    var artist: String = ""
    var releaseDate: String = ""
    var summary: String = ""
    var imageURL: String = ""

    override fun toString() :String{
        return """
            name : ${this.name}
            artist : ${this.artist}
            releaseDate : ${this.releaseDate}
            imageURL : $imageURL
        """.trimIndent()
    }
}