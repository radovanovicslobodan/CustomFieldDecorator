package tests;

import com.example.components.datepicker.PrimeVueDatePicker;
import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class DatePickerTest {

  private WebDriver driver;

  @BeforeMethod
  public void setup() {

    driver = new ChromeDriver();
    driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
  }

  @Test
  public void testDatePicker() {
    driver.get("https://primevue.org/datepicker/");
    var container = driver.findElement(By.cssSelector("section.py-6:nth-child(3)"));
    var datePickerContainer = container.findElement(By.cssSelector("input"));
    datePickerContainer.click();
    var id = datePickerContainer.getDomAttribute("aria-controls");
    var datePickerPanel = driver.findElement(By.id(id));

    PrimeVueDatePicker picker = new PrimeVueDatePicker(datePickerPanel);
//    var currentYear = picker.getCurrentYear();
//    picker.navigateToMonthYear("January", "2026");
//    picker.selectDay("19");
    picker.selectDate("2024-02-14");
    picker.close();
  }
}
