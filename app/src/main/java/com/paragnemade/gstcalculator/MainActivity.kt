package com.paragnemade.gstcalculator

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.paragnemade.gstcalculator.databinding.ActivityMainBinding
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val decimalFormat = DecimalFormat("##.##")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Handle edge-to-edge padding
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupButtonListeners()
    }

    private fun setupButtonListeners() {
        binding.apply {
            btnplus5.setOnClickListener { calculateGST(5.0, true) }
            btnplus12.setOnClickListener { calculateGST(12.0, true) }
            btnplus18.setOnClickListener { calculateGST(18.0, true) }
            btnplus28.setOnClickListener { calculateGST(28.0, true) }

            btnminus5.setOnClickListener { calculateGST(5.0, false) }
            btnminus12.setOnClickListener { calculateGST(12.0, false) }
            btnminus18.setOnClickListener { calculateGST(18.0, false) }
            btnminus28.setOnClickListener { calculateGST(28.0, false) }
        }
    }

    private fun calculateGST(gstPercent: Double, isAddition: Boolean) {
        val amountStr = binding.amountedittext.text.toString().trim()

        if (amountStr.isEmpty()) {
            showCustomToast("Please Enter Amount")
            return
        }

        val amount = amountStr.toDouble()
        val gstAmount: Double

        if (isAddition) {
            gstAmount = (amount * gstPercent) / 100
            binding.netamttv.text = decimalFormat.format(amount)
            binding.totalamttv.text = decimalFormat.format(amount + gstAmount)
        } else {
            gstAmount = amount - (amount / (1 + gstPercent / 100))
            binding.netamttv.text = decimalFormat.format(amount - gstAmount)
            binding.totalamttv.text = decimalFormat.format(amount)
        }

        val cgst = gstAmount / 2
        val sgst = gstAmount / 2

        binding.apply {
            gstamttv.text = decimalFormat.format(gstAmount)
            cgsttv.text = decimalFormat.format(cgst)
            sgcttv.text = decimalFormat.format(sgst)
        }

        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.amountedittext.windowToken, 0)
    }

    private fun showCustomToast(message: String) {
        val inflater: LayoutInflater = layoutInflater
        val layout: View = inflater.inflate(R.layout.custom_toast, null)

        val toastText: TextView = layout.findViewById(R.id.toastText)
        toastText.text = message
        val toastImage: ImageView = layout.findViewById(R.id.toastImage)
        toastImage.setImageResource(R.drawable.ic_launcher_background) // optional icon

        val toast = Toast(applicationContext)
        toast.duration = Toast.LENGTH_LONG
        toast.view = layout
        toast.setGravity(Gravity.CENTER_VERTICAL or Gravity.CENTER_HORIZONTAL, 0, 200)
        toast.show()
    }
}
