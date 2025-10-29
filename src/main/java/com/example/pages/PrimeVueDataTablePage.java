package com.example.pages;

import com.example.components.DataTable;
import com.example.components.VirtualDataTable;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class PrimeVueDataTablePage {

	private WebDriver driver;
	private DataTable table;
	private VirtualDataTable virtualTable;

	@FindBy(css = "[pv_id_188] table") // container div with p-dropdown
	private WebElement tableContainer;

	@FindBy(css = "[data-pc-extend='virtualscroller']")
	private WebElement virtualTableContainer;

	public PrimeVueDataTablePage(WebDriver driver) {

		this.driver = driver;
		PageFactory.initElements(driver, this);
		table = new DataTable(tableContainer, driver);
		virtualTable = new VirtualDataTable(tableContainer, virtualTableContainer, driver, 20);
	}

	public DataTable getTable() {

		return table;
	}

	public VirtualDataTable getVirtualTable() {

		return virtualTable;
	}
}
