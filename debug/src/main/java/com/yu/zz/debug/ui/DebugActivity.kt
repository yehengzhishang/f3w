package com.yu.zz.debug.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.google.android.material.snackbar.Snackbar
import com.yu.zz.debug.DebugManager
import com.yu.zz.debug.R
import com.yu.zz.debug.arrange.getAttrColor
import kotlinx.android.synthetic.main.activity_debug.*

class DebugActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(DebugManager.INSTANCE.themeId)
        setContentView(R.layout.activity_debug)
        themeAttrColor()
        setSupportActionBar(barDebug)
        btnMore.setOnClickListener { openOptionsMenu() }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_debug, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        fun openTicker() {
            Snackbar.make(tvUserName, "车票 进行开发中，敬请期待", Snackbar.LENGTH_SHORT).show()
        }

        fun openInterfaceMessage() {
            Snackbar.make(tvUserName, "接口信息 开发中，敬请期待", Snackbar.LENGTH_SHORT).show()
        }
        return when (item.itemId) {
            R.id.action_ticker -> {
                openTicker()
                true
            }
            R.id.action_interface -> {
                openInterfaceMessage()
                true
            }
            R.id.action_more -> {
                dlRoot.openDrawer(GravityCompat.START)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        if (dlRoot.isDrawerOpen(GravityCompat.START)) {
            dlRoot.closeDrawers()
            return
        }
        super.onBackPressed()
    }

    private fun themeAttrColor() {
        btnMore.setTextColor(getAttrColor(0, 0xFFFFFF))
    }
}

