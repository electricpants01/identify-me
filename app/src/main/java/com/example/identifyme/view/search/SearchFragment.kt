package com.example.identifyme.view.search

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.activityViewModels
import com.example.identifyme.R
import com.example.identifyme.databinding.FragmentSearchBinding
import org.bouncycastle.util.encoders.Base64


class SearchFragment : Fragment() {
    lateinit var searchBinding: FragmentSearchBinding
    val searchViewModel: SearchViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        searchBinding = FragmentSearchBinding.bind(view)
        return searchBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initSearchView()
        initSubscriptions()
    }

    private fun initSearchView(){
        searchBinding.mySearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                searchBinding.mySearchView.clearFocus()
                searchViewModel.search(query)
                return true
            }
            override fun onQueryTextChange(newText: String): Boolean {
                return true
            }
        })
    }

    private fun initSubscriptions(){
        searchViewModel.searchedPerson.observe(viewLifecycleOwner) {
            val nombreCompleto = it.nombres + " " + it.appaterno + " " + it.apmaterno
            searchBinding.nombreCompleto.text = nombreCompleto
            searchBinding.fechaNac.text = it.fnacimiento
            searchBinding.carnet.text = it.numdoc
            val decodedString = Base64.decode(it.foto)
            val decodedByte = android.graphics.BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
            searchBinding.imageView.setImageBitmap(decodedByte)
        }
    }

}