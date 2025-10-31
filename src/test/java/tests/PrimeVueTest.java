package tests;

import com.example.components.DataTable;
import com.example.components.VirtualDataTable;
import com.example.enums.City;
import com.example.enums.Ingredient;
import com.example.pages.PrimeVueDataTablePage;
import com.example.pages.PrimeVueDropdownPage;
import com.example.pages.PrimeVueSelectPage;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PrimeVueTest {

	private WebDriver driver;
	private PrimeVueSelectPage primeVueSelectPage;
	private PrimeVueDropdownPage primeVueDropdownPage;
	private PrimeVueDataTablePage primeVueDataTablePage;

	@BeforeClass
	public void setup() {

		driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
	}

	@Test
	public void shouldSelectOnion() {

		driver.get("https://primevue.org/radiobutton/");
		primeVueSelectPage = new PrimeVueSelectPage(driver);

		primeVueSelectPage.selectIngredient(Ingredient.MUSHROOM);

		Assert.assertEquals(primeVueSelectPage.getSelectedIngredient(), Ingredient.MUSHROOM);
	}

	@Test
	public void shouldSelectCity() {

		driver.get("https://v3.primevue.org/dropdown/");
		primeVueDropdownPage = new PrimeVueDropdownPage(driver);

		primeVueDropdownPage.selectCity(City.PARIS);

		Assert.assertEquals(primeVueDropdownPage.getSelectedCity(), City.PARIS.getDisplayName());
	}

	@Test
	public void tableTest() {

		driver.get("https://v3.primevue.org/datatable/#multiple_sort");
		primeVueDataTablePage = new PrimeVueDataTablePage(driver);

		DataTable table = primeVueDataTablePage.getTable();

		int nameColIndex = table.getColumnIndex("Name"); // 1
		boolean isNameSorted = table.isColumnSorted("Name"); // true
		String sortOrder = table.getColumnSortOrder("Name"); // "descending"

		WebElement row = table.getRowByColumnValue("Code", "h456wer53");
		String category = table.getCellText(0, "Category"); // "Accessories"
	}

	@Test
	public void virtualTableTest() {

		driver.get("https://v3.primevue.org/datatable/#virtualscroll");
		primeVueDataTablePage = new PrimeVueDataTablePage(driver);

		VirtualDataTable table = primeVueDataTablePage.getVirtualTable();

		int nameColIndex = table.getColumnIndex("Vin");
		boolean isNameSorted = table.isColumnSorted("Brand");
		String sortOrder = table.getColumnSortOrder("Brand");

		//		WebElement row = table.getRowByColumnValueInVirtualScroll("Id", "30", 39);
		table.scrollToRow(50);
		String category = table.getCellText(2, "Color");
	}

	@Test
	public void dummyTest() throws InterruptedException {

		JavascriptExecutor js = (JavascriptExecutor) driver;

		driver.get("https://v3.primevue.org/datatable/#virtualscroll");
		List<WebElement> rowsInitial = driver.findElements(By.cssSelector("section:nth-child(17) tbody tr"));
		var lastRowInitial = rowsInitial.get(rowsInitial.size() - 1).getAttribute("data-p-index");

		WebElement scrollableElement = driver.findElement(By.cssSelector("section:nth-child(17) .p-virtualscroller"));
		js.executeScript("arguments[0].scrollTop += 230;", scrollableElement);

		List<WebElement> rowsAfterScrolling = driver.findElements(By.cssSelector("section:nth-child(17) tbody tr"));
		var lastRowAfterScrolling = rowsAfterScrolling.get(rowsAfterScrolling.size() - 1).getAttribute("data-p-index");
		Thread.sleep(2000);
		var rowIndex = rowsAfterScrolling.get(20).getDomAttribute("data-p-index");
		var rowId = rowsAfterScrolling.get(20).findElements(By.cssSelector("td")).get(0).getText();
	}

	@Test
	public void findRowTestBlueprint() {

		JavascriptExecutor js = (JavascriptExecutor) driver;

		driver.get("https://v3.primevue.org/datatable/#virtualscroll");
		// initial position (only 19 row are in the DOM)
		// find itemHeight (for table row)
		WebElement firstElement = driver.findElement(
				By.cssSelector("section:nth-child(17) tbody tr[0]")); // fix finding first element if its not correct
		// find top position of first item in table (either by getting it and read top prop using js
		int itemHeight = (int) js.executeScript("find top of first element");
		// or get it by using table wrapper component and subtracting table header height
		// scroll down for amount of 7 rows (it will load first batch of rows, 25 items)
		js.executeScript("scrollBy(itemHeight * 7)");
		// create a list of row elements in DOM
		// and map them in list of rows
		// something like that List<WebElement> rows = driver.findElements(...)
		List<WebElement> rows = driver.findElements(By.cssSelector("section:nth-child(17) tbody tr"));
		// something like that List<Row> rowsMapped = mapRows(rows)
		List<Row> rowsMapped = mapRows(rows);
		// filter rowsMapped using some method that should be created
		// if value founded return Row
		// if not load next batch of rows by scrolling by amount of 25 rows height
		// do it filtering again
		// if at the end nothing found throws exception
		//		Row foundedRow = filterRows(rowsMapped);

		// use jsexecutor to set top property of founded element which index is in Row object (it is like data-p-index="index")
		js.executeScript("scrollTop ...");
	}

	@Test
	public void findRowTest() {

		JavascriptExecutor js = (JavascriptExecutor) driver;

		driver.get("https://v3.primevue.org/datatable/#virtualscroll");

		// Wait for table to load
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		WebElement tableWrapper = wait.until(
				ExpectedConditions.presenceOfElementLocated(
						By.cssSelector("section:nth-child(17) .p-datatable-wrapper"))
		);

		// Get row height from first visible row
		WebElement firstRow = wait.until(
				ExpectedConditions.presenceOfElementLocated(By.cssSelector("section:nth-child(17) tbody tr"))
		);
		int itemHeight = firstRow.getSize().getHeight();

		String targetId = "100"; // 👈 SET YOUR TARGET ID HERE

		Set<Integer> seenIndices = new HashSet<>();
		int scrollStep = itemHeight * 25; // scroll by ~25 rows
		int initialScroll = itemHeight * 7;
		int maxAttempts = 50; // safety limit
		int attempts = 0;

		WebElement scrollableElement = driver.findElement(By.cssSelector("section:nth-child(17) .p-virtualscroller"));
		js.executeScript("arguments[0].scrollTop += arguments[1];", scrollableElement, initialScroll);

		while (attempts < maxAttempts) {
			List<WebElement> rows = driver.findElements(By.cssSelector("section:nth-child(17) tbody tr"));

			// Map rows
			List<Row> mappedRows = mapRows(rows);
			Row found = filterRows(mappedRows, targetId);
			if (found != null) {
				System.out.println("Found row: index=" + found.index + ", id=" + found.text);

				// Optional: scroll the found row into view
				WebElement targetRow = driver.findElement(
						By.cssSelector("section:nth-child(17) tbody tr[data-p-index='" + found.index + "']")
				);
				js.executeScript("arguments[0].scrollIntoView({block: 'center'});", targetRow);

				return; // success
			}

			// Track seen indices to detect end of table
			int currentMaxIndex = mappedRows.stream().mapToInt(r -> r.index).max().orElse(-1);
			if (seenIndices.contains(currentMaxIndex) && !seenIndices.isEmpty()) {
				// No new rows loaded → reached end
				break;
			}
			seenIndices.add(currentMaxIndex);

			// Scroll down
			js.executeScript("arguments[0].scrollTop += arguments[1];", scrollableElement, scrollStep);

			// Wait for new rows to render (wait until row count changes or max index increases)
			try {
				wait.until(d -> {
					List<WebElement> newRows = d.findElements(By.cssSelector("section:nth-child(17) tbody tr"));
					return newRows.size() > rows.size() ||
							newRows.stream()
									.mapToInt(r -> Integer.parseInt(r.getAttribute("data-p-index")))
									.max()
									.orElse(-1) > currentMaxIndex;
				});
			} catch (TimeoutException e) {
				// No new content — assume end of table
				break;
			}

			attempts++;
		}

		throw new RuntimeException("Row with Id '" + targetId + "' not found in virtual table.");
	}

	class Row {

		int index;
		String text; // text from "Id" column

		Row(int index, String text) {

			this.index = index;
			this.text = text;
		}
	}

	private List<Row> mapRows(List<WebElement> rows) {

		List<Row> result = new ArrayList<>();
		for (WebElement row : rows) {
			try {
				String indexAttr = row.getAttribute("data-p-index");
				if (indexAttr == null || indexAttr.isEmpty())
					continue;

				int index = Integer.parseInt(indexAttr);

				// In the PrimeVue demo, the first column is "Id"
				// So we get the first <td> or cell
				List<WebElement> cells = row.findElements(By.tagName("td"));
				if (!cells.isEmpty()) {
					String idText = cells.get(0).getText().trim();
					result.add(new Row(index, idText));
				}
			} catch (Exception e) {
				// Skip malformed rows
			}
		}
		return result;
	}

	private Row filterRows(List<Row> rowList, String targetId) {

		for (Row row : rowList) {
			if (targetId.equals(row.text)) {
				return row;
			}
		}
		return null;
	}

	//  @AfterClass
	public void tearDown() {

		if (driver != null) {
			driver.quit();
		}
	}
}
