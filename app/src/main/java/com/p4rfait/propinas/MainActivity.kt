package com.p4rfait.propinas

import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.p4rfait.propinas.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding

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

        // view-binding NO los "saque de internet (ni IA)", esto esta en la documentacion oficial
        // https://developer.android.com/topic/libraries/view-binding
        binding.edtCustomPercent.visibility = View.GONE
        binding.llResult.visibility = View.GONE

        var tip = 10.0f

        binding.btnCalculate.setOnClickListener { this.calculateTip(
            binding.edtTotalAmount.getText().toString().toFloat(),
            if (binding.rbCustomPercent.isChecked) binding.edtCustomPercent.text.toString().toFloat() / 100 else tip /100,
            if (binding.edtPeople.getText().toString().isEmpty()) 1 else binding.edtPeople.getText().toString().toInt() ) }

        binding.btnClear.setOnClickListener { this.clear() }

        // A esto le di mucha vuelta y pirueta, pero esta forma me resulto bastante elegante :)
        binding.rgTips.setOnCheckedChangeListener { _, checkedId ->
            tip = when (checkedId) {
                binding.rbTenPercent.id -> 10.0f
                binding.rbFiftyPercent.id -> 15.0f
                binding.rbTwentyPercent.id -> 20.0f
                else -> 0.0f
            }

            binding.edtCustomPercent.visibility = when (checkedId) {
                binding.rbCustomPercent.id -> View.VISIBLE
                else -> View.GONE
            }
        }

    }

    private fun calculateTip(totalAmount:Float, tipPercentage:Float, people:Int){
        val tip : Float = totalAmount * tipPercentage
        val iva = if (binding.swIva.isChecked) 0.16f else 0.0f
        binding.tvTip.text = "$" + tip
        binding.tvTotal.text = "$${totalAmount + tip + (totalAmount * iva)}"
        binding.tvPerson.text = "$${((totalAmount + tip) + (totalAmount * iva)) / people}"
        if (binding.llResult.visibility != View.VISIBLE)
            binding.llResult.visibility = View.VISIBLE
    }

    private fun clear() {
        if (binding.llResult.visibility != View.GONE)
            binding.llResult.visibility = View.GONE
        binding.edtTotalAmount.setText("")
        binding.edtPeople.setText("")
    }

}