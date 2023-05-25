package kg.surfit.testweekapp3.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kg.surfit.testweekapp3.models.GitRepo
import kg.surfit.testweekapp3.models.GitUser
import kg.surfit.testweekapp3.repository.GitRepository
import kg.surfit.testweekapp3.ui.adapter.GitRepoAdapter
import kg.surfit.testweekapp3.utils.retrofit.RestApiInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class MainViewModel : ViewModel() {
    private val repository: GitRepository = GitRepository(RestApiInterface.invoke())
    val mainActionFlow: MutableSharedFlow<MainAction> = MutableSharedFlow()

    private val _userNameFlow = MutableStateFlow("nurs-34")
    val userName: StateFlow<String> get() = _userNameFlow.asStateFlow()

    private val _userFlow = MutableStateFlow<GitUser?>(null)
    val user: MutableStateFlow<GitUser?> get() = _userFlow

    private val _listFlow: MutableStateFlow<List<GitRepo>> = MutableStateFlow(emptyList())
    val repoList: StateFlow<List<GitRepo>> get() = _listFlow

    init {
        getGitUserInfo(userName.value)
        getGitRepo(userName.value)
    }

    fun getGitUserInfo(userName: String) {
        viewModelScope.launch {
            try {
                val gitUser = withContext(Dispatchers.IO) {
                    repository.getGitUser(userName)
                }

                _userFlow.value = gitUser
            } catch (e: HttpException) {
                mainActionFlow.emit(MainAction.ShowErrorToast)
                Log.e("MainViewModel", e.toString())
            }
        }
    }

    fun getGitRepo(userName: String) {
        viewModelScope.launch {
            try {
                val repos = withContext(Dispatchers.IO) {
                    repository.getGitRepo(userName)
                }

                val updatedList = mutableListOf<GitRepo>()

                for (i in repos.indices) {
                    val repo = repos[i]
                    updatedList.add(repo)
                }

                _listFlow.value = updatedList
            } catch (e: HttpException) {
                mainActionFlow.emit(MainAction.ShowErrorToast)
                Log.e("MainViewModel", e.toString())
            }

        }
    }

    fun getAll(adapter: GitRepoAdapter) {
        viewModelScope.launch {
            getGitUserInfo(userName.value)
            getGitRepo(userName.value)
            delayAndUpdateData(adapter)

        }
    }

    fun delayAndUpdateData(adapter: GitRepoAdapter) {
        viewModelScope.launch {
            withContext(Dispatchers.Main) {
                delay(1250) //апи грузится около 900 милисекунд
                adapter.updateData(repoList.value)
            }
        }
    }

    fun setText(text: String) {
        _userNameFlow.value = text
    }
}

sealed class MainAction {
    object ShowErrorToast : MainAction()
}