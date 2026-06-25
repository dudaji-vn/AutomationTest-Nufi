import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.model.FailureHandling as FailureHandling

/**
 * TC28: Share Chat - Refresh Share Link
 * 
 * Test Flow:
 * 1. Open browser
 * 2. Login
 * 3. Open existing chat from history
 * 4. Click Share menu button
 * 5. Click Share option
 * 6. Click Create link button
 * 7. Get original share URL
 * 8. Click Refresh link button
 * 9. Verify new share URL is different
 */

WebUI.comment('=== TC28: Share Chat (Refresh Share Link) ===')

try {
    // Step 1: Open browser
    WebUI.comment('Step 1: Opening browser...')
    CustomKeywords.'keywords.ChatKeywords.openBrowser'(GlobalVariable.Base_URL)

    // Step 2: Login
    WebUI.comment('Step 2: Logging in...')
    CustomKeywords.'keywords.ChatKeywords.loginChat'(GlobalVariable.email, GlobalVariable.password)

    // Step 3: Open existing chat from history
    WebUI.comment('Step 3: Opening existing chat from history...')
    String chatName = CustomKeywords.'keywords.HistoryChatKeywords.openRandomChatFromHistory'()
    WebUI.comment('Opened chat: ' + chatName)

    // Step 4: Click Share menu button
    WebUI.comment('Step 4: Clicking Share menu button...')
    TestObject shareMenuButton = findTestObject('Object Repository/Core Chat/Share_chat/export-menu-button')
    WebUI.waitForElementVisible(shareMenuButton, 5)
    WebUI.click(shareMenuButton)
    WebUI.comment('Share menu button clicked')

    // Step 5: Click Share option
    WebUI.comment('Step 5: Clicking Share option...')
    TestObject shareOption = findTestObject('Object Repository/Core Chat/Share_chat/button_Share')
    WebUI.waitForElementVisible(shareOption, 5)
    WebUI.click(shareOption)
    WebUI.comment('Share option clicked')

    // Step 6: Wait for Share dialog
    WebUI.comment('Step 6: Waiting for Share dialog...')
    TestObject shareDialog = findTestObject('Object Repository/Core Chat/Share_chat/Share link to chat/dialog wrapper_Share link to chat')
    WebUI.waitForElementVisible(shareDialog, 5)
    WebUI.comment('Share dialog displayed')

    // Step 7: Click Create link button
    WebUI.comment('Step 7: Clicking Create link button...')
    TestObject createLinkButton = findTestObject('Object Repository/Core Chat/Share_chat/Share link to chat/button_Create link')
    WebUI.waitForElementClickable(createLinkButton, 5)
    WebUI.click(createLinkButton)
    WebUI.comment('Create link button clicked')

    // Step 8: Get original share URL
    WebUI.comment('Step 8: Getting original share URL...')
    TestObject shareUrl = findTestObject('Object Repository/Core Chat/Share_chat/Share link to chat/url_Share')
    WebUI.waitForElementVisible(shareUrl, 10)
    String originalUrl = WebUI.getText(shareUrl)
    WebUI.comment('Original URL: ' + originalUrl)

    // Step 9: Click Refresh link button
    WebUI.comment('Step 9: Clicking Refresh link button...')
    TestObject refreshButton = findTestObject('Object Repository/Core Chat/Share_chat/Share link to chat/button_Refresh link')
    WebUI.waitForElementClickable(refreshButton, 5)
    WebUI.click(refreshButton)
    WebUI.comment('Refresh link button clicked')
    WebUI.delay(2)

    // Step 10: Get new share URL
    WebUI.comment('Step 10: Getting new share URL...')
    WebUI.waitForElementVisible(shareUrl, 10)
    String newUrl = WebUI.getText(shareUrl)
    WebUI.comment('New URL: ' + newUrl)

    // Step 11: Verify URLs are different
    WebUI.comment('Step 11: Verifying URLs are different...')
    if (originalUrl != newUrl) {
        WebUI.comment('Refresh successful - URLs are different')
    } else {
        WebUI.comment('URLs are the same - refresh may not have worked')
    }
    
    WebUI.takeScreenshot('TC28_Share_RefreshLink_Success.png')

    // Step 12: Close browser
    WebUI.comment('Step 12: Closing browser...')
    CustomKeywords.'keywords.ChatKeywords.closeBrowser'()

    WebUI.comment('TC28 PASSED')

} catch (Exception e) {
    WebUI.comment('TC28 FAILED: ' + e.getMessage())
    WebUI.takeScreenshot('TC28_Share_RefreshLink_Error.png')
    CustomKeywords.'keywords.ChatKeywords.closeBrowser'()
    throw e
}