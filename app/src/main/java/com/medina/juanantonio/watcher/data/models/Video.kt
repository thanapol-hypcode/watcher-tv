package com.medina.juanantonio.watcher.data.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.medina.juanantonio.watcher.network.models.home.HomePageBean
import com.medina.juanantonio.watcher.network.models.player.EpisodeBean
import com.medina.juanantonio.watcher.network.models.player.VideoSuggestion
import com.medina.juanantonio.watcher.network.models.search.SearchResultBean
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class Video(
    val category: Int?,
    val contentType: HomePageBean.ContentType,
    @PrimaryKey val contentId: Int,
    val imageUrl: String,
    val title: String,
    var episodeNumber: Int,
    val episodeCount: Int
) : Parcelable {

    @Ignore
    var isSearchResult = false

    var videoProgress: Long = 0L

    var lastWatchTime: Long = System.currentTimeMillis()

    constructor(bean: HomePageBean.Content) : this(
        category = bean.category,
        contentType = bean.contentType,
        contentId = bean.id,
        imageUrl = bean.imageUrl,
        title = bean.title,
        episodeNumber = 0,
        episodeCount = bean.resourceNum ?: 0
    )

    constructor(video: Video, bean: EpisodeBean, episodeCount: Int) : this(
        category = video.category,
        contentType = video.contentType,
        contentId = video.contentId,
        imageUrl = video.imageUrl,
        title = video.title,
        episodeNumber = bean.seriesNo,
        episodeCount = episodeCount
    )

    constructor(bean: SearchResultBean) : this(
        category = when (bean.dramaType?.code) {
            SearchResultBean.DramaCode.MOVIE -> 0
            else -> 1
        },
        contentType = when (bean.dramaType?.code) {
            SearchResultBean.DramaCode.MOVIE -> HomePageBean.ContentType.MOVIE
            else -> HomePageBean.ContentType.DRAMA
        },
        contentId = bean.id,
        imageUrl = bean.coverVerticalUrl,
        title = bean.name,
        episodeNumber = 0,
        episodeCount = 0
    ) {
        isSearchResult = true
    }

    constructor(videoSuggestion: VideoSuggestion): this(
        category = videoSuggestion.category,
        contentType = when (videoSuggestion.category) {
            0 -> HomePageBean.ContentType.MOVIE
            else -> HomePageBean.ContentType.DRAMA
        },
        contentId = videoSuggestion.id,
        imageUrl = videoSuggestion.coverVerticalUrl,
        title = videoSuggestion.name,
        episodeNumber = 0,
        episodeCount = 0
    )
}