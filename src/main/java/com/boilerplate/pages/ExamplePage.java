package com.boilerplate.pages;

import com.microsoft.playwright.Page;

public class ExamplePage {
    private final Page page;

    public ExamplePage(Page page) {
        this.page = page;
    }

    public void navigate() {
        page.navigate("https://example.com");
    }

    public String getHeading() {
        return page.locator("h1").textContent();
    }

    public String getParagraphText() {
        return page.locator("p").first().textContent();
    }

    public void clickMoreInformation() {
        // Simple locator since it's the only link on the page
        page.locator("a").click();
    }
}
