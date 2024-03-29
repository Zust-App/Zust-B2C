package `in`.opening.area.zustapp.pagination

import `in`.opening.area.zustapp.network.ApiRequestManager
import zustbase.orderHistory.models.OrderHistoryItem
import androidx.paging.PagingSource
import androidx.paging.PagingState

class UserBookingDataSource(private val apiRequestManager: ApiRequestManager) : PagingSource<Int, OrderHistoryItem>() {

    override fun getRefreshKey(state: PagingState<Int, OrderHistoryItem>): Int? {
        //state.anchorPosition
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, OrderHistoryItem> {
        try {
            val nextPage = params.key ?: 1
            val userBookings = apiRequestManager.getUserBookings(nextPage)
            return if (userBookings.data != null) {
                val metaData = userBookings.data._metadata
                if (metaData.currentPage <= metaData.totalPage) {
                    LoadResult.Page(
                        data = userBookings.data.orderHistories,
                        prevKey = if (nextPage == 1) null else nextPage - 1,
                        nextKey = metaData.currentPage + 1
                    )
                } else if (metaData.totalPage == 0) {
                    LoadResult.Error(NoPageFoundException())
                } else {
                    LoadResult.Error(PageMismatchedException())
                }
            } else {
                LoadResult.Error(NoDataFoundException())
            }
        } catch (e: Throwable) {
            return LoadResult.Error(LoadingError())
        }
    }

}

class UserNonVegBookingDataSource(private val apiRequestManager: ApiRequestManager) : PagingSource<Int, OrderHistoryItem>() {

    override fun getRefreshKey(state: PagingState<Int, OrderHistoryItem>): Int? {
        //state.anchorPosition
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, OrderHistoryItem> {
        try {
            val nextPage = params.key ?: 1
            val userBookings = apiRequestManager.getUserNonVegBookings(nextPage)
            return if (userBookings.data != null) {
                val metaData = userBookings.data._metadata
                if (metaData.currentPage <= metaData.totalPage) {
                    LoadResult.Page(
                        data = userBookings.data.orderHistories,
                        prevKey = if (nextPage == 1) null else nextPage - 1,
                        nextKey = metaData.currentPage + 1
                    )
                } else if (metaData.totalPage == 0) {
                    LoadResult.Error(NoPageFoundException())
                } else {
                    LoadResult.Error(PageMismatchedException())
                }
            } else {
                LoadResult.Error(NoDataFoundException())
            }
        } catch (e: Throwable) {
            return LoadResult.Error(LoadingError())
        }
    }

}

class NoPageFoundException : Exception() {
    override val message: String
        get() = NO_PAGE
}

class PageMismatchedException : Exception() {
    override val message: String
        get() = NO_PAGE
}

class NoDataFoundException : Exception() {
    override val message: String
        get() = NO_PAGE
}

class LoadingError : Exception() {
    override val message: String
        get() = "Something went error try again"
}

const val NO_PAGE = "no_page"