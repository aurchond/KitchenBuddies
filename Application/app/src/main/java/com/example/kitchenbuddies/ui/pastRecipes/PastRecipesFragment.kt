package com.example.kitchenbuddies.ui.pastRecipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.kitchenbuddies.databinding.FragmentPastRecipesBinding

class PastRecipesFragment : Fragment() {

    private var _binding: FragmentPastRecipesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val PastRecipesViewModel =
            ViewModelProvider(this).get(PastRecipesViewModel::class.java)

        _binding = FragmentPastRecipesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textPastRecipes
        PastRecipesViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}