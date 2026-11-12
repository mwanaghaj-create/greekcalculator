package com.greekcalculator.ui.main

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.onNodeWithText
import org.junit.Rule
import org.junit.Test

class MainScreenTest {
  @get:Rule val composeTestRule = createAndroidComposeRule<ComponentActivity>()

  @Test
  fun greekKeysCalculateAndDisplayGreekResult() {
    composeTestRule.setContent { MainScreen() }

    composeTestRule.onNodeWithContentDescription("Digit 2").performClick()
    composeTestRule.onNodeWithContentDescription("Add").performClick()
    composeTestRule.onNodeWithContentDescription("Digit 2").performClick()
    composeTestRule.onNodeWithContentDescription("Greek equals").performClick()

    composeTestRule.onNodeWithText("δ").assertExists()
  }
}
