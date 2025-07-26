package com.p4rfait.propinas

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import com.p4rfait.propinas.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding
    private val iva = 13.0f
    private var totalAmount: Float = 0.0f
    private var people: Int = 1
    private var selectedTipPercentage: Float = 10.0f
    private var customTip = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.llResult.visibility = View.GONE
        binding.edtCustomPercent.visibility = View.GONE

        binding.edtTotalAmount.addTextChangedListener { this.checkTotalAmountInput() }
        binding.edtPeople.addTextChangedListener { this.checkPersonInput() }
        binding.edtCustomPercent.addTextChangedListener { this.checkCustomTip() }

        binding.rgTips.setOnCheckedChangeListener { _, checkedId ->
            selectedTipPercentage = when(checkedId) {
                binding.rbTenPercent.id -> 10.0f
                binding.rbFiftyPercent.id -> 15.0f
                binding.rbTwentyPercent.id -> 20.0f
                else -> 0.0f
            }

            when(checkedId){
                binding.rbCustomPercent.id -> {
                    customTip = true
                    binding.edtCustomPercent.visibility = View.VISIBLE
                } else -> {
                    customTip = false
                    binding.edtCustomPercent.visibility = View.GONE
                }
            }
        }
        binding.btnCalculate.setOnClickListener { this.calculate() }
        binding.btnClear.setOnClickListener { this.clear() }
    }

    private fun calculate() {

        val tip = totalAmount * ( selectedTipPercentage / 100)

        var totalToPay = totalAmount
        if (binding.swIva.isChecked)
            totalToPay += (totalToPay * (iva / 100))
        totalToPay += tip

        val perPerson = totalToPay / people

        binding.tvTip.text = getString(R.string.dollar_sign, tip)
        binding.tvTotal.text = getString(R.string.dollar_sign, totalToPay)
        binding.tvPerson.text = getString(R.string.dollar_sign, perPerson)
        binding.llResult.visibility = View.VISIBLE
    }

    private fun clear() {
        binding.rbTenPercent.isChecked = true
        binding.rbFiftyPercent.isChecked = false
        binding.rbCustomPercent.isChecked = false
        binding.rbTwentyPercent.isChecked = false
        binding.edtPeople.setText("")
        binding.edtCustomPercent.setText("")
        binding.edtTotalAmount.setText("")
        binding.llResult.visibility = View.GONE
    }

    private fun checkTotalAmountInput(){
        totalAmount = if (binding.edtTotalAmount.text.toString().trim().isEmpty())
            0.0f else binding.edtTotalAmount.text.toString().trim().toFloat()
    }

    private fun checkPersonInput() {
        if (binding.edtPeople.text.toString().trim().isEmpty())
            return
        if (binding.edtPeople.text.toString().toInt() < 1)
            binding.edtPeople.setText("1")
        people = binding.edtPeople.text.toString().trim().toInt()
    }

    private fun checkCustomTip() {
        if (binding.edtCustomPercent.text.toString().trim().isEmpty())
            return
        if (customTip) {
            selectedTipPercentage = binding.edtCustomPercent.text.toString().trim().toFloat()
        }
    }

}