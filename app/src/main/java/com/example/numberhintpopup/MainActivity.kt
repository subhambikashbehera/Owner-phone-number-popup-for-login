package com.example.numberhintpopup

import android.app.Activity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.credentials.Credential
import com.google.android.gms.auth.api.credentials.Credentials
import com.google.android.gms.auth.api.credentials.HintRequest

class MainActivity : AppCompatActivity() {

    lateinit var resultLauncher:ActivityResultLauncher<IntentSenderRequest>
    lateinit var editText: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        editText=findViewById(R.id.editTextTextPersonName)

        resultLauncher=registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()){
            if (it.resultCode==Activity.RESULT_OK){
                val credentials:Credential?=it.data?.getParcelableExtra(Credential.EXTRA_KEY)
                val number=credentials?.id
                Toast.makeText(this,number.toString(),Toast.LENGTH_SHORT).show()
                editText.setText(number.toString())
            }
        }



        editText.setOnFocusChangeListener { view, b ->
            if (b){
                showPopUp()
            }
        }
    }

    private fun showPopUp() {
        val hintRequest=HintRequest.Builder()
            .setPhoneNumberIdentifierSupported(true)
            .build()

        val intent=Credentials.getClient(this).getHintPickerIntent(hintRequest)
        resultLauncher.launch(IntentSenderRequest.Builder(intent).build())
    }


}