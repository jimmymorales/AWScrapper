data class AndroidWeeklyIssue(
    val number: Int,
    val date: String,
    val items: List<WeeklyItem>
)

data class WeeklyItem(
    val link: String,
    val description: String,
    val type: String
)

