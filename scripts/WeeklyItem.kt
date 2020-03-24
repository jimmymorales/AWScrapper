data class WeeklyItem(
    val headline: String,
    val link: String,
    val description: String,
    val mainUrl: String,
    val type: Type,
    val imgLink: String?
) {
    enum class Type {
        ARTICLE,
        SPONSORED,
        LIBRARY,
        VIDEO,
        JOB,
        NEWS,
        SPECIAL,
        DESIGN,
        EVENT,
        TOOL,
        BUSINESS,
        UNKNOWN,
    }
}

fun String.toWeeklyItemType() = when (this.toUpperCase()) {
    "ARTICLES & TUTORIALS" -> WeeklyItem.Type.ARTICLE
    "SPONSORED" -> WeeklyItem.Type.SPONSORED
    "LIBRARIES & CODE" -> WeeklyItem.Type.LIBRARY
    "VIDEOS & PODCASTS" -> WeeklyItem.Type.VIDEO
    "JOBS" -> WeeklyItem.Type.JOB
    "NEWS" -> WeeklyItem.Type.NEWS
    "SPECIALS" -> WeeklyItem.Type.SPECIAL
    "DESIGN" -> WeeklyItem.Type.DESIGN
    "EVENTS" -> WeeklyItem.Type.EVENT
    "TOOLS" -> WeeklyItem.Type.TOOL
    "BUSINESS" -> WeeklyItem.Type.BUSINESS
    else -> WeeklyItem.Type.UNKNOWN
}
