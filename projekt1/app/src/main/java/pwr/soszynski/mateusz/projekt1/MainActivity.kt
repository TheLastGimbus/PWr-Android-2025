package pwr.soszynski.mateusz.projekt1

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import pwr.soszynski.mateusz.projekt1.ui.theme.Projekt1Theme
import kotlin.math.pow

sealed class Screen(val route: String) {
    object Main : Screen("main_screen")
    object Wow1 : Screen("wow1_screen")
    object Wow2 : Screen("wow2_screen")
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Projekt1Theme {
                NavigationStack(this)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NavigationStack(context: Context? = null) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Main.route) {
        composable(route = Screen.Main.route) {
            MainActivityScaffold(context = context, navController = navController)
        }
        composable(route = Screen.Wow1.route) {
            Wow1ActivityScaffold(context = context)
        }
        composable(
            route = Screen.Wow2.route + "?height={height}&weight={weight}",
            arguments = listOf(navArgument("height") {
                type = NavType.IntType
                nullable = false
            }, navArgument("weight") {
                type = NavType.IntType
                nullable = false
            })
        ) {
            Wow2ActivityScaffold(
                context = context,
                height = it.arguments?.getInt("height"),
                weight = it.arguments?.getInt("weight")
            )
        }
    }
}

@Composable
fun MainActivityScaffold(context: Context? = null, navController: NavController) {
    @Composable
    fun spacer() = Spacer(Modifier.height(16.dp))
    var textHeight by rememberSaveable { mutableStateOf("150") }
    var textWeight by rememberSaveable { mutableStateOf("60") }

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
            TextField(
                value = textHeight,
                onValueChange = {
                    textHeight = it
                },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                label = { Text("Twój wzrost (cm)") })
            spacer()
            TextField(
                value = textWeight,
                onValueChange = {
                    textWeight = it
                },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                label = { Text("Twoja waga (kg)") })
            spacer()
            Button(onClick = {
                navController.navigate(route = Screen.Wow2.route + "?height=$textHeight&weight=$textWeight")

            }) {
                Text("Wow 2 ‼")
            }
        }
    }
}

@Composable
fun Wow1ActivityScaffold(context: Context? = null) {
    @Composable
    fun spacer() = Spacer(Modifier.height(16.dp))

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
            Text("Oh wow!")
        }
    }
}


fun bmi(height: Double, weight: Double) = weight / (height / 100).pow(2)

@Preview(showBackground = true)
@Composable
fun Wow2ActivityScaffold(context: Context? = null, height: Int? = null, weight: Int? = null) {
    @Composable
    fun spacer() = Spacer(Modifier.height(16.dp))

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
            Text(
                "Twoje BMI to: ${
                    String.format(
                        "%.2f", bmi(height?.toDouble() ?: 0.0, weight?.toDouble() ?: 0.0)
                    )
                }"
            )
            spacer()

            Image(
                painter = painterResource(R.mipmap.bmi),
                contentDescription = "AAAA"
            )
        }
    }
}

