package com.medina.juanantonio.watcher.data.adapters

import androidx.leanback.widget.ArrayObjectAdapter
import androidx.leanback.widget.HeaderItem
import androidx.leanback.widget.ListRow
import androidx.leanback.widget.ListRowPresenter
import com.medina.juanantonio.watcher.data.models.VideoGroup
import com.medina.juanantonio.watcher.data.presenters.VideoCardPresenter

class ContentAdapter: ArrayObjectAdapter(ListRowPresenter()) {

    fun addContent(videoGroup: List<VideoGroup>) {
        val cardPresenter = VideoCardPresenter()
        videoGroup.forEach {
            val listRowAdapter = ArrayObjectAdapter(cardPresenter)
            listRowAdapter.addAll(0, it.videoList)
            val headerItem = HeaderItem(it.category)
            add(ListRow(headerItem, listRowAdapter))
        }
    }
}