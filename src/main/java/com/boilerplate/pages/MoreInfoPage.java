package com.boilerplate.pages;

import com.microsoft.playwright.Page;

public class MoreInfoPage {
    private final Page page;

    public MoreInfoPage(Page page) {
        this.page = page;
    }

    public String getHeading() {
        return page.locator("h1").textContent();
    }

    public String getParagraphText() {
        return page.locator("p").first().textContent();
    }
}
