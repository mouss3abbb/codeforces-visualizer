package com.example.codeforcesvisualizer

import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.codeforcesvisualizer.databinding.ActivityProfileBinding
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.squareup.picasso.Picasso

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private val TAG = "ProfileActivity"
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val avatar = intent.extras?.get("avatar").toString()
        val contribution = intent.extras?.get("contribution").toString()
        val fullName: String = if(intent.extras?.get("firstName") == null) ""
        else if(intent.extras?.get("lastName") == null) intent.extras?.get("firstName").toString()
        else intent.extras?.get("firstName").toString() + " " + intent.extras?.get("lastName").toString()
        val maxRank = intent.extras?.get("maxRank").toString()
        val maxRating = intent.extras?.get("maxRating").toString()
        val rating = intent.extras?.get("rating").toString()
        val rank = intent.extras?.get("rank").toString()
        val handle = intent.extras?.get("handle").toString()

        binding.contribution.text = "$contribution"
        binding.contribution.setTextColor(when{
            binding.contribution.text.toString().toInt() > 0 -> Color.GREEN
            else -> Color.DKGRAY
        })
        binding.fullName.text = "$fullName "
        binding.handle.text = "$handle "
        binding.rank.text = "$rank "
        binding.maxRank.text = "max $maxRank"
        binding.maxRating.text = "max $maxRating"
        binding.contestRating.text = "$rating "
        val maxRatingInt = maxRating.toInt()
        val ratingInt = rating.toInt()

        binding.contestRating.setTextColor(
            getColor(
                when (ratingInt) {
                    in 0..1199 -> R.color.newbie
                    in 1200..1399 -> R.color.pupil
                    in 1400..1599 -> R.color.specialist
                    in 1600..1899 -> R.color.expert
                    in 1900..2099 -> R.color.cm
                    in 2100..2299 -> R.color.m
                    in 2300..2399 -> R.color.im
                    in 2400..2599 -> R.color.gm
                    in 2600..2999 -> R.color.igm
                    else -> R.color.lgm
                })
        )

        binding.maxRating.setTextColor(
            getColor(
            when (maxRatingInt) {
                in 0..1199 -> R.color.newbie
                in 1200..1399 -> R.color.pupil
                in 1400..1599 -> R.color.specialist
                in 1600..1899 -> R.color.expert
                in 1900..2099 -> R.color.cm
                in 2100..2299 -> R.color.m
                in 2300..2399 -> R.color.im
                in 2400..2599 -> R.color.gm
                in 2600..2999 -> R.color.igm
                else -> R.color.lgm
            })
        )

        binding.rank.setTextColor(
            getColor(
                when (ratingInt) {
                    in 0..1199 -> R.color.newbie
                    in 1200..1399 -> R.color.pupil
                    in 1400..1599 -> R.color.specialist
                    in 1600..1899 -> R.color.expert
                    in 1900..2099 -> R.color.cm
                    in 2100..2299 -> R.color.m
                    in 2300..2399 -> R.color.im
                    in 2400..2599 -> R.color.gm
                    in 2600..2999 -> R.color.igm
                    else -> R.color.lgm
                })
        )

        binding.maxRank.setTextColor(
            getColor(
            when (maxRatingInt) {
                in 0..1199 -> R.color.newbie
                in 1200..1399 -> R.color.pupil
                in 1400..1599 -> R.color.specialist
                in 1600..1899 -> R.color.expert
                in 1900..2099 -> R.color.cm
                in 2100..2299 -> R.color.m
                in 2300..2399 -> R.color.im
                in 2400..2599 -> R.color.gm
                in 2600..2999 -> R.color.igm
                else -> R.color.lgm
            })
        )

        Picasso.with(this)
            .load("$avatar")
            .fit()
            .centerCrop()
            .placeholder(R.drawable.ic_pfp)
            .error(R.drawable.ic_pfp)
            .into(binding.imageView)

        setupLineChart()
        setupBarChart()
        setupPieChart()
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
            ratingChart.animateXY(6000, 6000)
            ratingChart.description.isEnabled = false
            ratingChart.setDrawGridBackground(false)
            ratingChart.setPinchZoom(true)
            ratingChart.isDragEnabled = true
            ratingChart.data = lineData
            ratingChart.zoom(6f, 0f, 10f, 10f)
        }
    }

    private fun setupPieChart(){
        val pieEntry = ArrayList<PieEntry>()

        val AC = intent.extras?.get("AC").toString().toFloat()
        val WA = intent.extras?.get("WA").toString().toFloat()
        val TLE = intent.extras?.get("TLE").toString().toFloat()
        val OTHER = intent.extras?.get("OTHER").toString().toFloat()
        val sum = AC+WA+TLE+OTHER
        pieEntry.add(PieEntry(AC/sum,"ACC"))
        pieEntry.add(PieEntry(WA/sum,"WA"))
        pieEntry.add(PieEntry(TLE/sum,"TLE"))
        pieEntry.add(PieEntry(OTHER/sum,"Others"))

        val pieDataSet = PieDataSet(pieEntry,"Verdicts")
        pieDataSet.colors = ColorTemplate.MATERIAL_COLORS.toMutableList()
        val data = PieData(pieDataSet)
        data.setDrawValues(true)
        data.setValueFormatter(PercentFormatter(binding.verdicts))
        binding.verdicts.data = data
        binding.verdicts.setUsePercentValues(true)
        binding.verdicts.description.isEnabled = false
        binding.verdicts.isDrawHoleEnabled = false
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
        barDataSet.colors = (ColorTemplate.MATERIAL_COLORS).toList()
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

