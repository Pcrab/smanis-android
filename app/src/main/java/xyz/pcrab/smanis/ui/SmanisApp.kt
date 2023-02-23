package xyz.pcrab.smanis.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch
import xyz.pcrab.smanis.ui.data.SmanisViewModel
import xyz.pcrab.smanis.utils.state.DevicePosture
import xyz.pcrab.smanis.utils.state.SmanisContentType
import xyz.pcrab.smanis.utils.state.SmanisNavigationType

@Composable
fun SmanisApp(
    windowSize: WindowWidthSizeClass,
    foldingDevicePosture: DevicePosture,
    viewModel: SmanisViewModel = SmanisViewModel(),
) {

    val navigationType: SmanisNavigationType
    val contentType: SmanisContentType

    when (windowSize) {
        WindowWidthSizeClass.Compact -> {
            navigationType = SmanisNavigationType.BOTTOM_NAVIGATION
            contentType = SmanisContentType.COMPACT
        }
        WindowWidthSizeClass.Medium -> {
            navigationType = SmanisNavigationType.NAVIGATION_RAIL
            contentType = if (foldingDevicePosture == DevicePosture.NormalPosture) {
                SmanisContentType.COMPACT
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
            contentType = SmanisContentType.COMPACT
        }
    }

    SmanisNavigationWrapperUI(
        navigationType = navigationType,
        contentType = contentType,
        viewModel = viewModel,
    )

}

@Composable
private fun SmanisNavigationWrapperUI(
    navigationType: SmanisNavigationType,
    contentType: SmanisContentType,
    viewModel: SmanisViewModel = SmanisViewModel(),
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val uiState = viewModel.uiState.collectAsState().value

    when (navigationType) {
        SmanisNavigationType.PERMANENT_NAVIGATION_DRAWER -> {
            PermanentNavigationDrawer(modifier = Modifier.background(MaterialTheme.colorScheme.inverseOnSurface),
                drawerContent = {
                    PermanentDrawerSheet {
                        NavigationDrawerContent(selectedDestination = uiState.currentDestination,
                            navigationType = navigationType,
                            selectDestination = {
                                viewModel.updateCurrentDestination(it)
                            })
                    }
                }) {
                SmanisAppContent(contentType = contentType, viewModel = viewModel)
            }
        }
        SmanisNavigationType.NAVIGATION_RAIL -> {
            ModalNavigationDrawer(
                drawerContent = {
                    ModalDrawerSheet {
                        NavigationDrawerContent(selectedDestination = uiState.currentDestination,
                            navigationType = navigationType,
                            closeDrawer = {
                                scope.launch {
                                    drawerState.close()
                                }
                            },
                            selectDestination = {
                                viewModel.updateCurrentDestination(it)
                            })
                    }
                }, drawerState = drawerState
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.inverseOnSurface)
                ) {
                    SmanisNavigationRail(selectedDestination = uiState.currentDestination,
                        openDrawer = {
                            scope.launch {
                                drawerState.open()
                            }
                        },
                        selectDestination = {
                            viewModel.updateCurrentDestination(it)
                        })
                    SmanisAppContent(contentType = contentType, viewModel = viewModel)
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
                SmanisAppContent(
                    modifier = Modifier.weight(1f),
                    contentType = contentType,
                    viewModel = viewModel
                )
                SmanisBottomNavigationBar(uiState.currentDestination, selectDestination = {
                    viewModel.updateCurrentDestination(it)
                })
            }
        }
    }
}


