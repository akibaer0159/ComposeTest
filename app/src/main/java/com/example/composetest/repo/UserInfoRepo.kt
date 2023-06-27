package com.example.composetest.repo

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.example.composetest.data.UserInfo
import com.example.composetest.data.UserListSerializer
import com.example.composetest.userInfoProto
import com.example.composetest.userListProto
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val Context.userListProto: DataStore<userListProto> by dataStore(
    fileName = "userList.db",
    serializer = UserListSerializer
)

class UserInfoRepo @Inject constructor(@ApplicationContext private val context: Context) {
    val userListDataStore = context.userListProto.data

    suspend fun addUser(userInfo: UserInfo) {
        val infoProto = userInfoProto.newBuilder()
        infoProto.userName = userInfo.userName
        infoProto.isOnOff = userInfo.isOnOff
        infoProto.userTeamName = userInfo.userTeamName
        infoProto.usePosition = userInfo.userPosition
        infoProto.userId = userInfo.userId

        context.userListProto.updateData { infoBuilder ->
            infoBuilder
                .toBuilder()
                .addUserInfoList(infoProto)
                .build()
        }
    }

    suspend fun removeUser(userInfo: userInfoProto) {
        context.userListProto.updateData { infoBuilder ->
            val builder = infoBuilder.toBuilder()
            val index = builder.userInfoListList.indexOf(userInfo)
            builder.removeUserInfoList(index)
            builder.build()
        }
    }

    fun getUser(id: String): Flow<userInfoProto> {
        return context.userListProto.data.map {
            it.userInfoListList.firstOrNull { userInfoProto -> userInfoProto.userId == id }
                ?: userInfoProto.getDefaultInstance()
        }
    }
}