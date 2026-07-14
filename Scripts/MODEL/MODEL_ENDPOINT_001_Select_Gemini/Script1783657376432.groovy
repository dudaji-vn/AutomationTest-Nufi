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
 * MODEL_ENDPOINT_001_Select_Gemini
 * 
 * Test Flow:
 * 1. Open browser and login
 * 2. Check if "Select a model" text appears => Basic interface is enabled
 * 3. If Basic interface is enabled, switch to Advanced:
 *    a. Check screen width: if <= 760px, open navbar first
 *    b. Click "Account Settings" in navbar
 *    c. Click "Settings" menu item
 *    d. In Settings popup (General tab), click Interface dropdown
 *    e. Select "Advanced" from dropdown
 *    f. Close Settings popup
 *    g. Verify "Select a model" text no longer exists
 * 4. Click "My Agents" button
 * 5. Select Gemini endpoint and verify
 */

WebUI.comment('=== MODEL_ENDPOINT_001_Select_Gemini ===')

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
    // Step 2: Check if Basic interface is enabled (check "Select a model" text)
    // ================================================================
    WebUI.comment('Step 2: Checking if Basic interface is enabled...')
    
    // Check for "Select a model" text
    TestObject selectModelText = new TestObject('selectModelText')
    selectModelText.addProperty('xpath', ConditionType.EQUALS, 
        "//*[contains(text(), 'Select a model')]")
    
    boolean isBasicInterface = WebUI.waitForElementVisible(selectModelText, 5, FailureHandling.OPTIONAL)
    
    if (!isBasicInterface) {
        WebUI.comment('✓ Advanced interface is already enabled. Skipping switch...')
    } else {
        WebUI.comment('✓ Basic interface detected (found "Select a model" text)')
        WebUI.comment('Switching to Advanced interface...')
        
        // ================================================================
        // Step 3a: Check screen width and open navbar if needed
        // ================================================================
        WebUI.comment('Step 3a: Checking screen width...')
        
        // Get screen width using JavaScript
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
        
        // ================================================================
        // Step 3b: Click Account Settings
        // ================================================================
        WebUI.comment('Step 3b: Clicking Account Settings...')
        WebUI.waitForElementClickable(
            findTestObject('Object Repository/nav/nav_items/button_Account Settings'),
            10
        )
        WebUI.click(findTestObject('Object Repository/nav/nav_items/button_Account Settings'))
        WebUI.delay(1)
        WebUI.comment('✓ Clicked Account Settings')
        
        // ================================================================
        // Step 3c: Click Settings menu item
        // ================================================================
        WebUI.comment('Step 3c: Clicking Settings menu item...')
        WebUI.waitForElementClickable(
            findTestObject('Object Repository/nav/Account_Settings/menuitem_Settings'),
            10
        )
        WebUI.click(findTestObject('Object Repository/nav/Account_Settings/menuitem_Settings'))
        WebUI.delay(2)
        WebUI.comment('✓ Settings popup opened')
        
        // ================================================================
        // Step 3d: In Settings popup, click Interface dropdown
        // ================================================================
        WebUI.comment('Step 3d: Clicking Interface dropdown...')
        
        // Find Interface dropdown by data-testid
        TestObject interfaceDropdown = new TestObject('interfaceDropdown')
        interfaceDropdown.addProperty('xpath', ConditionType.EQUALS, 
            "//button[@data-testid='ui-mode-selector']")
        
        // Alternative: find by label text
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
        
        // ================================================================
        // Step 3e: Select "Advanced" from dropdown
        // ================================================================
        WebUI.comment('Step 3e: Selecting "Advanced" from dropdown...')
        
        // Find Advanced option in dropdown
        TestObject advancedOption = new TestObject('advancedOption')
        advancedOption.addProperty('xpath', ConditionType.EQUALS, 
            "//div[@role='listbox']//div[@data-theme='advanced']")
        
        // Alternative: find by text
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
        
        // ================================================================
        // Step 3f: Close Settings popup
        // ================================================================
        WebUI.comment('Step 3f: Closing Settings popup...')
        
        // Find close button (X) in settings popup
        TestObject closeButton = new TestObject('closeButton')
        closeButton.addProperty('xpath', ConditionType.EQUALS, 
            "//button[contains(@class, 'rounded-sm')]//*[local-name()='svg']//*[local-name()='line' and @x1='6' and @x2='18' and @y1='6' and @y2='18']/ancestor::button")
        
        // Alternative: find close button by aria-label
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
        // Step 3g: Verify "Select a model" text no longer exists
        // ================================================================
        WebUI.comment('Step 3g: Verifying Advanced interface is enabled...')
        
        boolean advancedInterfaceEnabled = WebUI.waitForElementNotPresent(selectModelText, 5, FailureHandling.OPTIONAL)
        
        if (advancedInterfaceEnabled) {
            WebUI.comment('✓ Advanced interface enabled successfully (no "Select a model" text found)')
        } else {
            throw new Exception('Failed to switch to Advanced interface. "Select a model" text still exists.')
        }
    }
    
    // ================================================================
    // Step 4: Click "My Agents" button
    // ================================================================
    WebUI.comment('Step 4: Clicking My Agents button...')
    WebUI.click(findTestObject('Core Chat/button_My Agents'))
    WebUI.delay(2)
    WebUI.comment('✓ Clicked My Agents button')
    
    // ================================================================
    // Step 5: Select Gemini endpoint and verify
    // ================================================================
    WebUI.comment('Step 5: Selecting Gemini endpoint...')
    
    // Select Gemini option
    WebUI.click(findTestObject('Core Chat/Agent/Sellect_Gemini'))
    WebUI.delay(2)
    
    // Select Gemini (1)
    WebUI.click(findTestObject('Core Chat/Agent/sellect_gemini (1)'))
    WebUI.delay(2)
    
    // Verify that span_Agents shows "gemini"
    WebUI.waitForElementPresent(
        findTestObject('Core Chat/Agent/span_Agents'), 
        10, FailureHandling.STOP_ON_FAILURE)
    
    String agentName = WebUI.getText(findTestObject('Core Chat/Agent/span_Agents')).toLowerCase()
    WebUI.comment('Selected Agent: ' + agentName)
    
    if (agentName.contains('gemini')) {
        WebUI.comment('✓ Gemini endpoint selection successful')
        WebUI.takeScreenshot('MODEL_ENDPOINT_001_Select_Gemini_Success.png')
    } else {
        WebUI.comment('ERROR: Expected "gemini" but got: ' + agentName)
        WebUI.takeScreenshot('MODEL_ENDPOINT_001_Select_Gemini_Failed.png')
        throw new Exception('Gemini endpoint selection failed. Expected "gemini" but got: ' + agentName)
    }
    
    WebUI.delay(2)
    WebUI.comment('=== MODEL_ENDPOINT_001_Select_Gemini PASSED ===')

} catch (Exception e) {
    WebUI.comment('=== MODEL_ENDPOINT_001_Select_Gemini FAILED: ' + e.getMessage() + ' ===')
    WebUI.takeScreenshot('MODEL_ENDPOINT_001_Select_Gemini_Error.png')
    throw e
} finally {
    WebUI.closeBrowser()
}