package xyz.pcrab.smanis.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch
import xyz.pcrab.smanis.utils.state.DevicePosture
import xyz.pcrab.smanis.utils.state.SmanisContentType
import xyz.pcrab.smanis.utils.state.SmanisNavigationType

@Composable
fun SmanisApp(windowSize: WindowWidthSizeClass, foldingDevicePosture: DevicePosture) {

    val navigationType: SmanisNavigationType
    val contentType: SmanisContentType

    when (windowSize) {
        WindowWidthSizeClass.Compact -> {
            navigationType = SmanisNavigationType.BOTTOM_NAVIGATION
            contentType = SmanisContentType.COMPAT
        }
        WindowWidthSizeClass.Medium -> {
            navigationType = SmanisNavigationType.NAVIGATION_RAIL
            contentType = if (foldingDevicePosture == DevicePosture.NormalPosture) {
                SmanisContentType.COMPAT
            } else {
                SmanisContentType.EXTENDED
            }
        }
        WindowWidthSizeClass.Expanded -> {
            navigationType = if (foldingDevicePosture is DevicePosture.BookPosture) {
                SmanisNavigationType.NAVIGATION_RAIL
            } else {
                SmanisNavigationType.PERMANENT_NAVIGATION_DRAWER
            }
            contentType = SmanisContentType.EXTENDED
        }
        else -> {
            navigationType = SmanisNavigationType.BOTTOM_NAVIGATION
            contentType = SmanisContentType.COMPAT
        }
    }

    SmanisNavigationWrapperUI(navigationType = navigationType, contentType = contentType)

}

@Composable
private fun SmanisNavigationWrapperUI(
    navigationType: SmanisNavigationType,
    contentType: SmanisContentType
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var selectedDestination by remember {
        mutableStateOf(SmanisDestinations.MANAGE)
    }

    when (navigationType) {
        SmanisNavigationType.PERMANENT_NAVIGATION_DRAWER -> {
            PermanentNavigationDrawer(
                modifier = Modifier.background(MaterialTheme.colorScheme.inverseOnSurface),
                drawerContent = {
                    PermanentDrawerSheet {
                        NavigationDrawerContent(
                            selectedDestination = selectedDestination,
                            navigationType = navigationType,
                            selectDestination = {
                                selectedDestination = it
                            }
                        )
                    }
                }) {
                SmanisAppContent(contentType, selectedDestination)
            }
        }
        SmanisNavigationType.NAVIGATION_RAIL -> {
            ModalNavigationDrawer(
                drawerContent = {
                    ModalDrawerSheet {
                        NavigationDrawerContent(
                            selectedDestination = selectedDestination,
                            navigationType = navigationType,
                            closeDrawer = {
                                scope.launch {
                                    drawerState.close()
                                }
                            },
                            selectDestination = {
                                selectedDestination = it
                            }
                        )
                    }
                }, drawerState = drawerState
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.inverseOnSurface)
                ) {
                    SmanisNavigationRail(
                        selectedDestination = selectedDestination,
                        openDrawer = {
                            scope.launch {
                                drawerState.open()
                            }
                        },
                        selectDestination = {
                            selectedDestination = it
                        }
                    )
                    SmanisAppContent(
                        contentType, selectedDestination
                    )
                }
            }
        }
        else -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.inverseOnSurface),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                SmanisAppContent(contentType, selectedDestination)
                SmanisBottomNavigationBar(selectedDestination, selectDestination = {
                    selectedDestination = it
                })
            }
        }
    }
}


