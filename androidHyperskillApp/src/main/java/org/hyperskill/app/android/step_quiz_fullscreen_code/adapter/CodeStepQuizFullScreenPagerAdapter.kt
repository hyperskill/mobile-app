package org.hyperskill.app.android.step_quiz_fullscreen_code.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.viewpager.widget.PagerAdapter
import org.hyperskill.app.android.R

class CodeStepQuizFullScreenPagerAdapter(
    private val context: Context,
) : PagerAdapter() {

    private val layouts: List<Pair<View, String>>
    init {
        val result = listOf(
            inflateLayout(R.layout.layout_step_quiz_code_fullscreen_instruction,  org.hyperskill.app.R.string.step_quiz_code_full_screen_details_tab),
            inflateLayout(R.layout.layout_step_quiz_code_fullscreen_playground, org.hyperskill.app.R.string.step_quiz_code_full_screen_code_tab)
        )

        layouts = result
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = layouts[position].first
        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, view: Any) {
        container.removeView(layouts[position].first)
    }

    override fun isViewFromObject(p0: View, p1: Any): Boolean =
        p0 == p1

    override fun getPageTitle(position: Int): CharSequence =
        layouts[position].second

    override fun getCount(): Int =
        layouts.size

    fun getViewAt(position: Int): View =
        layouts[position].first

    private fun inflateLayout(@LayoutRes layoutId: Int, @StringRes stringId: Int): Pair<View, String> =
        View.inflate(context, layoutId, null) to context.resources.getString(stringId)
}