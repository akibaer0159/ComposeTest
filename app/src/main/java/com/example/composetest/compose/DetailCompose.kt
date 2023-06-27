package com.example.composetest.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.composetest.R
import com.example.composetest.ui.theme.black22
import com.example.composetest.ui.theme.darkBrown
import com.example.composetest.ui.theme.grayEA
import com.example.composetest.userInfoProto
import com.example.composetest.viewmodel.DetailViewModel

@Composable
fun DetailScreen(userId: String, detailViewModel: DetailViewModel = hiltViewModel()) {
    val userInfo: userInfoProto by detailViewModel.getUserInfo(userId)
        .collectAsState(initial = userInfoProto.getDefaultInstance())

    DetailContent(userInfo)
}

@Composable
fun DetailContent(userInfo: userInfoProto) {
    Box(modifier = Modifier
        .background(Color.White)
        .fillMaxSize()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.padding(vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier.weight(2f)) {
                    Image(
                        painter = painterResource(id = R.drawable.circle_icons_profile),
                        contentDescription = "avatar",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = userInfo.userName,
                    color = black22,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(8f)
                )
            }
            Divider(color = darkBrown, thickness = 1.dp)
            Row(modifier = Modifier.padding(vertical = 10.dp)) {
                Text(
                    text = "Position",
                    color = black22,
                    fontSize = 14.sp,
                    modifier = Modifier.weight(2f)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = userInfo.usePosition,
                    color = black22,
                    fontSize = 14.sp,
                    modifier = Modifier.weight(8f)
                )
            }
            Divider(color = grayEA, thickness = 1.dp)
            Row(modifier = Modifier.padding(vertical = 10.dp)) {
                Text(
                    text = "Team",
                    color = black22,
                    fontSize = 14.sp,
                    modifier = Modifier.weight(2f)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = userInfo.userTeamName,
                    color = black22,
                    fontSize = 14.sp,
                    modifier = Modifier.weight(8f)
                )
            }
        }
    }
}