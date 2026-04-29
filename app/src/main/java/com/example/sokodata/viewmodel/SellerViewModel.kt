package com.example.sokodata.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sokodata.data.SellerRepository
import com.example.sokodata.data.SellerRepositoryImpl
import com.example.sokodata.model.Seller
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class SellerViewModel(private val repository: SellerRepository = SellerRepositoryImpl()) : ViewModel() {

    // État interne des vendeurs
    private val _sellers = MutableStateFlow<List<Seller>>(emptyList())
    val sellers: StateFlow<List<Seller>> = _sellers.asStateFlow()

    // État du terme de recherche
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // Vendeurs filtrés par recherche
    private val _filteredSellers = MutableStateFlow<List<Seller>>(emptyList())
    val filteredSellers: StateFlow<List<Seller>> = _filteredSellers.asStateFlow()

    // État de chargement
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Message d'erreur
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    // Vendeur sélectionné pour édition
    private val _selectedSeller = MutableStateFlow<Seller?>(null)
    val selectedSeller: StateFlow<Seller?> = _selectedSeller.asStateFlow()

    init {
        loadAllSellers()
        setupSearchFilter()
    }

    /**
     * Charge tous les vendeurs depuis le repository
     */
    fun loadAllSellers() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val allSellers = repository.getAllSellers()
                _sellers.value = allSellers
                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value = "Erreur lors du chargement : ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Configure le filtrage réactif basé sur le terme de recherche
     */
    private fun setupSearchFilter() {
        viewModelScope.launch {
            combine(_sellers, _searchQuery) { sellers, query ->
                if (query.isEmpty()) {
                    sellers
                } else {
                    sellers.filter { seller ->
                        seller.name.contains(query, ignoreCase = true) ||
                        seller.tableNumber.contains(query, ignoreCase = true)
                    }
                }
            }.collect { filteredList ->
                _filteredSellers.value = filteredList
            }
        }
    }

    /**
     * Met à jour le terme de recherche
     */
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    /**
     * Ajoute un nouveau vendeur
     */
    fun addSeller(seller: Seller, imageBytes: ByteArray?) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val success = repository.addSeller(seller, imageBytes)
                if (success) {
                    loadAllSellers()
                    _errorMessage.value = null
                } else {
                    _errorMessage.value = "Erreur lors de l'ajout du vendeur"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Erreur : ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Met à jour un vendeur existant
     */
    fun updateSeller(seller: Seller, imageBytes: ByteArray? = null) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val success = repository.updateSeller(seller, imageBytes)
                if (success) {
                    loadAllSellers()
                    _selectedSeller.value = null
                    _errorMessage.value = null
                } else {
                    _errorMessage.value = "Erreur lors de la mise à jour"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Erreur : ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Supprime un vendeur
     */
    fun deleteSeller(sellerId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val success = repository.deleteSeller(sellerId)
                if (success) {
                    loadAllSellers()
                    _errorMessage.value = null
                } else {
                    _errorMessage.value = "Erreur lors de la suppression"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Erreur : ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Définit le vendeur sélectionné pour édition
     */
    fun selectSeller(seller: Seller) {
        _selectedSeller.value = seller
    }

    /**
     * Efface la sélection de vendeur
     */
    fun clearSelection() {
        _selectedSeller.value = null
    }

    /**
     * Efface le message d'erreur
     */
    fun clearError() {
        _errorMessage.value = null
    }
}
