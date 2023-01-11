package com.brandon.hidalgo.nycschools.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import com.brandon.hidalgo.nycschools.extensions.fullBoroughName
import com.brandon.hidalgo.nycschools.model.SchoolModel
import com.brandon.hidalgo.nycschools.model.ScoresModel

/**
 * A composable for displaying information about a school
 *
 * @param school The school's data
 * @param scores The school's scores data or null if not available
 */
@Composable
fun School(school: SchoolModel, scores: ScoresModel?) {
    School(school, scores, initiallyExpanded = false, composeScores = true)
}

/**
 * A composable for displaying information about a school
 *
 * @param school The school's data
 * @param scores The school's scores data or null if not available
 * @param loading Whether or not the scores data is still loading
 * @param initiallyExpanded Whether or not to display the scores immediately
 * @param composeScores (Internal/private use only) Whether or not to compose scores at all
 */
@Composable
private fun School(
    school: SchoolModel,
    scores: ScoresModel? = null,
    loading: Boolean = false,
    initiallyExpanded: Boolean = false,
    composeScores: Boolean = true
) {
    // The expanded state can be kept local and does not need to be hoisted.
    var expanded by rememberSaveable {
        mutableStateOf(initiallyExpanded)
    }

    Column {
        ConstraintLayout(modifier = Modifier
            .fillMaxWidth()
            .clickable {
                // Clicking with expand or contract the School composable
                expanded = !expanded
            }
            .padding(8.dp)) {
            val (nameText, boroText, expandIcon) = createRefs()

            SchoolTitle(school.schoolName, modifier = Modifier
                .constrainAs(nameText) {
                    top.linkTo(parent.top)
                    bottom.linkTo(boroText.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(expandIcon.start)
                })

            BoroSubtitle(school.boro.fullBoroughName(), modifier = Modifier.constrainAs(boroText) {
                top.linkTo(nameText.bottom)
                bottom.linkTo(parent.bottom)
                start.linkTo(nameText.start)
                end.linkTo(nameText.end)
                centerHorizontallyTo(nameText, 0f)
            })

            ExpandIcon(expanded, modifier = Modifier.constrainAs(expandIcon) {
                top.linkTo(nameText.top)
                bottom.linkTo(boroText.bottom)
                start.linkTo(nameText.end)
                end.linkTo(parent.end)
            })

            createHorizontalChain(nameText, expandIcon, chainStyle = ChainStyle.SpreadInside)
            createVerticalChain(nameText, boroText, chainStyle = ChainStyle.Packed)
        }

        // TODO: animate the expand/contract of scores.
        if (composeScores && expanded) {
            Divider(Modifier.padding(top = 8.dp, bottom = 8.dp, start = 32.dp, end = 32.dp))

            if (loading) {
                // Show a loading indicator while scores load.
                LoadingIndicator(fullScreen = false, modifier = Modifier.padding(top = 2.dp))
            } else {
                // Display the scores once they are loaded.
                Scores(scores)
            }
        }
    }
}

@Composable
private fun SchoolTitle(schoolName: String, modifier: Modifier = Modifier) {
    Text(
        text = schoolName,
        fontWeight = FontWeight.Bold,
        // schoolName may be super long. Cap its maximum width to 300dp.
        modifier = modifier.widthIn(max = 300.dp)
    )
}

@Composable
private fun BoroSubtitle(boro: String, modifier: Modifier = Modifier) {
    Text(text = boro, modifier = modifier)
}

@Composable
private fun ExpandIcon(expanded: Boolean, modifier: Modifier = Modifier) {
    // Set icon and its content description based on the expanded state
    val icon = if (expanded) {
        Icons.Default.ExpandLess
    } else {
        Icons.Default.ExpandMore
    }
    // TODO: Do not use hardcoded values. Load through resource file for advantages such as multi-lingual support.
    val iconContentDescription = if (expanded) {
        "Hide scores"
    } else {
        "Show scores"
    }

    Icon(icon, iconContentDescription, modifier = modifier)
}

private const val SCHOOL = "Schools"
private const val SCHOOL_ONLY_GROUP = "Schools Only"

@Composable
@Preview(group = SCHOOL)
fun CompleteSchoolPreview() {
    val school = SchoolModel(
        "1",
        "Something supeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeer long",
        "M"
    )

    Surface {
        School(school)
    }
}

@Composable
@Preview(group = SCHOOL)
fun CompleteSchoolPreviewExpanded() {
    val school = SchoolModel("1", "School Name Preview", "M")

    Surface {
        School(school, initiallyExpanded = true)
    }
}

@Composable
@Preview(group = SCHOOL)
fun CompleteSchoolPreviewExpandedWhileLoading() {
    val school = SchoolModel("1", "School Name Preview", "M")

    Surface {
        School(school, initiallyExpanded = true, loading = true)
    }
}

@Composable
@Preview(group = SCHOOL_ONLY_GROUP)
fun SchoolPreview() {
    Surface {
        val school = SchoolModel("1", "School Name Preview", "M")

        School(school, composeScores = false)
    }
}

@Composable
@Preview(group = SCHOOL_ONLY_GROUP)
fun SchoolPreviewExpanded() {
    val school = SchoolModel("1", "School Name Preview", "M")

    Surface {
        School(school, initiallyExpanded = true, composeScores = false)
    }
}