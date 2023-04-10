package xyz.pcrab.smaniszk.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.ManageAccounts
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import xyz.pcrab.smaniszk.R

@Composable
@Preview
fun SmanisBottomNavigationBar(
    selectedDestination: String = SmanisDestinations.MANAGE,
    selectDestination: (String) -> Unit = {}
) {
    NavigationBar(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        NavigationBarItem(
            selected = selectedDestination == SmanisDestinations.EXAM,
            onClick = {
                selectDestination(SmanisDestinations.EXAM)
            },
            label = { Text(text = stringResource(R.string.tab_exam)) },
            icon = {
                Icon(
                    imageVector = Icons.Default.Book,
                    contentDescription = stringResource(id = R.string.tab_exam)
                )
            }
        )
        NavigationBarItem(
            selected = selectedDestination == SmanisDestinations.MANAGE,
            onClick = {
                selectDestination(SmanisDestinations.MANAGE)
            },
            label = { Text(text = stringResource(R.string.tab_manage)) },
            icon = {
                Icon(
                    imageVector = Icons.Default.ManageAccounts,
                    contentDescription = stringResource(id = R.string.tab_manage)
                )
            }
        )
        NavigationBarItem(
            selected = selectedDestination == SmanisDestinations.SETTINGS,
            onClick = {
                selectDestination(SmanisDestinations.SETTINGS)
            },
            label = { Text(text = stringResource(R.string.tab_settings)) },
            icon = {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = stringResource(id = R.string.tab_settings)
                )
            }
        )
    }
}