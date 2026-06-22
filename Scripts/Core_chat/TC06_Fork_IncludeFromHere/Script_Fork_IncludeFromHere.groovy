import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import internal.GlobalVariable as GlobalVariable
import keywords.ChatKeywords

// Core Chat Setup - Login + Navigate + Select Gemini
ChatKeywords.coreChateSetup()
WebUI.delay(2)

// Ensure there is a message to fork
String testMessage = 'Fork test message - include from here'
WebUI.click(findTestObject('Core Chat/chat_input'))
WebUI.delay(1)
WebUI.clearText(findTestObject('Core Chat/chat_input'))
WebUI.setText(findTestObject('Core Chat/chat_input'), testMessage)
WebUI.delay(1)
WebUI.click(findTestObject('Core Chat/button__send-button'))
WebUI.delay(3)

// Open Fork menu and select Include all to/from here
WebUI.waitForElementPresent(findTestObject('Core Chat/Page_AIs Nature And Offer/button_Fork-Menu'), 15, FailureHandling.STOP_ON_FAILURE)
WebUI.click(findTestObject('Core Chat/Page_AIs Nature And Offer/button_Fork-Menu'))
WebUI.delay(1)
WebUI.waitForElementPresent(findTestObject('Core Chat/Page_T C Gio/Fork/Popup_Select a fork'), 10, FailureHandling.STOP_ON_FAILURE)
WebUI.click(findTestObject('Core Chat/Page_T C Gio/Fork/Opt_Include from here'))
WebUI.delay(1)

// Optionally check remember
WebUI.click(findTestObject('Core Chat/Page_T C Gio/Fork/remember-checkbox'))
WebUI.delay(1)

WebUI.waitForElementNotPresent(findTestObject('Core Chat/Page_T C Gio/Fork/Popup_Select a fork'), 10, FailureHandling.OPTIONAL)
WebUI.takeScreenshot('Fork_IncludeFromHere.png')

WebUI.comment('Fork (Include from here) test completed')
