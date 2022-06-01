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
import com.yu.zz.debug.databinding.ActivityDebugBinding

class DebugActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityDebugBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(DebugManager.INSTANCE.themeId)
        mBinding = ActivityDebugBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        themeAttrColor()
        setSupportActionBar(mBinding.barDebug)
        mBinding.btnMore.setOnClickListener { openOptionsMenu() }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_debug, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val openTicker: () -> Boolean = {
            Snackbar.make(mBinding.tvUserName, "车票 进行开发中，敬请期待", Snackbar.LENGTH_SHORT).show()
            true;
        }

        fun openInterfaceMessage() {
            Snackbar.make(mBinding.tvUserName, "接口信息 开发中，敬请期待", Snackbar.LENGTH_SHORT).show()
        }
        return when (item.itemId) {
            R.id.action_ticker -> {
                openTicker()
            }
            R.id.action_interface -> {
                openInterfaceMessage()
                true
            }
            R.id.action_more -> {
                mBinding.dlRoot.openDrawer(GravityCompat.START)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    override fun onBackPressed() {
        if (mBinding.dlRoot.isDrawerOpen(GravityCompat.START)) {
            mBinding.dlRoot.closeDrawers()
            return
        }
        super.onBackPressed()
    }

    private fun themeAttrColor() {
        mBinding.btnMore.setTextColor(getAttrColor(0, 0xFFFFFF))
    }
}

