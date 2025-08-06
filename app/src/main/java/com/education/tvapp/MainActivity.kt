package com.education.tvapp

import android.os.Bundle
import androidx.fragment.app.FragmentActivity

/**
 * 主Activity - Android TV应用的入口点
 */
class MainActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        // 添加主Fragment
        if (savedInstanceState == null) {
            val fragment = MainFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.main_browse_fragment, fragment)
                .commitNow()
        }
    }
}
