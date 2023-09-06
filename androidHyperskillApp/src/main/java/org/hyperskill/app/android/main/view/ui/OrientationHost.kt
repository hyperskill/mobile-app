package org.hyperskill.app.android.main.view.ui

interface OrientationHost {

    /**
     * Change the activity orientation
     * [orientation] should be one of the [android.content.pm.ActivityInfo] orientation flags
     */
    fun requestOrientation(orientation: Int)

    /**
     * Set the initial activity orientation
     * if the activity is not in changingConfigurations mode
     */
    fun backToInitialOrientation()
}