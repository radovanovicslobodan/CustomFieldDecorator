package com.example.components;

import com.example.utils.ComponentFactory;
import java.time.Duration;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

public class DataTable {

  protected final WebElement container; // Should be the <table>
  protected final WebDriver driver;
  protected final WebDriverWait wait;

  public DataTable(WebElement container, WebDriver driver) {
    this.container = container;
    this.driver = driver;
    this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));

    ComponentFactory.initElements(container, this);
  }

  // --- Column Helpers ---

  public int getColumnIndex(String columnName) {
    List<WebElement> headers = container.findElements(By.cssSelector("thead th .p-column-title"));
    for (int i = 0; i < headers.size(); i++) {
      if (columnName.equals(headers.get(i).getText().trim())) {
        return i;
      }
    }
    throw new IllegalArgumentException("Column '" + columnName + "' not found.");
  }

  public boolean isColumnSorted(String columnName) {
    WebElement headerCell = getHeaderCell(columnName);
    String sortedAttr = headerCell.getAttribute("sorted");
    return "true".equals(sortedAttr);
  }

  public String getColumnSortOrder(String columnName) {
    WebElement headerCell = getHeaderCell(columnName);
    if (!"true".equals(headerCell.getAttribute("sorted"))) {
      return null;
    }
    String sortOrderAttr = headerCell.getAttribute("sortOrder");
    if (sortOrderAttr == null || "1".equals(sortOrderAttr)) {
      return "ascending";
    } else if ("-1".equals(sortOrderAttr)) {
      return "descending";
    }
    return "ascending";
  }

  private WebElement getHeaderCell(String columnName) {
    int colIndex = getColumnIndex(columnName);
    return container.findElements(By.cssSelector("thead th")).get(colIndex);
  }

  // --- Row & Cell Helpers ---

  public WebElement getRowByColumnValue(String columnName, String value) {
    int colIndex = getColumnIndex(columnName);
    List<WebElement> rows = getVisibleRows();
    for (WebElement row : rows) {
      try {
        List<WebElement> cells = row.findElements(By.cssSelector("td[role='cell']"));
        if (colIndex < cells.size()) {
          String cellText = cells.get(colIndex).getText().trim();
          if (value.equals(cellText)) {
            return row;
          }
        }
      } catch (StaleElementReferenceException | NoSuchElementException ignored) {
      }
    }
    throw new IllegalArgumentException("No row found with " + columnName + " = '" + value + "'");
  }

  public String getCellText(int rowIndex, String columnName) {
    int colIndex = getColumnIndex(columnName);
    WebElement row = getVisibleRows().get(rowIndex);
    return row.findElements(By.cssSelector("td[role='cell']")).get(colIndex).getText().trim();
  }

  public int getRowCount() {
    // This won't work accurately due to virtual scroll — returns only rendered rows
    // You need total count from elsewhere
    return getVisibleRows().size();
  }

  protected List<WebElement> getVisibleRows() {
//    List<WebElement> list = driver.findElements(By.cssSelector(
//        "section:nth-child(17) .p-datatable-table tbody.p-virtualscroller-content tr[role='row']"));
//    return list;
    return container.findElements(By.cssSelector("tbody.p-virtualscroller-content tr[role='row']"));
  }
}
