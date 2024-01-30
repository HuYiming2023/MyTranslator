package com.example.mytranslation.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mytranslation.ui.theme.MyTranslationTheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.toSize
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.mytranslation.R
import com.example.mytranslation.viewmodel.TrnslViewModel
import androidx.compose.material3.Divider
import androidx.compose.ui.unit.sp
import com.example.mytranslation.viewmodel.TranslationResponseUiState

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTranslationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ScaffoldApp()
                }
            }
        }
    }
}

@Composable
fun BottomBar(selectedTab: String, navController: NavController) {
    val context = LocalContext.current
    BottomAppBar(
        modifier = Modifier
            .fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val homeColor = if (selectedTab == stringResource(R.string.HomePage)) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.tertiary
            val infoColor = if (selectedTab == stringResource(R.string.InfoPage)) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.tertiary

            IconButton(
                onClick = {
                    if (selectedTab == context.getString(R.string.InfoPage)) navController.navigate(context.getString(R.string.HomePage))
                },
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(homeColor)
            ) {
                Icon(
                    Icons.Filled.Home,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(70.dp),
                    tint = infoColor
                )
            }

            IconButton(
                onClick = {
                    if (selectedTab == context.getString(R.string.HomePage)) navController.navigate(context.getString(R.string.InfoPage))
                },
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(infoColor)
            ) {
                Icon(
                    Icons.Filled.Info,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(70.dp),
                    tint = homeColor
                )
            }
        }
    }
}




@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MainScreen(navController: NavController,trnslViewModel: TrnslViewModel = viewModel()) {
//    val context = LocalContext.current
//    var selectedTextSrc by remember { mutableStateOf(mapOf(context.getString(R.string.DefaultTrsnlKey) to context.getString(R.string.DefaultTrsnlName)).entries.first()) }
//    var selectedTextDes by remember { mutableStateOf(mapOf(context.getString(R.string.DefaultTrsnlKey) to context.getString(R.string.DefaultTrsnlName)).entries.first()) }
//    var selectedTextDes by remember { mutableStateOf("Option 1") }
    var expandedSrc by remember { mutableStateOf(false) }
    var expandedDes by remember { mutableStateOf(false) }
    var textFieldSize by remember { mutableStateOf(Size.Zero) }
    val iconSrc = if (expandedSrc)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown
    val iconDes = if (expandedDes)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    Scaffold (
        bottomBar = { BottomBar(stringResource(R.string.HomePage),navController) },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it),
            ) {
//                var text by remember { mutableStateOf("") }
                val keyboardController = LocalSoftwareKeyboardController.current
                Spacer(modifier = Modifier.weight(0.01f))
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                        .padding(8.dp),
                    onClick = {
                        trnslViewModel.result = trnslViewModel.result.copy(text = emptyList())
                        keyboardController?.hide()
                        trnslViewModel.trnslText()
                    },
                ) {
                    Text(text = stringResource(R.string.click_me))
                }
                OutlinedTextField(
                    value = trnslViewModel.text,
                    onValueChange = { newText -> trnslViewModel.text = newText },
                    label = { Text(stringResource(R.string.enter_text)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.5f)
                        .padding(
                            start = 16.dp,
                            top = 0.dp,
                            end = 16.dp,
                            bottom = 16.dp
                        ),
                )
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                        .wrapContentSize(Alignment.Center),
                    horizontalArrangement = Arrangement.Center
                ){
                    Spacer(modifier = Modifier
                        .height(8.dp)
                        .weight(0.1f))
                    Column(
                        modifier = Modifier
                            .weight(1f)
                    ) {
                        OutlinedTextField(
                            readOnly = true,
                            value = trnslViewModel.selectedTextSrc.value,
//                            textStyle = LocalTextStyle.current.copy(fontSize = 16.sp, color = Color.Black),
                            onValueChange = { },
                            modifier = Modifier
                                .fillMaxWidth()
                                .onGloballyPositioned { coordinates ->
                                    textFieldSize = coordinates.size.toSize()
                                },
                            label = { Text(stringResource(R.string.select_language)) },
                            trailingIcon = {
                                Icon(
                                    iconSrc,
                                    stringResource(R.string.open_select_language_menu),
                                    Modifier.clickable { expandedSrc = !expandedSrc }
                                )
                            }
                        )
                        DropdownMenu(
                            expanded = expandedSrc,
                            onDismissRequest = { expandedSrc = false },
                            modifier = Modifier
                                .width(with(LocalDensity.current) { textFieldSize.width.toDp() })
                                .padding(16.dp)
                        ) {
//                            trnslViewModel.getData.Success.translationResponse.langs.forEach { lang ->
//                                DropdownMenuItem(
//                                    text = { Text(lang.value) }, // Assuming option has a 'name' property
//                                    onClick = {
//                                        selectedTextSrc = lang // Assuming option has a 'code' property
//                                        expandedSrc = false
//                                        keyboardController?.hide()
//                                        trnslViewModel.trnslText(text,selectedTextSrc.key,selectedTextDes.key)
//                                    }
//                                )
//                            }
//                            trnslViewModel.getData = TranslationResponseUiState.Loading
                            trnslViewModel.getLanguageList()
                            when (val data = trnslViewModel.getData) {
                                is TranslationResponseUiState.Success -> {
                                    data.translationResponse.langs.forEach { lang ->
                                        DropdownMenuItem(
                                            text = { Text(lang.value) },
                                            onClick = {
                                                trnslViewModel.selectedTextSrc = lang
                                                expandedSrc = false
                                                keyboardController?.hide()
                                                trnslViewModel.trnslText()
                                            }
                                        )
                                        Divider(color = Color.LightGray, thickness = 1.dp)
                                    }
                                }
                                is TranslationResponseUiState.Error -> {
                                    DropdownMenuItem(
                                        text = { Text(stringResource(R.string.error)) },
                                        onClick = {           }
                                    )
                                }
                                is TranslationResponseUiState.Loading -> {
                                    DropdownMenuItem(
                                        text = { Text(stringResource(R.string.loading)) },
                                        onClick = {           }
                                    )
                                }
                            }
                        }
                    }
                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = stringResource(R.string.arrows),
                            modifier = Modifier.size(48.dp) // Adjust the size as needed
                        )
                    }
                    Column(
                        modifier = Modifier
                            .weight(1f)
                    ) {
                        OutlinedTextField(
                            readOnly = true,
                            value = trnslViewModel.selectedTextDes.value,
                            onValueChange = {  },
                            modifier = Modifier
                                .fillMaxWidth()
                                .onGloballyPositioned { coordinates ->
                                    textFieldSize = coordinates.size.toSize()
                                },
                            label = { Text(stringResource(R.string.select_language)) },
                            trailingIcon = {
                                Icon(
                                    iconDes,
                                    stringResource(R.string.open_select_language_menu),
                                    Modifier.clickable { expandedDes = !expandedDes })
                            }
                        )
                        DropdownMenu(
                            expanded = expandedDes,
                            onDismissRequest = { expandedDes = false },
                            modifier = Modifier
                                .width(with(LocalDensity.current) { textFieldSize.width.toDp() })
                                .padding(16.dp)
                        ) {
//
//                            trnslViewModel.getData.langs.forEach { lang ->
//                                DropdownMenuItem(
//                                    text = { Text(lang.value) },
//                                    onClick = {
//                                        selectedTextDes = lang
//                                        expandedDes = false
//                                        keyboardController?.hide()
//                                        trnslViewModel.trnslText(text,selectedTextSrc.key,selectedTextDes.key)
//                                    }
//                                )
//                            }
//                            trnslViewModel.getData = TranslationResponseUiState.Loading
                            trnslViewModel.getLanguageList()
                            when (val data = trnslViewModel.getData) {
                                is TranslationResponseUiState.Success -> {
                                    data.translationResponse.langs.forEach { lang ->
                                        DropdownMenuItem(
                                            text = { Text(lang.value) },
                                            onClick = {
                                                trnslViewModel.selectedTextDes = lang
                                                expandedDes = false
                                                keyboardController?.hide()
                                                trnslViewModel.trnslText()
                                            }
                                        )
                                        Divider(color = Color.LightGray, thickness = 1.dp)
                                    }
                                }
                                is TranslationResponseUiState.Error -> {
                                    DropdownMenuItem(
                                        text = { Text(stringResource(R.string.error)) },
                                        onClick = {           }
                                    )
                                }
                                is TranslationResponseUiState.Loading -> {
                                    DropdownMenuItem(
                                        text = { Text(stringResource(R.string.loading)) },
                                        onClick = {           }
                                    )
                                }
                            }

                        }
                    }
                    Spacer(modifier = Modifier
                        .height(8.dp)
                        .weight(0.1f))
                }
                OutlinedTextField(
                    readOnly = true,
                    value = trnslViewModel.result.text.joinToString(),
                    label = { Text(stringResource(R.string.waiting_to_be_translated)) },
                    onValueChange = { },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.5f)
                        .padding(16.dp)
                )
                Spacer(modifier = Modifier.weight(0.01f))
            }
        },
    )
}

@Composable
fun InfoScreen(navController: NavController) {
    Scaffold (
        bottomBar = { BottomBar(stringResource(R.string.InfoPage),navController) },
        content = {
            Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
            ) {

                AsyncImage(
                    model = (R.drawable.owl),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState())
                )
                {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        text = stringResource(R.string._1_software_logic) +
                                stringResource(R.string._2_viewmodel) +
                                stringResource(R.string._3_mobile_ui) +
                                stringResource(R.string._4_theme_modification) +
                                stringResource(R.string._5_strings_in_resource_file) +
                                stringResource(R.string._6_navigation_and_screens) +
                                stringResource(R.string._7_api_service_usage) +
                                stringResource(R.string._8_mvvm_architecture) +
                                stringResource(R.string._9_code_organization) +
                                stringResource(R.string._10_error_handling_and_loading_indication) +
                                stringResource(R.string._11_displaying_an_image)
                    )
                    Divider(color = Color.LightGray, thickness = 2.dp)
                    Text(
                        text = stringResource(R.string.author),
                        fontSize = 24.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                    )
                    Text(
                        text = stringResource(R.string.contact),
                        fontSize = 20.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                    )
                }
            }
        },
    )
}

@Composable
fun ScaffoldApp() {
    val navController = rememberNavController()
    val context = LocalContext.current
    NavHost(navController = navController, startDestination = stringResource(R.string.HomePage)
    )
    {
        composable(route = context.getString(R.string.HomePage)){
            MainScreen(navController)
        }
        composable(route = context.getString(R.string.InfoPage)){
            InfoScreen(navController)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyTranslationTheme {
        ScaffoldApp()
    }
}