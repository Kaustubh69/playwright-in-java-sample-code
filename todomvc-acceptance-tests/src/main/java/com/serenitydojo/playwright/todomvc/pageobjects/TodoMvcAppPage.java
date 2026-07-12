package com.serenitydojo.playwright.todomvc.pageobjects;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class TodoMvcAppPage {

    private final Page page;
    private final String baseUrl;
    private final Locator todoItems;
    private final Locator todoField;

    public TodoMvcAppPage(Page page) {
        this.page = page;
        baseUrl = (StringUtils.isEmpty(System.getenv("APP_HOST_URL"))) ? "http://localhost:8080" : System.getenv("APP_HOST_URL");
        todoItems = page.getByTestId("todo-item-label");
        todoField = page.getByTestId("text-input");
    }

    public void open() {
        page.navigate(baseUrl);
    }

    public List <String> todoItemsDisplayed() {
        return todoItems.allTextContents();
    }

    public Locator todoField() {
        return todoField;
    }

    public void addItem(String itemName) {
        todoField.fill(itemName);
        todoField.press("Enter");
    }

    public void addItems(String... todoItems) {
        for (String todoItem : todoItems){
            addItem((todoItem));
        }
    }

    public void deleteItem(String itemName) {
        Locator itemRow = itemRow(itemName);
        Locator delteButoon = itemRow.getByTestId("todo-item-button");
        itemRow.hover();
        delteButoon.click();
    }

    private Locator itemRow(String itemName) {
        return page.getByTestId("todo-item").filter(new Locator.FilterOptions().setHasText(itemName));
    }

}
