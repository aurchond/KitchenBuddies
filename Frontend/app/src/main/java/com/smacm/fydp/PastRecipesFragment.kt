package com.smacm.fydp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import com.smacm.fydp.notifications.NotificationData
import com.smacm.fydp.notifications.PushNotification
import com.smacm.fydp.notifications.RetrofitInstance
import kotlinx.android.synthetic.main.fragment_past_recipes.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

const val TOPIC = "/topics/myTopic"

class PastRecipesFragment : Fragment() {

    val TAG = "PastRecipesFragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // allows us to subscribe to notifications from the TOPIC
        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        btn_send?.setOnClickListener() {
            val title = et_title.text.toString()
            val message = et_message.text.toString()

            // allows us to send notifications to the TOPIC
            if (title.isNotEmpty() && message.isNotEmpty()) {
                PushNotification(
                    NotificationData(title, message),
                    TOPIC
                ).also {
                    // send the push notification
                    sendNotification(it)
                }
            }
        }

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_past_recipes, container, false)
    }

    private fun sendNotification(notification: PushNotification) = CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = RetrofitInstance.api.postNotification(notification)

            if (response.isSuccessful) {
                Log.d(TAG, "Response: ${Gson().toJson(response)}")
            } else {
                Log.e(TAG, response.errorBody().toString())
            }

        } catch(e: Exception) {
            Log.e(TAG, e.toString())
        }
    }
}