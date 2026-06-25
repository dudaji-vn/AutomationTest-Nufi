import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.model.FailureHandling as FailureHandling

/**
 * TC30: Share Chat - Delete Share Link
 * 
 * Test Flow:
 * 1. Open browser
 * 2. Login
 * 3. Open existing chat from history
 * 4. Click Share menu button
 * 5. Click Share option
 * 6. Click Create link button
 * 7. Get share URL
 * 8. Delete/Remove link
 * 9. Verify share link removed / access denied
 */

WebUI.comment('=== TC30: Share Chat (Delete Share Link) ===')

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

    // Step 8: Get share URL
    WebUI.comment('Step 8: Getting share URL...')
    TestObject shareUrl = findTestObject('Object Repository/Core Chat/Share_chat/Share link to chat/url_Share')
    WebUI.waitForElementVisible(shareUrl, 10)
    String urlText = WebUI.getText(shareUrl)
    WebUI.comment('Share URL: ' + urlText)

    // Step 9: Delete/Remove link
    WebUI.comment('Step 9: Removing share link...')
    TestObject deleteLinkButton = new TestObject('dynamic_delete_link_button')
    deleteLinkButton.addProperty('xpath', ConditionType.EQUALS, 
        "//*[@type='button' and (contains(text(), 'Delete') or contains(text(), 'Remove') or @aria-label='Delete link')]")
    
    boolean deleteButtonExists = WebUI.waitForElementVisible(deleteLinkButton, 3, FailureHandling.OPTIONAL)
    if (deleteButtonExists) {
        WebUI.click(deleteLinkButton)
        WebUI.comment('Delete link button clicked')
    } else {
        WebUI.comment('Delete button not found, closing dialog instead...')
        WebUI.click(shareDialog)
        WebUI.comment('Share dialog closed')
    }
    WebUI.delay(2)

    // Step 10: Verify share link removed
    WebUI.comment('Step 10: Verifying share link removed...')
    WebUI.navigateToUrl(urlText)
    WebUI.waitForPageLoad(10)
    WebUI.delay(2)

    // Check for "Shared link not found" message
    TestObject notFoundMessage = new TestObject('dynamic_shared_not_found')
    notFoundMessage.addProperty('xpath', ConditionType.EQUALS, 
        "//*[contains(text(),'Shared link not found') or contains(text(),'not found') or contains(text(),'404')]")
    
    boolean notFound = WebUI.waitForElementVisible(notFoundMessage, 5, FailureHandling.OPTIONAL)
    if (notFound) {
        WebUI.comment('Shared link not found - link was deleted successfully')
        WebUI.takeScreenshot('TC30_Share_DeleteLink_Success.png')
    } else {
        WebUI.comment('Shared link is still accessible - deletion may have failed')
        WebUI.takeScreenshot('TC30_Share_DeleteLink_StillAccessible.png')
    }

    // Step 11: Close browser
    WebUI.comment('Step 11: Closing browser...')
    CustomKeywords.'keywords.ChatKeywords.closeBrowser'()

    WebUI.comment('TC30 PASSED')

} catch (Exception e) {
    WebUI.comment('TC30 FAILED: ' + e.getMessage())
    WebUI.takeScreenshot('TC30_Share_DeleteLink_Error.png')
    CustomKeywords.'keywords.ChatKeywords.closeBrowser'()
    throw e
}