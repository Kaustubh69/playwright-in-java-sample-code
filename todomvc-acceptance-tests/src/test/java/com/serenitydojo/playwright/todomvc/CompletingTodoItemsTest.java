package com.serenitydojo.playwright.todomvc;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.junit.UsePlaywright;
import com.serenitydojo.playwright.fixtures.ChromeHeadlessOptions;
import com.serenitydojo.playwright.todomvc.pageobjects.TodoMvcAppPage;
import io.qameta.allure.Feature;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

@DisplayName("Completing todo items to the list")
@UsePlaywright(ChromeHeadlessOptions.class)
@Feature("Completing todo items to the list")
class CompletingTodoItemsTest {

    TodoMvcAppPage todoMvcApp;

    @BeforeEach
    void openApp(Page page) {
        todoMvcApp = new TodoMvcAppPage(page);

        todoMvcApp.open();
    }

    @DisplayName("Completed items should be marked as completed")
    @Test
    void completedItemsShouldBeMarkedAsCompleted() {
        todoMvcApp.addItems("Walk the dog", "Buy some milk");
        todoMvcApp.completeItem("Walk the dog");
        assertThat(todoMvcApp.itemRow("Walk the dog")).hasClass("completed");
        // 1) Add "Feed the cat", "Walk the dog", "Buy some milk"
        // 2) Complete "Feed the cat"
        // 3) Check that "Feed the cat" is shown as completed
    }

    @DisplayName("Completing an item should update the number of items left count")
    @Test
    void shouldUpdateNumberOfItemsLeftCount() {
        todoMvcApp.addItems("Walk the dog", "Buy some milk");
        todoMvcApp.completeItem("Walk the dog");
        Assertions.assertThat(todoMvcApp.todoCount()).isEqualTo("1 item left!");
        // 1) Add "Feed the cat", "Walk the dog", "Buy some milk"
        // 2) Complete "Feed the cat"
        // 3) Verify the count shows "2 items left!"
    }

    @DisplayName("Should be able to clear completed items")
    @Test
    void shouldBeAbleToClearCompletedItems() {
        todoMvcApp.addItems("Walk the dog", "Buy some milk");
        todoMvcApp.completeItems("Walk the dog");
        todoMvcApp.clearCompletedItems();
        Assertions.assertThat(todoMvcApp.todoItemsDisplayed()).containsExactly("Buy some milk");
        // 1) Add "Feed the cat", "Walk the dog", "Buy some milk"
        // 2) Complete "Walk the dog"
        // 3) Clear the completed items
        // 4) Verify that the remaining items are "Feed the cat" and "Buy some milk"
    }
}
