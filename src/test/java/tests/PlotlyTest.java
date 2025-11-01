package tests;

import com.example.components.SunburstChart;
import com.example.components.SunburstSlice;
import java.time.Duration;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class PlotlyTest {

  private WebDriver driver;

  @BeforeClass
  public void setup() {

    driver = new ChromeDriver();
    driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
  }

  @Test
  public void basicSunburstTest() {

    driver.get("https://plotly.com/javascript/sunburst-charts/");

    WebElement container = driver.findElement(By.cssSelector("#myDiv_1 .sunburstlayer"));
    
    SunburstChart sunburst = new SunburstChart(container, driver);
    List<SunburstSlice> slices = sunburst.getSlices();

    for (SunburstSlice slice : slices) {
      System.out.println(
          slice); // e.g., SunburstSlice{text='Seth', color=java.awt.Color[r=31,g=119,b=180]}
    }
  }

  //  @AfterClass
  public void tearDown() {

    if (driver != null) {
      driver.quit();
    }
  }
}
