package com.axlecho.restraint.data

data class Info (
    val name:String,
    var count:Long = 0L,
    var time:Long = 0L,
    var type:String = ""
)

data class Filter(
    val type:String,
    val app:MutableList<Info> = arrayListOf()
) {
    companion object {
        const val IGNORE:String = "ignore"
        const val GAME:String = "game"
        const val COMIC:String = "comic"
        const val VIDEO:String = "video"
        const val BROWSE:String  = "browse"
    }
}
