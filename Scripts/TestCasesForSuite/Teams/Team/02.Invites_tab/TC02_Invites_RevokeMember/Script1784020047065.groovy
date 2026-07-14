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
 * TC02_Invites_RevokeMember
 * 
 * Test Flow:
 * 1. Ensure on Team detail page
 * 2. Click Invites tab
 * 3. Verify user has Owner or Admin role (can manage invites)
 * 4. Revoke the last/first member in the invites list
 * 5. Verify revoke action completed (member removed from list)
 */

WebUI.comment('=== TC02_Invites_RevokeMember ===')

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
    // Step 3: Verify user has Owner or Admin role
    // ================================================================
    WebUI.comment('Step 3: Verifying user has Owner or Admin role...')
    
    TestObject restrictedMessage = new TestObject('restrictedMessage')
    restrictedMessage.addProperty('xpath', ConditionType.EQUALS, 
        "//p[contains(text(), 'Only owners and admins can manage invites')]")
    
    boolean isRestricted = WebUI.waitForElementVisible(restrictedMessage, 3, FailureHandling.OPTIONAL)
    
    if (isRestricted) {
        throw new Exception('User does not have Owner or Admin role. Cannot manage invites.')
    }
    WebUI.comment('✓ User has Owner or Admin role')

    // ================================================================
    // Step 4: Get all invite items and revoke last member
    // ================================================================
    WebUI.comment('Step 4: Getting all invite items...')
    
    List<TestObject> inviteItems = WebUI.findWebElements(
        new TestObject('inviteItems').addProperty('xpath', ConditionType.EQUALS, 
            "//li[contains(@class, 'flex') and contains(@class, 'items-center') and contains(@class, 'rounded-lg')]"),
        10
    )
    
    int inviteCount = inviteItems != null ? inviteItems.size() : 0
    WebUI.comment('Number of pending invites: ' + inviteCount)
    
    if (inviteCount == 0) {
        throw new Exception('No pending invites found to revoke.')
    }
    
    // Choose which member to revoke: LAST MEMBER
    int targetIndex = inviteCount  // Last member (1-based)
    // To revoke FIRST member, change to: int targetIndex = 1
    
    WebUI.comment('Target index: ' + targetIndex + ' (1-based)')
    
    // Get email of target member
    TestObject targetEmailObj = new TestObject('targetEmailObj')
    targetEmailObj.addProperty('xpath', ConditionType.EQUALS, 
        "(" + "//li[contains(@class, 'flex') and contains(@class, 'items-center') and contains(@class, 'rounded-lg')]" + ")" + 
        "[" + targetIndex + "]" + 
        "//p[contains(@class, 'text-text-primary')]")
    
    String targetEmail = WebUI.getText(targetEmailObj)
    WebUI.comment('Target invite email: ' + targetEmail)
    
    // Get role of target member
    TestObject targetRoleObj = new TestObject('targetRoleObj')
    targetRoleObj.addProperty('xpath', ConditionType.EQUALS, 
        "(" + "//li[contains(@class, 'flex') and contains(@class, 'items-center') and contains(@class, 'rounded-lg')]" + ")" + 
        "[" + targetIndex + "]" + 
        "//span[contains(@class, 'text-text-secondary')]")
    
    String targetRole = WebUI.getText(targetRoleObj)
    WebUI.comment('Target invite role: ' + targetRole)
    
    // ================================================================
    // Step 5: Click Revoke button
    // ================================================================
    WebUI.comment('Step 5: Clicking Revoke button for invite: ' + targetEmail)
    
    TestObject revokeButton = new TestObject('revokeButton')
    revokeButton.addProperty('xpath', ConditionType.EQUALS, 
        "(" + "//li[contains(@class, 'flex') and contains(@class, 'items-center') and contains(@class, 'rounded-lg')]" + ")" + 
        "[" + targetIndex + "]" + 
        "//button[contains(text(), 'Revoke')]")
    
    WebUI.waitForElementClickable(revokeButton, 10)
    WebUI.click(revokeButton)
    WebUI.delay(2)
    WebUI.comment('✓ Revoke button clicked for: ' + targetEmail)
    
    // ================================================================
    // Step 6: Verify revoke action completed
    // ================================================================
    WebUI.comment('Step 6: Verifying invite revoked successfully...')
    
    // Check if the revoked invite no longer appears in the list
    TestObject revokedEmailCheck = new TestObject('revokedEmailCheck')
    revokedEmailCheck.addProperty('xpath', ConditionType.EQUALS, 
        "//li[contains(@class, 'flex') and contains(@class, 'items-center') and contains(@class, 'rounded-lg')]//p[contains(text(), '" + targetEmail + "')]")
    
    boolean emailStillExists = WebUI.waitForElementNotPresent(revokedEmailCheck, 5, FailureHandling.OPTIONAL)
    
    if (emailStillExists) {
        WebUI.comment('✓ Invite for "' + targetEmail + '" has been revoked successfully')
        WebUI.takeScreenshot('TC02_Invites_RevokeMember_Success.png')
    } else {
        // Check if email still appears in list
        boolean emailExists = WebUI.waitForElementVisible(revokedEmailCheck, 3, FailureHandling.OPTIONAL)
        if (emailExists) {
            throw new Exception('Revoke failed. Invite for "' + targetEmail + '" still exists in the list.')
        } else {
            WebUI.comment('✓ Invite for "' + targetEmail + '" has been revoked successfully')
            WebUI.takeScreenshot('TC02_Invites_RevokeMember_Success.png')
        }
    }
    
    WebUI.comment('=== TC02_Invites_RevokeMember PASSED ===')

} catch (Exception e) {
    WebUI.comment('=== TC02_Invites_RevokeMember FAILED: ' + e.getMessage() + ' ===')
    WebUI.takeScreenshot('TC02_Invites_RevokeMember_Error.png')
    throw e
}