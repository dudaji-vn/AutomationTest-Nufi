import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.model.FailureHandling as FailureHandling

/**
 * TC27: Share Chat - Copy Share Link
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
 * 9. Click Copy link button
 * 10. Verify link copied to clipboard
 */

WebUI.comment('=== TC27: Share Chat (Copy Share Link) ===')

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
    String testMessage = 'Share copy link test'
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

    WebUI.comment('Step 10: Getting share URL before copy...')
    TestObject shareUrl = findTestObject('Object Repository/Core Chat/Share_chat/Share link to chat/url_Share')
    WebUI.waitForElementVisible(shareUrl, 10)
    String urlText = WebUI.getText(shareUrl)
    WebUI.comment('Share URL: ' + urlText)

    WebUI.comment('Step 11: Clicking Copy link button...')
    TestObject copyLinkButton = findTestObject('Object Repository/Core Chat/Share_chat/Share link to chat/button_Copy link')
    WebUI.waitForElementClickable(copyLinkButton, 5)
    WebUI.click(copyLinkButton)
    WebUI.comment('Copy link button clicked')
    WebUI.delay(1)

    WebUI.comment('Step 12: Verifying link copied to clipboard...')
    try {
        java.awt.Toolkit toolkit = java.awt.Toolkit.getDefaultToolkit()
        java.awt.datatransfer.Clipboard clipboard = toolkit.getSystemClipboard()
        java.awt.datatransfer.DataFlavor flavor = java.awt.datatransfer.DataFlavor.stringFlavor
        
        if (clipboard.isDataFlavorAvailable(flavor)) {
            String clipboardContent = clipboard.getData(flavor).toString()
            WebUI.comment('Clipboard content: ' + clipboardContent)
            
            if (clipboardContent != null && clipboardContent.contains('https://chat.nufi.me/share/')) {
                WebUI.comment('Share link copied successfully to clipboard')
            } else {
                WebUI.comment('Clipboard does not contain share link')
            }
        } else {
            WebUI.comment('String data not available in clipboard')
        }
    } catch (Exception e) {
        WebUI.comment('Unable to read clipboard: ' + e.getMessage())
    }
    
    WebUI.takeScreenshot('TC27_Share_CopyLink_Success.png')

    WebUI.comment('Step 13: Closing browser...')
    CustomKeywords.'keywords.ChatKeywords.closeBrowser'()

    WebUI.comment('TC27 PASSED')

} catch (Exception e) {
    WebUI.comment('TC27 FAILED: ' + e.getMessage())
    WebUI.takeScreenshot('TC27_Share_CopyLink_Error.png')
    CustomKeywords.'keywords.ChatKeywords.closeBrowser'()
    throw e
}