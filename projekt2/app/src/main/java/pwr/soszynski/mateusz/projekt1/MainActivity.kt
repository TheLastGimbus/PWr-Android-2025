package pwr.soszynski.mateusz.projekt1

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
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
    object Wow3 : Screen("wow3_screen")
}

class MainActivity : ComponentActivity(), SensorEventListener {


    private var accelerometerVals = mutableStateListOf(0.0f, 0.0f, 0.0f)
    private var gravityVals = mutableStateListOf(0.0f, 0.0f, 0.0f)
    private var lightVals = mutableStateListOf(0.0f)


    private lateinit var sensorManager: SensorManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        enableEdgeToEdge()
        setContent {
            Projekt1Theme {
                NavigationStack(this)
            }
        }
    }

    override fun onResume() {
        for (type in listOf(Sensor.TYPE_LINEAR_ACCELERATION, Sensor.TYPE_GRAVITY, Sensor.TYPE_LIGHT)) {
            sensorManager.registerListener(this, sensorManager.getDefaultSensor(type), 100000)
        }
        super.onResume()
    }

    override fun onPause() {
        sensorManager.unregisterListener(this)
        super.onPause()
    }


    //    @Preview(showBackground = true)
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
            composable(route = Screen.Wow3.route) {
                Wow3ActivityScaffold(context = context)
            }
        }
    }

    @Composable
    fun MainActivityScaffold(context: Context? = null, navController: NavController) {
        @Composable
        fun spacer() = Spacer(Modifier.height(16.dp))
        var textHeight by rememberSaveable { mutableStateOf("150") }
        var textWeight by rememberSaveable { mutableStateOf("60") }

        @Composable
        fun ColorChangingBox(colorProgress: Float) {
            val color by animateColorAsState(
                targetValue = lerp(Color.Red, Color.Green, (colorProgress / 150.0f)),
                label = "color_animation"
            )
            Box(
                modifier = Modifier
                    .size(64.dp, 64.dp)
                    .background(color)
            )
        }
//        val progress = remember { mutableFloatStateOf(lightVals[0]) }

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
                ColorChangingBox(lightVals[0])
                Text("${accelerometerVals[0].toInt()} | ${accelerometerVals[1].toInt()} | ${accelerometerVals[2].toInt()}")
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
                Button(onClick = {
                    navController.navigate(route = Screen.Wow3.route)
                }) {
                    Text("Wow 3 ‼")
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


    @Composable
    fun Wow3ActivityScaffold(context: Context? = null, height: Int? = null, weight: Int? = null) {


        val configuration = LocalConfiguration.current
        val density = LocalDensity.current

        val screenWidthDp = with(density) { configuration.screenWidthDp.dp }
        val screenHeightDp = with(density) { configuration.screenHeightDp.dp }

        @Composable
        fun spacer() = Spacer(Modifier.height(16.dp))

        val x = (screenWidthDp / 2) - ((screenWidthDp / 2) * (gravityVals[0] / 10))
        val y = (screenHeightDp / 2) + ((screenHeightDp) * (gravityVals[1] / 10) / 2)

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
                Box(modifier = Modifier.fillMaxSize()) {
                    Text(
                        text = "O",
                        modifier = Modifier.offset(x, y)
                    )
                }
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

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
    }

    override fun onSensorChanged(event: SensorEvent) {
        when (event.sensor.type) {
            Sensor.TYPE_LINEAR_ACCELERATION -> {
                accelerometerVals[0] = event.values[0]
                accelerometerVals[1] = event.values[1]
                accelerometerVals[2] = event.values[2]
            }

            Sensor.TYPE_GRAVITY -> {
                gravityVals[0] = event.values[0]
                gravityVals[1] = event.values[1]
                gravityVals[2] = event.values[2]
            }

            Sensor.TYPE_LIGHT -> {
                lightVals[0] = event.values[0]
            }
        }
    }

}

