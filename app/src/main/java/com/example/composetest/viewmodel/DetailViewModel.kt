package com.example.composetest.viewmodel

import androidx.lifecycle.ViewModel
import com.example.composetest.repo.UserInfoRepo
import com.example.composetest.userInfoProto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val userInfoRepo: UserInfoRepo) : ViewModel() {
    fun getUserInfo(id: String): Flow<userInfoProto> {
        return userInfoRepo.getUser(id)
    }
}