package org.dubiner.arithmetic

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import org.dubiner.arithmetic.ui.theme.ArithmeticTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ArithmeticTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigator()
                }
            }
        }
    }
}

@Composable
fun AppNavigator() {
    var screen by remember { mutableStateOf("menu") }
    var additionRange1 by remember { mutableStateOf(2..100) }
    var additionRange2 by remember { mutableStateOf(2..100) }
    var multiplicationRange1 by remember { mutableStateOf(2..12) }
    var multiplicationRange2 by remember { mutableStateOf(2..100) }
    var timeLimit by remember { mutableStateOf(120) }
    var filters by remember { mutableStateOf(setOf("Addition", "Subtraction", "Multiplication", "Division")) }

    when (screen) {
        "menu" -> MenuScreen(
            additionRange1 = additionRange1,
            additionRange2 = additionRange2,
            multiplicationRange1 = multiplicationRange1,
            multiplicationRange2 = multiplicationRange2,
            timeLimit = timeLimit,
            filters = filters,
            onStartGame = { newAdditionRange1, newAdditionRange2, newMultiplicationRange1, newMultiplicationRange2, newTimeLimit, newFilters ->
                additionRange1 = newAdditionRange1
                additionRange2 = newAdditionRange2
                multiplicationRange1 = newMultiplicationRange1
                multiplicationRange2 = newMultiplicationRange2
                timeLimit = newTimeLimit
                filters = newFilters
                screen = "game"
            }
        )

        "game" -> GameScreen(
            additionRange1 = additionRange1,
            additionRange2 = additionRange2,
            multiplicationRange1 = multiplicationRange1,
            multiplicationRange2 = multiplicationRange2,
            timeLimit = timeLimit,
            filters = filters,
            onGameEnd = { screen = "end" }
        )

        "end" -> EndScreen(
            onTryAgain = { screen = "game" },
            onChangeSettings = { screen = "menu" }
        )
    }
}

@Composable
fun MenuScreen(
    additionRange1: IntRange,
    additionRange2: IntRange,
    multiplicationRange1: IntRange,
    multiplicationRange2: IntRange,
    timeLimit: Int,
    filters: Set<String>,
    onStartGame: (IntRange, IntRange, IntRange, IntRange, Int, Set<String>) -> Unit
) {
    var newAdditionRange1Start by remember { mutableStateOf(additionRange1.first.toString()) }
    var newAdditionRange1End by remember { mutableStateOf(additionRange1.last.toString()) }
    var newAdditionRange2Start by remember { mutableStateOf(additionRange2.first.toString()) }
    var newAdditionRange2End by remember { mutableStateOf(additionRange2.last.toString()) }
    var newMultiplicationRange1Start by remember { mutableStateOf(multiplicationRange1.first.toString()) }
    var newMultiplicationRange1End by remember { mutableStateOf(multiplicationRange1.last.toString()) }
    var newMultiplicationRange2Start by remember { mutableStateOf(multiplicationRange2.first.toString()) }
    var newMultiplicationRange2End by remember { mutableStateOf(multiplicationRange2.last.toString()) }
    var newTimeLimit by remember { mutableStateOf(timeLimit.toString()) }
    var selectedFilters by remember { mutableStateOf(filters) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text("Configure Settings", style = MaterialTheme.typography.headlineMedium)

        // Addition Settings
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = "Addition" in selectedFilters,
                onCheckedChange = { checked -> selectedFilters = selectedFilters.toggle("Addition", checked) }
            )
            Text("Addition", style = MaterialTheme.typography.headlineSmall)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Range: (")
            StyledTextField(
                value = newAdditionRange1Start,
                onValueChange = { newAdditionRange1Start = it }
            )
            Text(" to ")
            StyledTextField(
                value = newAdditionRange1End,
                onValueChange = { newAdditionRange1End = it }
            )
            Text(") + (")
            StyledTextField(
                value = newAdditionRange2Start,
                onValueChange = { newAdditionRange2Start = it }
            )
            Text(" to ")
            StyledTextField(
                value = newAdditionRange2End,
                onValueChange = { newAdditionRange2End = it }
            )
            Text(")")
        }

        // Subtraction Settings
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = "Subtraction" in selectedFilters,
                onCheckedChange = { checked -> selectedFilters = selectedFilters.toggle("Subtraction", checked) }
            )
            Text("Subtraction", style = MaterialTheme.typography.headlineSmall)
        }
        Text("Addition problems in reverse.", style = MaterialTheme.typography.bodyMedium)

        // Multiplication Settings
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = "Multiplication" in selectedFilters,
                onCheckedChange = { checked -> selectedFilters = selectedFilters.toggle("Multiplication", checked) }
            )
            Text("Multiplication", style = MaterialTheme.typography.headlineSmall)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Range: (")
            StyledTextField(
                value = newMultiplicationRange1Start,
                onValueChange = { newMultiplicationRange1Start = it }
            )
            Text(" to ")
            StyledTextField(
                value = newMultiplicationRange1End,
                onValueChange = { newMultiplicationRange1End = it }
            )
            Text(") × (")
            StyledTextField(
                value = newMultiplicationRange2Start,
                onValueChange = { newMultiplicationRange2Start = it }
            )
            Text(" to ")
            StyledTextField(
                value = newMultiplicationRange2End,
                onValueChange = { newMultiplicationRange2End = it }
            )
            Text(")")
        }

        // Division Settings
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = "Division" in selectedFilters,
                onCheckedChange = { checked -> selectedFilters = selectedFilters.toggle("Division", checked) }
            )
            Text("Division", style = MaterialTheme.typography.headlineSmall)
        }
        Text("Multiplication problems in reverse.", style = MaterialTheme.typography.bodyMedium)

        // Time Limit Selector
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Time Limit (seconds): ", modifier = Modifier.width(180.dp))
            StyledTextField(
                value = newTimeLimit,
                onValueChange = { newTimeLimit = it }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Start Game Button
        Button(
            onClick = {
                val additionRange1 = newAdditionRange1Start.toInt()..newAdditionRange1End.toInt()
                val additionRange2 = newAdditionRange2Start.toInt()..newAdditionRange2End.toInt()
                val multiplicationRange1 = newMultiplicationRange1Start.toInt()..newMultiplicationRange1End.toInt()
                val multiplicationRange2 = newMultiplicationRange2Start.toInt()..newMultiplicationRange2End.toInt()
                val timeLimit = newTimeLimit.toInt()
                onStartGame(
                    additionRange1,
                    additionRange2,
                    multiplicationRange1,
                    multiplicationRange2,
                    timeLimit,
                    selectedFilters
                )
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Start Game")
        }
    }
}

@Composable
fun StyledTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var text by remember { mutableStateOf(TextFieldValue(value)) }
    BasicTextField(
        value = text,
        onValueChange = {
            text = it
            onValueChange(it.text)
        },
        modifier = modifier
            .background(Color.LightGray, shape = MaterialTheme.shapes.small)
            .padding(4.dp) // Reduced padding for smaller input fields
            .width(50.dp) // Smaller width
            .onFocusChanged { focusState ->
                if (focusState.isFocused) {
                    // Place cursor at the end when focused
                    text = TextFieldValue(text.text, TextRange(text.text.length))
                }
            },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        textStyle = TextStyle(fontSize = 14.sp, color = Color.Black)
    )
}

@Composable
fun GameScreen(
    additionRange1: IntRange,
    additionRange2: IntRange,
    multiplicationRange1: IntRange,
    multiplicationRange2: IntRange,
    timeLimit: Int,
    filters: Set<String>,
    onGameEnd: () -> Unit
) {
    var problem by remember {
        mutableStateOf(
            generateProblem(
                additionRange1,
                additionRange2,
                multiplicationRange1,
                multiplicationRange2,
                filters
            )
        )
    }
    var userAnswer by remember { mutableStateOf("") }
    var score by remember { mutableStateOf(0) }
    var timeLeft by remember { mutableStateOf(timeLimit) }

    // Start a timer countdown
    LaunchedEffect(timeLimit) {
        while (timeLeft > 0) {
            delay(1000L)  // Delay for 1 second
            timeLeft -= 1
        }
        onGameEnd()  // End game when timer reaches zero
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Timer at the top-left corner
        Text(
            text = "Time: $timeLeft seconds",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.align(Alignment.TopStart)
        )

        // Game content centered
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Display the math problem
            Text("Solve: ${problem.first}", style = MaterialTheme.typography.headlineLarge)

            Spacer(modifier = Modifier.height(16.dp))

            // Input for user answer
            BasicTextField(
                value = userAnswer,
                onValueChange = {
                    userAnswer = it
                    if (it.toIntOrNull() == problem.second) {
                        score++
                        problem = generateProblem(
                            additionRange1,
                            additionRange2,
                            multiplicationRange1,
                            multiplicationRange2,
                            filters
                        )
                        userAnswer = ""
                    }
                },
                modifier = Modifier
                    .background(Color.White)
                    .padding(8.dp)
                    .width(100.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                textStyle = TextStyle(fontSize = 24.sp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("Score: $score", style = MaterialTheme.typography.bodyLarge)
        }
    }
}


@Composable
fun EndScreen(onTryAgain: () -> Unit, onChangeSettings: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Time's Up!", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onTryAgain) { Text("Try Again") }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onChangeSettings) { Text("Change Settings") }
    }
}

fun generateProblem(
    additionRange1: IntRange,
    additionRange2: IntRange,
    multiplicationRange1: IntRange,
    multiplicationRange2: IntRange,
    filters: Set<String>
): Pair<String, Int> {
    val problems = mutableListOf<Pair<String, Int>>()
    if ("Addition" in filters) {
        val a = additionRange1.random()
        val b = additionRange2.random()
        problems.add("$a + $b" to a + b)
    }
    if ("Subtraction" in filters) {
        val a = additionRange1.random()
        val b = additionRange2.random()
        problems.add("$a - $b" to a - b)
    }
    if ("Multiplication" in filters) {
        val a = multiplicationRange1.random()
        val b = multiplicationRange2.random()
        problems.add("$a × $b" to a * b)
    }
    if ("Division" in filters) {
        val a = multiplicationRange2.random()
        val b = multiplicationRange1.random().coerceAtLeast(1) // Avoid division by zero
        val dividend = a * b // Ensures the dividend is divisible
        problems.add("$dividend ÷ $b" to dividend / b)
    }
    return problems.random()
}

fun Set<String>.toggle(item: String, add: Boolean): Set<String> =
    if (add) this + item else this - item
