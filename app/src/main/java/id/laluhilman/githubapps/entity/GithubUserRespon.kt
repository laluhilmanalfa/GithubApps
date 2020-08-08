package id.laluhilman.githubapps.entity

import com.google.gson.annotations.SerializedName

data class GithubUserRespon(
    @SerializedName("total_count")
    var userName : String = "",

    @SerializedName("items")
    var items : ArrayList<GithubUser> = ArrayList()


)