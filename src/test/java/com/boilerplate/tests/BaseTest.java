package com.boilerplate.tests;

import com.boilerplate.config.ConfigReader;
import com.microsoft.playwright.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.net.HttpURLConnection;
import java.net.URL;

public class BaseTest {
    protected static Playwright playwright;
    protected static Browser browser;
    protected BrowserContext context;
    protected Page page;

    @BeforeAll
    public static void setUpBeforeAll() {
        // 1. Health Check / Polling
        waitForAppToBeReady();

        // 2. Playwright Factory
        playwright = Playwright.create();
        String browserName = ConfigReader.getBrowser();
        boolean headless = ConfigReader.isHeadless();

        switch (browserName.toLowerCase()) {
            case "firefox":
                browser = playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(headless));
                break;
            case "webkit":
                browser = playwright.webkit().launch(new BrowserType.LaunchOptions().setHeadless(headless));
                break;
            default:
                browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(headless));
                break;
        }
    }

    @AfterAll
    public static void tearDownAfterAll() {
        if (browser != null) {
            browser.close();
        }
        if (playwright != null) {
            playwright.close();
        }
    }

    @BeforeEach
    public void setUp() {
        context = browser.newContext();
        page = context.newPage();
    }

    @AfterEach
    public void tearDown() {
        if (context != null) {
            context.close();
        }
    }

    private static void waitForAppToBeReady() {
        String baseUrl = ConfigReader.getBaseUrl();
        // Skip health check for public URLs or if explicitly disabled
        // But for this boilerplate, we'll try it if it looks like a local or service
        // URL.
        // Or simply always try it but handle timeouts gracefully if it's external?
        // Requirement says: "try to connect to the BASE_URL repeatedly ... ensures the
        // tests wait for the local application"

        System.out.println("Wait Strategy: Checking availability of " + baseUrl);
        int maxRetries = 12; // 12 * 5 seconds = 60 seconds
        int retryDelayMs = 5000;

        for (int i = 0; i < maxRetries; i++) {
            try {
                URL url = new URL(baseUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(3000);
                // Add User-Agent to avoid 403 Forbidden from sites like Wikipedia that block
                // default Java UA
                connection.setRequestProperty("User-Agent",
                        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36");
                connection.connect();

                int code = connection.getResponseCode();
                if (code == 200) {
                    System.out.println("App is ready! Reached " + baseUrl + " with status 200.");
                    return;
                }
                System.out.println("Attempt " + (i + 1) + ": Received status code " + code + ". Retrying in 5s...");
            } catch (Exception e) {
                System.out.println(
                        "Attempt " + (i + 1) + ": Connection failed (" + e.getMessage() + "). Retrying in 5s...");
            }

            try {
                Thread.sleep(retryDelayMs);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Wait interrupted", ie);
            }
        }

        // Decide if we should fail or proceed. Strict requirement says "until it
        // receives a 200 OK".
        // So we should probably throw an exception if we can't connect, otherwise tests
        // will fail fast anyway.
        throw new RuntimeException("App was not ready after 60 seconds at " + baseUrl);
    }
}
