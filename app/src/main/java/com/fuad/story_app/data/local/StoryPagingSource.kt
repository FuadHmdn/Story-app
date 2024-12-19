package com.fuad.story_app.data.local

import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.fuad.story_app.data.remote.ApiService
import com.fuad.story_app.data.remote.response.ListStoryItem

class StoryPagingSource(
    private val apiService: ApiService, private val token: String,
    private val errorMessage: MutableLiveData<String?>,
    private val isLoading: MutableLiveData<Boolean?>
) :
    PagingSource<Int, ListStoryItem>() {

    override fun getRefreshKey(state: PagingState<Int, ListStoryItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStoryItem> {
        return try {
            isLoading.postValue(true)
            val position = params.key ?: INITIAL_PAGE_INDEX
            val responseData =
                apiService.getAllStories(token, page = position, size = params.loadSize)

            val stories = responseData.listStory?.filterNotNull() ?: emptyList()

            LoadResult.Page(
                data = stories,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (stories.isEmpty()) null else position + 1
            ).also {
                isLoading.postValue(false)
            }
        } catch (exception: Exception) {
            isLoading.postValue(false)
            errorMessage.postValue("Tidak dapat memuat: ${exception.message}")
            return LoadResult.Error(exception)
        }
    }

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}