package br.com.firstsoft.parlathon.data.util

import com.google.gson.Gson
import com.google.gson.internal.LinkedTreeMap
import java.lang.reflect.Type

class DataParser(private val gson: Gson) {

    fun <T> parseList(lista: List<*>, classOfT: Class<T>): ArrayList<T> {
        val list = lista
                .map { it as LinkedTreeMap<*, *> }
                .map { gson.toJsonTree(it).asJsonObject }
                .mapTo(ArrayList<T>()) { gson.fromJson(it, classOfT as Type) }

        return list
    }

    fun <T> parseObject(obj: Any, classOfT: Class<T>): T {
        val treeMap = obj as LinkedTreeMap<*, *>
        val jsonObject = gson.toJsonTree(treeMap).asJsonObject
        return gson.fromJson(jsonObject, classOfT as Type)
    }

}