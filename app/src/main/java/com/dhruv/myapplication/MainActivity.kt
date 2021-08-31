package com.dhruv.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.dhruv.myapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var canAddOperation = false
    private var canAddDecimal = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    fun operationAction(view: View) {
        if(view is Button && canAddOperation){
            binding.workingTextView.append(view.text)
            canAddOperation = false
        }
    }
    fun numberAction(view: View)
    {
        if(view is Button )
        {
            if(view.text==".")
            {
                if (canAddDecimal)
                    binding.workingTextView.append(view.text)
                canAddDecimal = false
            }
            else
                binding.workingTextView.append(view.text)
            canAddOperation = true
        }
    }
    fun allClear(view: View) {
       binding.workingTextView.text =""
        binding.resultsTextView.text=""
    }
    fun delAction(view: View) {
        val length = binding.workingTextView.length()
        if (length>0)
        {
            binding.workingTextView.text = binding.workingTextView.text.subSequence(0,length-1)
        }
    }
    fun equalsAction(view: View) {
         binding.resultsTextView.text = calculateResults()
        
    }

    private fun calculateResults(): String {
        val digitsOperators = digitsOperators()
        if(digitsOperators.isEmpty()) return ""

        val timesDivision = timesDivisionCal(digitsOperators)
        if(timesDivision.isEmpty()) return ""

        val result = addSubCal(timesDivision)
        return result.toString()
    }

    private fun addSubCal(passedList: MutableList<Any>): Float {
        var result = passedList[0] as Float

        for(i in passedList.indices){
            if(passedList[i] is Char && i!= passedList.lastIndex)
            {
                val operator = passedList[i]
                val nextDigit = passedList[i+1] as Float
                if (operator=="+")
                    result+=nextDigit
                if (operator=="-")
                    result-=nextDigit
            }
        }

        return result
    }

    private fun timesDivisionCal(passedList: MutableList<Any>): MutableList<Any> {
        var list = passedList
        while (list.contains('x') || list.contains('/')){
            list.calcTimesDiv(list)
        }
        return list
    }
    private fun <E> MutableList<E>.calcTimesDiv(passedList: MutableList<Any>): MutableList<Any> {
        val newList = mutableListOf<Any>()
        var restartIndex = passedList.size
        for(i in passedList.indices){
            if (passedList[i] is Char  && i != passedList.lastIndex && i < restartIndex)
            {
                val operator = passedList[i]
                val prevDigit = passedList[i-1] as Float
                val nextDigit = passedList[i+1] as Float
                when(operator){
                    'x'->
                    {
                        newList.add(prevDigit * nextDigit)
                        restartIndex = i +1
                    }
                    '/'->
                    {
                        newList.add(prevDigit / nextDigit)
                        restartIndex = i +1
                    }
                    else ->
                    {
                        newList.add(prevDigit)
                        newList.add(operator)
                    }
                }
            }

           if(i>restartIndex){
               newList.add(passedList[i])
           }
        }
        return newList
    }
/*    private fun calcTimesDiv(passedList: MutableList<Any>):MutableList<Any>{

    }*/

    private fun digitsOperators(): MutableList<Any>{
        val list = mutableListOf<Any>()
        var currentDigit = ""
        for(character in binding.workingTextView.text){
            if (character.isDigit() || character == '.')
                currentDigit+=character
            else
            {
                list.add(currentDigit.toFloat())
                currentDigit = ""
                list.add(character)
            }
        }
        if (currentDigit != "")
            list.add(currentDigit.toFloat())

        return list
    }
}




