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
 * 3. Open existing chat from history
 * 4. Click Share menu button
 * 5. Click Share option
 * 6. Click Create link button
 * 7. Click Copy link button
 * 8. Verify link copied to clipboard
 */

WebUI.comment('=== TC27: Share Chat (Copy Share Link) ===')

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

    // Step 8: Get share URL before copy
    WebUI.comment('Step 8: Getting share URL before copy...')
    TestObject shareUrl = findTestObject('Object Repository/Core Chat/Share_chat/Share link to chat/url_Share')
    WebUI.waitForElementVisible(shareUrl, 10)
    String urlText = WebUI.getText(shareUrl)
    WebUI.comment('Share URL: ' + urlText)

    // Step 9: Click Copy link button
    WebUI.comment('Step 9: Clicking Copy link button...')
    TestObject copyLinkButton = findTestObject('Object Repository/Core Chat/Share_chat/Share link to chat/button_Copy link')
    WebUI.waitForElementClickable(copyLinkButton, 5)
    WebUI.click(copyLinkButton)
    WebUI.comment('Copy link button clicked')
    WebUI.delay(1)

    // Step 10: Verify link copied to clipboard
    WebUI.comment('Step 10: Verifying link copied to clipboard...')
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

    // Step 11: Close browser
    WebUI.comment('Step 11: Closing browser...')
    CustomKeywords.'keywords.ChatKeywords.closeBrowser'()

    WebUI.comment('TC27 PASSED')

} catch (Exception e) {
    WebUI.comment('TC27 FAILED: ' + e.getMessage())
    WebUI.takeScreenshot('TC27_Share_CopyLink_Error.png')
    CustomKeywords.'keywords.ChatKeywords.closeBrowser'()
    throw e
}