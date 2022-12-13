package com.difa.myapplication.core.utils

import com.difa.myapplication.core.data.source.local.entity.CastEntity
import com.difa.myapplication.core.data.source.local.entity.ShowEntity
import com.difa.myapplication.core.data.source.remote.response.CastItem
import com.difa.myapplication.core.data.source.remote.response.MovieItem
import com.difa.myapplication.core.data.source.remote.response.TvItem
import com.difa.myapplication.core.domain.model.CastModel
import com.difa.myapplication.core.domain.model.ShowModel

object DataMapper {

    //FOR HOME
    fun mapMovieResponseToEntities(input: List<MovieItem>, category: Int): List<ShowEntity> {
        return input.map {
            ShowEntity(
                //first data from response
                id = it.id_movie,
                title = it.title,
                overview = it.overview,
                posterPath = it.posterPath ?: "",
                releaseDate = it.releaseDate,
                voteAverage = it.voteAverage.toString(),
                //additional data
                showType = MOVIE,
                category = category,
                isFavorite = false,
                //update data
                backdropPath = it.backdropPath ?: "",
                genres1 = "",
                genres2 = "",
                genres3 = "",
                runtime = it.runtime ?: 0,
                adult = it.adult ?: false
            )
        }
    }

    fun mapMovieResponseToDomain(input: List<MovieItem>): List<ShowModel> {
        return input.map {
            ShowModel(
                //first data from response
                id = it.id_movie,
                title = it.title,
                overview = it.overview,
                posterPath = it.posterPath ?: "",
                releaseDate = it.releaseDate,
                voteAverage = it.voteAverage.toString().substring(0, 3),
                //additional data
                showType = MOVIE,
                category = (0..2).random(),
                isFavorite = false,
                //update data
                backdropPath = it.backdropPath ?: "",
                genres1 = "",
                genres2 = "",
                genres3 = "",
                runtime = it.runtime ?: 0,
                adult = it.adult ?: false
            )
        }
    }

    fun mapTvResponseToDomain(input: List<TvItem>): List<ShowModel> {
        return input.map {
            ShowModel(
                //first data from response
                id = it.id_tv,
                title = it.title,
                overview = it.overview,
                posterPath = it.posterPath ?: "",
                releaseDate = it.releaseDate,
                voteAverage = it.voteAverage.toString().substring(0, 3),
                //additional data
                showType = TV_SERIES,
                category = (0..2).random(),
                isFavorite = false,
                //update data
                backdropPath = it.backdropPath ?: "",
                genres1 = "",
                genres2 = "",
                genres3 = "",
                runtime = 0,
                adult = it.adult ?: false
            )
        }
    }

    fun mapTvResponseToEntities(input: List<TvItem>, category: Int): List<ShowEntity> {
        return input.map {
            ShowEntity(
                //first data from response
                id = it.id_tv,
                title = it.title,
                overview = it.overview,
                posterPath = it.posterPath ?: "",
                releaseDate = it.releaseDate ?: "-",
                voteAverage = it.voteAverage.toString(),
                //additional data
                showType = TV_SERIES,
                category = category,
                isFavorite = false,
                //update data
                backdropPath = it.backdropPath ?: "",
                genres1 = "",
                genres2 = "",
                genres3 = "",
                runtime = 0,
                adult = it.adult ?: false
            )
        }
    }


    fun mapShowEntitiesToDomain(input: List<ShowEntity>): List<ShowModel> =
        input.map {
            ShowModel(
                id = it.id,
                title = it.title,
                overview = it.overview,
                posterPath = it.posterPath,
                releaseDate = it.releaseDate,
                voteAverage = it.voteAverage.toString(),
                //additional data
                showType = it.showType,
                category = it.category,
                isFavorite = it.isFavorite,
                //update data
                backdropPath = it.backdropPath,
                genres1 = it.genres1,
                genres2 = it.genres2,
                genres3 = it.genres3,
                runtime = it.runtime,
                adult = it.adult
            )
        }

    //FOR DETAIL
    fun mapMovieDetailResponseToEntities(input: MovieItem, category: Int): ShowEntity {
        var genres1: String? = null
        var genres2: String? = null
        var genres3: String? = null

        if (input.genres?.size!! > 0) {
            genres1 = input.genres[0]?.name
        }

        if (input.genres.size > 2) {
            genres3 = input.genres[2]?.name
        }

        if (input.genres.size > 1) {
            genres2 = input.genres[1]?.name
        }
        return ShowEntity(
            id = input.id_movie,
            title = input.title,
            overview = input.overview,
            posterPath = input.posterPath,
            releaseDate = input.releaseDate,
            voteAverage = input.voteAverage.toString().substring(0, 3),
            category = category,
            showType = MOVIE,
            backdropPath = input.backdropPath,
            genres1 = genres1,
            genres2 = genres2,
            genres3 = genres3,
            runtime = input.runtime,
            adult = input.adult,
            isFavorite = false
        )

    }

    fun mapTvDetailResponseToEntities(input: TvItem, category: Int): ShowEntity {
        var genres1: String? = null
        var genres2: String? = null
        var genres3: String? = null

        if (input.genres?.size!! > 0) {
            genres1 = input.genres[0]?.name
        }

        if (input.genres.size > 2) {
            genres3 = input.genres[2]?.name
        }

        if (input.genres.size > 1) {
            genres2 = input.genres[1]?.name
        }
        return ShowEntity(
            id = input.id_tv,
            title = input.title,
            overview = input.overview,
            posterPath = input.posterPath ?: "",
            releaseDate = input.releaseDate ?: "-",
            voteAverage = input.voteAverage.toString().substring(0, 3),
            category = category,
            showType = TV_SERIES,
            backdropPath = input.backdropPath,
            genres1 = genres1,
            genres2 = genres2,
            genres3 = genres3,
            runtime = 0,
            adult = input.adult,
            isFavorite = false
        )

    }

    fun mapDetailEntitiesToDomain(input: ShowEntity?): ShowModel =
        ShowModel(
            id = input!!.id,
            title = input.title,
            overview = input.overview,
            posterPath = input.posterPath ?: "",
            releaseDate = input.releaseDate,
            voteAverage = input.voteAverage.toString(),
            category = input.category,
            showType = input.showType,
            backdropPath = input.backdropPath,
            genres1 = input.genres1,
            genres2 = input.genres2,
            genres3 = input.genres3,
            runtime = input.runtime,
            adult = input.adult,
            isFavorite = input.isFavorite
        )

    fun mapCastResponseToEntities(input: List<CastItem>, showId: String): List<CastEntity> {
        return input.map {
            CastEntity(
                id = showId,
                castId = it.castId,
                name = it.name,
                character = it.character,
                profilePath = it.profilePath,
                knownAs = it.knownAs,
                biography = it.biography ?: "",
                birthDay = it.birthDay ?: "",
                deathDay = it.deathDay ?: ""
            )
        }

    }

    fun mapCastEntitiesToDomain(input: List<CastEntity>): List<CastModel> =
        input.map {
            CastModel(
                id = it.id,
                castId = it.castId,
                name = it.name,
                character = it.character ?: "",
                profilePath = it.profilePath,
                knownAs = it.knownAs,
                biography = it.biography ?: "",
                birthDay = it.birthDay ?: "",
                deathDay = it.deathDay ?: ""
            )
        }

    fun mapDetailCastEntitiesToDomain(it: CastEntity): CastModel =
        CastModel(
            id = it.id,
            castId = it.castId,
            name = it.name,
            character = it.character ?: "",
            profilePath = it.profilePath,
            knownAs = it.knownAs,
            biography = it.biography ?: "",
            birthDay = it.birthDay ?: "",
            deathDay = it.deathDay ?: ""
        )

    fun mapDetailCastResponseToEntities(it: CastItem, showId: String, character: String): CastEntity {
        return CastEntity(
                id = showId,
                castId = it.castId,
                name = it.name,
                character = character,
                profilePath = it.profilePath,
                knownAs = it.knownAs,
                biography = it.biography ?: "",
                birthDay = it.birthDay ?: "",
                deathDay = it.deathDay ?: ""
            )
    }
    fun mapDomainToEntity(input: ShowModel) = ShowEntity(
        id = input.id,
        title = input.title,
        overview = input.overview,
        posterPath = input.posterPath,
        releaseDate = input.releaseDate,
        voteAverage = input.voteAverage,
        category = input.category,
        showType = input.showType,
        isFavorite = input.isFavorite,
        backdropPath = input.backdropPath,
        genres1 = input.genres1,
        genres2 = input.genres2,
        genres3 = input.genres3,
        runtime = input.runtime,
        adult = input.adult
    )

    fun dummyData(): ShowModel =
        ShowModel("view_all", "", "", "", "", "", 5, 5, false, "", "", "", "", 0, false)

    fun dummyDataEntity(): ShowEntity =
        ShowEntity("view_all", "", "", "", "", "", 5, 5, false, "", "", "", "", 0, false)

}