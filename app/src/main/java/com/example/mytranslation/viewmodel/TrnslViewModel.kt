package com.example.mytranslation.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class TrnslViewModel: ViewModel() {
    var getData:TranslationResponseUiState by mutableStateOf(TranslationResponseUiState.Loading)
//    init {
//        getLanguageList()
//    }
    var selectedTextSrc by mutableStateOf(mapOf("en" to "English").entries.first())
    var selectedTextDes by mutableStateOf(mapOf("en" to "English").entries.first())
    var text by mutableStateOf("")

    var result: TranslationResult by mutableStateOf(
        TranslationResult(
            code = 0,
            lang = "",
            text = emptyList()
        )
    )

    fun getLanguageList(){
        viewModelScope.launch {
            val trnslApi:TrnslApi = TrnslApi.getInstance()
            getData = try{
                TranslationResponseUiState.Success(trnslApi.getLanguage())
            } catch (e:Exception){
                Log.d("TRNSLVIEWMODEL",e.message.toString())
                TranslationResponseUiState.Error
            }
        }
    }

    fun trnslText(){
        viewModelScope.launch {
            val trnslApi:TrnslTextApi = TrnslTextApi.getInstance()
            try{
                result = trnslApi.getTranslate(textCode = text,langFromTo = "${selectedTextSrc.key}-${selectedTextDes.key}")
            } catch (e:Exception){
                Log.d("TRNSLVIEWMODEL",e.message.toString())
            }
        }
    }
}