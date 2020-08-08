package com.samriddha.covid19trackerkotlin.ui

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.samriddha.covid19trackerkotlin.R
import kotlinx.android.synthetic.main.fragment_about_app.*


class AboutAppFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_about_app, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        linkedIn.movementMethod = LinkMovementMethod.getInstance()
        github.movementMethod = LinkMovementMethod.getInstance()

    }


    }
