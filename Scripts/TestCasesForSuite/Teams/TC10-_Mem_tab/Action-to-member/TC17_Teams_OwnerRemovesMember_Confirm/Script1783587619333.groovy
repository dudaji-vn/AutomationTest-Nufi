import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.testobject.ConditionType

import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile

import internal.GlobalVariable as GlobalVariable

/**
 * TC17: Teams - Owner Removes Member (Confirm)
 * 
 * Test Flow:
 * 1. Ensure on Members tab
 * 2. Click Remove button on the last member in list (not owner)
 * 3. Verify popup appears
 * 4. Click Confirm button
 * 5. Verify popup closes and member removed from list
 */

WebUI.comment('=== TC17: Teams - Owner Removes Member (Confirm) ===')

try {
    // ============================================================
    // STEP 1: Ensure on Members tab
    // ============================================================
    WebUI.comment('Step 1: Ensuring on Members tab...')
    
    TestObject membersTab = new TestObject('membersTab')
    membersTab.addProperty('xpath', ConditionType.EQUALS, 
        "//button[@role='tab' and contains(text(), 'Members') and @aria-selected='true']")
    
    boolean isMembersTabActive = WebUI.verifyElementPresent(membersTab, 3, FailureHandling.OPTIONAL)
    
    if (!isMembersTabActive) {
        WebUI.comment('Members tab not active, clicking Members tab...')
        TestObject membersTabClick = new TestObject('membersTabClick')
        membersTabClick.addProperty('xpath', ConditionType.EQUALS, 
            "//button[@role='tab' and contains(text(), 'Members')]")
        WebUI.waitForElementClickable(membersTabClick, 10)
        WebUI.click(membersTabClick)
        WebUI.delay(2)
        WebUI.comment('✓ Members tab clicked')
    } else {
        WebUI.comment('✓ Members tab already active')
    }
    
    // ============================================================
    // STEP 2: Get list of members and find the last member
    // ============================================================
    WebUI.comment('Step 2: Finding last member in list...')
    
    List<TestObject> allMemberItems = WebUI.findWebElements(
        new TestObject('allMemberItems').addProperty('xpath', ConditionType.EQUALS, 
            "//li[contains(@class, 'flex') and contains(@class, 'items-center')]"),
        10
    )
    
    int memberCount = allMemberItems.size()
    WebUI.comment('Total members found: ' + memberCount)
    
    if (memberCount < 2) {
        throw new Exception('Not enough members to remove. Need at least 2 members.')
    }
    
    // Get email of last member using TestObject
    TestObject lastMemberEmailObj = new TestObject('lastMemberEmailObj')
    lastMemberEmailObj.addProperty('xpath', ConditionType.EQUALS, 
        "(" + "//li[contains(@class, 'flex') and contains(@class, 'items-center')]" + ")" + "[" + memberCount + "]" + 
        "//p[contains(@class, 'text-text-secondary')]")
    
    String lastMemberEmail = WebUI.getText(lastMemberEmailObj)
    WebUI.comment('Last member email: ' + lastMemberEmail)
    
    // ============================================================
    // STEP 3: Click Remove button on the last member
    // ============================================================
    WebUI.comment('Step 3: Clicking Remove button on last member...')
    
    TestObject removeButton = new TestObject('removeButton')
    removeButton.addProperty('xpath', ConditionType.EQUALS, 
        "(" + "//li[contains(@class, 'flex') and contains(@class, 'items-center')]" + ")" + "[" + memberCount + "]" + 
        "//button[@aria-label='Remove']")
    
    WebUI.waitForElementClickable(removeButton, 10)
    WebUI.click(removeButton)
    WebUI.delay(1)
    WebUI.comment('✓ Remove button clicked')
    
    // ============================================================
    // STEP 4: Verify popup appears
    // ============================================================
    WebUI.comment('Step 4: Verifying confirmation popup appears...')
    
    TestObject popupRemove = findTestObject('Object Repository/nav/Teams/Tab_member/Action_to_members/Remove/popup_Remove')
    WebUI.waitForElementVisible(popupRemove, 5)
    WebUI.comment('✓ Remove confirmation popup displayed')
    
    // ============================================================
    // STEP 5: Click Confirm button
    // ============================================================
    WebUI.comment('Step 5: Clicking Confirm button...')
    
    TestObject confirmButton = findTestObject('Object Repository/nav/Teams/Tab_member/Action_to_members/Remove/button_Confirm')
    WebUI.waitForElementClickable(confirmButton, 5)
    WebUI.click(confirmButton)
    WebUI.delay(3)
    WebUI.comment('✓ Confirm button clicked')
    
    // ============================================================
    // STEP 6: Verify popup closed and member removed
    // ============================================================
    WebUI.comment('Step 6: Verifying popup closed and member removed...')
    
    // Verify popup is no longer visible
    boolean popupClosed = WebUI.waitForElementNotVisible(popupRemove, 5)
    if (popupClosed) {
        WebUI.comment('✓ Popup closed')
    } else {
        WebUI.comment('⚠ Popup still visible')
    }
    
    // Verify member is removed from list
    TestObject memberRemoved = new TestObject('memberRemoved')
    memberRemoved.addProperty('xpath', ConditionType.EQUALS, 
        "//p[contains(@class, 'text-text-secondary') and contains(text(), '" + lastMemberEmail + "')]")
    
    boolean memberExists = WebUI.verifyElementPresent(memberRemoved, 3, FailureHandling.OPTIONAL)
    if (!memberExists) {
        WebUI.comment('✓ Member "' + lastMemberEmail + '" removed from list')
    } else {
        throw new Exception('Member "' + lastMemberEmail + '" still in list after confirmation')
    }
    
    WebUI.takeScreenshot('TC17_RemoveMember_Confirm_Success.png')
    WebUI.comment('=== TC17 PASSED ===')

} catch (Exception e) {
    WebUI.comment('=== TC17 FAILED: ' + e.getMessage() + ' ===')
    WebUI.takeScreenshot('TC17_RemoveMember_Confirm_Error.png')
    throw e
}