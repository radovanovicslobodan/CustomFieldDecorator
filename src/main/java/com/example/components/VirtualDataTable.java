package com.example.components;

import java.time.Duration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

public class VirtualDataTable extends DataTable {

  private final WebElement scrollableContainer; // now .p-virtualscroller
  private final int itemSize;
  private final WebDriverWait wait;

  public VirtualDataTable(WebElement tableContainer, WebDriver driver, int itemSize) {
    super(tableContainer, driver);
    this.itemSize = itemSize;
    this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));

    // CORRECT: Find the actual scrollable virtual scroller
    this.scrollableContainer = container.findElement(By.cssSelector(".p-virtualscroller"));

    if (this.scrollableContainer == null) {
      throw new IllegalStateException("PrimeVue .p-virtualscroller not found.");
    }

    System.out.println("Using .p-virtualscroller for scrolling. Height: " +
        scrollableContainer.getSize().getHeight() + "px");
  }

  /**
   * Scrolls to the given logical row index.
   */
  public void scrollToRow(int logicalRowIndex) {
    int targetOffset = logicalRowIndex * itemSize;

    // Use JavaScript to set scrollTop on the correct container
    ((JavascriptExecutor) driver).executeScript(
        "var container = arguments[0];\n" +
            "var target = arguments[1];\n" +
            "container.scrollTop = target;\n" +

            // Trigger scroll event manually so Angular/PrimeVue detects it
            "var event = new Event('scroll', { bubbles: true });\n" +
            "container.dispatchEvent(event);\n",

        scrollableContainer,  // will fix this below
        targetOffset
    );

    // Wait for any row with data-p-index near target
    wait.until(d -> {
      List<WebElement> rows = getVisibleRows();
      return !rows.isEmpty() && rows.stream().anyMatch(r -> {
        try {
          String idxStr = r.getAttribute("data-p-index");
          return idxStr != null && Math.abs(Integer.parseInt(idxStr) - logicalRowIndex) < 10;
        } catch (Exception e) {
          return false;
        }
      });
    });
  }

  /**
   * Gets total number of rows by parsing the spacer height.
   */
  public int getTotalRowCount() {
    try {
      WebElement spacer = container.findElement(
          By.cssSelector("tbody.p-datatable-virtualscroller-spacer"));
      String style = spacer.getAttribute("style"); // e.g., "height: calc(4.59913e+06px);"

      // Extract number from calc(...)
      Pattern p = Pattern.compile("calc\\s*\\(\\s*([\\d.Ee+]+)px\\s*\\)");
      Matcher m = p.matcher(style);
      if (m.find()) {
        double spacerHeight = Double.parseDouble(m.group(1));
        double totalTableHeight = Double.parseDouble(
            container.getCssValue("height").replace("px", ""));
        double contentHeight = totalTableHeight - spacerHeight;
        return (int) Math.round(contentHeight / itemSize);
      }
    } catch (Exception e) {
      System.err.println("Could not parse total row count: " + e.getMessage());
    }
    throw new IllegalStateException("Unable to determine total row count.");
  }

  /**
   * Finds a row by column value in large virtual dataset.
   */
  public WebElement getRowByColumnValueInVirtualScroll(String columnName, String value) {
    int colIndex = getColumnIndex(columnName);
    int totalRows = getTotalRowCount();

    for (int i = 0; i < totalRows; i += 20) {
      scrollToRow(i);

      List<WebElement> visibleRows = getVisibleRows();
      for (WebElement row : visibleRows) {
        try {
          List<WebElement> cells = row.findElements(By.cssSelector("td[role='cell']"));
          if (colIndex < cells.size()) {
            String text = cells.get(colIndex).getText().trim();
            if (value.equals(text)) {
              return row;
            }
          }
        } catch (Exception ignored) {
        }
      }
    }
    throw new IllegalArgumentException("Row with " + columnName + " = '" + value + "' not found.");
  }
}
