package ru.kartollika.simplecanvas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import ru.kartollika.simplecanvas.compose.CanvasScreen
import ru.kartollika.simplecanvas.ui.theme.RippleTheme
import ru.kartollika.simplecanvas.ui.theme.SimpleCanvasTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()

    setContent {
      SimpleCanvasTheme {
        CompositionLocalProvider(
          LocalRippleTheme provides RippleTheme(
            rippleColor = MaterialTheme.colorScheme.onSurface
          )
        ) {
          CanvasScreen(
            modifier = Modifier
              .fillMaxSize()
              .padding(32.dp)
              .systemBarsPadding(),
          )
        }
      }
    }
  }

}