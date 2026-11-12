package com.greekcalculator.ui.main

import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class CalculatorEngineTest {
  @Test
  fun multiplicationHasPrecedenceOverAddition() {
    assertEquals(14.0, CalculatorEngine.evaluate("2+3*4"), 0.0)
  }

  @Test
  fun supportsDecimalsAndUnarySigns() {
    assertEquals(-1.5, CalculatorEngine.evaluate("2.5*-0.6"), 1e-10)
  }

  @Test
  fun rejectsDivisionByZero() {
    assertThrows(IllegalArgumentException::class.java) {
      CalculatorEngine.evaluate("8/0")
    }
  }

  @Test
  fun rejectsIncompleteExpressions() {
    assertThrows(IllegalArgumentException::class.java) {
      CalculatorEngine.evaluate("3+")
    }
  }
}