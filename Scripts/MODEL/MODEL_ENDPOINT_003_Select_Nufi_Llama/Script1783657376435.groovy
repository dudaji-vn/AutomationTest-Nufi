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
 * MODEL_ENDPOINT_003_Select_Nufi_Llama
 * 
 * Test Flow:
 * 1. Open browser and login
 * 2. Switch to Advanced interface (always perform this step)
 *    a. Check screen width: if <= 760px, open navbar first
 *    b. Click "Account Settings" in navbar
 *    c. Click "Settings" menu item
 *    d. In Settings popup (General tab), click Interface dropdown
 *    e. Select "Advanced" from dropdown 
 *    f. Close Settings popup
 * 3. Click "My Agents" button
 * 4. Select Nufi Llama endpoint and verify
 */

WebUI.comment('=== MODEL_ENDPOINT_003_Select_Nufi_Llama ===')

try {
    // ================================================================
    // Step 1: Open browser and login
    // ================================================================
    WebUI.comment('Step 1: Opening browser and logging in...')
    CustomKeywords.'keywords.ChatKeywords.openBrowser'(GlobalVariable.Base_URL)
    CustomKeywords.'keywords.ChatKeywords.loginChat'(GlobalVariable.email, GlobalVariable.password)
    WebUI.delay(2)
    WebUI.comment('✓ Login successful')

    // ================================================================
    // Step 2: Switch to Advanced interface
    // ================================================================
    WebUI.comment('Step 2: Switching to Advanced interface...')
    
    // Step 2a: Check screen width and open navbar if needed
    WebUI.comment('Step 2a: Checking screen width...')
    
    String screenWidthScript = "return window.innerWidth"
    int screenWidth = (int) WebUI.executeJavaScript(screenWidthScript, null)
    WebUI.comment('Screen width: ' + screenWidth + 'px')
    
    if (screenWidth <= 760) {
        WebUI.comment('Screen width <= 760px, ensuring navbar is open...')
        
        TestObject navSidebar = new TestObject('navSidebar')
        navSidebar.addProperty('xpath', ConditionType.EQUALS, "//nav")
        
        String ariaHidden = WebUI.getAttribute(navSidebar, 'aria-hidden')
        WebUI.comment('Navbar aria-hidden: ' + ariaHidden)
        
        if (ariaHidden == 'true') {
            WebUI.comment('Navbar is closed, opening...')
            TestObject openButton = new TestObject('openButton')
            openButton.addProperty('xpath', ConditionType.EQUALS, "//button[@id='open-sidebar-button']")
            WebUI.waitForElementClickable(openButton, 5)
            WebUI.click(openButton)
            WebUI.delay(1)
            WebUI.comment('✓ Navbar opened')
        } else {
            WebUI.comment('✓ Navbar already open')
        }
    } else {
        WebUI.comment('Screen width > 760px, skipping navbar check')
    }
    
    // Step 2b: Click Account Settings
    WebUI.comment('Step 2b: Clicking Account Settings...')
    WebUI.waitForElementClickable(
        findTestObject('Object Repository/nav/nav_items/button_Account Settings'),
        10
    )
    WebUI.click(findTestObject('Object Repository/nav/nav_items/button_Account Settings'))
    WebUI.delay(1)
    WebUI.comment('✓ Clicked Account Settings')
    
    // Step 2c: Click Settings menu item
    WebUI.comment('Step 2c: Clicking Settings menu item...')
    WebUI.waitForElementClickable(
        findTestObject('Object Repository/nav/Account_Settings/menuitem_Settings'),
        10
    )
    WebUI.click(findTestObject('Object Repository/nav/Account_Settings/menuitem_Settings'))
    WebUI.delay(2)
    WebUI.comment('✓ Settings popup opened')
    
    // Step 2d: In Settings popup, click Interface dropdown
    WebUI.comment('Step 2d: Clicking Interface dropdown...')
    
    TestObject interfaceDropdown = new TestObject('interfaceDropdown')
    interfaceDropdown.addProperty('xpath', ConditionType.EQUALS, 
        "//button[@data-testid='ui-mode-selector']")
    
    if (!WebUI.waitForElementVisible(interfaceDropdown, 5, FailureHandling.OPTIONAL)) {
        WebUI.comment('Trying alternative selector for Interface dropdown...')
        interfaceDropdown = new TestObject('interfaceDropdownAlt')
        interfaceDropdown.addProperty('xpath', ConditionType.EQUALS, 
            "//div[@id='ui-mode-selector-label']/following-sibling::div//button")
    }
    
    WebUI.waitForElementClickable(interfaceDropdown, 10)
    WebUI.click(interfaceDropdown)
    WebUI.delay(1)
    WebUI.comment('✓ Interface dropdown opened')
    
    // Step 2e: Select "Advanced" from dropdown
    WebUI.comment('Step 2e: Selecting "Advanced" from dropdown...')
    
    TestObject advancedOption = new TestObject('advancedOption')
    advancedOption.addProperty('xpath', ConditionType.EQUALS, 
        "//div[@role='listbox']//div[@data-theme='advanced']")
    
    if (!WebUI.waitForElementVisible(advancedOption, 5, FailureHandling.OPTIONAL)) {
        WebUI.comment('Trying alternative selector for Advanced option...')
        advancedOption = new TestObject('advancedOptionAlt')
        advancedOption.addProperty('xpath', ConditionType.EQUALS, 
            "//div[@role='listbox']//span[contains(text(), 'Advanced')]/ancestor::div[@role='option']")
    }
    
    WebUI.waitForElementClickable(advancedOption, 10)
    WebUI.click(advancedOption)
    WebUI.delay(1)
    WebUI.comment('✓ Selected Advanced interface')
    
    // Step 2f: Close Settings popup
    WebUI.comment('Step 2f: Closing Settings popup...')
    
    TestObject closeButton = new TestObject('closeButton')
    closeButton.addProperty('xpath', ConditionType.EQUALS, 
        "//button[contains(@class, 'rounded-sm')]//*[local-name()='svg']//*[local-name()='line' and @x1='6' and @x2='18' and @y1='6' and @y2='18']/ancestor::button")
    
    if (!WebUI.waitForElementVisible(closeButton, 5, FailureHandling.OPTIONAL)) {
        WebUI.comment('Trying alternative selector for close button...')
        closeButton = new TestObject('closeButtonAlt')
        closeButton.addProperty('xpath', ConditionType.EQUALS, 
            "//button[@aria-label='Close Settings']")
    }
    
    WebUI.waitForElementClickable(closeButton, 10)
    WebUI.click(closeButton)
    WebUI.delay(2)
    WebUI.comment('✓ Settings popup closed')
    
    // ================================================================
    // Step 3: Click "My Agents" button
    // ================================================================
    WebUI.comment('Step 3: Clicking My Agents button...')
    WebUI.click(findTestObject('Core Chat/button_My Agents'))
    WebUI.delay(2)
    WebUI.comment('✓ Clicked My Agents button')
    
    // ================================================================
    // Step 4: Select Nufi Llama endpoint and verify
    // ================================================================
    WebUI.comment('Step 4: Selecting Nufi Llama endpoint...')
    
    WebUI.click(findTestObject('Core Chat/Agent/Sellect_sys_test_npu'))
    WebUI.delay(2)
    
    WebUI.click(findTestObject('Core Chat/Agent/sellect_llama-3-1-8b-v1-rngd'))
    WebUI.delay(2)
    
    WebUI.waitForElementPresent(
        findTestObject('Core Chat/Agent/span_Agents'), 
        10, FailureHandling.STOP_ON_FAILURE)
    
    String agentName = WebUI.getText(findTestObject('Core Chat/Agent/span_Agents'))
    WebUI.comment('Selected Agent: ' + agentName)
    
    if (agentName.contains('llama')) {
        WebUI.comment('✓ Nufi/Llama endpoint selection successful')
        WebUI.takeScreenshot('MODEL_ENDPOINT_003_Select_Nufi_Llama_Success.png')
    } else {
        WebUI.comment('ERROR: Expected model containing "llama" but got: ' + agentName)
        WebUI.takeScreenshot('MODEL_ENDPOINT_003_Select_Nufi_Llama_Failed.png')
        throw new Exception('Nufi/Llama endpoint selection failed. Expected model containing "llama" but got: ' + agentName)
    }
    
    WebUI.delay(2)
    WebUI.comment('=== MODEL_ENDPOINT_003_Select_Nufi_Llama PASSED ===')

} catch (Exception e) {
    WebUI.comment('=== MODEL_ENDPOINT_003_Select_Nufi_Llama FAILED: ' + e.getMessage() + ' ===')
    WebUI.takeScreenshot('MODEL_ENDPOINT_003_Select_Nufi_Llama_Error.png')
    throw e
} finally {
    WebUI.closeBrowser()
}