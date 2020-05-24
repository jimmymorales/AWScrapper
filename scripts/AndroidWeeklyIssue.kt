data class AndroidWeeklyIssue(
    val number: Int,
    val date: String,
    val items: List<WeeklyItem>
)

data class WeeklyItem(
    val header: String,
    val description: String,
    val type: String,
    val link: String,
    val location: String,
    val img: String? = null
)

