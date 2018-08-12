package com.example.czd.kotlinone

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.tech_fragment.*
import kotlinx.android.synthetic.main.tech_fragment.view.*


class TcFragment : Fragment() {
    lateinit internal var view: View
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.tech_fragment, container, false)
        setview(view)
        return view
    }

    fun setview(view: View) {
        this.view = view

        view.list_view

    }


}
