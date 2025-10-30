package com.example.pages;

import com.example.components.DataTable;
import com.example.components.VirtualDataTable;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class PrimeVueDataTablePage {

  private WebDriver driver;
  private DataTable table;
  private VirtualDataTable virtualTable;

  @FindBy(css = "section:nth-child(17)") // container div with p-dropdown
  private WebElement tableContainer;

  //  @FindBy(css = "section:nth-child(17) .p-datatable-wrapper")
  @FindBy(css = "section:nth-child(17)")
  private WebElement virtualTableContainer;

  public PrimeVueDataTablePage(WebDriver driver) {

    this.driver = driver;
    PageFactory.initElements(driver, this);
    table = new DataTable(tableContainer, driver);
    int size = detectItemSize(this.driver, virtualTableContainer);
    virtualTable = new VirtualDataTable(virtualTableContainer, driver, size);
  }

  public DataTable getTable() {

    return table;
  }

  public VirtualDataTable getVirtualTable() {

    return virtualTable;
  }

  public static int detectItemSize(WebDriver driver, WebElement virtualTableContainer) {
    List<WebElement> sampleRows = virtualTableContainer.findElements(
        By.cssSelector("tbody.p-virtualscroller-content tr[role='row']")
    );

    if (sampleRows.size() < 2) {
      throw new IllegalStateException("Not enough visible rows to detect item size.");
    }

    // Use Y positions of first two rows
    Point row0Pos = sampleRows.get(0).getLocation();
    Point row1Pos = sampleRows.get(1).getLocation();

    return Math.abs(row1Pos.getY() - row0Pos.getY());
  }
}
