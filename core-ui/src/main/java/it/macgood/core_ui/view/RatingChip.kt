package it.macgood.core_ui.view

import androidx.compose.foundation.layout.size
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import it.macgood.core_ui.CustomColor
import it.macgood.core_ui.Fonts
import it.macgood.core_ui.R

@Composable
fun RatingChip(rating: Int, ratingName: String) {
    AssistChip(
        colors = AssistChipDefaults.assistChipColors(containerColor = CustomColor.RatingColorBackground),
        border = null,
        leadingIcon = {
            Icon(
                modifier = Modifier.size(15.dp),
                painter = painterResource(id = R.drawable.ic_star),
                tint = CustomColor.RatingColorOnSurface,
                contentDescription = null
            )
        },
        onClick = { },
        label = {
            Text(
                text = stringResource(
                    id = R.string.rating_value,
                    rating,
                    ratingName
                ),
                fontFamily = Fonts.SfProDisplay,
                fontSize = 16.sp,
                color = CustomColor.RatingColorOnSurface
            )
        }
    )
}