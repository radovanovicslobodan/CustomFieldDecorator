package tests;

import com.example.components.AdvancedFilter;
import com.example.components.ColumnHeader;
import com.example.components.DropdownList;
import com.example.enums.Operator;
import com.example.enums.Rule;
import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class TableTest {

  private WebDriver driver;

  @BeforeClass
  public void setup() {

    driver = new ChromeDriver();
    driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
  }

  @Test
  public void testTableFiltering() {
    driver.get("https://primevue.org/datatable/#advanced_filter");

    WebElement section = driver.findElement(By.xpath("//section[12]"));
    WebElement tableHeader = section.findElement(By.cssSelector(".p-datatable-thead"));
    WebElement firstColumnHeaderContainer = tableHeader.findElements(By.cssSelector("th")).get(0);

    ColumnHeader columnHeader = new ColumnHeader(firstColumnHeaderContainer, driver);

    AdvancedFilter advancedFilter = columnHeader.openFilter();
    DropdownList dropDownList = advancedFilter.openOperatorList();

    dropDownList.selectOption(Operator.MATCH_ANY);

    DropdownList ruleDropDown = advancedFilter.openRuleList();
    ruleDropDown.selectOption(Rule.NOT_EQUALS);

    advancedFilter.enterSearchTerm("Italy");
    advancedFilter.clearFilter();
  }
}
