sealed class WeeklyItem {
    abstract val headline: String
    abstract val link: String
    abstract val mainUrl: String
    abstract val description: String
    abstract val imgLink: String?

    data class Article(
        override val headline: String,
        override val link: String,
        override val mainUrl: String,
        override val description: String,
        override val imgLink: String?
    ) : WeeklyItem()
} 
