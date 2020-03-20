sealed class WeeklyItem {
    abstract val headline: String
    abstract val link: String
    abstract val description: String

    data class Article(
        override val headline: String,
        override val link: String,
        override val description: String,
        val mainUrl: String,
        val imgLink: String?
    ) : WeeklyItem()

    data class Sponsored(
        override val headline: String,
        override val link: String,
        override val description: String,
        val mainUrl: String,
        val imgLink: String?
    ) : WeeklyItem()

    data class Library(
        override val headline: String,
        override val link: String,
        override val description: String,
        val mainUrl: String,
        val imgLink: String?
    ) : WeeklyItem()

    data class Video(
        override val headline: String,
        override val link: String,
        override val description: String,
        val mainUrl: String,
        val imgLink: String?
    ) : WeeklyItem()

    data class Job(
        override val headline: String,
        override val link: String,
        override val description: String,
        val location: String
    ) : WeeklyItem()

    data class News(
        override val headline: String,
        override val link: String,
        override val description: String,
        val mainUrl: String,
        val imgLink: String?
    ) : WeeklyItem()

    data class Special(
        override val headline: String,
        override val link: String,
        override val description: String,
        val mainUrl: String,
        val imgLink: String?
    ) : WeeklyItem()

    data class Design(
        override val headline: String,
        override val link: String,
        override val description: String,
        val mainUrl: String,
        val imgLink: String?
    ) : WeeklyItem()

    data class Event(
        override val headline: String,
        override val link: String,
        override val description: String,
        val mainUrl: String,
        val imgLink: String?
    ) : WeeklyItem()

    data class Tool(
        override val headline: String,
        override val link: String,
        override val description: String,
        val mainUrl: String,
        val imgLink: String?
    ) : WeeklyItem()

    data class Business(
        override val headline: String,
        override val link: String,
        override val description: String,
        val mainUrl: String,
        val imgLink: String?
    ) : WeeklyItem()

    data class Unknown(
        override val headline: String,
        override val link: String,
        override val description: String,
        val subHeadline: String,
        val imgLink: String?
    ) : WeeklyItem()
}
