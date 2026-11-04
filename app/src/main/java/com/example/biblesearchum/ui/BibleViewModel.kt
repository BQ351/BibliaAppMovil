package com.example.biblesearchum.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.biblesearchum.model.BibleBook
import com.example.biblesearchum.network.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BibleViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(BibleUiState())
    val uiState: StateFlow<BibleUiState> = _uiState.asStateFlow()

    init {
        fetchBooks()
    }

    fun onTranslationSelected(translation: String) {
        _uiState.update { 
            it.copy(
                selectedTranslation = translation,
                books = emptyList(),
                selectedBook = null,
                selectedChapter = null,
                selectedVerse = null,
                availableChapters = emptyList(),
                availableVerses = emptyList(),
                currentVerseContent = null
            ) 
        }
        fetchBooks()
    }

    fun fetchBooks() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val currentTranslation = _uiState.value.selectedTranslation
                val bookList = RetrofitInstance.api.getBooks(currentTranslation)
                _uiState.update { 
                    it.copy(books = bookList, isLoading = false) 
                }
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        isLoading = false, 
                        errorMessage = "Error al cargar libros (${_uiState.value.selectedTranslation}): ${e.message ?: e.javaClass.simpleName}"
                    ) 
                }
            }
        }
    }

    fun onBookSelected(book: BibleBook) {
        val chaptersList = (1..book.chapters).toList()
        _uiState.update {
            it.copy(
                selectedBook = book,
                availableChapters = chaptersList,
                selectedChapter = null,
                selectedVerse = null,
                availableVerses = emptyList(),
                currentVerseContent = null
            )
        }
    }

    fun onChapterSelected(chapter: Int) {
        // Asumimos un máximo genérico de 150 versículos por capítulo para consulta
        val versesList = (1..150).toList()
        _uiState.update {
            it.copy(
                selectedChapter = chapter,
                availableVerses = versesList,
                selectedVerse = null,
                currentVerseContent = null
            )
        }
    }

    fun onVerseSelected(verse: Int) {
        val currentBook = _uiState.value.selectedBook ?: return
        val currentChapter = _uiState.value.selectedChapter ?: return

        _uiState.update { it.copy(selectedVerse = verse, isLoading = true, errorMessage = null) }
        
        viewModelScope.launch {
            try {
                val currentTranslation = _uiState.value.selectedTranslation
                val result = RetrofitInstance.api.getVerse(currentTranslation, currentBook.bookId, currentChapter, verse)
                _uiState.update {
                    it.copy(currentVerseContent = result, isLoading = false)
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "No se pudo obtener el versículo solicitado."
                    )
                }
            }
        }
    }

    fun clearSelection() {
        _uiState.update {
            BibleUiState(books = it.books)
        }
    }
}