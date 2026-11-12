package com.greekcalculator.windows

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

private val Cabinet = Color(0xFF20352C)
private val Paper = Color(0xFFF0E4C8)
private val Display = Color(0xFFC9B98B)
private val Ink = Color(0xFF28382D)
private val Brass = Color(0xFFC38D4C)
private val Clay = Color(0xFFB9684B)
private val KeyGreen = Color(0xFF3B5A4B)

private enum class KeyKind { Digit, Operator, Decimal, Sign, Clear, Delete, Equals }

private data class CalculatorKey(
  val label: String,
  val description: String,
  val kind: KeyKind,
  val value: String = "",
)

private val keypad = listOf(
  listOf(
    CalculatorKey("Κ", "Clear", KeyKind.Clear),
    CalculatorKey("⌫", "Delete", KeyKind.Delete),
    CalculatorKey("λ", "Divide", KeyKind.Operator, "/"),
    CalculatorKey("χ", "Multiply", KeyKind.Operator, "*"),
  ),
  listOf(
    CalculatorKey("η", "Digit 7", KeyKind.Digit, "7"),
    CalculatorKey("θ", "Digit 8", KeyKind.Digit, "8"),
    CalculatorKey("ι", "Digit 9", KeyKind.Digit, "9"),
    CalculatorKey("μ", "Subtract", KeyKind.Operator, "-"),
  ),
  listOf(
    CalculatorKey("δ", "Digit 4", KeyKind.Digit, "4"),
    CalculatorKey("ε", "Digit 5", KeyKind.Digit, "5"),
    CalculatorKey("ζ", "Digit 6", KeyKind.Digit, "6"),
    CalculatorKey("π", "Add", KeyKind.Operator, "+"),
  ),
  listOf(
    CalculatorKey("α", "Digit 1", KeyKind.Digit, "1"),
    CalculatorKey("β", "Digit 2", KeyKind.Digit, "2"),
    CalculatorKey("γ", "Digit 3", KeyKind.Digit, "3"),
    CalculatorKey("Σ", "Equals", KeyKind.Equals),
  ),
  listOf(
    CalculatorKey("ν", "Change sign", KeyKind.Sign),
    CalculatorKey("·", "Decimal", KeyKind.Decimal),
    CalculatorKey("ο", "Digit 0", KeyKind.Digit, "0"),
    CalculatorKey("=", "Conventional equals", KeyKind.Equals),
  ),
)

fun main() = application {
  Window(
    onCloseRequest = ::exitApplication,
    title = "Greek Calculator",
    state = rememberWindowState(width = 460.dp, height = 720.dp),
  ) {
    MaterialTheme(
      colorScheme = lightColorScheme(
        primary = KeyGreen,
        secondary = Brass,
        surface = Paper,
        onSurface = Ink,
      ),
    ) {
      CalculatorApp()
    }
  }
}

@Composable
private fun CalculatorApp() {
  var expression by remember { mutableStateOf("") }
  var result by remember { mutableStateOf<String?>(null) }
  var hasError by remember { mutableStateOf(false) }

  fun press(key: CalculatorKey) {
    hasError = false
    when (key.kind) {
      KeyKind.Clear -> {
        expression = ""
        result = null
      }
      KeyKind.Delete -> {
        if (result != null) {
          result = null
          expression = ""
        } else {
          expression = expression.dropLast(1)
        }
      }
      KeyKind.Digit -> {
        expression = if (result != null) key.value else expression + key.value
        result = null
      }
      KeyKind.Decimal -> {
        if (result != null) expression = ""
        result = null
        val lastOperator = expression.indexOfLast { it in "+-*/" }
        val currentNumber = expression.substring(lastOperator + 1)
        if ('.' !in currentNumber) expression += if (currentNumber.isEmpty()) "0." else "."
      }
      KeyKind.Operator -> {
        if (result != null) expression = result.orEmpty()
        result = null
        expression = appendOperator(expression, key.value.single())
      }
      KeyKind.Sign -> {
        if (result != null) {
          expression = formatResult(-result.orEmpty().toDouble())
          result = null
        } else {
          expression = toggleLastSign(expression)
        }
      }
      KeyKind.Equals -> if (expression.isNotBlank()) {
        try {
          result = formatResult(CalculatorEngine.evaluate(expression))
        } catch (_: IllegalArgumentException) {
          result = null
          hasError = true
        }
      }
    }
  }

  Column(
    modifier = Modifier.fillMaxSize().background(Cabinet).padding(horizontal = 20.dp, vertical = 14.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp),
  ) {
    Row(
      modifier = Modifier.fillMaxWidth().height(38.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween,
    ) {
      Text(
        text = "ΑΡΙΘΜΟΜΗΧΑΝΗ",
        color = Paper,
        style = MaterialTheme.typography.titleSmall,
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.Bold,
      )
      Text("WINDOWS  •  01", color = Brass, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
    }

    Surface(
      modifier = Modifier.fillMaxWidth().height(150.dp),
      shape = RoundedCornerShape(8.dp),
      color = Display,
      shadowElevation = 3.dp,
    ) {
      Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 18.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.End,
      ) {
        Text(
          text = if (expression.isBlank()) "ΟΘΟΝΗ" else toGreek(expression),
          color = Ink.copy(alpha = 0.72f),
          fontSize = 16.sp,
          fontFamily = FontFamily.Monospace,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis,
        )
        Text(
          text = when {
            hasError -> "ΣΦΑΛΜΑ"
            result != null -> toGreek(result.orEmpty())
            expression.isEmpty() -> "ο"
            else -> toGreek(expression)
          },
          modifier = Modifier.fillMaxWidth(),
          color = Ink,
          fontSize = 42.sp,
          fontFamily = FontFamily.Monospace,
          fontWeight = FontWeight.Medium,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis,
          textAlign = TextAlign.End,
        )
      }
    }

    Column(
      modifier = Modifier.weight(1f).fillMaxWidth(),
      verticalArrangement = Arrangement.spacedBy(9.dp),
    ) {
      keypad.forEach { row ->
        Row(
          modifier = Modifier.weight(1f).fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(9.dp),
        ) {
          row.forEach { key ->
            val color = when (key.kind) {
              KeyKind.Digit, KeyKind.Decimal, KeyKind.Sign -> Paper
              KeyKind.Operator -> KeyGreen
              KeyKind.Clear, KeyKind.Delete -> Clay
              KeyKind.Equals -> Brass
            }
            val textColor = if (key.kind in setOf(KeyKind.Digit, KeyKind.Decimal, KeyKind.Sign)) Ink else Paper

            Button(
              onClick = { press(key) },
              modifier = Modifier.weight(1f).fillMaxHeight().semantics {
                contentDescription = key.description
              },
              shape = RoundedCornerShape(8.dp),
              colors = ButtonDefaults.buttonColors(containerColor = color, contentColor = textColor),
              elevation = ButtonDefaults.buttonElevation(defaultElevation = 5.dp, pressedElevation = 1.dp),
              contentPadding = PaddingValues(0.dp),
            ) {
              Text(
                text = key.label,
                fontSize = if (key.kind == KeyKind.Digit) 25.sp else 20.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
              )
            }
          }
        }
      }
    }
  }
}

private fun appendOperator(expression: String, operator: Char): String {
  if (expression.isEmpty()) return if (operator == '-') "-" else expression
  return if (expression.last() in "+-*/") expression.dropLast(1) + operator else expression + operator
}

internal fun toggleLastSign(expression: String): String {
  if (expression.isEmpty()) return "-"
  var index = expression.lastIndex
  while (index >= 0) {
    if (expression[index] in "+-*/" &&
      !(expression[index] == '-' && (index == 0 || expression[index - 1] in "+-*/"))
    ) break
    index--
  }
  val numberStart = index + 1
  if (numberStart == expression.length) return expression
  return if (expression[numberStart] == '-') {
    expression.removeRange(numberStart, numberStart + 1)
  } else {
    expression.substring(0, numberStart) + "-" + expression.substring(numberStart)
  }
}

private fun formatResult(value: Double): String {
  if (value == 0.0) return "0"
  return BigDecimal.valueOf(value)
    .round(MathContext(12, RoundingMode.HALF_UP))
    .stripTrailingZeros()
    .toPlainString()
}

private fun toGreek(value: String): String = buildString {
  value.forEach { character ->
    append(
      when (character) {
        '0' -> 'ο'
        '1' -> 'α'
        '2' -> 'β'
        '3' -> 'γ'
        '4' -> 'δ'
        '5' -> 'ε'
        '6' -> 'ζ'
        '7' -> 'η'
        '8' -> 'θ'
        '9' -> 'ι'
        '+' -> 'π'
        '-' -> 'μ'
        '*' -> 'χ'
        '/' -> 'λ'
        '.' -> '·'
        else -> character
      }
    )
  }
}