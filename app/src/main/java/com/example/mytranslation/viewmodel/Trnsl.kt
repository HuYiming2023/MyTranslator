package com.example.mytranslation.viewmodel

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

//data class Success(
//    val translationResponse : TranslationResponse
//)

data class TranslationResponse(
    val dirs: List<String>,
    val langs: Map<String, String>
)

data class TranslationResult(
    val code: Int,
    val lang: String,
    val text: List<String>
)

sealed interface TranslationResponseUiState{
    data class Success(val translationResponse: TranslationResponse):TranslationResponseUiState
    data object Error: TranslationResponseUiState
    data object Loading: TranslationResponseUiState
}

const val TRNSL_URL = "https://translate.yandex.net/api/v1.5/tr.json/"
const val API_KEY = "trnsl.1.1.20240122T155827Z.185ec2e7a6566318.9ffcf765f3bfbf65d358f562535324cf1441e00e"

interface TrnslApi{
    @GET("getLangs")
    suspend fun getLanguage(
        @Query("key") apiKey: String = API_KEY,
        @Query("ui") languageCode: String = "en",
//        @Query("callback") callbackFunctionName: String? = null
    ):TranslationResponse

    companion object {
        private var trnslService: TrnslApi? = null

        fun getInstance(): TrnslApi{
            if (trnslService === null){
                trnslService = Retrofit.Builder()
                    .baseUrl(TRNSL_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build().create(TrnslApi::class.java)
            }
            return trnslService!!
        }
    }
}
interface TrnslTextApi{
    @GET("translate")
    suspend fun getTranslate(
        @Query("key") apiKey: String = API_KEY,
        @Query("text") textCode: String,
        @Query("lang") langFromTo: String,
//        @Query("callback") callbackFunctionName: String? = null
    ):TranslationResult

    companion object {
        private var trnslService: TrnslTextApi? = null

        fun getInstance(): TrnslTextApi{
            if (trnslService === null){
                trnslService = Retrofit.Builder()
                    .baseUrl(TRNSL_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build().create(TrnslTextApi::class.java)
            }
            return trnslService!!
        }
    }
}