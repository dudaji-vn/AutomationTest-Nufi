import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject
import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testng.keyword.TestNGBuiltinKeywords as TestNGKW
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import internal.GlobalVariable as GlobalVariable
import org.openqa.selenium.Keys as Keys

import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.util.KeywordUtil

/**
 * TC24E_Parameters_Reasoning_Effort_Medium
 * Verify dragging Reasoning Effort slider to Medium
 *
 * Test Flow:
 * 1. Open Parameters panel
 * 2. Get Reasoning Effort slider
 * 3. Drag slider to Medium and verify
 */

WebUI.comment('=== TC24E: Reasoning Effort - Drag to Medium ===')

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
	// STEP 2: GET REASONING EFFORT SLIDER
	// ============================================================
	WebUI.comment('Step 2: Getting Reasoning Effort slider...')
	
	TestObject sliderThumb = new TestObject('reasoningEffortSliderThumb')
	sliderThumb.addProperty('xpath', ConditionType.EQUALS,
		"//span[contains(@id,'reasoning') or contains(@id,'Reasoning')]//span[@role='slider']")
	
	TestObject inputDisplay = findTestObject('Object Repository/Core Chat/nav/Parameter/input_Reasoning Effort')
	
	WebUI.waitForElementVisible(sliderThumb, 10)
	WebUI.waitForElementVisible(inputDisplay, 10)
	WebUI.comment('Reasoning Effort slider and input found')

	// ============================================================
	// STEP 3: GET SLIDER WIDTH
	// ============================================================
	WebUI.comment('Step 3: Getting slider width...')
	
	WebUI.click(sliderThumb)
	WebUI.delay(0.5)
	
	String widthScript = "return document.evaluate(\"//span[contains(@id,'reasoning') or contains(@id,'Reasoning')]\", document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue.getBoundingClientRect().width;"
	double sliderWidth = (Double) WebUI.executeJavaScript(widthScript, null)
	WebUI.comment('Slider width: ' + sliderWidth + 'px')
	
	double stepSize = sliderWidth / 6

	// ============================================================
	// STEP 4: DRAG TO MEDIUM
	// ============================================================
	WebUI.comment('Step 4: Dragging slider to Medium...')
	
	int targetPosition = (int)(4 * stepSize) // Position 66.67%
	int dragOffset = targetPosition - 0
	
	WebUI.comment('Dragging to position ' + targetPosition + ' (offset: ' + dragOffset + 'px)')
	WebUI.dragAndDropByOffset(sliderThumb, dragOffset, 0)
	WebUI.delay(1.5)
	
	String actualValue = WebUI.getAttribute(inputDisplay, 'value')
	WebUI.comment('After dragging to Medium: ' + actualValue)
	
	if (actualValue == 'Medium') {
		WebUI.comment('TC24E PASSED - Slider moved to Medium successfully')
	} else {
		WebUI.comment('WARNING: First attempt failed. Trying again...')
		WebUI.dragAndDropByOffset(sliderThumb, (int)(stepSize * 0.5), 0)
		WebUI.delay(1.5)
		actualValue = WebUI.getAttribute(inputDisplay, 'value')
		WebUI.comment('After retry: ' + actualValue)
		
		if (actualValue == 'Medium') {
			WebUI.comment('TC24E PASSED - Slider moved to Medium on second attempt')
		} else {
			WebUI.comment('FAILED: Could not drag slider to Medium. Current value: ' + actualValue)
			KeywordUtil.markFailed("FAILED: Could not drag slider to Medium. Current value: " + actualValue)
		}
	}

	WebUI.takeScreenshot('TC24E_ReasoningEffort_Medium.png')
	WebUI.comment('=== TC24E Completed ===')

} catch (Exception e) {
	WebUI.comment('TC24E FAILED: ' + e.getMessage())
	WebUI.takeScreenshot('TC24E_ReasoningEffort_Error.png')
	KeywordUtil.markFailedAndStop("Exception occurred: " + e.getMessage())
}