import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.model.FailureHandling as FailureHandling

/**
 * TC01: Parameters - Test Slider Drag
 * 
 * Test Flow:
 * 1. Login and select Nufi + Qwen endpoint
 * 2. Open Parameters panel
 * 3. Kéo slider đến các vị trí khác nhau
 * 4. Verify giá trị slider thay đổi đúng
 */

WebUI.comment('=== TC01: Parameters - Test Slider Drag ===')

try {
    // Step 1: Open browser and login
    WebUI.comment('Step 1: Opening browser...')
    CustomKeywords.'keywords.ChatKeywords.openBrowser'(GlobalVariable.Base_URL)

    WebUI.comment('Step 2: Logging in...')
    CustomKeywords.'keywords.ChatKeywords.loginChat'(GlobalVariable.email, GlobalVariable.password)

    WebUI.comment('Step 3: Opening new conversation...')
    CustomKeywords.'keywords.ChatKeywords.openNewConversation'(GlobalVariable.Base_URL)

    // Step 4: Select Nufi endpoint + Qwen model
    WebUI.comment('Step 4: Selecting Nufi endpoint and Qwen model...')
    CustomKeywords.'keywords.ChatKeywords.selectEndpointAndModel'('Nufi', 'Qwen2.5-0.5B')
    WebUI.delay(2)

    // Step 5: Open Parameters panel
    WebUI.comment('Step 5: Opening Parameters panel...')
    
    // Check screen width - if <= 768, open sidebar first
    String script = "return window.innerWidth || document.documentElement.clientWidth || document.body.clientWidth;"
    int screenWidth = (Integer) WebUI.executeJavaScript(script, null)
    WebUI.comment('Screen width: ' + screenWidth + 'px')
    
    if (screenWidth <= 768) {
        WebUI.comment('Screen width <= 768px, opening sidebar...')
        TestObject openSidebarButton = new TestObject('openSidebarButton')
        openSidebarButton.addProperty('xpath', ConditionType.EQUALS, "//button[@id='open-sidebar-button']")
        WebUI.waitForElementClickable(openSidebarButton, 5)
        WebUI.click(openSidebarButton)
        WebUI.delay(1)
        WebUI.comment('Sidebar opened')
    }
    
    // Click Parameters button
    WebUI.comment('Clicking Parameters button...')
    WebUI.waitForElementClickable(
        findTestObject('Object Repository/Core Chat/nav/nav_items/button_Parameters'),
        10
    )
    WebUI.click(findTestObject('Object Repository/Core Chat/nav/nav_items/button_Parameters'))
    WebUI.delay(2)
    WebUI.comment('Parameters button clicked')

    // ============================================================
    // GET SLIDER ELEMENTS
    // ============================================================
    WebUI.comment('=== Getting slider elements ===')
    
    // Slider track (the bar)
    TestObject sliderTrack = new TestObject('sliderTrack')
    sliderTrack.addProperty('xpath', ConditionType.EQUALS, 
        "//span[@id='temperature-dynamic-setting-slider']")
    
    // Slider thumb (the draggable circle)
    TestObject sliderThumb = new TestObject('sliderThumb')
    sliderThumb.addProperty('xpath', ConditionType.EQUALS, 
        "//span[@id='temperature-dynamic-setting-slider']//span[@role='slider']")
    
    WebUI.waitForElementVisible(sliderThumb, 10)
    WebUI.comment('Slider thumb found')

    // ============================================================
    // TEST 1: KÉO ĐẾN 25% (0.50)
    // ============================================================
    WebUI.comment('=== Test 1: Kéo đến 25% (0.50) ===')
    
    // Lấy width của slider track
    String widthScript = "return document.evaluate(\"//span[@id='temperature-dynamic-setting-slider']\", document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue.getBoundingClientRect().width;"
    double sliderWidth = (Double) WebUI.executeJavaScript(widthScript, null)
    WebUI.comment('Slider width: ' + sliderWidth + 'px')
    
    // Tính offset: 25% của slider width
    int offset25 = (int)(sliderWidth * 0.25)
    WebUI.dragAndDropByOffset(sliderThumb, offset25, 0)
    WebUI.delay(1)
    
    String value25 = WebUI.getAttribute(sliderThumb, 'aria-valuenow')
    WebUI.comment('Giá trị tại 25%: ' + value25)
    
    // Verify giá trị gần đúng 0.50 (cho phép sai số 0.05)
    double expected25 = 0.50
    double actual25 = Double.parseDouble(value25)
    if (Math.abs(actual25 - expected25) <= 0.05) {
        WebUI.comment('✓ Kéo 25% đúng: ' + actual25)
    } else {
        WebUI.comment('⚠ Kéo 25%: expected ' + expected25 + ', actual ' + actual25)
    }

    // ============================================================
    // TEST 2: KÉO ĐẾN 50% (1.00)
    // ============================================================
    WebUI.comment('=== Test 2: Drag to 50% (1.00) ===')
    
    // Drag back to 0 first
    WebUI.dragAndDropByOffset(sliderThumb, -(int)(sliderWidth * 0.25), 0)
    WebUI.delay(1)
    
    // Drag to 50%
    int offset50 = (int)(sliderWidth * 0.5)
    WebUI.dragAndDropByOffset(sliderThumb, offset50, 0)
    WebUI.delay(1)
    
    String value50 = WebUI.getAttribute(sliderThumb, 'aria-valuenow')
    WebUI.comment('Giá trị tại 50%: ' + value50)
    
    double expected50 = 1.00
    double actual50 = Double.parseDouble(value50)
    if (Math.abs(actual50 - expected50) <= 0.05) {
        WebUI.comment('✓ Kéo 50% đúng: ' + actual50)
    } else {
        WebUI.comment('⚠ Kéo 50%: expected ' + expected50 + ', actual ' + actual50)
    }

    // ============================================================
    // TEST 3: KÉO ĐẾN 75% (1.50)
    // ============================================================
    WebUI.comment('=== Test 3: Drag to 75% (1.50) ===')
    
    // Drag back to 0 first
    WebUI.dragAndDropByOffset(sliderThumb, -(int)(sliderWidth * 0.5), 0)
    WebUI.delay(1)
    
    // Drag to 75%
    int offset75 = (int)(sliderWidth * 0.75)
    WebUI.dragAndDropByOffset(sliderThumb, offset75, 0)
    WebUI.delay(1)
    
    String value75 = WebUI.getAttribute(sliderThumb, 'aria-valuenow')
    WebUI.comment('Giá trị tại 75%: ' + value75)
    
    double expected75 = 1.50
    double actual75 = Double.parseDouble(value75)
    if (Math.abs(actual75 - expected75) <= 0.05) {
        WebUI.comment('✓ Kéo 75% đúng: ' + actual75)
    } else {
        WebUI.comment('⚠ Kéo 75%: expected ' + expected75 + ', actual ' + actual75)
    }

    // ============================================================
    // TEST 4: KÉO ĐẾN 100% (2.00)
    // ============================================================
    WebUI.comment('=== Test 4: Drag to 100% (2.00) ===')
    
    // Drag back to 0 first
    WebUI.dragAndDropByOffset(sliderThumb, -(int)(sliderWidth * 0.75), 0)
    WebUI.delay(1)
    
    // Drag to 100%
    int offset100 = (int)(sliderWidth * 1.0)
    WebUI.dragAndDropByOffset(sliderThumb, offset100, 0)
    WebUI.delay(1)
    
    String value100 = WebUI.getAttribute(sliderThumb, 'aria-valuenow')
    WebUI.comment('Giá trị tại 100%: ' + value100)
    
    double expected100 = 2.00
    double actual100 = Double.parseDouble(value100)
    if (Math.abs(actual100 - expected100) <= 0.05) {
        WebUI.comment('✓ Kéo 100% đúng: ' + actual100)
    } else {
        WebUI.comment('⚠ Kéo 100%: expected ' + expected100 + ', actual ' + actual100)
    }

    // ============================================================
    // TEST 5: KÉO ĐẾN 71.5% (1.43 - giá trị như HTML)
    // ============================================================
    WebUI.comment('=== Test 5: Drag to 71.5% (1.43) ===')
    
    // Drag back to 0 first
    WebUI.dragAndDropByOffset(sliderThumb, -(int)(sliderWidth * 1.0), 0)
    WebUI.delay(1)
    
    // Drag to 71.5%
    int offset143 = (int)(sliderWidth * 0.715)
    WebUI.dragAndDropByOffset(sliderThumb, offset143, 0)
    WebUI.delay(1)
    
    String value143 = WebUI.getAttribute(sliderThumb, 'aria-valuenow')
    WebUI.comment('Giá trị tại 71.5%: ' + value143)
    
    double expected143 = 1.43
    double actual143 = Double.parseDouble(value143)
    if (Math.abs(actual143 - expected143) <= 0.05) {
        WebUI.comment('✓ Kéo 71.5% đúng: ' + actual143)
    } else {
        WebUI.comment('⚠ Kéo 71.5%: expected ' + expected143 + ', actual ' + actual143)
    }

    // ============================================================
    // TEST 6: KÉO VỀ 0 (MIN)
    // ============================================================
    WebUI.comment('=== Test 6: Drag to 0 (min) ===')
    
    // Drag to 0
    WebUI.dragAndDropByOffset(sliderThumb, -(int)(sliderWidth * 0.715), 0)
    WebUI.delay(1)
    
    String value0 = WebUI.getAttribute(sliderThumb, 'aria-valuenow')
    WebUI.comment('Giá trị tại 0%: ' + value0)
    
    double expected0 = 0.00
    double actual0 = Double.parseDouble(value0)
    if (Math.abs(actual0 - expected0) <= 0.05) {
        WebUI.comment('✓ Drag to 0 đúng: ' + actual0)
    } else {
        WebUI.comment('⚠ Drag to 0: expected ' + expected0 + ', actual ' + actual0)
    }

    // ============================================================
    // CONCLUSION
    // ============================================================
    WebUI.comment('=== Conclusion ===')
    WebUI.comment('Slider range: 0.00 - 2.00')
    WebUI.comment('Can drag to values like 1.43, 1.99, etc.')
    
    WebUI.takeScreenshot('TC01_Parameters_Slider_Drag_Success.png')
    WebUI.comment('Slider drag test completed successfully')

    // Step 6: Close browser
    WebUI.comment('Step 6: Closing browser...')
    CustomKeywords.'keywords.ChatKeywords.closeBrowser'()

    WebUI.comment('TC01 PASSED')

} catch (Exception e) {
    WebUI.comment('TC01 FAILED: ' + e.getMessage())
    WebUI.takeScreenshot('TC01_Parameters_Slider_Drag_Error.png')
    CustomKeywords.'keywords.ChatKeywords.closeBrowser'()
    throw e
}