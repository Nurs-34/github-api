package kg.surfit.testweekapp3.models

import com.google.gson.annotations.SerializedName

data class GitUser(
    @SerializedName("login") val username: String?,
    @SerializedName("avatar_url") val avatar: String?
)