package com.example.codeforcesvisualizer

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.codeforcesvisualizer.databinding.ActivityProfileBinding
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.squareup.picasso.Picasso

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private val TAG = "ProfileActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d(TAG, "onCreate: ")
        val avatar = intent.extras?.get("avatar").toString()
        val contribution = intent.extras?.get("contribution").toString()
        val fullName: String
        if(intent.extras?.get("firstName") == null)fullName = ""
        else if(intent.extras?.get("lastName") == null)fullName = intent.extras?.get("firstName").toString()
        else fullName = intent.extras?.get("firstName").toString() + " " + intent.extras?.get("lastName").toString()
        val maxRank = intent.extras?.get("maxRank").toString()
        val maxRating = intent.extras?.get("maxRating").toString()
        val rating = intent.extras?.get("rating").toString()
        val rank = intent.extras?.get("rank").toString()
        val handle = intent.extras?.get("handle").toString()

        binding.contribution.text = "Contribution: $contribution"
        binding.fullName.text = "Name: $fullName"
        binding.handle.text = "Handle: $handle"
        binding.rank.text = "$rank max($maxRank)"
        binding.contestRating.text = "Rating: $rating max($maxRating)"
        Picasso.with(this)
            .load("https:$avatar")
            .fit()
            .centerCrop()
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.ic_launcher_foreground)
            .into(binding.imageView)

        setupLineChart()
        setupBarChart()
    }
    private fun setupLineChart(){
        val lineEntry = ArrayList<Entry>()
        val ratingArray = intent.extras?.getIntArray("Ratings")
        if (ratingArray != null) {
            for((counter, rating) in ratingArray.withIndex()){
                lineEntry.add(Entry(counter+1f,rating.toFloat()))
            }
        }
        val lineDataSet = LineDataSet(lineEntry,"User Rating")
        lineDataSet.color = ColorTemplate.getHoloBlue()
        lineDataSet.isHighlightEnabled = false
        val lineData = LineData(lineDataSet)
        with(binding) {
            ratingChart.setBackgroundColor(Color.WHITE)
            ratingChart.animateXY(6000, 6000)
            ratingChart.description.isEnabled = false
            ratingChart.setDrawGridBackground(false)
            ratingChart.setPinchZoom(true)
            ratingChart.isDragEnabled = true
            ratingChart.data = lineData
            ratingChart.zoom(6f, 0f, 10f, 10f)
        }
    }
    private fun setupBarChart(){
        val barChart = ArrayList<BarEntry>()
        val problemRating = intent.extras?.getIntArray("problemRating")
        val problemRatingCount = intent.extras?.getIntArray("problemRatingCount")
        if (problemRating != null && problemRatingCount != null) {
            for(problem in problemRating.indices){
                barChart.add(BarEntry(problemRating[problem].toFloat(),problemRatingCount[problemRating[problem]].toFloat()))
            }
        }
        val barDataSet = BarDataSet(barChart,"Problem Ratings")
        barDataSet.colors = (ColorTemplate.COLORFUL_COLORS).toList()
        barDataSet.barBorderWidth = 0f

        val barData = BarData(barDataSet)
        barData.barWidth = 70f

        val xAxis = binding.problemRatingChart.xAxis
        val stringProblemRating = ArrayList<String>()
        if (problemRating != null) {
            for(i in problemRating){
                stringProblemRating.add(i.toString())
            }
        }
        xAxis.valueFormatter = object : ValueFormatter(){
            override fun getFormattedValue(value: Float): String {
                return if(problemRating?.indexOf(value.toInt())!= -1)
                    "${problemRating?.get(problemRating.indexOf(value.toInt()))}"
                else ""
            }
        }
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(false)
        xAxis.granularity = 1f
        if (problemRating != null) {
            xAxis.labelCount = problemRating.size
        }

        binding.problemRatingChart.data = barData
        binding.problemRatingChart.animateY(2000)
        binding.problemRatingChart.zoom(6f, 0f, 10f, 10f)
        binding.problemRatingChart.description.isEnabled = false
    }
}

