package com.brandon.hidalgo.nycschools.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.ExpandLess
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.brandon.hidalgo.nycschools.model.SchoolModel
import com.brandon.hidalgo.nycschools.model.ScoresModel
import com.brandon.hidalgo.nycschools.ui.viewmodel.MainViewModel

/**
 * Main screen composable. This screen composes the entire application.
 *
 * @param viewModel The ViewModel holding the UI state.
 */
@Composable
fun MainScreen(viewModel: MainViewModel) {
    MainScreen(viewModel = viewModel, initiallyLoading = true, initiallySearching = false)
}

/**
 * Main screen composable. This screen composes the entire application.
 *
 * @param viewModel The ViewModel holding the UI state.
 * @param initiallyLoading Initialize the UI state as loading
 * @param initiallySearching Initialize the UI state as searching
 *
 * TODO Lookout for Material 3 API releases to remove the OptIn annotation in the future
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
private fun MainScreen(
    viewModel: MainViewModel, initiallyLoading: Boolean, initiallySearching: Boolean
) {
    // Searching state can be kept internal and does not need to be hoisted.
    var searching by rememberSaveable {
        mutableStateOf(initiallySearching)
    }
    // Observe the loading state.
    val loading by viewModel.loading.observeAsState(initiallyLoading)
    // Observe the school and scores data
    val schoolsAndScores: Map<SchoolModel, ScoresModel?> by viewModel.schoolsAndScores.observeAsState(
        emptyMap()
    )

    Scaffold(topBar = {
        TopAppBar(title = {
            // TODO: Do not use hardcoded Strings. Load them through a resource file for advantages such as multi-lingual support.
            Text("NYC Schools")
        }, actions = {
            if (searching) {
                // Show the Hide Search action
                // Explicitly providing the parameter name to demystify the lambda expression.
                HideSearchAction(onClick = {
                    searching = false
                })
            } else {
                // Show the Search action
                // Explicitly providing the parameter name to demystify the lambda expression.
                ShowSearchAction(onClick = {
                    searching = true
                })
            }
        })
    }) {
        Surface(
            modifier = Modifier
                .padding(it)
                .consumedWindowInsets(it)
                .fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column {
                if (searching) {
                    // Show the Search Bar
                    SearchBar(viewModel = viewModel)
                }

                if (loading) {
                    // Show a progress indicator while data is finished loading
                    LoadingIndicator(true)
                } else {
                    // Show the school list
                    SchoolList(schoolsAndScores = schoolsAndScores)
                }
            }
        }
    }
}

/**
 * A composable for the Search Action.
 *
 * @param onClick The action that will run when the icon is clicked
 */
@Composable
private fun ShowSearchAction(onClick: () -> Unit) {
    TopBarAction(icon = Icons.Outlined.Search, "Search", onClick = onClick)
}

/**
 * A composable for the Hide Search Action.
 *
 * @param onClick The action that will run when the icon is clicked
 */
@Composable
private fun HideSearchAction(onClick: () -> Unit) {
    TopBarAction(icon = Icons.Outlined.ExpandLess, "Close Search", onClick = onClick)
}

/**
 * A generic composable for a TopBar Action.
 *
 * @param icon The icon to display
 * @param contentDescription The icon's content description
 * @param onClick The action that will run then the icon is clicked
 */
@Composable
private fun TopBarAction(icon: ImageVector, contentDescription: String = "", onClick: () -> Unit) {
    Icon(
        icon,
        contentDescription,
        modifier = Modifier
            .padding(end = 8.dp)
            .clickable(onClick = onClick)
    )
}

/**
 * A composable for a Search Bar
 *
 * @param viewModel The ViewModel holding the UI state
 *
 * TODO Lookout for Material 3 API releases to remove the OptIn annotation in the future
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchBar(viewModel: MainViewModel) {
    TextField(
        value = viewModel.searchCriteria,
        onValueChange = viewModel.onSearchCriteriaChanged,
        leadingIcon = { Icon(Icons.Outlined.Search, "Search") },
        trailingIcon = {
            Icon(Icons.Outlined.Clear, "Clear Search", modifier = Modifier.clickable {
                // Clears the search criteria value
                viewModel.onSearchCriteriaChanged("")
            })
        },
        label = { Text("Search") },
        singleLine = true,
        modifier = Modifier.fillMaxWidth()
    )
}

/**
 * A composable which displays a list of schools
 *
 * @param schoolsAndScores The Map of schools associated with their scores to display.
 */
@Composable
private fun SchoolList(schoolsAndScores: Map<SchoolModel, ScoresModel?>) {
    // Using a LazyList implementation since there may be a large number of schools to display and we want to stay performant.
    LazyColumn {
        for ((school, schoolScores) in schoolsAndScores) {
            item {
                School(school, schoolScores)
                // TODO: No need to show the divider on the last list item.
                Divider()
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun MainScreenPreview() {
    MainScreen(viewModelForPreview(), initiallyLoading = false, initiallySearching = false)
}

@Composable
@Preview(showBackground = true)
fun MainScreenSearchingPreview() {
    MainScreen(viewModelForPreview(), initiallyLoading = false, initiallySearching = true)
}

@Composable
@Preview(showBackground = true)
fun MainScreenLoadingPreview() {
    MainScreen(viewModelForPreview(true), initiallyLoading = true, initiallySearching = false)
}

@Composable
@Preview(showBackground = true)
fun MainScreenLoadingWhileSearchingPreview() {
    MainScreen(viewModelForPreview(true), initiallyLoading = true, initiallySearching = true)
}

private fun viewModelForPreview(loading: Boolean = false): MainViewModel {
    val previewSchoolModels = mutableListOf(
        SchoolModel(dbn = "1", schoolName = "School 1", boro = "M"),
        SchoolModel(dbn = "2", schoolName = "School 2", boro = "X"),
        SchoolModel(dbn = "3", schoolName = "School 3", boro = "Q"),
        SchoolModel(dbn = "4", schoolName = "School 4", boro = "K"),
        SchoolModel(dbn = "5", schoolName = "School 5", boro = "R")
    )
    val previewScores = mutableListOf(
        ScoresModel("1", "234", "100", "200"),
        ScoresModel("2", "345", "200", "300"),
        ScoresModel("3", "456", "300", "400"),
        ScoresModel("4", "567", "400", "500"),
        ScoresModel("5", "678", "500", "500"),
    )

    return MainViewModel(previewSchoolModels, previewScores, loading)
}