package com.example.codeforcesvisualizer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.codeforcesvisualizer.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private val base_url = "https://codeforces.com/api/"
    private lateinit var binding: ActivityMainBinding
    private lateinit var userInfoHandle: UserInfo
    private val TAG = "MainActivity"
    private val problemRating = ArrayList<Int>()
    private val problemRatingCount = Array<Int>(4000) { 0 }
    private var AC = 0
    private var WA = 0
    private var TLE = 0
    private var OTHER = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.analyzeBtn.setOnClickListener{
            doNetworkCalls()
        }

    }

    private fun doNetworkCalls(){
        val retrofit = Retrofit.Builder()
            .baseUrl(base_url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val codeforcesApi = retrofit.create(CodeforcesApi::class.java)
        val activityIntent = Intent(applicationContext, ProfileActivity::class.java)
        val handle = binding.handleEt.text.toString().trim()
        userInfoCall(handle,codeforcesApi,activityIntent)
        userRatingCall(handle, codeforcesApi, activityIntent)
        userStatusCall(handle, codeforcesApi, activityIntent)

    }

    private fun userInfoCall(
        handle:String,
        codeforcesApi: CodeforcesApi,
        activityIntent: Intent
    ){
        val userInfoCall = codeforcesApi.getUser(handle)
        userInfoCall.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                Log.d(TAG, "onResponse: userInfoCall")
                if(response.body()?.status != "OK")return
                userInfoHandle = response.body()?.result?.get(0)!!
                activityIntent.putExtra("avatar", userInfoHandle.avatar)
                    .putExtra("contribution", userInfoHandle.contribution)
                    .putExtra("firstName", userInfoHandle.firstName)
                    .putExtra("lastName", userInfoHandle.lastName)
                    .putExtra("friendOfCount", userInfoHandle.friendOfCount)
                    .putExtra("maxRank", userInfoHandle.maxRank)
                    .putExtra("maxRating", userInfoHandle.maxRating)
                    .putExtra("rank", userInfoHandle.rank)
                    .putExtra("rating", userInfoHandle.rating)
                    .putExtra("handle", handle)

            }

            override fun onFailure(call: Call<User>, t: Throwable) {

            }
        })
    }

    private fun userRatingCall(
        handle: String,
        codeforcesApi: CodeforcesApi,
        activityIntent: Intent
    ){
        val userRatingCall = codeforcesApi.getUserRating(handle)
        userRatingCall.enqueue(object : Callback<UserRating>{
            override fun onResponse(call: Call<UserRating>, response: Response<UserRating>) {
                val userRating = response.body()?.result
                if(response.body()?.status != "OK")return
                val ratingArray = ArrayList<Int>()
                ratingArray.add(0)
                if (userRating != null) {
                    for(rating in userRating){
                        ratingArray.add(rating.newRating)
                    }
                }
                activityIntent.putExtra("Ratings",ratingArray.toIntArray())
            }

            override fun onFailure(call: Call<UserRating>, t: Throwable) {
            }
        })
    }

    private fun userStatusCall(
        handle: String,
        codeforcesApi: CodeforcesApi,
        activityIntent: Intent
    ){
        val userStatusCall = codeforcesApi.getUserStatus(handle)
        userStatusCall.enqueue(object : Callback<UserStatus>{
            override fun onResponse(call: Call<UserStatus>, response: Response<UserStatus>) {
                val submissionList = response.body()?.result
                if (submissionList != null) {
                    for(submission in submissionList){
                        problemRatingCount[submission.problem.rating?:0]++
                        Log.d(TAG, "onResponse: ${submission.problem.rating}")
                        when (submission.verdict) {
                            "OK" -> AC++
                            "WRONG_ANSWER" -> WA++
                            "TIME_LIMIT_EXCEEDED" -> TLE++
                            else -> OTHER++
                        }
                    }
                }
                for(i in 1..3999){
                    if(problemRatingCount[i]!=0)problemRating.add(i)
                }
                activityIntent.putExtra("problemRating",problemRating.toIntArray())
                    .putExtra("problemRatingCount",problemRatingCount.toIntArray())
                    .putExtra("AC",AC)
                    .putExtra("WA",WA)
                    .putExtra("TLE",TLE)
                    .putExtra("OTHER",OTHER)
                startActivity(activityIntent)
            }

            override fun onFailure(call: Call<UserStatus>, t: Throwable) {
            }
        })
    }



}