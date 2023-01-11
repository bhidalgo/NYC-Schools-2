package com.brandon.hidalgo.nycschools.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.ShowChart
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import com.brandon.hidalgo.nycschools.model.ScoresModel

@Composable
fun Scores(
    scores: ScoresModel? = null
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .padding(PaddingValues(start = 8.dp, end = 8.dp, bottom = 8.dp))
    ) {
        val (title, readingScore, mathScore, writingScore) = createRefs()

        Text("SAT Averages", fontWeight = FontWeight.Bold, modifier = Modifier.constrainAs(title) {
            top.linkTo(parent.top)
            bottom.linkTo(readingScore.top)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        })

        Score(Icons.Outlined.MenuBook,
            "Reading",
            scores?.criticalReadingAverage,
            Modifier.constrainAs(readingScore) {
                top.linkTo(title.bottom)
                bottom.linkTo(parent.bottom)
                start.linkTo(title.start)
                end.linkTo(mathScore.start)
            })

        Score(Icons.Outlined.ShowChart, "Math", scores?.mathAverage, Modifier.constrainAs(mathScore) {
            top.linkTo(readingScore.top)
            bottom.linkTo(readingScore.bottom)
            start.linkTo(readingScore.end)
            end.linkTo(writingScore.start)
        })

        Score(Icons.Outlined.Edit, "Writing", scores?.writingAverage, Modifier.constrainAs(writingScore) {
            top.linkTo(mathScore.top)
            bottom.linkTo(mathScore.bottom)
            start.linkTo(mathScore.end)
            end.linkTo(parent.end)
        })

        createVerticalChain(title, readingScore, chainStyle = ChainStyle.Packed)
        createHorizontalChain(
            readingScore, mathScore, writingScore, chainStyle = ChainStyle.Spread
        )
    }
}

@Composable
private fun Score(
    icon: ImageVector, description: String, score: String?, modifier: Modifier = Modifier
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Row(
            verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 8.dp)
        ) {
            Icon(icon, "$description icon", Modifier.scale(.8f))

            Spacer(Modifier.padding(2.dp))

            Text(description, textAlign = TextAlign.Center)
        }

        Spacer(modifier = Modifier.padding(2.dp))

        Text(
            text = score ?: "Unavailable", color = if (score == null) {
                Color.Gray
            } else {
                Color.Black
            }
        )
    }
}

private const val SCORES_ONLY_GROUP = "Scores Only"

@Composable
@Preview(group = SCORES_ONLY_GROUP)
fun ScoresPreview() {
    val scores = ScoresModel(dbn = "1", criticalReadingAverage = "500", mathAverage = "500", writingAverage = "500")

    Surface {
        Scores(scores)
    }
}

@Composable
@Preview(group = SCORES_ONLY_GROUP)
fun ScoresPreviewUnavailable() {
    Surface {
        Scores()
    }
}