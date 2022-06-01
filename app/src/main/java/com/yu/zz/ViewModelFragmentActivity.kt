package com.yu.zz

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.yu.zz.fwww.R
import com.yu.zz.topbook.employ.TopBookViewModel


class ViewModelFragmentActivity : AppCompatActivity() {
    private val fragment = ViewModelFragment();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragment_view_model)
        addFragment()
        findViewById<View>(R.id.btn_debug).setOnClickListener {
            log()
        }
        findViewById<View>(R.id.btn_remove).setOnClickListener { removeFragment() }
    }

    private fun addFragment() {
        supportFragmentManager.beginTransaction()
            .add(R.id.fl, fragment, fragment.toString())
            .show(fragment).commitAllowingStateLoss()
    }

    private fun log() {
        Log.e("rain", "debug")
    }

    private fun removeFragment() {
        supportFragmentManager.beginTransaction().remove(fragment).commitAllowingStateLoss()
    }

}

class ViewModelFragment : Fragment() {
    private val view: TopBookViewModel by lazy {
        ViewModelProvider(this)[TopBookViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        view.getApplication<App>().toString()
        return inflater.inflate(R.layout.activity_main, container, false)
    }

    override fun onDestroy() {
        super.onDestroy()

        Log.e("rain", "destroy")
    }


}