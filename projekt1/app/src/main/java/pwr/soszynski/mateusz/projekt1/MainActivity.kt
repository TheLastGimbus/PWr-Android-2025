package pwr.soszynski.mateusz.projekt1

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pwr.soszynski.mateusz.projekt1.ui.theme.Projekt1Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Projekt1Theme {
                MainActivityScaffold(this)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainActivityScaffold(context: Context? = null) {
    @Composable
    fun spacer() = Spacer(Modifier.height(16.dp))
    var text by rememberSaveable { mutableStateOf("") }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) { innerPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Button(onClick = {
                if (context != null) {
                    Toast.makeText(context, "Wow ❗", Toast.LENGTH_SHORT).show()
                }
            }) {
                Text("Wow 1 ❗")
            }
            spacer()
            TextField(value = text, onValueChange = {
                text = it
            }, label = { Text("Tekst do Wow2‼") })
            spacer()
            Button(onClick = {}) {
                Text("Wow 2 ‼")
            }
        }
    }
}
