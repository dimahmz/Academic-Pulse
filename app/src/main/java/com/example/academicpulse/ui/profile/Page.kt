package com.example.academicpulse.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.academicpulse.databinding.PageProfileBinding

class Page : Fragment() {

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
		val page = PageProfileBinding.inflate(inflater, container, false)
		val viewModel = ViewModelProvider(this)[ViewModel::class.java]

		viewModel.textContent.observe(viewLifecycleOwner) {
			page.textview.text = it
		}
		return page.root
	}
}