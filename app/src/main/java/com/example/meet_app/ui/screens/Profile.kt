package com.example.meet_app.ui.screens


//import coil.compose.rememberAsyncImagePainter
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.meet_app.R
import com.example.meet_app.auth.AuthResult
import com.example.meet_app.navigation.Screen
import com.example.meet_app.ui.auth.AuthUIEvent
import com.example.meet_app.ui.theme.fonts
import com.example.meet_app.ui.theme.getFonts
import com.example.meet_app.viewmodel.AuthViewModel
import com.example.meet_app.viewmodel.UserViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private val optionsList: ArrayList<OptionsData> = ArrayList()

@Composable
fun Profile(
    navController: NavController,
    name: String?,
    viewModel: AuthViewModel = hiltViewModel(),
    userViewModel: UserViewModel = hiltViewModel()
) {
    val fonts = getFonts()
    val context = LocalContext.current
    val currentUser by userViewModel.currentUser.observeAsState()
    val icons = Icons.Outlined
    var selectedImageUri by remember {
        mutableStateOf<Uri?>(null)
    }
    val profilePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),

        ) { uri ->
        uri?.let { selectedUri ->
            val newProfileImagePath = selectedUri.toString()
            currentUser?.let { userViewModel.updateProfileImage(it.id, newProfileImagePath) }
        }
    }

    LaunchedEffect(viewModel, userViewModel, context) {
        userViewModel.loadCurrentUser()
        viewModel.authResults.collect { result ->
            when (result) {
                is AuthResult.Unauthorized -> {
                    navController.navigate(Screen.Login.withArgs("login")) {
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                    }
                }
                is AuthResult.UnknownError -> {
                    Toast.makeText(
                        context,
                        "${result.message}",
                        Toast.LENGTH_LONG
                    ).show()
                    println(result.message)
                }

            }
        }

    }
    Column(//
        modifier = Modifier
            .fillMaxSize()

    ) {

        TopAppBar(
            title = {
                Text(
                    text = "Profile",
                    style = TextStyle(
                        fontFamily = fonts,
                        fontWeight = FontWeight.SemiBold
                    )
                )
            },
            backgroundColor = MaterialTheme.colors.background,
            elevation = 4.dp,
            navigationIcon = {
                IconButton(
                    onClick = { navController.navigateUp() }

                ) {
                    Icon(
                        Icons.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }


            }
        )
        Spacer(modifier = Modifier.height(5.dp))
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier
                        .size(65.dp)
                        .clip(shape = CircleShape)
                        .clickable {
                            profilePhotoPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        },
                    painter = (if (currentUser?.profileImage != null) {
                        rememberAsyncImagePainter(currentUser?.profileImage)
                    } else {
                        painterResource(R.drawable.profile_image_placeholder)
                    }),

                    contentDescription = "Image"
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier
                            .weight(weight = 3f, fill = false)
                            .padding(start = 16.dp)
                    ) {
                        Text(
                            text = "${currentUser?.firstName} ${currentUser?.lastName}",
                            style = TextStyle(
                                fontSize = 22.sp,
                                fontFamily = fonts,
                                fontWeight = FontWeight.SemiBold
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
//                        Spacer(modifier = Modifier.height(1.dp))

                        Text(
                            text = "@${currentUser?.username}",
                            style = TextStyle(
                                fontSize = 14.sp,
                                color = Color.Gray,
                                letterSpacing = (0.8).sp,
                                fontFamily = fonts,
                                fontWeight = FontWeight.Normal
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            imageVector = Icons.Outlined.Edit,
                            contentDescription = "Edit Details",
                            tint = MaterialTheme.colors.primary
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
            Column {
                ShowOptionsList()

            }

        }

    }


}

@Composable
fun ShowOptionsList(
    context: Context = LocalContext.current.applicationContext,
) {
    var listPrepared by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.Default) {
            optionsList.clear()

            profileOptions()
            listPrepared = true
        }
    }
    if (listPrepared) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {
            items(optionsList) { item ->
                OptionsItemStyle(item = item, context = context)
            }
        }
    }
}

@Composable
private fun OptionsItemStyle(
    item: OptionsData,
    context: Context,
    viewModel: AuthViewModel = hiltViewModel()
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = true) {
                if (item.title == "Logout")
                    viewModel.onEvent(AuthUIEvent.SignOut)
                else
                    Toast
                        .makeText(context, "something else was clicked", Toast.LENGTH_SHORT)
                        .show()
            }
            .padding(all = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // icon
        Icon(
            modifier = Modifier
                .size(32.dp),
            imageVector = item.icon,
            contentDescription = item.title,
            tint = MaterialTheme.colors.primary
        )

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(weight = 3f, fill = false)
                    .padding(start = 16.dp)
            ) {
                // title
                Text(
                    text = item.title,
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontFamily = fonts,
                        fontWeight = FontWeight.Medium
                    )
                )
                Spacer(modifier = Modifier.height(2.dp))
                // description
                Text(
                    text = item.description,
                    style = TextStyle(
                        fontSize = 14.sp,
                        letterSpacing = (0.8).sp,
                        fontFamily = fonts,
                        fontWeight = FontWeight.Normal,
                        color = Color.Gray
                    )
                )
                // right arrow icon
                Icon(
                    modifier = Modifier
                        .weight(weight = 1f, fill = false),
                    imageVector = Icons.Outlined.ChevronRight,
                    contentDescription = item.title,
                    tint = Color.Black.copy(alpha = 0.70f)
                )
            }
        }
    }

}


//@Composable
private fun profileOptions() {
    val icons = Icons.Outlined
//    val viewModel: AuthViewModel = hiltViewModel()
    optionsList.add(
        OptionsData(
            icon = icons.Person,
            title = "Account",
            description = "Manage your account"

        )
    )

    optionsList.add(
        OptionsData(
            icon = icons.Search,
            title = "Connections",
            description = "Font your connections here"

        )
    )

    optionsList.add(
        OptionsData(
            icon = icons.Settings,
            title = "Settings",
            description = "App Settings"
        )
    )

    optionsList.add(
        OptionsData(
            icon = icons.Logout,
            title = "Logout",
            description = "logout from this account",
            navTo = "{viewModel.onEvent(AuthUIEvent.SignOut)}"


        )
    )
}

data class OptionsData(
    val icon: ImageVector,
    val title: String,
    val description: String,
    val navTo: String? = null
)


fun getImagePathFromUri(context: Context, uri: Uri): String? {
    val cursor = context.contentResolver.query(uri, null, null, null, null, null)
    cursor?.let {
        it.moveToFirst()
        val columnIndex = it.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
        if (columnIndex != -1) {
            val imagePath = it.getString(columnIndex)
            it.close()
        }
    }
    cursor?.close()
    return null
}