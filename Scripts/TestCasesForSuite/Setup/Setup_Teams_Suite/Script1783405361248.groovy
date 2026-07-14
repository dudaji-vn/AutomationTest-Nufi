import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.model.FailureHandling as FailureHandling

/**
 * Setup_Teams_Suite
 * 
 * Setup for Teams test suite.
 * Performs:
 * 1. Open browser
 * 2. Login with existing user
 * 3. Switch to Advanced interface (required for navbar components to display fully)
 * 4. Navigate to Teams page
 */

WebUI.comment('=== SETUP: Teams Suite ===')

try {
    // ================================================================
    // Step 1: Open browser
    // ================================================================
    WebUI.comment('Step 1: Opening browser...')
    CustomKeywords.'keywords.ChatKeywords.openBrowser'(
        GlobalVariable.Base_URL
    )

    // ================================================================
    // Step 2: Login with existing user
    // ================================================================
    WebUI.comment('Step 2: Logging in...')
    CustomKeywords.'keywords.ChatKeywords.loginChat'(
        GlobalVariable.email,
        GlobalVariable.password
    )
    WebUI.delay(2)
    WebUI.comment('✓ Login successful')

    // ================================================================
    // Step 3: Switch to Advanced interface
    // ================================================================
    WebUI.comment('Step 3: Switching to Advanced interface...')
    
    // Step 3a: Check screen width and open navbar if needed
    WebUI.comment('Step 3a: Checking screen width...')
    String screenWidthScript = "return window.innerWidth || document.documentElement.clientWidth || document.body.clientWidth;"
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
    
    // Step 3b: Click Account Settings
    WebUI.comment('Step 3b: Clicking Account Settings...')
    WebUI.waitForElementClickable(
        findTestObject('Object Repository/nav/nav_items/button_Account Settings'),
        10
    )
    WebUI.click(findTestObject('Object Repository/nav/nav_items/button_Account Settings'))
    WebUI.delay(1)
    WebUI.comment('✓ Clicked Account Settings')
    
    // Step 3c: Click Settings menu item
    WebUI.comment('Step 3c: Clicking Settings menu item...')
    WebUI.waitForElementClickable(
        findTestObject('Object Repository/nav/Account_Settings/menuitem_Settings'),
        10
    )
    WebUI.click(findTestObject('Object Repository/nav/Account_Settings/menuitem_Settings'))
    WebUI.delay(2)
    WebUI.comment('✓ Settings popup opened')
    
    // Step 3d: In Settings popup, click Interface dropdown
    WebUI.comment('Step 3d: Clicking Interface dropdown...')
    
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
    
    // Step 3e: Select "Advanced" from dropdown
    WebUI.comment('Step 3e: Selecting "Advanced" from dropdown...')
    
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
    
    // Step 3f: Close Settings popup
    WebUI.comment('Step 3f: Closing Settings popup...')
    
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
    
    WebUI.comment('✓ Successfully switched to Advanced interface')

    // ================================================================
    // Step 4: Navigate to Teams page
    // ================================================================
    WebUI.comment('Step 4: Navigating to Teams page...')
    
    // Check if sidebar is open (for mobile)
    if (screenWidth <= 760) {
        WebUI.comment('Screen width <= 760px, checking sidebar...')
        TestObject navSidebar = new TestObject('navSidebar')
        navSidebar.addProperty('xpath', ConditionType.EQUALS, "//nav")
        
        String ariaHidden = WebUI.getAttribute(navSidebar, 'aria-hidden')
        if (ariaHidden == 'true') {
            WebUI.comment('Sidebar is closed, opening...')
            TestObject openButton = new TestObject('openButton')
            openButton.addProperty('xpath', ConditionType.EQUALS, "//button[@id='open-sidebar-button']")
            WebUI.waitForElementClickable(openButton, 5)
            WebUI.click(openButton)
            WebUI.delay(1)
            WebUI.comment('✓ Sidebar opened')
        } else {
            WebUI.comment('✓ Sidebar already open')
        }
    }
    
    // Click Teams button on navbar
    WebUI.waitForElementClickable(
        findTestObject('Object Repository/nav/nav_items/button_Teams'),
        10
    )
    WebUI.click(findTestObject('Object Repository/nav/nav_items/button_Teams'))
    WebUI.delay(3)
    
    // Verify navigated to Teams page
    String currentUrl = WebUI.getUrl()
    if (!currentUrl.contains('/teams')) {
        throw new Exception('Failed to navigate to Teams page. Current URL: ' + currentUrl)
    }
    WebUI.comment('✓ Navigated to Teams page: ' + currentUrl)
    
    WebUI.takeScreenshot('Setup_Teams_Suite_Success.png')
    WebUI.comment('✓ Setup completed - ready for Teams test cases')

} catch (Exception e) {
    WebUI.comment('✗ SETUP FAILED: ' + e.getMessage())
    WebUI.takeScreenshot('Setup_Teams_Suite_Error.png')
    CustomKeywords.'keywords.ChatKeywords.closeBrowser'()
    throw e
}