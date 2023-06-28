package com.example.composetest.compose

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.composetest.*
import com.example.composetest.R
import com.example.composetest.data.UserInfo
import com.example.composetest.ui.theme.*
import com.example.composetest.viewmodel.ListViewModel

@Preview
@Composable
fun MainScreen(listViewModel: ListViewModel = hiltViewModel()) {
    val name: String by listViewModel.name.observeAsState("")
    val nameList: userListProto by listViewModel.userDataStore.collectAsState(initial = userListProto.getDefaultInstance())
    val job: String by listViewModel.job.observeAsState("")
    val team: String by listViewModel.team.observeAsState("")

    MainContent(
        name = name,
        nameList = nameList,
        job = job,
        team = team,
        jobList = listViewModel.jobList,
        teamList = listViewModel.teamList,
        onNameChange = { listViewModel.onNameChanged(it) },
        onInputName = { listViewModel.onInputName() },
        onJobSelected = { listViewModel.onJobSelected(it) },
        onTeamSelected = { listViewModel.onTeamSelected(it) },
        onItemLongClick = { listViewModel.onItemLongClick(it) },
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MainContent(
    name: String,
    nameList: userListProto,
    job: String,
    team: String,
    jobList: ArrayList<String>,
    teamList: ArrayList<String>,
    onNameChange: (String) -> Unit,
    onInputName: () -> Unit,
    onJobSelected: (String) -> Unit,
    onTeamSelected: (String) -> Unit,
    onItemLongClick: (userInfoProto) -> Unit,
) {
    val keyboard = LocalSoftwareKeyboardController.current
    val isShowTeamDialog = remember { mutableStateOf(false) }

    Box(modifier = Modifier.background(Color.White)) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            TeamDialog(
                isShowTeamDialog = isShowTeamDialog,
                teamList = teamList,
                team = team,
                onTeamSelected = onTeamSelected,
            )

            Text(
                text = "Hello, $name",
                modifier = Modifier.padding(bottom = 8.dp),
                style = MaterialTheme.typography.headlineLarge
            )
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.height(60.dp)) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                ) {
                    OutlinedTextField(
                        value = name,
                        onValueChange = onNameChange,
                        placeholder = { Text(text = "name", color = grayBB) },
                        maxLines = 1,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                onInputName()
                                onNameChange("")
                                keyboard?.hide()
                            },
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Spacer(Modifier.size(10.dp))
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                ) {
                    JobDropDown(job = job, jobList = jobList, onJobSelected = onJobSelected)
                }
                Spacer(Modifier.size(10.dp))
                Column(
                    modifier = Modifier
                        .weight(0.7f)
                        .fillMaxHeight()
                ) {
                    Button(
                        modifier = Modifier.fillMaxSize(),
                        onClick = { isShowTeamDialog.value = true },
                        shape = RoundedCornerShape(4.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = buttonOK),
                    ) {
                        Text(text = "Team", fontSize = 14.sp)
                    }
                }
            }
            UserListView(userList = nameList, onItemLongClick)
        }
    }
}

@Composable
fun UserListView(userList: userListProto, onItemLongClick: (userInfoProto) -> Unit) {
    LazyColumn(
        modifier = Modifier
            .fillMaxHeight()
            .padding(top = 20.dp)
    ) {
        item {
            Column {
                Text(text = "NameList")
                Divider(
                    color = Color.Gray,
                    thickness = 1.dp,
                )
            }
        }
        items(userList.userInfoListCount) { index ->
            UserInfoItem(userList.getUserInfoList(index), onItemLongClick)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobDropDown(job: String, jobList: ArrayList<String>, onJobSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        modifier = Modifier
            .fillMaxSize()
            .spinnerBorder(
                strokeWidth = 1.dp,
                cornerRadiusDp = 4.dp,
                color = darkBrown,
                expanded = expanded,
            ),
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        },
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = job,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                fontSize = 15.sp,
            )
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "dropdown",
                modifier = Modifier.rotate(
                    if (expanded)
                        180f
                    else
                        360f
                ),
            )
            Spacer(modifier = Modifier.width(16.dp))
        }
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .background(Color.White)
                .dropDownBorder(strokeWidth = 1.dp, cornerRadiusDp = 4.dp, color = darkBrown)
        ) {
            jobList.forEach { clickedJob ->
                DropdownMenuItem(
                    text = {
                        Text(text = clickedJob)
                    },
                    onClick = {
                        expanded = false
                        onJobSelected(clickedJob)
                    },
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun UserInfoItem(userProto: userInfoProto, onItemLongClick: (userInfoProto) -> Unit) {
    val userInfo = UserInfo(
        userName = userProto.userName,
        isOnOff = userProto.isOnOff,
        userTeamName = userProto.userTeamName,
        userPosition = userProto.usePosition,
        userId = userProto.userId,
    )

    val context = LocalContext.current

    ConstraintLayout(
        modifier = Modifier
            .combinedClickable(
                onClick = {
                    context.startActivity(DetailActivity.newInstance(context, userInfo.userId))
                },
                onLongClick = {
                    onItemLongClick(userProto)
                }
            )
            .fillMaxWidth()
            .padding(top = 10.dp, bottom = 10.dp),
    ) {
        val (profile, name, onlineIcon, position, team, bar, divider) = createRefs()
        createVerticalChain(name, team, chainStyle = ChainStyle.Packed)

        Image(
            painter = painterResource(id = R.drawable.circle_icons_profile),
            contentDescription = "avatar",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .constrainAs(profile) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    bottom.linkTo(divider.bottom)
                    end.linkTo(onlineIcon.start)
                },
        )

        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(userInfo.onOffColor)
                .padding(bottom = 4.dp)
                .constrainAs(onlineIcon) {
                    start.linkTo(profile.end, margin = 8.dp)
                    top.linkTo(name.top)
                    bottom.linkTo(name.bottom)
                    end.linkTo(name.start)
                },
        )
        Text(
            text = userInfo.userName,
            color = black22,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(bottom = 4.dp)
                .constrainAs(name) {
                    start.linkTo(onlineIcon.end, margin = 8.dp)
                    top.linkTo(parent.top)
                    bottom.linkTo(team.top)
                },
        )

        Text(
            text = userInfo.userPosition,
            color = grayBB,
            fontSize = 12.sp,
            modifier = Modifier.constrainAs(position) {
                start.linkTo(profile.end, margin = 8.dp)
                top.linkTo(team.top)
                bottom.linkTo(team.bottom)
                end.linkTo(bar.start)
            },
        )
        Text(
            text = "|",
            color = grayBB,
            fontSize = 12.sp,
            modifier = Modifier.constrainAs(bar) {
                start.linkTo(position.end, margin = 8.dp)
                top.linkTo(team.top)
                bottom.linkTo(team.bottom)
                end.linkTo(team.start)
            },
        )
        Text(
            text = userInfo.userTeamName,
            color = grayBB,
            fontSize = 12.sp,
            modifier = Modifier.constrainAs(team) {
                start.linkTo(bar.end, margin = 8.dp)
                top.linkTo(name.bottom)
                bottom.linkTo(parent.bottom)
            },
        )

        Divider(
            color = grayEA, thickness = 1.dp,
            modifier = Modifier.constrainAs(divider) {
                start.linkTo(parent.start)
                top.linkTo(profile.bottom, margin = 16.dp)
                bottom.linkTo(parent.bottom)
                end.linkTo(parent.end)
            },
        )
    }
}

@Composable
fun TeamDialog(
    isShowTeamDialog: MutableState<Boolean>,
    teamList: ArrayList<String>,
    team: String,
    onTeamSelected: (String) -> Unit,
) {
    if (isShowTeamDialog.value) {
        customDialog(
            onDismiss = {
                isShowTeamDialog.value = false
            },
            onSelectTeam = {
                onTeamSelected(it)
                isShowTeamDialog.value = false
            },
            teamList = teamList,
            team = team,
        )
    }
}