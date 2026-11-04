package com.example.biblesearchum.ui

import com.example.biblesearchum.model.BibleBook
import com.example.biblesearchum.model.BibleVerse

data class BibleUiState(
    val availableTranslations: List<String> = listOf("RV1960", "NVI", "LBLA", "RVR1960"),
    val selectedTranslation: String = "RV1960",
    val books: List<BibleBook> = emptyList(),
    val availableChapters: List<Int> = emptyList(),
    val availableVerses: List<Int> = emptyList(),
    val selectedBook: BibleBook? = null,
    val selectedChapter: Int? = null,
    val selectedVerse: Int? = null,
    val currentVerseContent: BibleVerse? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)