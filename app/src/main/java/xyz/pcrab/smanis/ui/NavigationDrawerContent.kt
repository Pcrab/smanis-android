package xyz.pcrab.smanis.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MenuOpen
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import xyz.pcrab.smanis.R
import xyz.pcrab.smanis.utils.state.SmanisNavigationType

@Composable
@Preview
fun NavigationDrawerContent(
    modifier: Modifier = Modifier,
    navigationType: SmanisNavigationType = SmanisNavigationType.NAVIGATION_RAIL,
    selectedDestination: String = SmanisDestinations.MANAGE,
    closeDrawer: (() -> Unit) = {},
    selectDestination: ((String) -> Unit) = {}
) {
    Column(
        modifier = modifier
            .wrapContentWidth()
            .fillMaxHeight()
            .padding(24.dp)
            .width(200.dp)
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Menu", fontWeight = FontWeight.Black)
            AnimatedVisibility(visible = navigationType != SmanisNavigationType.PERMANENT_NAVIGATION_DRAWER) {
                IconButton(onClick = closeDrawer) {
                    Icon(
                        imageVector = Icons.Default.MenuOpen,
                        contentDescription = stringResource(id = R.string.navigation_drawer)
                    )
                }
            }
        }

        NavigationDrawerItem(
            selected = selectedDestination == SmanisDestinations.EXAM,
            label = { Text(text = stringResource(R.string.tab_exam)) },
            onClick = {
                selectDestination(SmanisDestinations.EXAM)
                closeDrawer()
            }
        )
        NavigationDrawerItem(
            selected = selectedDestination == SmanisDestinations.MANAGE,
            label = { Text(text = stringResource(R.string.tab_manage)) },
            onClick = {
                selectDestination(SmanisDestinations.MANAGE)
                closeDrawer()
            }
        )
        NavigationDrawerItem(
            selected = selectedDestination == SmanisDestinations.SETTINGS,
            label = { Text(text = stringResource(R.string.tab_settings)) },
            onClick = {
                selectDestination(SmanisDestinations.SETTINGS)
                closeDrawer()
            }
        )
    }
}