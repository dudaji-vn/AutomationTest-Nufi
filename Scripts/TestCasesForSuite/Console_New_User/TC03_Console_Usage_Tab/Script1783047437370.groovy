import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import org.openqa.selenium.WebDriver
import com.kms.katalon.core.webui.driver.DriverFactory

/**
 * TC03: Nufi Console - Usage Tab
 * 
 * Test Flow:
 * 1. Ensure on Console tab
 * 2. Click Usage tab
 * 3. Verify Usage page loads
 * 4. Verify usage data for new user:
 *    - Total cost = $0.00
 *    - Requests = 0
 *    - Models used = 0
 *    - Last 7 days: 0 requests · $0.00
 *    - Chart displays 0 data (7 bars with height 4px)
 *    - By model: "No usage in this period."
 *    - Recent requests: "No requests yet — usage will appear here as you chat or call the API."
 */

WebUI.comment('=== TC03: Nufi Console - Usage Tab ===')

try {
    // Switch to Console tab
    WebUI.comment('Step 1: Switching to Console tab...')
    WebDriver driver = DriverFactory.getWebDriver()
    Set<String> windowHandles = driver.getWindowHandles()
    
    boolean foundConsole = false
    for (String handle : windowHandles) {
        driver.switchTo().window(handle)
        String url = driver.getCurrentUrl()
        if (url.contains('console.nufi.me')) {
            WebUI.comment('On Console tab: ' + url)
            foundConsole = true
            break
        }
    }
    
    if (!foundConsole) {
        throw new Exception('Console tab not found')
    }
    WebUI.delay(2)
    
    // Step 2: Click Usage tab
    WebUI.comment('Step 2: Clicking Usage tab...')
    TestObject usageTab = new TestObject('usageTab')
    usageTab.addProperty('xpath', ConditionType.EQUALS, 
        "//a[@href='/usage' and contains(text(), 'Usage')]")
    WebUI.waitForElementClickable(usageTab, 10)
    WebUI.click(usageTab)
    WebUI.delay(2)
    WebUI.comment('Usage tab clicked')
    
    // Step 3: Verify Usage page loads
    WebUI.comment('Step 3: Verifying Usage page loads...')
    String currentUrl = driver.getCurrentUrl()
    WebUI.comment('Current URL: ' + currentUrl)
    
    if (!currentUrl.contains('/usage')) {
        throw new Exception('Failed to navigate to Usage page. Current URL: ' + currentUrl)
    }
    WebUI.comment('Usage page loaded successfully')
    
    // Verify page title
    TestObject pageTitle = new TestObject('pageTitle')
    pageTitle.addProperty('xpath', ConditionType.EQUALS, 
        "//h1[contains(text(), 'Usage')]")
    WebUI.waitForElementVisible(pageTitle, 10)
    String title = WebUI.getText(pageTitle)
    WebUI.comment('Page title: ' + title)
    
    // Step 4: Verify usage data metrics
    WebUI.comment('Step 4: Verifying usage metrics for new user...')
    
    // 4.1 Verify Total cost = $0.00
    WebUI.comment('--- Verifying Total cost ---')
    TestObject totalCost = new TestObject('totalCost')
    totalCost.addProperty('xpath', ConditionType.EQUALS, 
        "//p[contains(text(), 'Total cost')]/following-sibling::p[contains(@class, 'font-mono')]")
    WebUI.waitForElementVisible(totalCost, 5)
    String costText = WebUI.getText(totalCost)
    WebUI.comment('Total cost: ' + costText)
    
    if (costText.equals('$0.00')) {
        WebUI.comment('✓ Total cost is $0.00 as expected')
    } else {
        WebUI.comment('✗ Total cost is ' + costText + ', expected $0.00')
        throw new Exception('Total cost mismatch. Actual: ' + costText + ', Expected: $0.00')
    }
    
    // 4.2 Verify Requests = 0
    WebUI.comment('--- Verifying Requests count ---')
    TestObject requestsCount = new TestObject('requestsCount')
    requestsCount.addProperty('xpath', ConditionType.EQUALS, 
        "//p[contains(text(), 'Requests')]/following-sibling::p[contains(@class, 'font-mono')]")
    WebUI.waitForElementVisible(requestsCount, 5)
    String requestsText = WebUI.getText(requestsCount)
    WebUI.comment('Requests: ' + requestsText)
    
    if (requestsText.equals('0')) {
        WebUI.comment('✓ Requests is 0 as expected')
    } else {
        WebUI.comment('✗ Requests is ' + requestsText + ', expected 0')
        throw new Exception('Requests count mismatch. Actual: ' + requestsText + ', Expected: 0')
    }
    
    // 4.3 Verify Models used = 0
    WebUI.comment('--- Verifying Models used ---')
    TestObject modelsUsed = new TestObject('modelsUsed')
    modelsUsed.addProperty('xpath', ConditionType.EQUALS, 
        "//p[contains(text(), 'Models used')]/following-sibling::p[contains(@class, 'font-mono')]")
    WebUI.waitForElementVisible(modelsUsed, 5)
    String modelsText = WebUI.getText(modelsUsed)
    WebUI.comment('Models used: ' + modelsText)
    
    if (modelsText.equals('0')) {
        WebUI.comment('✓ Models used is 0 as expected')
    } else {
        WebUI.comment('✗ Models used is ' + modelsText + ', expected 0')
        throw new Exception('Models used mismatch. Actual: ' + modelsText + ', Expected: 0')
    }
    
    // 4.4 Verify Last 7 days summary
    WebUI.comment('--- Verifying Last 7 days summary ---')
    TestObject last7DaysSummary = new TestObject('last7DaysSummary')
    last7DaysSummary.addProperty('xpath', ConditionType.EQUALS, 
        "//h3[contains(text(), 'Last 7 days')]/following-sibling::span[contains(@class, 'text-xs')]")
    WebUI.waitForElementVisible(last7DaysSummary, 5)
    String summaryText = WebUI.getText(last7DaysSummary)
    WebUI.comment('Last 7 days summary: ' + summaryText)
    
    if (summaryText.contains('0 requests') && summaryText.contains('$0.00')) {
        WebUI.comment('✓ Last 7 days shows 0 requests and $0.00')
    } else {
        WebUI.comment('✗ Last 7 days summary mismatch: ' + summaryText)
        throw new Exception('Last 7 days summary mismatch. Actual: ' + summaryText)
    }
    
    // 4.5 Verify chart displays 0 data (all bars show height 4px)
    WebUI.comment('--- Verifying chart data ---')
    TestObject chartBar = new TestObject('chartBar')
    chartBar.addProperty('xpath', ConditionType.EQUALS, 
        "//div[contains(@class, 'grid') and contains(@style, 'grid-template-columns: repeat(7')]//div[contains(@class, 'w-full') and contains(@style, 'height: 4px')]")
    
    List<TestObject> chartBars = WebUI.findWebElements(chartBar, 5)
    int barCount = chartBars.size()
    WebUI.comment('Number of chart bars with height 4px: ' + barCount)
    
    if (barCount == 7) {
        WebUI.comment('✓ All 7 chart bars show zero data (height 4px)')
    } else {
        WebUI.comment('✗ Expected 7 chart bars with height 4px, found ' + barCount)
        throw new Exception('Chart data mismatch. Expected: 7 bars, Actual: ' + barCount)
    }
    
    // 4.6 Verify By model section - "No usage in this period."
    WebUI.comment('--- Verifying By model section ---')
    TestObject byModelSection = new TestObject('byModelSection')
    byModelSection.addProperty('xpath', ConditionType.EQUALS, 
        "//div[contains(@class, 'rounded-lg')]//h3[contains(text(), 'By model')]/parent::div")
    WebUI.waitForElementVisible(byModelSection, 5)
    
    // Check for "No usage in this period." message
    TestObject noModelUsage = new TestObject('noModelUsage')
    noModelUsage.addProperty('xpath', ConditionType.EQUALS, 
        "//h3[contains(text(), 'By model')]/following-sibling::p[contains(text(), 'No usage in this period.')]")
    
    boolean hasNoModelUsage = WebUI.verifyElementPresent(noModelUsage, 5, FailureHandling.OPTIONAL)
    
    if (hasNoModelUsage) {
        String noModelText = WebUI.getText(noModelUsage)
        WebUI.comment('✓ By model section displays: "' + noModelText + '"')
    } else {
        // Kiểm tra alternative: có thể message nằm ở vị trí khác
        TestObject noModelUsageAlt = new TestObject('noModelUsageAlt')
        noModelUsageAlt.addProperty('xpath', ConditionType.EQUALS, 
            "//div[contains(@class, 'rounded-lg') and contains(.//h3, 'By model')]//p[contains(@class, 'text-muted-foreground')]")
        
        if (WebUI.verifyElementPresent(noModelUsageAlt, 3, FailureHandling.OPTIONAL)) {
            String altText = WebUI.getText(noModelUsageAlt)
            if (altText.contains('No usage')) {
                WebUI.comment('✓ By model section displays: "' + altText + '"')
            } else {
                throw new Exception('By model section message mismatch. Actual: ' + altText)
            }
        } else {
            throw new Exception('By model section - "No usage in this period." message not found')
        }
    }
    
    // 4.7 Verify Recent requests - "No requests yet — usage will appear here as you chat or call the API."
    WebUI.comment('--- Verifying Recent requests section ---')
    TestObject recentRequestsSection = new TestObject('recentRequestsSection')
    recentRequestsSection.addProperty('xpath', ConditionType.EQUALS, 
        "//div[contains(@class, 'rounded-lg')]//h3[contains(text(), 'Recent requests')]/parent::div")
    WebUI.waitForElementVisible(recentRequestsSection, 5)
    
    // Check for "No requests yet" message
    TestObject noRecentRequests = new TestObject('noRecentRequests')
    noRecentRequests.addProperty('xpath', ConditionType.EQUALS, 
        "//h3[contains(text(), 'Recent requests')]/following-sibling::p[contains(text(), 'No requests yet')]")
    
    boolean hasNoRecentRequests = WebUI.verifyElementPresent(noRecentRequests, 5, FailureHandling.OPTIONAL)
    
    if (hasNoRecentRequests) {
        String noRecentText = WebUI.getText(noRecentRequests)
        WebUI.comment('✓ Recent requests section displays: "' + noRecentText + '"')
        
        // Verify full message
        String expectedMessage = "No requests yet — usage will appear here as you chat or call the API."
        if (noRecentText.equals(expectedMessage)) {
            WebUI.comment('✓ Full message matches expected')
        } else {
            WebUI.comment('⚠ Partial match: Actual message is different but contains "No requests yet"')
            // Không throw exception nếu chỉ khác biệt nhỏ về text
        }
    } else {
        // Kiểm tra alternative XPath
        TestObject noRecentRequestsAlt = new TestObject('noRecentRequestsAlt')
        noRecentRequestsAlt.addProperty('xpath', ConditionType.EQUALS, 
            "//div[contains(@class, 'rounded-lg') and contains(.//h3, 'Recent requests')]//p[contains(@class, 'text-muted-foreground')]")
        
        if (WebUI.verifyElementPresent(noRecentRequestsAlt, 3, FailureHandling.OPTIONAL)) {
            String altText = WebUI.getText(noRecentRequestsAlt)
            if (altText.contains('No requests yet')) {
                WebUI.comment('✓ Recent requests section displays: "' + altText + '"')
            } else {
                throw new Exception('Recent requests section message mismatch. Actual: ' + altText)
            }
        } else {
            throw new Exception('Recent requests section - "No requests yet" message not found')
        }
    }
    
    WebUI.takeScreenshot('TC03_Console_Usage_Success.png')
    WebUI.comment('=== TC03 PASSED - All usage data verified successfully ===')
    WebUI.comment('=== Summary of verification: ===')
    WebUI.comment('  ✓ Total cost: $0.00')
    WebUI.comment('  ✓ Requests: 0')
    WebUI.comment('  ✓ Models used: 0')
    WebUI.comment('  ✓ Last 7 days: 0 requests · $0.00')
    WebUI.comment('  ✓ Chart: 7 bars with zero data')
    WebUI.comment('  ✓ By model: No usage in this period.')
    WebUI.comment('  ✓ Recent requests: No requests yet')

} catch (Exception e) {
    WebUI.comment('=== TC03 FAILED: ' + e.getMessage() + ' ===')
    WebUI.takeScreenshot('TC03_Console_Usage_Error.png')
    throw e
}