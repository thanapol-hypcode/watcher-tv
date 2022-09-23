package com.medina.juanantonio.watcher.data.models

import android.os.Parcelable
import com.medina.juanantonio.watcher.network.models.player.*
import kotlinx.android.parcel.Parcelize

@Parcelize
data class VideoMedia(
    val id: Int, // The id of the specific video media
    val contentId: Int, // The id of the show or movie
    val categoryId: Int, // The id indicator if the show is a movie or series
    val title: String,
    val introduction: String,
    val mediaUrl: String,
    val subtitles: List<Subtitle>?,
    val connectedVideos: List<VideoSuggestion>?,
    val videoSuggestions: List<VideoSuggestion>?
) : Parcelable {

    constructor(
        contentId: Int,
        categoryId: Int,
        episodeBean: EpisodeBean,
        detailsResponse: GetVideoDetailsResponse.Data,
        mediaResponse: GetVideoResourceResponse.Data
    ) : this(
        id = episodeBean.id,
        contentId = contentId,
        categoryId = categoryId,
        title = if (categoryId == 0) detailsResponse.name
        else "${detailsResponse.name} - Episode ${episodeBean.seriesNo}",
        introduction = detailsResponse.introduction,
        mediaUrl = mediaResponse.mediaUrl,
        subtitles = detailsResponse.episodeVo.firstOrNull {
            it.id == episodeBean.id
        }?.subtitlingList,
        connectedVideos = detailsResponse.refList.filter {
            it.name != detailsResponse.name
        },
        videoSuggestions = detailsResponse.likeList
    )

    fun getPreferredSubtitle(): Subtitle? {
        return subtitles?.firstOrNull { it.languageAbbr == "en" } ?: subtitles?.firstOrNull()
    }
}