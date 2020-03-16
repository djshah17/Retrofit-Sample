package com.example.retrofitsample.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.retrofitsample.R
import com.example.retrofitsample.adapters.UsersAdapter
import com.example.retrofitsample.api.ApiClient
import com.example.retrofitsample.response.User
import com.example.retrofitsample.utils.Utility.hideProgressBar
import com.example.retrofitsample.utils.Utility.isInternetAvailable
import com.example.retrofitsample.utils.Utility.showProgressBar
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private var listUsers: MutableList<User> = mutableListOf<User>()
    private var adapter: UsersAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listUsers = mutableListOf()

        recycler_main.layoutManager = LinearLayoutManager(this@MainActivity)
        adapter = UsersAdapter(
            this,
            listUsers
        )
        recycler_main.adapter = adapter

        if (isInternetAvailable()) {
            getUsersData()
        }

    }

    private fun getUsersData() {

        showProgressBar()

        ApiClient.apiService.getUsers().enqueue(object : Callback<MutableList<User>> {
            override fun onFailure(call: Call<MutableList<User>>, t: Throwable) {
                hideProgressBar()
                Log.e("error", t.localizedMessage)
            }

            override fun onResponse(
                call: Call<MutableList<User>>,
                response: Response<MutableList<User>>
            ) {
                hideProgressBar()
                val usersResponse = response.body()
                listUsers.clear()
                usersResponse?.let { listUsers.addAll(it) }
                adapter?.notifyDataSetChanged()
            }

        })

    }

}
