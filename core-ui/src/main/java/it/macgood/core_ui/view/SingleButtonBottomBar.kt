package it.macgood.core_ui.view

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import it.macgood.core_ui.CustomColor
import it.macgood.core_ui.GradientButton

@Composable
fun SingleButtonBottomBar(
    buttonText: String,
    onNavigate: () -> Unit
) {
    BottomAppBar(
        modifier = Modifier
            .height(96.dp),
        containerColor = Color.White,
        contentColor = Color.Black,
        tonalElevation = 8.dp
    ) {
        GradientButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .height(48.dp),
            onClick = onNavigate,
            text = buttonText,
            containerColor = CustomColor.AddressColor
        )
    }
}