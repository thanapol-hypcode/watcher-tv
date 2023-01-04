package com.medina.juanantonio.watcher.sources.content

import android.content.Context
import com.medina.juanantonio.watcher.network.ApiService
import com.medina.juanantonio.watcher.network.models.home.GetHomePageResponse
import com.medina.juanantonio.watcher.network.wrapWithResult
import kotlinx.coroutines.CancellationException
import com.medina.juanantonio.watcher.network.Result
import com.medina.juanantonio.watcher.network.models.home.GetAlbumDetailsResponse
import com.medina.juanantonio.watcher.network.models.home.GetNavigationBarResponse
import com.medina.juanantonio.watcher.network.models.search.GetSearchLeaderboardResponse
import com.medina.juanantonio.watcher.network.models.search.SearchByKeywordRequest
import com.medina.juanantonio.watcher.network.models.search.SearchByKeywordResponse
import com.medina.juanantonio.watcher.shared.utils.CoroutineDispatchers
import com.medina.juanantonio.watcher.sources.BaseRemoteSource
import kotlinx.coroutines.withContext

class ContentRemoteSource(
    context: Context,
    private val apiService: ApiService,
    private val dispatchers: CoroutineDispatchers
) : BaseRemoteSource(context), IContentRemoteSource {

    override suspend fun getNavigationBar(): Result<GetNavigationBarResponse> {
        return try {
            val response = withContext(dispatchers.io) {
                apiService.getNavigationBar()
            }
            response.wrapWithResult()
        } catch (exception: CancellationException) {
            Result.Cancelled()
        } catch (exception: Exception) {
            getDefaultErrorResponse()
        }
    }

    override suspend fun getHomePage(page: Int, navigationId: Int?): Result<GetHomePageResponse> {
        return try {
            val response = withContext(dispatchers.io) {
                apiService.getHomePage(page, navigationId)
            }
            response.wrapWithResult()
        } catch (exception: CancellationException) {
            Result.Cancelled()
        } catch (exception: Exception) {
            getDefaultErrorResponse()
        }
    }

    override suspend fun getAlbumDetails(
        page: Int,
        size: Int,
        id: Int
    ): Result<GetAlbumDetailsResponse> {
        return try {
            val response = withContext(dispatchers.io) {
                apiService.getAlbumDetails(page, size, id)
            }
            response.wrapWithResult()
        } catch (exception: CancellationException) {
            Result.Cancelled()
        } catch (exception: Exception) {
            getDefaultErrorResponse()
        }
    }

    override suspend fun searchByKeyword(
        searchKeyword: String,
        size: Int,
        sort: String,
        searchType: String
    ): Result<SearchByKeywordResponse> {
        return try {
            val response = withContext(dispatchers.io) {
                apiService.searchByKeyword(
                    SearchByKeywordRequest(
                        searchKeyword,
                        size,
                        sort,
                        searchType
                    )
                )
            }
            response.wrapWithResult()
        } catch (exception: CancellationException) {
            Result.Cancelled()
        } catch (exception: Exception) {
            getDefaultErrorResponse()
        }
    }

    override suspend fun getSearchLeaderboard(): Result<GetSearchLeaderboardResponse> {
        return try {
            val response = withContext(dispatchers.io) {
                apiService.getSearchLeaderboard()
            }
            response.wrapWithResult()
        } catch (exception: CancellationException) {
            Result.Cancelled()
        } catch (exception: Exception) {
            getDefaultErrorResponse()
        }
    }
}

interface IContentRemoteSource {
    suspend fun getNavigationBar(): Result<GetNavigationBarResponse>
    suspend fun getHomePage(page: Int, navigationId: Int? = null): Result<GetHomePageResponse>
    suspend fun getAlbumDetails(
        page: Int = 0,
        size: Int = 50,
        id: Int
    ): Result<GetAlbumDetailsResponse>

    suspend fun searchByKeyword(
        searchKeyword: String,
        size: Int = 50,
        sort: String = "",
        searchType: String = ""
    ): Result<SearchByKeywordResponse>

    suspend fun getSearchLeaderboard(): Result<GetSearchLeaderboardResponse>
}