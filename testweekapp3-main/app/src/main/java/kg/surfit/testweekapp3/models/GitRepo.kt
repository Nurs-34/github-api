package kg.surfit.testweekapp3.models

import com.google.gson.annotations.SerializedName

data class GitRepo(
    @SerializedName("name") val name: String?,
    @SerializedName("description") val desc: String?
)