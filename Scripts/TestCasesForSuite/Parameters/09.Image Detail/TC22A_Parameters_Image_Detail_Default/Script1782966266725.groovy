import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.util.KeywordUtil

/**
 * TC22A_Parameters_Image_Detail_Default
 * Verify Image Detail slider default value is Auto
 * 
 * Test Flow:
 * 1. Open Parameters panel
 * 2. Get Image Detail slider and verify default value is Auto
 */

WebUI.comment('=== TC22A: Image Detail - Default Value ===')

try {
    // ============================================================
    // STEP 0: CHECK NAVBAR
    // ============================================================
    WebUI.comment('Step 0: Checking Navbar state...')
    
    TestObject navSidebar = new TestObject('navSidebar')
    navSidebar.addProperty('xpath', ConditionType.EQUALS, "//nav")
    WebUI.waitForElementVisible(navSidebar, 10)
    
    if (WebUI.getAttribute(navSidebar, 'aria-hidden') == 'true') {
        WebUI.comment('Navbar is closed, opening sidebar...')
        TestObject openBtn = new TestObject('openSidebarButton')
        openBtn.addProperty('xpath', ConditionType.EQUALS, "//button[@id='open-sidebar-button']")
        WebUI.click(openBtn)
        WebUI.delay(1)
        WebUI.comment('Sidebar opened')
    } else {
        WebUI.comment('Navbar is already open')
    }

    // ============================================================
    // STEP 1: OPEN PARAMETERS TAB
    // ============================================================
    WebUI.comment('Step 1: Opening Parameters tab...')
    
    TestObject parametersButton = findTestObject('Object Repository/Core Chat/nav/nav_items/button_Parameters')
    WebUI.waitForElementVisible(parametersButton, 10)
    
    if (WebUI.getAttribute(parametersButton, 'aria-pressed') != 'true') {
        WebUI.comment('Parameters tab is closed, clicking to open...')
        WebUI.click(parametersButton)
        WebUI.delay(2)
        WebUI.comment('Parameters tab opened')
    } else {
        WebUI.comment('Parameters tab is already open')
    }

    // ============================================================
    // STEP 2: GET IMAGE DETAIL SLIDER
    // ============================================================
    WebUI.comment('Step 2: Getting Image Detail slider...')
    
    TestObject sliderThumb = new TestObject('imageDetailSliderThumb')
    sliderThumb.addProperty('xpath', ConditionType.EQUALS,
        "//span[contains(@id,'imageDetail') or contains(@id,'image-detail') or contains(@id,'image_detail')]//span[@role='slider']")
    
    TestObject inputDisplay = findTestObject('Object Repository/Core Chat/nav/Parameter/input_Image Detail')
    
    WebUI.waitForElementVisible(sliderThumb, 10)
    WebUI.waitForElementVisible(inputDisplay, 10)
    WebUI.comment('Image Detail slider and input found')

    // ============================================================
    // STEP 3: CHECK DEFAULT VALUE
    // ============================================================
    WebUI.comment('Step 3: Checking default value...')
    String defaultValue = WebUI.getAttribute(inputDisplay, 'value')
    WebUI.comment('Default Image Detail: ' + defaultValue)
    
    if (defaultValue == 'Auto') {
        WebUI.comment('TC22A PASSED - Default value is Auto')
    } else {
        WebUI.comment('FAILED: Default value should be Auto but got: ' + defaultValue)
        KeywordUtil.markFailed("FAILED: Default value should be 'Auto' but is '" + defaultValue + "'")
    }

    WebUI.takeScreenshot('TC22A_ImageDetail_Default.png')
    WebUI.comment('=== TC22A Completed ===')

} catch (Exception e) {
    WebUI.comment('TC22A FAILED: ' + e.getMessage())
    WebUI.takeScreenshot('TC22A_ImageDetail_Error.png')
    KeywordUtil.markFailedAndStop("Exception occurred: " + e.getMessage())
}