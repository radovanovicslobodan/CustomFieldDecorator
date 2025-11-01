package com.example.components;

import java.awt.Color;
import org.openqa.selenium.WebElement;
//import org.openqa.selenium.support.Color;

public class SunburstSlice {

  private Color sliceColor;
  private String sliceText;

  public SunburstSlice(WebElement pathElement, WebElement textElement) {
    // Extract RGB color from the 'fill' style of the <path>
    String fillColor = pathElement.getCssValue("fill");
    this.sliceColor = parseColorFromRgbString(fillColor);

    // Get the visible text from the <text> element
    this.sliceText = textElement.getText();
  }

  // Helper to parse rgb(r, g, b) string into java.awt.Color
  private Color parseColorFromRgbString(String rgb) {
    if (rgb == null || !rgb.startsWith("rgb")) {
      return Color.BLACK;
    }

    // Remove "rgb(" and ")" and split
    String[] parts = rgb.replaceAll("rgb\\(|\\)", "").split(",");
    int r = Integer.parseInt(parts[0].trim());
    int g = Integer.parseInt(parts[1].trim());
    int b = Integer.parseInt(parts[2].trim());
    return new Color(r, g, b);
  }

  // Getters
  public Color getSliceColor() {
    return sliceColor;
  }

  public String getSliceText() {
    return sliceText;
  }

  @Override
  public String toString() {
    return "SunburstSlice{text='" + sliceText + "', color=" + sliceColor + "}";
  }
}
