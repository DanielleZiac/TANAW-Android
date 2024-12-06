package com.example.testtanaw

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.testtanaw.models.UserParcelable
import com.example.testtanaw.util.DB

class InstitutionFragment(val userData: UserParcelable) : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_institution, container, false)




        return view
    }
}
