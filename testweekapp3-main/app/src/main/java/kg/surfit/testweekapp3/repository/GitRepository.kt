package kg.surfit.testweekapp3.repository

import kg.surfit.testweekapp3.models.GitRepo
import kg.surfit.testweekapp3.models.GitUser
import kg.surfit.testweekapp3.utils.retrofit.RestApiInterface

class GitRepository(private val apiService: RestApiInterface) {

    suspend fun getGitUser(userName: String): GitUser {
        return apiService.getGitUser(userName)
    }

    suspend fun getGitRepo(userName: String): List<GitRepo> {
        return apiService.getUserRepositories(userName)
    }

}