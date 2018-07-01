package br.com.firstsoft.parlathon.data.util

import android.content.Context
import android.widget.Toast
import br.com.firstsoft.parlathon.business.vo.AppResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HttpUtils {

    companion object {

        fun <T> callback(success: (Response<T>) -> Unit, failure: (t: Throwable) -> Unit): Callback<T>? {
            return object : Callback<T> {
                override fun onResponse(call: Call<T>, response: retrofit2.Response<T>) = success(response)
                override fun onFailure(call: Call<T>, t: Throwable) = failure(t)
            }
        }

        fun parseResponse(listener: (AppResponse?, Throwable?) -> Unit): Callback<AppResponse>? {
            return callback(
                    { response ->
                        if (response.body() != null) {
                            listener(response.body(), null)
                        } else {
                            listener(null, Throwable(response.errorBody()?.string()))
                        }
                    },
                    { throwable -> listener(null, throwable) }
            )
        }

        fun parseResponse(context: Context, listener: (AppResponse) -> Unit): Callback<AppResponse>? {
            return callback(
                    { response ->
                        if (response.body() != null) {
                            listener(response.body()!!)
                        } else {
                            Toast.makeText(context, "Não foi possível recuperar os dados", Toast.LENGTH_SHORT).show()
                            Throwable(response.errorBody()?.string()).printStackTrace()
                        }
                    },
                    { throwable ->
                        Toast.makeText(context, "Não foi possível recuperar os dados", Toast.LENGTH_SHORT).show()
                        throwable.printStackTrace()
                    }
            )
        }

        fun parseLoginResponse(listener: (AppResponse?, Throwable?, cookie: String?) -> Unit): Callback<AppResponse>? {
            return callback(
                    {
                        if (it.body() != null) listener(it.body(), null, it.headers().get("Set-Cookie")) else listener(null, Throwable(it.errorBody()?.string()), null)
                    },
                    { listener(null, it, null) }
            )
        }

    }
}