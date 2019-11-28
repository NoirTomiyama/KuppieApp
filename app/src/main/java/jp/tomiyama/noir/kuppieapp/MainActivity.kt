package jp.tomiyama.noir.kuppieapp

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var selectedItems: Array<String> = arrayOf("", "", "")
    var titleItems: Array<String> = arrayOf("時間", "場所", "内容")

    private val mOnItemSelectedListener = object : AdapterView.OnItemSelectedListener {

        // アイテムが選択されなかったとき
        override fun onNothingSelected(parent: AdapterView<*>?) {
        }

        // アイテムが選択されたとき
        override fun onItemSelected(parent: AdapterView<*>, view: View, postion: Int, id: Long) {
            val item = parent.getItemAtPosition(postion)

            // 初回起動時の動作
            if (!parent.isFocusable) {
                parent.isFocusable = true
                return
            }

            // 初回以降の動作
            parent.findViewById<TextView>(android.R.id.text1)
                .setTextColor(Color.parseColor("#000000"))

            when (parent.id) {
                R.id.timeSpinner -> {
                    selectedItems[0] = item as String
                    Log.d("item[0]", item)
                }
                R.id.placeSpinner -> {
                    selectedItems[1] = item as String
                    Log.d("item[1]", item)
                }
                R.id.contentsSpinner -> {
                    selectedItems[2] = item as String
                    Log.d("item[2]", item)
                }
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSpinnerAdapter()

        val stringBuilder = StringBuilder()

        addButton.setOnClickListener {

            for ((index, value) in selectedItems.withIndex()) {
                stringBuilder.append("${titleItems[index]} : ${value}\n")
            }
            Log.d("stringBuilder", stringBuilder.toString())
            stringBuilder.append("----------\n")
            planTextView.text = stringBuilder.toString()
        }

        scrollView.isScrollbarFadingEnabled = false

        fab.setOnClickListener {
            val sendIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_SUBJECT, "練習計画")
                putExtra(Intent.EXTRA_TEXT, stringBuilder.toString())
                type = "text/plain"
            }
            if (sendIntent.resolveActivity(packageManager) != null) {
                startActivity(sendIntent)
            }
        }
    }

    private fun setSpinnerAdapter() {
        val timeAdapter = ArrayAdapter<String>(
            this,
            R.layout.custom_spinner,
            resources.getStringArray(R.array.timeList)
        )
        val placeAdapter = ArrayAdapter<String>(
            this,
            R.layout.custom_spinner,
            resources.getStringArray(R.array.placeList)
        )
        val contentsAdapter = ArrayAdapter<String>(
            this,
            R.layout.custom_spinner,
            resources.getStringArray(R.array.contentList)
        )
        timeAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown)
        timeSpinner.adapter = timeAdapter
        placeAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown)
        placeSpinner.adapter = placeAdapter
        contentsAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown)
        contentsSpinner.adapter = contentsAdapter

        timeSpinner.isFocusable = false
        placeSpinner.isFocusable = false
        contentsSpinner.isFocusable = false

        timeSpinner.onItemSelectedListener = mOnItemSelectedListener
        placeSpinner.onItemSelectedListener = mOnItemSelectedListener
        contentsSpinner.onItemSelectedListener = mOnItemSelectedListener

    }

}
