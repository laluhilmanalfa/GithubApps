package id.laluhilman.githubapps.di

import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import id.laluhilman.githubapps.repo.GithubUserRepo
import id.laluhilman.githubapps.screen.searchUser.SearchUserViewModel
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


val networkModule = module {

    single { provideOkHttpClient() }
    single { provideRetrofit(get()) }
    single { provideGithubUserRepo(get()) }

}

fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder().baseUrl("https://api.github.com/").client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .build()
}


fun provideOkHttpClient(): OkHttpClient {

    var okHttpClient = OkHttpClient().newBuilder()
    okHttpClient.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .build()
    return okHttpClient.build()

}

fun provideGithubUserRepo(retrofit: Retrofit): GithubUserRepo = retrofit.create(GithubUserRepo::class.java)

private val viewModelModules = module {
    viewModel {
        SearchUserViewModel(get())
    }
}

val appModules = listOf(networkModule, viewModelModules)
