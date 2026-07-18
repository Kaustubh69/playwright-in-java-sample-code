package com.serenitydojo.playwright.todomvc;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.junit.UsePlaywright;
import com.serenitydojo.playwright.fixtures.ChromeHeadlessOptions;
import com.serenitydojo.playwright.todomvc.pageobjects.TodoMvcAppPage;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Adding and deleting todo items to the list")
@Feature("Adding and deleting todo items to the list")
@UsePlaywright(ChromeHeadlessOptions.class)
class AddingAndDeletingTodoItemsTest {
    private Page page;
    TodoMvcAppPage todoMvcApp;

    @BeforeEach
    void openApp(Page page) {
        this.page = page;
        todoMvcApp = new TodoMvcAppPage(page);
        todoMvcApp.open();
    }

    @Story("When the application starts")
    @DisplayName("When the application starts")
    @Nested
    class WhenTheApplicationStarts {
        @DisplayName("The list should be empty")
        @Test
        void the_list_should_initially_be_empty() {
            assertThat(todoMvcApp.todoItemsDisplayed()).isEmpty();
        }

        @DisplayName("The user should be prompted to enter a todo item")
        @Test
        void the_user_should_be_prompted_to_enter_a_value() {
            assertThat(todoMvcApp.todoField()).isVisible();
            assertThat(todoMvcApp.todoField()).hasAttribute("placeholder", "What needs to be done?");

        }
    }

    @Story("When we want to add item to the list")
    @DisplayName("When we want to add item to the list")
    @Nested
    class WhenAddingItems {

        @DisplayName("We can add a single item")
        @Test
        void addingASingleItem() {
            todoMvcApp.addItem("Feed the cat");
            assertThat(todoMvcApp.todoItemsDisplayed()).containsExactly("Feed the cat");

        }

        @DisplayName("We can add multiple items")
        @Test
        void addingSeveralItem() {
            todoMvcApp.addItem("Feed the cat");
            todoMvcApp.addItem("Walk the dog");
            assertThat(todoMvcApp.todoItemsDisplayed()).containsExactly("Feed the cat", "Walk the dog");
        }

        @DisplayName("We can't add an empty item")
        @Test
        void addingAnEmptyItem() {
            todoMvcApp.addItem("Feed the cat");
            todoMvcApp.addItem(" ");
            assertThat(todoMvcApp.todoItemsDisplayed()).containsExactly("Feed the cat");

        }

        @DisplayName("We can add duplicate items")
        @Test
        void addingDuplicateItem() {
            todoMvcApp.addItems("Feed the cat", "Walk the dog", "Feed the cat");
            assertThat(todoMvcApp.todoItemsDisplayed()).containsExactly("Feed the cat", "Walk the dog", "Feed the cat");

        }

        @DisplayName("We can add items with non-English characters")
        @Test
        void addingNonEnglishItems() {
            todoMvcApp.addItems("Feed the cat");
            todoMvcApp.addItem("إطعام القط");
            assertThat(todoMvcApp.todoItemsDisplayed()).containsExactly("Feed the cat", "إطعام القط");

        }
    }

    @Story("When we want to delete item in the list")
    @DisplayName("When we want to delete item in the list")
    @Nested
    class WhenDeletingItems {

        @BeforeEach
        void addItems(){
            todoMvcApp.addItems("Feed the cat", "Walk the dog", "Buy some milk");
        }

        @DisplayName("We can delete an item in the middle of the list")
        @Test
        void deletingAnItemInTheMiddleOfTheList() {
            todoMvcApp.deleteItem("Walk the dog");
            Assertions.assertThat(todoMvcApp.todoItemsDisplayed()).containsExactly("Feed the cat","Buy some milk");

        }

        @DisplayName("We can delete an item at the end of the list")
        @Test
        void deletingAnItemAtTheEndOfTheList() {
            todoMvcApp.deleteItem("Buy some milk");
            Assertions.assertThat(todoMvcApp.todoItemsDisplayed()).containsExactly("Feed the cat","Walk the dog");

        }

        @DisplayName("We can delete an item at the start of the list")
        @Test
        void deletingAnItemAtTheStartOfTheList() {
            todoMvcApp.deleteItem("Feed the cat");
            Assertions.assertThat(todoMvcApp.todoItemsDisplayed()).containsExactly("Walk the dog","Buy some milk");

        }
    }
}
