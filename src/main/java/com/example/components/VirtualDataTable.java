package com.example.components;

import com.example.annotations.FindInside;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class VirtualDataTable extends DataTable {

  private final WebDriver driver;
  private final WebElement tableContainer;

  @FindInside(css = ".p-virtualscroller")
  private WebElement scrollableContainer; // e.g., div.p-virtualscroller or parent with overflow

  private final int itemSize; // from virtualScrollerOptions.itemSize

  public VirtualDataTable(WebElement tableContainer, WebDriver driver, int itemSize) {

    super(tableContainer, driver);
    this.driver = driver;
    this.tableContainer = tableContainer;
//		this.scrollableContainer = scrollableContainer;
    this.itemSize = itemSize;
  }

  /**
   * Scrolls to a logical row index and waits for it to appear.
   */
  public void scrollToRow(int logicalRowIndex) {

    int scrollTop = logicalRowIndex * itemSize;
    ((JavascriptExecutor) driver).executeScript(
        "arguments[0].scrollTop = arguments[1];", scrollableContainer, scrollTop
    );

    // Wait until a row with expected index/data appears
    wait.until(d -> {
      List<WebElement> visibleRows = tableContainer.findElements(
          By.cssSelector("tbody tr[role='row']"));
      return !visibleRows.isEmpty(); // or more precise check
    });
  }

  /**
   * Attempts to find a row by column value across the entire dataset. WARNING: Only works if you
   * can scroll to every possible row (slow for large datasets).
   */
  public WebElement getRowByColumnValueInVirtualScroll(String columnName, String value,
      int totalRowCount) {

    int colIndex = getColumnIndex(columnName);
    for (int i = 0; i < totalRowCount; i++) {
      scrollToRow(i);
      try {
        List<WebElement> visibleRows = tableContainer.findElements(
            By.cssSelector("tbody tr[role='row']"));
        for (WebElement row : visibleRows) {
          List<WebElement> cells = row.findElements(By.cssSelector("td[role='cell']"));
          if (colIndex < cells.size() && value.equals(cells.get(colIndex).getText())) {
            return row;
          }
        }
      } catch (StaleElementReferenceException | NoSuchElementException ignored) {
      }
    }
    throw new IllegalArgumentException("Row with " + columnName + " = '" + value + "' not found.");
  }
}
