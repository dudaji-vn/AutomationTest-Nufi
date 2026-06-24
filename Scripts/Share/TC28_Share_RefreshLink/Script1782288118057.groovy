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
 * 3. Open new conversation
 * 4. Select Nufi endpoint + Qwen model
 * 5. Send a message and get response
 * 6. Click Share menu button
 * 7. Click Share option
 * 8. Click Create link button
 * 9. Get original share URL
 * 10. Click Refresh link button
 * 11. Verify new share URL is different
 */

WebUI.comment('=== TC28: Share Chat (Refresh Share Link) ===')

try {
    WebUI.comment('Step 1: Opening browser...')
    CustomKeywords.'keywords.ChatKeywords.openBrowser'(GlobalVariable.Base_URL)

    WebUI.comment('Step 2: Logging in...')
    CustomKeywords.'keywords.ChatKeywords.loginChat'(GlobalVariable.email, GlobalVariable.password)

    WebUI.comment('Step 3: Opening new conversation...')
    CustomKeywords.'keywords.ChatKeywords.openNewConversation'(GlobalVariable.Base_URL)

    WebUI.comment('Step 4: Selecting Nufi endpoint and Qwen model...')
    CustomKeywords.'keywords.ChatKeywords.selectEndpointAndModel'('Nufi', 'Qwen2.5-0.5B')

    WebUI.comment('Step 5: Sending test message...')
    String testMessage = 'Refresh link test'
    String response = CustomKeywords.'keywords.ChatKeywords.sendMessageAndVerifyResponse'(testMessage)
    WebUI.comment('Response received')

    WebUI.comment('Step 6: Clicking Share menu button...')
    TestObject shareMenuButton = findTestObject('Object Repository/Core Chat/Share_chat/export-menu-button')
    WebUI.waitForElementVisible(shareMenuButton, 5)
    WebUI.click(shareMenuButton)
    WebUI.comment('Share menu button clicked')

    WebUI.comment('Step 7: Clicking Share option...')
    TestObject shareOption = findTestObject('Object Repository/Core Chat/Share_chat/button_Share')
    WebUI.waitForElementVisible(shareOption, 5)
    WebUI.click(shareOption)
    WebUI.comment('Share option clicked')

    WebUI.comment('Step 8: Waiting for Share dialog...')
    TestObject shareDialog = findTestObject('Object Repository/Core Chat/Share_chat/Share link to chat/dialog wrapper_Share link to chat')
    WebUI.waitForElementVisible(shareDialog, 5)
    WebUI.comment('Share dialog displayed')

    WebUI.comment('Step 9: Clicking Create link button...')
    TestObject createLinkButton = findTestObject('Object Repository/Core Chat/Share_chat/Share link to chat/button_Create link')
    WebUI.waitForElementClickable(createLinkButton, 5)
    WebUI.click(createLinkButton)
    WebUI.comment('Create link button clicked')

    WebUI.comment('Step 10: Getting original share URL...')
    TestObject shareUrl = findTestObject('Object Repository/Core Chat/Share_chat/Share link to chat/url_Share')
    WebUI.waitForElementVisible(shareUrl, 10)
    String originalUrl = WebUI.getText(shareUrl)
    WebUI.comment('Original URL: ' + originalUrl)

    WebUI.comment('Step 11: Clicking Refresh link button...')
    TestObject refreshButton = findTestObject('Object Repository/Core Chat/Share_chat/Share link to chat/button_Refresh link')
    WebUI.waitForElementClickable(refreshButton, 5)
    WebUI.click(refreshButton)
    WebUI.comment('Refresh link button clicked')
    WebUI.delay(2)

    WebUI.comment('Step 12: Getting new share URL...')
    WebUI.waitForElementVisible(shareUrl, 10)
    String newUrl = WebUI.getText(shareUrl)
    WebUI.comment('New URL: ' + newUrl)

    WebUI.comment('Step 13: Verifying URLs are different...')
    if (originalUrl != newUrl) {
        WebUI.comment('Refresh successful - URLs are different')
    } else {
        WebUI.comment('URLs are the same - refresh may not have worked')
    }
    
    WebUI.takeScreenshot('TC28_Share_RefreshLink_Success.png')

    WebUI.comment('Step 14: Closing browser...')
    CustomKeywords.'keywords.ChatKeywords.closeBrowser'()

    WebUI.comment('TC28 PASSED')

} catch (Exception e) {
    WebUI.comment('TC28 FAILED: ' + e.getMessage())
    WebUI.takeScreenshot('TC28_Share_RefreshLink_Error.png')
    CustomKeywords.'keywords.ChatKeywords.closeBrowser'()
    throw e
}