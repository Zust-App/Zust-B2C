package `in`.opening.area.zustapp.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*

import androidx.compose.runtime.*

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun BottomSheet(){
//    val scope = rememberCoroutineScope()
//    val scaffoldState = rememberBottomSheetScaffoldState()
//    BottomSheetScaffold(
//        sheetContent = {
//            Box(
//                Modifier.fillMaxWidth().height(128.dp),
//                contentAlignment = Alignment.Center
//            ) {
//                Text("Swipe up to expand sheet")
//            }
//            Column(
//                Modifier.fillMaxWidth().padding(64.dp),
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                Text("Sheet content")
//                Spacer(Modifier.height(20.dp))
//                Button(
//                    onClick = {
//                        scope.launch { scaffoldState.bottomSheetState.hide() }
//                    }
//                ) {
//                    Text("Click to collapse sheet")
//                }
//            }
//        },
//        scaffoldState = scaffoldState,
//        topBar = {
//            TopAppBar(
//                title = { Text("Bottom sheet scaffold") },
//                navigationIcon = {
//                    IconButton(onClick = { scope.launch { scaffoldState.st.open() } }) {
//                        Icon(Icons.Default.Menu, contentDescription = "Localized description")
//                    }
//                }
//            )
//        },
//        floatingActionButton = {
//            var clickCount by remember { mutableStateOf(0) }
//            FloatingActionButton(
//                onClick = {
//                    // show snackbar as a suspend function
//                    scope.launch {
//                        scaffoldState.snackbarHostState.showSnackbar("Snackbar #${++clickCount}")
//                    }
//                }
//            ) {
//                Icon(Icons.Default.Favorite, contentDescription = "Localized description")
//            }
//        },
//        floatingActionButtonPosition = FabPosition.End,
//        sheetPeekHeight = 128.dp,
//        drawerContent = {
//            Column(
//                Modifier.fillMaxWidth().padding(16.dp),
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                Text("Drawer content")
//                Spacer(Modifier.height(20.dp))
//                Button(onClick = { scope.launch { scaffoldState.drawerState.close() } }) {
//                    Text("Click to close drawer")
//                }
//            }
//        }
//    ) { innerPadding ->
//        LazyColumn(contentPadding = innerPadding) {
//            items(100) {
//                Box(
//                    Modifier
//                        .fillMaxWidth()
//                        .height(50.dp)
//                )
//            }
//        }
//    }
//}
