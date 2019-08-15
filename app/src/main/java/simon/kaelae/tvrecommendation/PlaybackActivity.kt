package simon.kaelae.tvrecommendation

import android.app.PictureInPictureParams
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Bundle
import android.view.KeyEvent
import android.view.SurfaceView
import android.view.View
import android.view.WindowManager
import android.widget.RadioButton
import android.widget.Toast
import androidx.fragment.app.FragmentActivity


class PlaybackActivity : FragmentActivity() {
    var id: Int = -1
    var shortPress = false
    var longPress = false
    var mVideoSurface: SurfaceView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //id = intent.getIntExtra("id",0)
        //Toast.makeText(this, channe_id.toString(), Toast.LENGTH_LONG).show()
        val sharedPreference = getSharedPreferences("layout", Context.MODE_PRIVATE)
        if (sharedPreference.getString("player", "exoplayer") == "originalplayer") {
            supportFragmentManager
                .beginTransaction()
                .replace(android.R.id.content, PlaybackVideoFragment())
                .commitAllowingStateLoss()
        }
        else {
            supportFragmentManager
                .beginTransaction()
                .replace(android.R.id.content, PlaybackVideoExoFragment())
                .commitAllowingStateLoss()
        }

        gofullscreen()
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    private fun isTV(): Boolean {
        return packageManager.hasSystemFeature(PackageManager.FEATURE_LEANBACK)

    }

    override fun onUserLeaveHint() {
        val sharedPreference = getSharedPreferences("layout", Context.MODE_PRIVATE)


        if (sharedPreference.getString("player", "exoplayer") == "originalplayer"||sharedPreference.getString("player", "exoplayer") == "exoplayer") {
            if (isTV() || android.os.Build.VERSION.SDK_INT <= 25) {
                //Toast.makeText(this, "no PIP support", Toast.LENGTH_LONG).show()
            } else {
                try {
                    val params = PictureInPictureParams.Builder()

                        .build()
                    //Toast.makeText(this, "PIP triggred", Toast.LENGTH_LONG).show()
                    enterPictureInPictureMode(params)
                } catch (e: Exception) {
                    Toast.makeText(this, "Picture-in-picture mode error", Toast.LENGTH_LONG).show()
                }
            }
        }


    }

    override fun onPictureInPictureModeChanged(
        isInPictureInPictureMode: Boolean,
        newConfig: Configuration
    ) {
        if (isInPictureInPictureMode) {

            // Hide the full-screen UI (controls, etc.) while in picture-in-picture mode.
        } else {
            // Restore the full-screen UI.
            gofullscreen()
        }
    }

    override fun onResume() {
        super.onResume()
        gofullscreen()
    }

    override fun onPause() {
        super.onPause()

        // If called while in PIP mode, do not pause playback
        if (android.os.Build.VERSION.SDK_INT > 23) {
            if (isInPictureInPictureMode) {
            } else {

            }
        }
    }

    override fun onStop() {
        super.onStop()
        //Toast.makeText(this, "onstop", Toast.LENGTH_LONG).show()
        this.finish()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        adjustFullScreen(newConfig)

    }

    private fun adjustFullScreen(config: Configuration) {

        if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            gofullscreen()

        } else {
            gofullscreen()

        }
    }


    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        gofullscreen()

    }


    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        //Toast.makeText(this, "dispatchKeyEvent", Toast.LENGTH_LONG).show()

        lateinit var direction: String

        if (
            event.keyCode == KeyEvent.KEYCODE_CHANNEL_UP ||
            event.keyCode == KeyEvent.KEYCODE_DPAD_UP ||
            event.keyCode == KeyEvent.KEYCODE_DPAD_LEFT ||
            event.keyCode == KeyEvent.KEYCODE_MEDIA_PREVIOUS ||
            event.keyCode == KeyEvent.KEYCODE_MEDIA_REWIND ||
            event.keyCode == KeyEvent.KEYCODE_MEDIA_SKIP_BACKWARD ||
            event.keyCode == KeyEvent.KEYCODE_MEDIA_STEP_BACKWARD ||
            event.keyCode == KeyEvent.KEYCODE_NAVIGATE_PREVIOUS ||
            event.keyCode == KeyEvent.KEYCODE_SYSTEM_NAVIGATION_LEFT
        ) {
            direction = "PREVIOUS"
        } else if (
            event.keyCode == KeyEvent.KEYCODE_CHANNEL_DOWN ||
            event.keyCode == KeyEvent.KEYCODE_DPAD_DOWN ||
            event.keyCode == KeyEvent.KEYCODE_DPAD_RIGHT ||
            event.keyCode == KeyEvent.KEYCODE_MEDIA_NEXT ||
            event.keyCode == KeyEvent.KEYCODE_MEDIA_FAST_FORWARD ||
            event.keyCode == KeyEvent.KEYCODE_MEDIA_SKIP_FORWARD ||
            event.keyCode == KeyEvent.KEYCODE_MEDIA_STEP_FORWARD ||
            event.keyCode == KeyEvent.KEYCODE_NAVIGATE_NEXT ||
            event.keyCode == KeyEvent.KEYCODE_SYSTEM_NAVIGATION_RIGHT
        ) {
            direction = "NEXT"
        } else {
            // Toast.makeText(this, direction, Toast.LENGTH_LONG).show()

            return super.dispatchKeyEvent(event)
        }

        if (event.action == KeyEvent.ACTION_UP) {
            //Toast.makeText(this, direction, Toast.LENGTH_LONG).show()

            PlaybackVideoExoFragment().channelSwitch(direction, true)
        }

        return true
    }

    fun gofullscreen(){
        val decorView = window.decorView
        decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
    }

}
