package com.serenitydojo.playwright;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;


import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class PlaywrightFromsTest {
    private static Playwright playwright;
    private static Browser browser;
    private static BrowserContext browserContext;

    Page page;

    @BeforeAll
    public static void setUpBrowser() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions()
                        .setHeadless(false)
                        .setArgs(Arrays.asList("--no-sandbox","--disable-extensions","--disable-gpu", "--start-maximized"))
                        .setSlowMo(500)
        );
        playwright.selectors().setTestIdAttribute("data-test");
    }

    @BeforeEach
    public void setUp() {
        browserContext = browser.newContext(
                new Browser.NewContextOptions().setViewportSize(null));
        page = browserContext.newPage();
    }

    @AfterAll
    public static void tearDown() {
        browser.close();
        playwright.close();
    }
    @DisplayName("Playwright forms test")
    @Test
    void formsTest() throws URISyntaxException {
        page.navigate("https://practicesoftwaretesting.com/contact");
        var firstNameField = page.getByLabel("First name");
        var lastNameField = page.getByLabel("Last name");
        var emailField = page.getByLabel("Email");
        var messageField = page.getByLabel("Message");
        var subjectDropDown = page.getByLabel("Subject");
        var fileUpload = page.getByLabel("Attachment");

        firstNameField.fill("Kau");
        lastNameField.fill("Kau");
        emailField.fill("kau@gmail.com");
        messageField.fill("Hello world");
        subjectDropDown.selectOption("Warranty");

        Path fileToUpload = Paths.get(ClassLoader.getSystemResource("data/sample.txt").toURI());
        page.setInputFiles("#attachment", fileToUpload);

        assertThat(firstNameField).hasValue("Kau");
        assertThat(lastNameField).hasValue("Kau");
        assertThat(emailField).hasValue("kau@gmail.com");
        assertThat(messageField).hasValue("Hello world");
        assertThat(subjectDropDown).hasValue("warranty");

        String uploadFile = fileUpload.inputValue();
        org.assertj.core.api.Assertions.assertThat(uploadFile).endsWith("sample.txt");
    }
    @DisplayName("Test for mandatory fields")
    @ParameterizedTest
    @ValueSource(strings = {"First name", "Last name", "Email", "Message"})
    void mandatoryFields(String fieldName){
        page.navigate("https://practicesoftwaretesting.com/contact");
        var firstNameField = page.getByLabel("First name");
        var lastNameField = page.getByLabel("Last name");
        var emailField = page.getByLabel("Email");
        var messageField = page.getByLabel("Message");
        var sendButton = page.getByTestId("contact-submit");


        //Fill in field value
        firstNameField.fill("Kau");
        lastNameField.fill("Kau");
        emailField.fill("Kau");
        messageField.fill("Kau");

        //clear one of the fields
        page.getByLabel(fieldName).clear();
        sendButton.click();

        //check error message foer the field
        var errorMessage = page.getByRole(AriaRole.ALERT).getByText(fieldName + " is required");
        assertThat(errorMessage).isVisible();
    }
}
