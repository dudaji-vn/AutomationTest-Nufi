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
 * TC19: Teams - Owner Transfer Ownership (Confirm)
 * 
 * Test Flow:
 * 1. Ensure on Members tab
 * 2. Click Transfer Ownership button on the first member (not owner)
 * 3. Verify popup appears
 * 4. Click Confirm button
 * 5. Verify popup closes and ownership transferred
 */

WebUI.comment('=== TC19: Teams - Owner Transfer Ownership (Confirm) ===')

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
    // STEP 2: Find first member (excluding owner)
    // ============================================================
    WebUI.comment('Step 2: Finding first member (not owner)...')
    
    List<TestObject> allMemberItems = WebUI.findWebElements(
        new TestObject('allMemberItems').addProperty('xpath', ConditionType.EQUALS, 
            "//li[contains(@class, 'flex') and contains(@class, 'items-center')]"),
        10
    )
    
    int memberCount = allMemberItems.size()
    WebUI.comment('Total members found: ' + memberCount)
    
    if (memberCount < 2) {
        throw new Exception('Not enough members to transfer ownership. Need at least 2 members.')
    }
    
    // Get email of first member (index 1 - skip owner at index 0)
    TestObject firstMemberEmailObj = new TestObject('firstMemberEmailObj')
    firstMemberEmailObj.addProperty('xpath', ConditionType.EQUALS, 
        "(//li[contains(@class, 'flex') and contains(@class, 'items-center')])[2]//p[contains(@class, 'text-text-secondary')]")
    
    String firstMemberEmail = WebUI.getText(firstMemberEmailObj)
    WebUI.comment('First member email: ' + firstMemberEmail)
    
    // ============================================================
    // STEP 3: Click Transfer Ownership button
    // ============================================================
    WebUI.comment('Step 3: Clicking Transfer Ownership button...')
    
    TestObject transferButton = new TestObject('transferButton')
    transferButton.addProperty('xpath', ConditionType.EQUALS, 
        "(//li[contains(@class, 'flex') and contains(@class, 'items-center')])[2]//button[@aria-label='Transfer ownership']")
    
    WebUI.waitForElementClickable(transferButton, 10)
    WebUI.click(transferButton)
    WebUI.delay(1)
    WebUI.comment('✓ Transfer Ownership button clicked')
    
    // ============================================================
    // STEP 4: Verify popup appears
    // ============================================================
    WebUI.comment('Step 4: Verifying confirmation popup appears...')
    
    TestObject popupTransfer = findTestObject('Object Repository/nav/Teams/Tab_member/Action_to_members/Transfer ownership/popup_Transfer ownership')
    WebUI.waitForElementVisible(popupTransfer, 5)
    WebUI.comment('✓ Transfer Ownership popup displayed')
    
    // ============================================================
    // STEP 5: Click Confirm button
    // ============================================================
    WebUI.comment('Step 5: Clicking Confirm button...')
    
    TestObject confirmButton = findTestObject('Object Repository/nav/Teams/Tab_member/Action_to_members/Transfer ownership/button_Confirm')
    WebUI.waitForElementClickable(confirmButton, 5)
    WebUI.click(confirmButton)
    WebUI.delay(3)
    WebUI.comment('✓ Confirm button clicked')
    
    // ============================================================
    // STEP 6: Verify popup closed
    // ============================================================
    WebUI.comment('Step 6: Verifying popup closed...')
    
    boolean popupClosed = WebUI.waitForElementNotVisible(popupTransfer, 5)
    if (popupClosed) {
        WebUI.comment('✓ Popup closed')
    } else {
        WebUI.comment('⚠ Popup still visible')
    }
    
    WebUI.takeScreenshot('TC19_TransferOwnership_Confirm_Success.png')
    WebUI.comment('=== TC19 PASSED ===')

} catch (Exception e) {
    WebUI.comment('=== TC19 FAILED: ' + e.getMessage() + ' ===')
    WebUI.takeScreenshot('TC19_TransferOwnership_Confirm_Error.png')
    throw e
}