import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import org.openqa.selenium.WebDriver
import com.kms.katalon.core.webui.driver.DriverFactory

/**
 * TC05: Nufi Console - Delete User
 * 
 * Test Flow:
 * 1. Switch back to main chat tab
 * 2. Click Account Settings button
 * 3. Click Settings menu item
 * 4. Click Account tab
 * 5. Click Delete Account button
 * 6. Enter email to confirm
 * 7. Click Permanently Delete button
 * 8. Verify redirected to /login
 */

WebUI.comment('=== TC05: Nufi Console - Delete User ===')

try {
    // Step 1: Switch back to main chat tab
    WebUI.comment('Step 1: Switching back to main chat tab...')
    WebDriver driver = DriverFactory.getWebDriver()
    Set<String> windowHandles = driver.getWindowHandles()
    
    for (String handle : windowHandles) {
        driver.switchTo().window(handle)
        String url = driver.getCurrentUrl()
        WebUI.comment('Current URL: ' + url)
        
        if (url.contains(GlobalVariable.Base_URL) && !url.contains('console.nufi.me')) {
            WebUI.comment('Found main chat tab: ' + url)
            break
        }
    }
    WebUI.delay(2)
    
    // Step 2: Click Account Settings button
    WebUI.comment('Step 2: Clicking Account Settings button...')
    WebUI.waitForElementClickable(
        findTestObject('Object Repository/Core Chat/nav/nav_items/button_Account Settings'),
        10
    )
    WebUI.click(findTestObject('Object Repository/Core Chat/nav/nav_items/button_Account Settings'))
    WebUI.delay(1)
    WebUI.comment('Account Settings button clicked')
    
    // Step 3: Click Settings menu item
    WebUI.comment('Step 3: Clicking Settings menu item...')
    WebUI.waitForElementClickable(
        findTestObject('Object Repository/Core Chat/nav/Account_Settings/menuitem_Settings'),
        10
    )
    WebUI.click(findTestObject('Object Repository/Core Chat/nav/Account_Settings/menuitem_Settings'))
    WebUI.delay(2)
    WebUI.comment('Settings menu item clicked')
    
    // Step 4: Click Account tab
    WebUI.comment('Step 4: Clicking Account tab...')
    WebUI.waitForElementClickable(
        findTestObject('Object Repository/Core Chat/nav/Account_Settings/item_Settings/Tab/button_Account'),
        10
    )
    WebUI.click(findTestObject('Object Repository/Core Chat/nav/Account_Settings/item_Settings/Tab/button_Account'))
    WebUI.delay(2)
    WebUI.comment('Account tab clicked')
    
    // Step 5: Click Delete Account button
    WebUI.comment('Step 5: Clicking Delete Account button...')
    WebUI.waitForElementClickable(
        findTestObject('Object Repository/Core Chat/nav/Account_Settings/item_Settings/Account_tab/button_Delete'),
        10
    )
    WebUI.click(findTestObject('Object Repository/Core Chat/nav/Account_Settings/item_Settings/Account_tab/button_Delete'))
    WebUI.delay(2)
    WebUI.comment('Delete Account button clicked')
    
    // Step 6: Enter email to confirm
    WebUI.comment('Step 6: Entering email to confirm deletion...')
    WebUI.waitForElementVisible(
        findTestObject('Object Repository/Core Chat/nav/Account_Settings/item_Settings/Account_tab/Delete/email-confirm-input'),
        10
    )
    WebUI.setText(
        findTestObject('Object Repository/Core Chat/nav/Account_Settings/item_Settings/Account_tab/Delete/email-confirm-input'),
        GlobalVariable.consoleUserEmail
    )
    WebUI.comment('Email entered: ' + GlobalVariable.consoleUserEmail)
    WebUI.delay(1)
    
    // Step 7: Click Permanently Delete button
    WebUI.comment('Step 7: Clicking Permanently Delete button...')
    WebUI.waitForElementClickable(
        findTestObject('Object Repository/Core Chat/nav/Account_Settings/item_Settings/Account_tab/Delete/button_Permanently delete my account'),
        10
    )
    WebUI.click(findTestObject('Object Repository/Core Chat/nav/Account_Settings/item_Settings/Account_tab/Delete/button_Permanently delete my account'))
    WebUI.delay(3)
    WebUI.comment('Permanently Delete button clicked')
    
    // Step 8: Verify redirected to /login
    WebUI.comment('Step 8: Verifying redirected to /login...')
    WebUI.delay(2)
    String currentUrl = WebUI.getUrl()
    WebUI.comment('Current URL after deletion: ' + currentUrl)
    
    if (currentUrl.contains('/login')) {
        WebUI.comment('✓ User deleted successfully - redirected to /login')
        WebUI.takeScreenshot('TC05_Console_DeleteUser_Success.png')
    } else {
        WebUI.comment('✗ User deletion failed - not redirected to /login. Current URL: ' + currentUrl)
        WebUI.takeScreenshot('TC05_Console_DeleteUser_Failed.png')
        throw new Exception('User deletion failed - not redirected to /login. Current URL: ' + currentUrl)
    }
    
    WebUI.comment('TC05 PASSED')

} catch (Exception e) {
    WebUI.comment('TC05 FAILED: ' + e.getMessage())
    WebUI.takeScreenshot('TC05_Console_DeleteUser_Error.png')
    throw e
}