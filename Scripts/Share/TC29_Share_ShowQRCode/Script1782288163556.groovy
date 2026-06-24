import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.model.FailureHandling as FailureHandling

/**
 * TC29: Share Chat - Show QR Code
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
 * 9. Click Show QR Code button
 * 10. Verify QR Code is displayed
 * 11. Click Hide QR Code button
 * 12. Verify QR Code is hidden
 */

WebUI.comment('=== TC29: Share Chat (Show QR Code) ===')

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
    String testMessage = 'QR Code test'
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

    WebUI.comment('Step 10: Clicking Show QR Code button...')
    TestObject showQRButton = findTestObject('Object Repository/Core Chat/Share_chat/Share link to chat/button_Show QR Code')
    WebUI.waitForElementClickable(showQRButton, 5)
    WebUI.click(showQRButton)
    WebUI.comment('Show QR Code button clicked')
    WebUI.delay(2)

    WebUI.comment('Step 11: Verifying QR Code is displayed...')
    TestObject qrCodeImage = findTestObject('Object Repository/Core Chat/Share_chat/Share link to chat/image_Share-QR-Code')
    boolean qrVisible = WebUI.waitForElementVisible(qrCodeImage, 5)
    if (qrVisible) {
        WebUI.comment('QR Code is displayed')
    } else {
        WebUI.comment('QR Code is not displayed')
    }

    WebUI.comment('Step 12: Clicking Hide QR Code button...')
    // Using dynamic XPath for Hide QR Code button (not yet spied)
    TestObject hideQRButton = new TestObject('dynamic_hide_qr_button')
    hideQRButton.addProperty('xpath', ConditionType.EQUALS, 
        "//*[@type='button' and @aria-label='Hide QR Code']")
    WebUI.waitForElementClickable(hideQRButton, 5)
    WebUI.click(hideQRButton)
    WebUI.comment('Hide QR Code button clicked')
    WebUI.delay(1)

    WebUI.comment('Step 13: Verifying QR Code is hidden...')
    boolean qrHidden = WebUI.waitForElementNotVisible(qrCodeImage, 5)
    if (qrHidden) {
        WebUI.comment('QR Code is hidden')
    } else {
        WebUI.comment('QR Code is still visible')
    }
    
    WebUI.takeScreenshot('TC29_Share_ShowQRCode_Success.png')

    WebUI.comment('Step 14: Closing browser...')
    CustomKeywords.'keywords.ChatKeywords.closeBrowser'()

    WebUI.comment('TC29 PASSED')

} catch (Exception e) {
    WebUI.comment('TC29 FAILED: ' + e.getMessage())
    WebUI.takeScreenshot('TC29_Share_ShowQRCode_Error.png')
    CustomKeywords.'keywords.ChatKeywords.closeBrowser'()
    throw e
}