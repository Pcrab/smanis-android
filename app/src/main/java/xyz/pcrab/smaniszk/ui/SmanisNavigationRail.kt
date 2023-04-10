package xyz.pcrab.smaniszk.ui

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.ManageAccounts
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import xyz.pcrab.smaniszk.R

@Composable
@Preview
fun SmanisNavigationRail(
    selectedDestination: String = SmanisDestinations.MANAGE,
    openDrawer: () -> Unit = {},
    selectDestination: ((String) -> Unit) = {}
) {
    NavigationRail(modifier = Modifier.fillMaxHeight()) {
        NavigationRailItem(
            selected = false,
            onClick = openDrawer,
            icon = {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = stringResource(id = R.string.navigation_drawer)
                )
            }
        )
        NavigationRailItem(
            selected = selectedDestination == SmanisDestinations.EXAM,
            onClick = { selectDestination(SmanisDestinations.EXAM) },
            icon = {
                Icon(
                    imageVector = Icons.Default.Book,
                    contentDescription = stringResource(id = R.string.tab_exam)
                )
            }
        )
        NavigationRailItem(
            selected = selectedDestination == SmanisDestinations.MANAGE,
            onClick = { selectDestination(SmanisDestinations.MANAGE) },
            icon = {
                Icon(
                    imageVector = Icons.Default.ManageAccounts,
                    contentDescription = stringResource(id = R.string.tab_manage)
                )
            }
        )
        NavigationRailItem(
            selected = selectedDestination == SmanisDestinations.SETTINGS,
            onClick = { selectDestination(SmanisDestinations.SETTINGS) },
            icon = {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = stringResource(id = R.string.tab_settings)
                )
            }
        )
    }
}

