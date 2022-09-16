package com.medina.juanantonio.watcher.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * A group of content that has a String category and a List of Video objects
 */
@Parcelize
class VideoGroup(val category: String, val videoList: List<Video>): Parcelable