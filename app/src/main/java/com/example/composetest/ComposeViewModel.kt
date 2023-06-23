package com.example.composetest

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class ComposeViewModel @Inject constructor(private val userInfoRepo: UserInfoRepo) :
    ViewModel() {
    val name = MutableLiveData("")
    val nameList = MutableLiveData<ArrayList<UserInfo>>(arrayListOf())
    val jobList = arrayListOf("Android", "iOS", "Server", "Design", "Operator", "Leader")
    val job = MutableLiveData(jobList[0])
    val teamList = arrayListOf("기획팀", "개발팀", "인사팀")
    val team = MutableLiveData(teamList[0])
    val userDataStore: Flow<userListProto> = userInfoRepo.userListDataStore

    fun onNameChanged(newName: String) {
        name.value = newName
    }

    fun onInputName() {
        val userinfo = UserInfo(
            userName = name.value ?: "",
            userPosition = job.value ?: "",
            isOnOff = Random.nextBoolean(),
            userTeamName = team.value ?: "",
            userId = System.currentTimeMillis().toString(),
        )
        nameList.value?.add(
            userinfo
        )
        nameList.value = nameList.value

        saveUserInfo(userinfo)
    }

    fun onJobSelected(selectedJob: String) {
        job.value = selectedJob
    }

    fun onTeamSelected(teamName: String) {
        team.value = teamName
    }

    fun saveUserInfo(userInfo: UserInfo) {
        viewModelScope.launch {
            userInfoRepo.addUser(userInfo)
        }
    }
}