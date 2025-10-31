package com.example.components;

import com.example.annotations.FindInside;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class VirtualDataTable extends DataTable {

	private final WebDriver driver;
	private JavascriptExecutor js;
	private final WebElement tableContainer;
	private Integer cachedItemSize = null;

	@FindInside(css = ".p-virtualscroller")
	private WebElement scrollableContainer; // e.g., div.p-virtualscroller or parent with overflow

	private final int itemSize; // from virtualScrollerOptions.itemSize

	public VirtualDataTable(WebElement tableContainer, WebDriver driver, int itemSize) {

		super(tableContainer, driver);
		this.driver = driver;
		this.js = (JavascriptExecutor) driver;
		this.tableContainer = tableContainer;
		//		this.scrollableContainer = scrollableContainer;
		this.itemSize = itemSize;
	}

	/**
	 * Scrolls to a logical row index and waits for it to appear.
	 */
	public void scrollToRow(int targetIndex) {

		// CONFIG — adjust these if needed
		final int ITEM_HEIGHT = 39;           // ← must match your :itemSize
		final int TOLERANCE = 10;             // PrimeVue default: numToleratedItems
		final int MAX_RETRIES = 5;
		final long WAIT_MS = 300;

		// 1. Locate the scrollable container (PrimeVue virtual scroller body)
		WebElement scrollable = container.findElement(
				By.cssSelector("section:nth-child(17) .p-virtualscroller-content"));

		// 2. Calculate scrollTop to bring target INTO PrimeVue's render buffer
		int safeIndex = Math.max(0, targetIndex - TOLERANCE);
		int scrollTop = safeIndex * ITEM_HEIGHT;

		// 3. Scroll and retry until row is fully in viewport
		for (int i = 0; i < MAX_RETRIES; i++) {
			js.executeScript("arguments[0].scrollTop = arguments[1];", scrollable, scrollTop);

			try {
				Thread.sleep(WAIT_MS);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				throw new RuntimeException(e);
			}

			// Check: is target row rendered AND fully visible?
			if (isTargetRowFullyInViewport(targetIndex)) {
				return; // success
			}

			// Optional: nudge scroll down slightly on retry (helps edge cases)
			scrollTop += ITEM_HEIGHT;
		}

		throw new RuntimeException("Failed to scroll to row " + targetIndex);
	}

	private boolean isTargetRowFullyInViewport(int targetIndex) {

		String script = """
				const row = arguments[0].querySelector('tr[data-p-index="%d"]');
				if (!row) return false;
				
				const container = row.closest('.p-virtualscroller__content');
				if (!container) return false;
				
				const table = container.closest('table');
				const header = table ? table.querySelector('thead') : null;
				const headerHeight = header ? header.offsetHeight : 0;
				
				const containerRect = container.getBoundingClientRect();
				const rowRect = row.getBoundingClientRect();
				
				const topVisible = rowRect.top >= (containerRect.top + headerHeight);
				const bottomVisible = rowRect.bottom <= containerRect.bottom;
				
				return topVisible && bottomVisible;
				""".formatted(targetIndex);

		Object result = js.executeScript(script, container);
		return Boolean.TRUE.equals(result);
	}

	private void safeScrollTo(int index) {

		int itemSize = getItemSize();
		int tolerance = 10; // match PrimeVue's numToleratedItems
		long scrollTop = Math.max(0, (long) (index - tolerance) * itemSize);
		((JavascriptExecutor) driver).executeScript(
				"arguments[0].scrollTop = arguments[1];",
				scrollableContainer,
				scrollTop
		);
	}

	// --- Helper: perform the actual scroll
	private void scrollToIndex(int logicalRowIndex) {

		int itemSize = getItemSize();
		Long containerHeight = (Long) ((JavascriptExecutor) driver)
				.executeScript("return arguments[0].clientHeight;", scrollableContainer);

		// Estimate how many rows fit in viewport
		int visibleRowCount = (int) (containerHeight / itemSize);

		// PrimeVue default: numToleratedItems = 10
		int tolerance = 10;

		// Scroll so target row is near the TOP of the viewport
		// This ensures it's within the "pre-rendered" zone
		long targetScroll = Math.max(0, (long) (logicalRowIndex - tolerance) * itemSize);

		((JavascriptExecutor) driver).executeScript(
				"arguments[0].scrollTop = arguments[1];",
				scrollableContainer,
				targetScroll
		);
	}

	/**
	 * Returns the height (in pixels) of a single row. Caches the value after first measurement for performance.
	 */
	private int getItemSize() {

		if (cachedItemSize == null) {
			// Get first visible row
			WebElement firstRow = tableContainer.findElement(By.cssSelector("tbody tr[role='row']"));
			Long height = (Long) ((JavascriptExecutor) driver)
					.executeScript("return arguments[0].offsetHeight;", firstRow);
			cachedItemSize = height.intValue();
		}
		return cachedItemSize;
	}

	// --- Helper: check if row is fully visible (accounts for sticky header)
	public boolean isRowInViewport(int logicalIndex) {

		return (Boolean) ((JavascriptExecutor) driver).executeScript(
				"const table = arguments[0];" +
						"const container = arguments[1];" +
						"const thead = table.querySelector('thead');" +
						"const row = table.querySelector('tbody tr[data-p-index=\"" + logicalIndex + "\"]');" +
						"if (!row || !thead) return false;" +

						"const rowRect = row.getBoundingClientRect();" +
						"const containerRect = container.getBoundingClientRect();" +
						"const headerHeight = thead.offsetHeight;" +

						"const visibleTop = containerRect.top + headerHeight;" +
						"const visibleBottom = containerRect.bottom;" +

						"return rowRect.top >= visibleTop && rowRect.bottom <= visibleBottom;",
				tableContainer,
				scrollableContainer
		);
	}

	/**
	 * Attempts to find a row by column value across the entire dataset. WARNING: Only works if you can scroll to every
	 * possible row (slow for large datasets).
	 */
	public WebElement getRowByColumnValueInVirtualScroll(
			String columnName, String value,
			int totalRowCount
	) {

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
