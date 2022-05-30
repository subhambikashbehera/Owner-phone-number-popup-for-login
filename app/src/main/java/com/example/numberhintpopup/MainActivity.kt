package com.example.numberhintpopup

import android.app.Activity
import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.credentials.Credential
import com.google.android.gms.auth.api.credentials.Credentials
import com.google.android.gms.auth.api.credentials.HintRequest
import com.google.android.gms.auth.api.identity.GetPhoneNumberHintIntentRequest
import com.google.android.gms.auth.api.identity.Identity

class MainActivity : AppCompatActivity() {

   // lateinit var resultLauncher:ActivityResultLauncher<IntentSenderRequest>

    lateinit var editText: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        editText=findViewById(R.id.editTextTextPersonName)

        //old type
//        resultLauncher=registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()){
//            if (it.resultCode==Activity.RESULT_OK){
//                val credentials:Credential?=it.data?.getParcelableExtra(Credential.EXTRA_KEY)
//                val number=credentials?.id
//                Toast.makeText(this,number.toString(),Toast.LENGTH_SHORT).show()
//                editText.setText(number.toString())
//            }
//        }
        val request: GetPhoneNumberHintIntentRequest = GetPhoneNumberHintIntentRequest.builder().build()
        val phoneNumberHintIntentResultLauncher = registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
                try {
                    val phoneNumber = Identity.getSignInClient(this).getPhoneNumberFromIntent(result.data)
                    Log.d("getPhoneNumber", "onCreate: $phoneNumber")
                } catch(e: Exception) {
                    Log.e("number", "Phone Number Hint failed")
                }
            }

        Identity.getSignInClient(this)
            .getPhoneNumberHintIntent(request)
            .addOnSuccessListener {
                try {
                    phoneNumberHintIntentResultLauncher.launch(
                        IntentSenderRequest.Builder(it.intentSender).build())
                } catch(e: Exception) {
                    Log.e("number", e.printStackTrace().toString())
                }
            }.addOnFailureListener {
                it.printStackTrace()
            }

        editText.setOnFocusChangeListener { view, b ->
            if (b){
                showPopUp()
            }
        }



    }
// old one
    private fun showPopUp() {
//        val hintRequest=HintRequest.Builder()
//            .setPhoneNumberIdentifierSupported(true)
//            .build()
//
//        val intent=Credentials.getClient(this).getHintPickerIntent(hintRequest)
//        resultLauncher.launch(IntentSenderRequest.Builder(intent).build())
    }

}