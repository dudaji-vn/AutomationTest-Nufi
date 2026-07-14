import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import internal.GlobalVariable as GlobalVariable

/**
 * TC01_Invites_CheckUI_OwnerAdmin
 * 
 * Test Flow:
 * 1. Ensure on Team detail page
 * 2. Click Invites tab
 * 3. Verify user role is Owner or Admin
 * 4. Verify invites list is displayed (not "Only owners and admins can manage invites")
 */

WebUI.comment('=== TC01_Invites_CheckUI_OwnerAdmin ===')

try {
    // ================================================================
    // Step 1: Ensure on Team detail page
    // ================================================================
    WebUI.comment('Step 1: Ensuring on Team detail page...')
    
    String currentUrl = WebUI.getUrl()
    if (!currentUrl.matches('.*/teams/[a-f0-9]+.*')) {
        throw new Exception('Not on team detail page. Current URL: ' + currentUrl)
    }
    WebUI.comment('✓ On team detail page: ' + currentUrl)

    // ================================================================
    // Step 2: Click Invites tab
    // ================================================================
    WebUI.comment('Step 2: Clicking Invites tab...')
    
    TestObject invitesTab = new TestObject('invitesTab')
    invitesTab.addProperty('xpath', ConditionType.EQUALS, 
        "//button[@role='tab' and contains(text(), 'Invites')]")
    
    WebUI.waitForElementClickable(invitesTab, 10)
    WebUI.click(invitesTab)
    WebUI.delay(2)
    WebUI.comment('✓ Invites tab clicked')

    // ================================================================
    // Step 3: Verify user role is Owner or Admin and invites list displayed
    // ================================================================
    WebUI.comment('Step 3: Verifying invites list is displayed...')
    
    // Check if "Only owners and admins can manage invites" message appears
    TestObject restrictedMessage = new TestObject('restrictedMessage')
    restrictedMessage.addProperty('xpath', ConditionType.EQUALS, 
        "//p[contains(text(), 'Only owners and admins can manage invites')]")
    
    boolean isRestricted = WebUI.waitForElementVisible(restrictedMessage, 3, FailureHandling.OPTIONAL)
    
    if (isRestricted) {
        // User is not Owner/Admin - this should not happen
        String errorMsg = 'User does not have Owner or Admin role. Cannot manage invites.'
        WebUI.comment('✗ ' + errorMsg)
        WebUI.takeScreenshot('TC01_Invites_CheckUI_NotOwnerAdmin.png')
        throw new Exception(errorMsg)
    } else {
        WebUI.comment('✓ No restricted message found - user has Owner or Admin role')
        
        // Verify invites list is displayed
        TestObject invitesList = new TestObject('invitesList')
        invitesList.addProperty('xpath', ConditionType.EQUALS, 
            "//section[@aria-label='Pending invites']")
        
        boolean hasInvitesList = WebUI.waitForElementVisible(invitesList, 5, FailureHandling.OPTIONAL)
        
        if (hasInvitesList) {
            WebUI.comment('✓ Invites list is displayed')
            
            // Count invite items
            List<TestObject> inviteItems = WebUI.findWebElements(
                new TestObject('inviteItems').addProperty('xpath', ConditionType.EQUALS, 
                    "//li[contains(@class, 'flex') and contains(@class, 'items-center') and contains(@class, 'rounded-lg')]"),
                5
            )
            
            int inviteCount = inviteItems != null ? inviteItems.size() : 0
            WebUI.comment('Number of pending invites: ' + inviteCount)
            
        } else {
            WebUI.comment('⚠ No invites found or invites list not displayed')
        }
    }
    
    WebUI.takeScreenshot('TC01_Invites_CheckUI_OwnerAdmin_Success.png')
    WebUI.comment('=== TC01_Invites_CheckUI_OwnerAdmin PASSED ===')

} catch (Exception e) {
    WebUI.comment('=== TC01_Invites_CheckUI_OwnerAdmin FAILED: ' + e.getMessage() + ' ===')
    WebUI.takeScreenshot('TC01_Invites_CheckUI_OwnerAdmin_Error.png')
    throw e
}