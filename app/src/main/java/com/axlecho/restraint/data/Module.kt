package com.axlecho.restraint.data

data class Info (
    val name:String,
    var count:Long = 0L,
    var time:Long = 0L
)

data class Filter(
    val type:String,
    val app:MutableList<Info> = arrayListOf()
) {
    companion object {
        const val IGNORE = "ignore"
    }
}
