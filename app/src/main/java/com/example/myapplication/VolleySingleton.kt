package com.example.myapplication

import android.content.Context
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

class VolleySingleton private constructor(context: Context) {
    val requestQueue: RequestQueue

    companion object {
        private var mInstance: VolleySingleton? = null
        @Synchronized
        fun getmInstance(context: Context): VolleySingleton? {
            if (mInstance == null) {
                mInstance = VolleySingleton(context)
            }
            return mInstance
        }
    }

    init {
        requestQueue = Volley.newRequestQueue(context.getApplicationContext())
    }
}