package com.myjar.jarassignment.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myjar.jarassignment.createRetrofit
import com.myjar.jarassignment.data.model.ComputerItem
import com.myjar.jarassignment.data.repository.JarRepository
import com.myjar.jarassignment.data.repository.JarRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class JarViewModel : ViewModel() {

    private val _listStringData = MutableStateFlow<List<ComputerItem>>(emptyList())
    val listStringData: StateFlow<List<ComputerItem>>
        get() = _listStringData

    private val repository: JarRepository = JarRepositoryImpl(createRetrofit())

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _filteredItems = MutableStateFlow<List<ComputerItem>>(emptyList())
    val filteredItems: StateFlow<List<ComputerItem>> = _filteredItems

    fun fetchData() {
        viewModelScope.launch {
            repository.fetchResults().collect{items ->
                _listStringData.value = items
                filterItems()
            }
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        filterItems()

    }

    private fun filterItems(){
        val query = searchQuery.value.lowercase()
        _filteredItems.value = if (query.isEmpty()) {
            listStringData.value
        } else {
            listStringData.value.filter { item ->
                item.name.lowercase().contains(query)
            }
        }

    }
}