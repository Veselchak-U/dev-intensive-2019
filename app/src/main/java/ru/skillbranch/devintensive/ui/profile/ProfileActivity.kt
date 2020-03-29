package ru.skillbranch.devintensive.ui.profile

import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Editable
import android.text.TextPaint
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_profile.*
import ru.skillbranch.devintensive.R
import ru.skillbranch.devintensive.utils.Utils
import ru.skillbranch.devintensive.viewmodels.ProfileViewModel
import kotlin.math.abs


class ProfileActivity : AppCompatActivity() {

    companion object {
        const val IS_EDIT_MODE = "IS_EDIT_MODE"
    }

    private lateinit var viewModel: ProfileViewModel
    lateinit var viewFields: Map<String, TextView>
    private var isEditMode = false
    private var isBadRepoUrl = false

    override fun onCreate(savedInstanceState: Bundle?) {
        // restore default theme after splash screen
        setTheme(R.style.AppTheme)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        initViews(savedInstanceState)
        initViewModel()
        Log.d("M_ProfileActivity", "onCreate")
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putBoolean(IS_EDIT_MODE, isEditMode)
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(ProfileViewModel::class.java)
//        viewModel.getProfileData().observe(this, Observer { updateUI(it) })
        viewModel.getTheme().observe(this, Observer { updateTheme(it) })

        // Select user avatar image if it not defined in activity
//        if (iv_avatar.drawable == null) {
//            var profileFromViewModel = viewModel.getProfileData().value
//            var initials =
//                Utils.toInitials(profileFromViewModel?.firstName, profileFromViewModel?.lastName)
//            if (initials != "") {
//                iv_avatar.setImageDrawable(createDrawableText(initials))
//            } else {
//                iv_avatar.setImageResource(R.drawable.avatar_default)
//            }
//        }
    }

    private fun updateTheme(mode: Int) {
        Log.d("M_ProfileActivity", "updateTheme")
        delegate.setLocalNightMode(mode)
    }

//    private fun updateUI(profile: Profile) {
//        profile.toMap().also {
//            for ((k, v) in viewFields) {
//                v.text = it[k].toString()
//            }
//        }
//    }

    private fun initViews(savedInstanceState: Bundle?) {
        viewFields = mapOf(
            "nickName" to tv_nick_name,
            "rank" to tv_rank,
            "firstName" to et_first_name,
            "lastName" to et_last_name,
            "about" to et_about,
            "repository" to et_repository,
            "rating" to tv_rating,
            "respect" to tv_respect
        )

        isEditMode = savedInstanceState?.getBoolean(IS_EDIT_MODE, false) ?: false
        showCurrentMode(isEditMode)

        btn_edit.setOnClickListener {
//            if (isEditMode) saveProfileInfo()
            isEditMode = !isEditMode
            showCurrentMode(isEditMode)
        }

        btn_switch_theme.setOnClickListener {
            viewModel.switchTheme()
        }

        // Validate repository URL
        et_repository.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                if (s.isNotEmpty() && !isValidGitUrl(s.toString())) {
                    wr_repository.error = "Невалидный адрес репозитория"
                    isBadRepoUrl = true
                } else {
                    wr_repository.error = null
                    isBadRepoUrl = false
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })


    }

    private fun createDrawableText(initials: String): Drawable? {
        val height = resources.getDimension(R.dimen.avatar_round_size)
        val width = resources.getDimension(R.dimen.avatar_round_size)

        val bitmap = Bitmap.createBitmap(height.toInt(), width.toInt(), Bitmap.Config.ARGB_8888)

        val textPaint = TextPaint()
        textPaint.color = Color.WHITE
        textPaint.textAlign = Paint.Align.CENTER
        textPaint.textSize = height / 2 // In px
        textPaint.typeface = Typeface.DEFAULT
        textPaint.isAntiAlias = true

        val c = Canvas()
        c.setBitmap(bitmap)
        val paint = Paint()
        paint.color = resources.getColor(R.color.color_accent, theme)
        c.drawCircle(width / 2, height / 2, height / 2, paint)

        val textBounds = Rect()
        textPaint.getTextBounds(initials, 0, initials.length, textBounds)
        c.drawText(initials, width / 2, height / 2 + abs(textBounds.exactCenterY()), textPaint)

        return BitmapDrawable(resources, bitmap)
    }

    private fun isValidGitUrl(url: String): Boolean {
        val userName =
            Utils.transliteration("${et_first_name.text}${et_last_name.text}", "").toLowerCase()
        val lowerUrl = url.toLowerCase()

        return when (lowerUrl) {
            "github.com/$userName" -> true
            "www.github.com/$userName" -> true
            "https://github.com/$userName" -> true
            "https://www.github.com/$userName" -> true
            else -> false
        }

//        enterprise
//        features
//        topics
//        collections
//        trending
//        events
//        marketplace
//        pricing
//        nonprofit
//        customer-stories
//        security
//        login
//        join

    }

    private fun showCurrentMode(isEdit: Boolean) {
        val info = viewFields.filter {
            setOf("firstName", "lastName", "about", "repository").contains(it.key)
        }

        for ((_, v) in info) {
            v as EditText
            v.isFocusable = isEdit
            v.isFocusableInTouchMode = isEdit
            v.isEnabled = isEdit
            v.background.alpha = if (isEdit) 255 else 0
        }

        ic_eye.visibility = if (isEdit) View.GONE else View.VISIBLE
        wr_about.isCounterEnabled = isEdit

        with(btn_edit) {
            val filter: ColorFilter? = if (isEdit) {
                PorterDuffColorFilter(
                    resources.getColor(R.color.color_accent, theme),
                    PorterDuff.Mode.SRC_IN
                )
            } else {
                null
            }

            val icon = if (isEdit) {
                resources.getDrawable(R.drawable.ic_save_black_24dp, theme)
            } else {
                resources.getDrawable(R.drawable.ic_edit_black_24dp, theme)
            }

            background.colorFilter = filter
            setImageDrawable(icon)
        }

    }

//    private fun saveProfileInfo() {
//        Profile(
//            firstName = et_first_name.text.toString(),
//            lastName = et_last_name.text.toString(),
//            about = et_about.text.toString(),
//            repository = if (isBadRepoUrl) "" else et_repository.text.toString()
//        ).apply {
//            viewModel.saveProfileData(this)
//        }
//    }

}


