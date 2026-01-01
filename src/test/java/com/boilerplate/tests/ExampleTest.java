package com.boilerplate.tests;

import com.boilerplate.pages.ExamplePage;
import com.boilerplate.pages.MoreInfoPage;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class ExampleTest extends BaseTest {

    @Test
    public void testNavigationToMoreInfo() {
        ExamplePage examplePage = new ExamplePage(page);
        examplePage.navigate();

        // Assert Home Page
        assertThat(examplePage.getHeading()).contains("Example Domain");

        // Navigate to second page
        examplePage.clickMoreInformation();

        // Verify Second Page
        MoreInfoPage moreInfoPage = new MoreInfoPage(page);
        System.out.println("Second Page Heading: " + moreInfoPage.getHeading());

        // IANA page explicitly says "Domains" in title or heading usually
        assertThat(moreInfoPage.getHeading()).contains("Domains");
    }

    @Test
    public void testMoreInfoContent() {
        ExamplePage examplePage = new ExamplePage(page);
        examplePage.navigate();
        examplePage.clickMoreInformation();

        MoreInfoPage moreInfoPage = new MoreInfoPage(page);

        // Verify some content on the IANA page
        // The page usually contains "Root Zone Management" or similar text in
        // paragraphs
        assertThat(moreInfoPage.getParagraphText()).isNotBlank();

        // Use a known string from IANA reserved domains page
        assertThat(moreInfoPage.getParagraphText()).contains("documentation purposes");
    }
}
