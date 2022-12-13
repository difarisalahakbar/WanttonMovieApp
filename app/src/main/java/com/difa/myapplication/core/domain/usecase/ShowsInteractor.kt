package com.difa.myapplication.core.domain.usecase

 import com.difa.myapplication.core.data.Resource
 import com.difa.myapplication.core.domain.model.CastModel
 import com.difa.myapplication.core.domain.model.ShowModel
 import com.difa.myapplication.core.domain.repository.IShowRepository
 import kotlinx.coroutines.flow.Flow
 import javax.inject.Inject

class ShowsInteractor @Inject constructor(private val showRepository: IShowRepository): ShowUseCase {
    override fun getAllMovie(category: Int, page: Int, limit: Int): Flow<Resource<List<ShowModel>>> {
        return showRepository.getAllMovie(category, page, limit)
    }
    override fun getAllTv(category: Int, page: Int, limit: Int): Flow<Resource<List<ShowModel>>> {
        return showRepository.getAllTv(category, page, limit)
    }

    override fun getMovieDetail(movieId: String, category: Int): Flow<Resource<ShowModel>> {
        return showRepository.getMovieDetail(movieId, category)
    }

    override fun getTvDetail(tvId: String, category: Int): Flow<Resource<ShowModel>> {
        return showRepository.getTvDetail(tvId, category)
    }

    override fun getMovieCast(showId: String): Flow<Resource<List<CastModel>>> {
        return showRepository.getMovieCast(showId)
    }

    override fun getTvCast(showId: String): Flow<Resource<List<CastModel>>> {
        return showRepository.getTvCast(showId)
    }

    override fun getAllFavorite(): Flow<List<ShowModel>> {
        return showRepository.getAllFavorite()
    }

    override fun searchShow(keyword: String): Flow<Resource<List<ShowModel>>> {
        return showRepository.searchShow(keyword)
    }

    override fun getAllSimilarShow(id: String, showType: Int): Flow<Resource<List<ShowModel>>> {
        return showRepository.getAllSimilarShow(id, showType)
    }

    override fun getDetailCastById(castId: String, showId: String, character: String): Flow<Resource<CastModel>> {
        return showRepository.getDetailCastById(castId, showId, character)
    }

    override fun getCastMovieOrTv(castId: String, showType: Int): Flow<Resource<List<ShowModel>>> {
        return showRepository.getCastMovieOrTv(castId, showType)
    }

    override suspend fun setFavorite(showModel: ShowModel, state: Boolean) {
        return showRepository.setFavorite(showModel, state)
    }




}