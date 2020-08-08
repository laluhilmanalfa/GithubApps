package id.laluhilman.githubapps.repo

import id.laluhilman.githubapps.entity.GithubUserRespon
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface GithubUserRepo {

    @GET("search/users")
    fun getUser(@Query("q") userName :  String, @Query("page") page :  Int,
                      @Query("per_page") perPage :  Int ): Observable<GithubUserRespon>
}