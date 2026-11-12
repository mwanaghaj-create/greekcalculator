package com.greekcalculator.windows

internal object CalculatorEngine {
  fun evaluate(expression: String): Double = Parser(expression).parse()

  private class Parser(private val input: String) {
    private var position = 0

    fun parse(): Double {
      val value = parseExpression()
      skipWhitespace()
      require(position == input.length) { "Unexpected input" }
      require(value.isFinite()) { "Result is not finite" }
      return value
    }

    private fun parseExpression(): Double {
      var value = parseTerm()
      while (true) {
        skipWhitespace()
        value = when {
          consume('+') -> value + parseTerm()
          consume('-') -> value - parseTerm()
          else -> return value
        }
      }
    }

    private fun parseTerm(): Double {
      var value = parseFactor()
      while (true) {
        skipWhitespace()
        value = when {
          consume('*') -> value * parseFactor()
          consume('/') -> {
            val divisor = parseFactor()
            require(divisor != 0.0) { "Cannot divide by zero" }
            value / divisor
          }
          else -> return value
        }
      }
    }

    private fun parseFactor(): Double {
      skipWhitespace()
      return when {
        consume('+') -> parseFactor()
        consume('-') -> -parseFactor()
        else -> parseNumber()
      }
    }

    private fun parseNumber(): Double {
      skipWhitespace()
      val start = position
      var hasDigit = false
      var hasDecimal = false
      while (position < input.length) {
        when (input[position]) {
          in '0'..'9' -> hasDigit = true
          '.' -> {
            require(!hasDecimal) { "Invalid number" }
            hasDecimal = true
          }
          else -> break
        }
        position++
      }
      require(hasDigit) { "Expected a number" }
      return input.substring(start, position).toDouble()
    }

    private fun consume(character: Char): Boolean {
      if (position < input.length && input[position] == character) {
        position++
        return true
      }
      return false
    }

    private fun skipWhitespace() {
      while (position < input.length && input[position].isWhitespace()) position++
    }
  }
}