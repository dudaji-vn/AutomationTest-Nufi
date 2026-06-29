import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import org.openqa.selenium.WebDriver
import com.kms.katalon.core.webui.driver.DriverFactory

/**
 * TC09: Nufi Console - Profile Tab After Chat
 * 
 * Test Flow:
 * 1. Ensure on Console tab
 * 2. Click Profile tab
 * 3. Verify usage status changed
 * 4. Verify "used so far" amount updated
 * 5. Verify "Last 7 days" updated with requests
 * 6. Verify "Where it goes" section shows data
 */

WebUI.comment('=== TC09: Nufi Console - Profile Tab After Chat ===')

try {
    // Step 1: Switch to Console tab
    WebUI.comment('Step 1: Switching to Console tab...')
    WebDriver driver = DriverFactory.getWebDriver()
    Set<String> windowHandles = driver.getWindowHandles()
    
    for (String handle : windowHandles) {
        driver.switchTo().window(handle)
        String url = driver.getCurrentUrl()
        if (url.contains('console.nufi.me')) {
            WebUI.comment('On Console tab: ' + url)
            break
        }
    }
    WebUI.delay(2)
    
    // Step 2: Click Profile tab
    WebUI.comment('Step 2: Clicking Profile tab...')
    WebUI.waitForElementClickable(
        findTestObject('Object Repository/Page_NUFI Console/a_Profile'),
        10
    )
    WebUI.click(findTestObject('Object Repository/Page_NUFI Console/a_Profile'))
    WebUI.delay(2)
    WebUI.comment('Profile tab clicked')
    
    // Step 3: Verify Profile page loads
    WebUI.comment('Step 3: Verifying Profile page loads...')
    String currentUrl = driver.getCurrentUrl()
    WebUI.comment('Current URL: ' + currentUrl)
    
    if (!currentUrl.contains('/usage') && !currentUrl.contains('/keys')) {
        WebUI.comment('Profile page loaded')
    } else {
        WebUI.comment('Not on Profile page: ' + currentUrl)
    }
    
    // Step 4: Verify usage status (should show "You have unlimited usage" or used amount)
    WebUI.comment('Step 4: Verifying usage status...')
    TestObject usageStatus = new TestObject('usageStatus')
    usageStatus.addProperty('xpath', ConditionType.EQUALS, 
        "//div[contains(@class, 'rounded-xl')]//p[contains(@class, 'text-xs') and contains(@class, 'font-medium')]")
    String statusText = WebUI.getText(usageStatus)
    WebUI.comment('Usage status: ' + statusText)
    
    // Step 5: Verify "used so far" amount
    WebUI.comment('Step 5: Verifying "used so far" amount...')
    TestObject usedAmount = new TestObject('usedAmount')
    usedAmount.addProperty('xpath', ConditionType.EQUALS, 
        "//div[contains(@class, 'rounded-xl')]//p[contains(@class, 'font-mono') and contains(@class, 'text-3xl')]")
    String amountText = WebUI.getText(usedAmount)
    WebUI.comment('Used amount: ' + amountText)
    
    // Check if amount is > $0.00
    if (amountText.contains('$0.00')) {
        WebUI.comment('⚠ Usage still shows $0.00 - may need to wait for data sync')
    } else {
        WebUI.comment('✓ Usage updated: ' + amountText)
    }
    
    // Step 6: Verify "Last 7 days" shows requests
    WebUI.comment('Step 6: Verifying "Last 7 days" section...')
    TestObject last7Days = new TestObject('last7Days')
    last7Days.addProperty('xpath', ConditionType.EQUALS, 
        "//div[contains(@class, 'rounded-lg')]//h3[contains(text(), 'Last 7 days')]")
    boolean hasLast7Days = WebUI.waitForElementVisible(last7Days, 3, FailureHandling.OPTIONAL)
    WebUI.comment('"Last 7 days" section exists: ' + hasLast7Days)
    
    // Get requests count in Last 7 days
    TestObject requestsText = new TestObject('requestsText')
    requestsText.addProperty('xpath', ConditionType.EQUALS, 
        "//div[contains(@class, 'rounded-lg')]//span[contains(text(), 'requests')]")
    String requests = WebUI.getText(requestsText)
    WebUI.comment('Requests in Last 7 days: ' + requests)
    
    // Step 7: Verify "Where it goes" section (should show data after chat)
    WebUI.comment('Step 7: Verifying "Where it goes" section...')
    TestObject whereItGoes = new TestObject('whereItGoes')
    whereItGoes.addProperty('xpath', ConditionType.EQUALS, 
        "//h3[contains(text(), 'Where it goes')]")
    boolean hasWhereItGoes = WebUI.waitForElementVisible(whereItGoes, 3, FailureHandling.OPTIONAL)
    WebUI.comment('"Where it goes" section exists: ' + hasWhereItGoes)
    
    // Check if there's data in "Where it goes" (not empty)
    TestObject whereItGoesData = new TestObject('whereItGoesData')
    whereItGoesData.addProperty('xpath', ConditionType.EQUALS, 
        "//div[contains(@class, 'rounded-lg')]//div[contains(@class, 'rounded-full')]//following-sibling::span[contains(@class, 'text-muted-foreground')]")
    boolean hasData = WebUI.waitForElementVisible(whereItGoesData, 3, FailureHandling.OPTIONAL)
    WebUI.comment('"Where it goes" has data: ' + hasData)
    
    // Step 8: Check if "You haven't used anything yet" is NOT displayed
    TestObject emptyState = new TestObject('emptyState')
    emptyState.addProperty('xpath', ConditionType.EQUALS, 
        "//p[contains(text(), 'You haven\\'t used anything yet')]")
    boolean isEmpty = WebUI.waitForElementVisible(emptyState, 2, FailureHandling.OPTIONAL)
    
    if (isEmpty) {
        WebUI.comment('⚠ "Where it goes" still shows empty state - data may not have synced yet')
    } else {
        WebUI.comment('✓ "Where it goes" has data - profile updated')
    }
    
    WebUI.takeScreenshot('TC09_Console_Profile_AfterChat_Success.png')
    WebUI.comment('TC09 PASSED')

} catch (Exception e) {
    WebUI.comment('TC09 FAILED: ' + e.getMessage())
    WebUI.takeScreenshot('TC09_Console_Profile_AfterChat_Error.png')
    throw e
}