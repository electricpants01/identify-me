package com.example.identifyme.view.search

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo3.exception.ApolloException
import com.example.graphql.GetUserDocumentQuery
import com.example.identifyme.CudapEncryption
import com.example.identifyme.apollo.MyApolloClient
import com.example.identifyme.model.Person
import kotlinx.coroutines.launch

class SearchViewModel: ViewModel() {
    val searchedPerson = MutableLiveData<Person.Data.BuscaPorDocumento>()

    fun search(ciUsuario: String){
        viewModelScope.launch {
            val client = MyApolloClient().setUpApolloClient()
            val newKey = CudapEncryption.encrypt(ciUsuario)
            try {
                val response = client.query(GetUserDocumentQuery(ciUsuario,"CEDULA DE IDENTIDAD","",newKey.toString())).execute()
                response.data?.buscaPorDocumento?.first()?.let {myPerson ->
                    searchedPerson.postValue(Person.Data.BuscaPorDocumento(
                        myPerson.apmaterno,
                        myPerson.appaterno,
                        myPerson.fnacimiento.toString(),
                        myPerson.foto,
                        myPerson.nombres,
                        myPerson.numdoc,
                        myPerson.respuesta
                    ))
                }
            } catch (e : ApolloException){
                Log.d("chris","search query went wrong")
            }
        }
    }
}