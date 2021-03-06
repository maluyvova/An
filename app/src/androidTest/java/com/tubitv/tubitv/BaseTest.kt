package com.tubitv.tubitv

/**
 * Created by vburian on 2/19/18.
 */
import android.content.Intent
import android.support.test.InstrumentationRegistry
import android.support.test.uiautomator.*
import android.util.Log
import android.support.test.uiautomator.BySelector
import org.hamcrest.CoreMatchers
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
open class BaseTest {

    protected lateinit var uiDevice: UiDevice
    @Before
    fun setUp() {

        uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        killApp()
        clearAppData()
        launchApp()
        SignIn()
        casting()
    }
    fun killApp() = executeShellCommand("am force-stop " + appPackage)
    fun executeShellCommand(command: String) {
        val stdOut = uiDevice.executeShellCommand(command)
        Log.i("Test", command + ": " + stdOut)
    }
    fun clearAppData() = executeShellCommand("pm clear " + appPackage)



    protected fun launchApp() {
        uiDevice.pressHome()
        val launcherPackage = uiDevice.launcherPackageName
        assertThat(launcherPackage, CoreMatchers.notNullValue())
        uiDevice.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)),
                globalTimeout)
        Log.i("Test", "launching the app")
        val context = InstrumentationRegistry.getContext()
        val intent = context.packageManager
                .getLaunchIntentForPackage(appPackage)
        assertThat("Application is not installed or disabled (Package not found or cannot be launched)", intent, CoreMatchers.notNullValue())
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        context.startActivity(intent)
        uiDevice.wait(Until.hasObject(By.pkg(appPackage).depth(0)),
                globalTimeout)
    }


    protected fun  SignIn() {
        val titleBox= UiCollection(UiSelector().resourceId("com.tubitv:id/empty_holder"))
        val castPopUp=uiDevice.findObject(UiSelector().resourceId("com.tubitv:id/cast_featurehighlight_help_text_header_view"))
        uiDevice.findObject(UiSelector().packageName("com.tubitv").text("Sign In")).click()
        uiDevice.findObject(UiSelector().resourceId("com.tubitv:id/email")).setText("tubitv@tubitv.tubitv")
        uiDevice.findObject(UiSelector().resourceId("com.tubitv:id/password")).setText("tubitv")
        uiDevice.findObject(UiSelector().resourceId("com.tubitv:id/sign_in_button")).click()

        assertThat(uiDevice.findObject(UiSelector().resourceId("com.tubitv:id/nav_app_bar_main_logo"))
                .waitForExists(Configurator.getInstance().waitForSelectorTimeout), CoreMatchers.`is`(true))

    }





    protected fun casting(){

        val custButton=uiDevice.findObject(UiSelector().description("Cast button. Disconnected"))
        val castButton= uiDevice.findObject(UiSelector().className("android.view.View"))
       // val d=uiDevice.findObject(BySelector().descContains(""))

        if(uiDevice.findObject(UiSelector().resourceId("com.tubitv:id/cast_featurehighlight_help_text_header_view")).waitForExists(globalTimeout)){
            custButton.click()
            uiDevice.pressBack()

        }
    }


}



