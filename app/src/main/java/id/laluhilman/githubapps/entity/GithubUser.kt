package id.laluhilman.githubapps.entity

import com.google.gson.annotations.SerializedName

data class GithubUser(
    @SerializedName("login")
    var userName : String = "",

    @SerializedName("avatar_url")
    var profileUrl : String = ""
)