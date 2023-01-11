package com.brandon.hidalgo.nycschools.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

/**
 * A composable loading indicator.
 *
 * @param fullScreen Whether or not to display the loading indicator using as much screen real estate available. Otherwise, the loading indicator will take up its full width.
 * @param modifier Additional modifiers to apply to the loading indicator.
 *
 * TODO: Current usages all require a Box to layout the progress indicator. We may need to pass in the Box modifiers as a parameter?
 * TODO: Consider extracting content alignment as a parameter to further promote reusability.
 */
@Composable
fun LoadingIndicator(fullScreen: Boolean, modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center, modifier = if (fullScreen) {
            Modifier.fillMaxSize()
        } else {
            Modifier.fillMaxWidth()
        }
    ) {
        CircularProgressIndicator(modifier = modifier)
    }
}