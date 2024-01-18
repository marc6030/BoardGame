package com.example.myapplication.views


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.myapplication.R
import com.example.myapplication.modelviews.BoardGameInfoActivity
import com.example.myapplication.modelviews.BoardSearchViewModel
import kotlinx.coroutines.delay

@Composable
fun goBack(navController: NavHostController) {

    IconButton(
        onClick = {
            val currentRoute = navController.currentBackStackEntry?.destination?.route
            if (currentRoute != "home") {
                navController.popBackStack()}
        },
        modifier = Modifier
            .padding(4.dp)
            .width(40.dp)
            .size(30.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_action_back),
            contentDescription = "Go Back",
            modifier = Modifier.fillMaxHeight()
        )
    }
}


@Composable
fun PopUpOptions(boardSearchViewModel: BoardSearchViewModel, showPopup: MutableState<Boolean>) {
    val categories = boardSearchViewModel.categories
    val maxWidth = 400.dp
    val maxHeight = 650.dp
    var currentWidth = 0.dp
    var currentRow = mutableListOf<String>()
    val rows = mutableListOf<List<String>>()

    fun calculateWidth(category: String): Dp {
        val baseWidth = 75.dp
        val padding = 10.dp

        return baseWidth + (category.length * 4).dp + padding
    }

    categories.keys.forEach { category ->
        val categoryWidth = calculateWidth(category) + 10.dp
        if (currentWidth + categoryWidth > maxWidth) {

            rows.add(currentRow)
            currentRow = mutableListOf(category)
            currentWidth = categoryWidth
        } else {
            currentRow.add(category)
            currentWidth += categoryWidth
        }
    }

    Popup(
        alignment = Alignment.Center,
        properties = PopupProperties()
    ) {
        Box(
            Modifier
                .size(maxWidth, maxHeight)
                .padding(top = 5.dp)
                .background(Color.Transparent)

        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                items(items = rows) { rowCategories ->
                    LazyRow {
                        items(rowCategories) { category ->
                            Box(
                                Modifier
                                    .wrapContentSize()
                                    .padding(top = 5.dp)
                                    .background(color = if (!boardSearchViewModel.categories[category]!!) Color.Gray else Color.DarkGray, RoundedCornerShape(25.dp))
                                    .shadow(8.dp, RoundedCornerShape(20.dp))
                                    .clickable {
                                        boardSearchViewModel.categories[category] = !boardSearchViewModel.categories[category]!!
                                    }
                            ) {
                                Text(
                                    text = "$category ",
                                    color = MaterialTheme.colorScheme.onBackground,
                                    modifier = Modifier
                                        .padding(start = 10.dp, end = 10.dp, top = 6.5.dp, bottom = 6.5.dp),
                                    style = TextStyle(
                                        shadow = Shadow(color = Color.Black, offset = Offset(1f, 1f), blurRadius = 6f)
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}



@Composable
fun Options(showPopup: MutableState<Boolean>, boardSearchViewModel: BoardSearchViewModel) {
    val categories = boardSearchViewModel.categories
    var selectedCategories = 0
    categories.forEach {
        if (it.value) {
            selectedCategories += 1
        }
    }

    if (!showPopup.value) {
        if (selectedCategories == 0) {
            IconButton(
                onClick = { showPopup.value = !showPopup.value },
                modifier = Modifier.padding(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "Settings",
                    modifier = Modifier
                        .fillMaxHeight()
                        .size(30.dp)
                        .width(40.dp),
                    tint = Color.White
                )
            }
        } else {
            IconButton(
                onClick = { showPopup.value = !showPopup.value },
                modifier = Modifier.padding(4.dp)
            ) {
                Text(
                    text = selectedCategories.toString(),
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .padding(start = 1.dp, end = 1.dp),
                    fontSize = 22.sp,
                )
            }

        }

    } else {
        IconButton(
            onClick = { showPopup.value = !showPopup.value },
            modifier = Modifier
                .padding(4.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close",
                modifier = Modifier
                    .fillMaxHeight()
                    .size(30.dp)
                    .width(40.dp),
                tint = Color.White
            )
        }
    }
}


@OptIn(ExperimentalLayoutApi::class, ExperimentalComposeUiApi::class)
@Composable
fun searchActivity(navController: NavHostController, boardSearchViewModel: BoardSearchViewModel, boardGameInfoActivity: BoardGameInfoActivity) {
    val input = boardSearchViewModel.input
    val scrollState = rememberLazyListState()
    val keyboardVisible = WindowInsets.isImeVisible
    val searchBarHeight = 46.dp
    val showPopup = remember { mutableStateOf(false) }
    val expandedStates = remember { mutableStateMapOf<Int, Boolean>() }
    val controller = LocalSoftwareKeyboardController.current

    val shouldLoadMore = remember {
        derivedStateOf {
            val layoutInfo = scrollState.layoutInfo
            val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()
            lastVisibleItem != null && lastVisibleItem.index >= layoutInfo.totalItemsCount - 5
        }
    }

    LaunchedEffect(boardSearchViewModel.input){
        delay(300)
        boardSearchViewModel.fetchGameBoardSearch()
        scrollState.scrollToItem(0)
    }
    LaunchedEffect(shouldLoadMore.value) {
        if (shouldLoadMore.value) {
            if (boardSearchViewModel.boardGameSearch.size > 8) {
                boardSearchViewModel.fetchAdditionalSearchResults()
            }

        }
    }
    LaunchedEffect(Unit) {
        do {
            boardSearchViewModel.getAllCategories()
            delay(500)
        }while(boardSearchViewModel.categories.isEmpty())
    }

    if (showPopup.value){
        PopUpOptions(boardSearchViewModel, showPopup)
        boardSearchViewModel.input = ""
        controller?.hide()
    }
    val gradientFrom = MaterialTheme.colorScheme.surface
    val gradientTo = MaterialTheme.colorScheme.background

    Scaffold {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .drawBehind {
            drawRect(
                    Brush.verticalGradient(
                            colorStops = arrayOf(0f to gradientFrom, 1f to gradientTo),
                            tileMode = TileMode.Decal
                    )
            )
        }
            ) {
            Spacer(modifier = Modifier.height(25.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth(0.99f)
                    .shadow(8.dp, RoundedCornerShape(25.dp))
                    .background(Color.DarkGray)
                    .height(searchBarHeight)
                    .align(Alignment.CenterHorizontally),


            ) {

                goBack(navController)
                BasicTextField(
                    value = input,
                    onValueChange = { boardSearchViewModel.input = it },
                    modifier = Modifier
                        .weight(1f),
                    textStyle = TextStyle(
                        fontSize = 22.sp,
                        fontWeight = FontWeight(600),
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onBackground
                    ),
                    singleLine = true,
                )

                Options(showPopup, boardSearchViewModel)

            }




            // List of search results
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth(0.99f)
                    .align(Alignment.CenterHorizontally)
                    .padding(),
                    state = scrollState
            ) {
                items(boardSearchViewModel.boardGameSearch) { result ->

                    if (!expandedStates.containsKey(result.id.toInt())) {
                        expandedStates[result.id.toInt()] = false
                    }

                    Row(
                        modifier = Modifier
                            .height(100.dp)
                            .fillMaxWidth() // Ensures the Row takes full width
                            .padding(horizontal = 10.dp, vertical = 0.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .clickable {
                                navController.navigate("boardgameinfo/${result.id}")
                            },
                        verticalAlignment = Alignment.CenterVertically, // Aligns children vertically center
                        horizontalArrangement = Arrangement.SpaceBetween // Distributes space between children
                    ) {
                        Box(
                            modifier = Modifier.padding(4.dp)
                        ){
                            // Image on the left
                            AsyncImage(
                                model = result.imgUrl,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(100.dp)
                                    .shadow(8.dp, RoundedCornerShape(20.dp)),
                                contentScale = ContentScale.FillBounds
                            )
                        }

                        // Text in the middle
                        Text(
                            text = result.name,
                            color = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 10.dp, end = 10.dp)
                        )

                        // Icon/Button on the right
                        Icon(
                            imageVector = if (expandedStates[result.id.toInt()]!!) {Icons.Default.KeyboardArrowUp} else {Icons.Default.KeyboardArrowDown}, // Using the default arrow drop down icon
                            contentDescription = "Dropdown Menu",
                            modifier = Modifier
                                .size(40.dp) // Adjust the size as needed
                                .clip(RoundedCornerShape(20.dp))
                                .clickable {
                                    expandedStates[result.id.toInt()] =
                                        !expandedStates[result.id.toInt()]!!
                                },
                            tint = Color.White
                        )
                    }

                    if (expandedStates[result.id.toInt()] == true) {
                        val description = result.description
                        val periodIndex = description.indexOf(".\n").takeIf { it >= 0 }
                        val exclamationIndex = description.indexOf("!\n").takeIf { it >= 0 }

                        val endIndex = listOfNotNull(periodIndex, exclamationIndex).minOrNull()

                        val first50Words = description.split(" ").take(50).joinToString(" ")

                        val shortenedDescription = if (endIndex != null && endIndex < first50Words.length) {
                            description.substring(0, endIndex + 1) // +1 to include the period or exclamation mark
                        } else {
                            first50Words
                        }

                        val text = "Description: $shortenedDescription..."
                        Text(
                            text = text,
                            color = Color.White,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
        }
    }
}