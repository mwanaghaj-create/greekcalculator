package com.greekcalculator.windows

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class CalculatorEngineTest {
  @Test
  fun multiplicationHasPrecedenceOverAddition() {
    assertEquals(14.0, CalculatorEngine.evaluate("2+3*4"))
  }

  @Test
  fun supportsDecimalsAndUnarySigns() {
    assertEquals(-1.5, CalculatorEngine.evaluate("2.5*-0.6"), 1e-10)
  }

  @Test
  fun rejectsDivisionByZero() {
    assertFailsWith<IllegalArgumentException> { CalculatorEngine.evaluate("8/0") }
  }

  @Test
  fun signToggleRemovesUnaryMinusAtStart() {
    assertEquals("3", toggleLastSign("-3"))
  }
}