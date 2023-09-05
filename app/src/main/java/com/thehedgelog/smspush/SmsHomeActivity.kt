package com.thehedgelog.smspush

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get

class SmsHomeActivity : AppCompatActivity() {

    val view: SmsHomeViewModel by lazy { ViewModelProvider(this@SmsHomeActivity, object: ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return SmsHomeViewModel(this@SmsHomeActivity.application as SmsApplication) as T
        }
    }).get() }

    lateinit var targetInput: EditText
    lateinit var updateTarget: Button

    private var isUpdated = false;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = Intent(this@SmsHomeActivity, NumberListActivity::class.java)
        startActivity(intent)
        finish()


        setContentView(R.layout.activity_sms_home)

        targetInput = findViewById(R.id.target)
        updateTarget = findViewById(R.id.btn_update_target)

        view.targetPhoneNumber.observe(this) { target ->
            if (target == targetInput.text.toString()) {
                isUpdated = true;
            }
            targetInput.setText(target)
        };

        updateTarget.setOnClickListener {
            if (isUpdated) {
                val number = targetInput.text.toString()
                if (view.verifyPhoneNumber(number)) {
                    view.setTargetPhoneNumber(number)
                }
                isUpdated = false
            }
        }

        updateTarget.setOnClickListener {
        }
    }

}