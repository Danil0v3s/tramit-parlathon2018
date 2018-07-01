package br.com.firstsoft.parlathon.view.viewmodel

import android.app.Activity
import android.arch.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignInResult

/**
 * Created by danilolemes on 23/12/2017.
 */
class AccountViewModel : ViewModel() {

//    @Inject
//    lateinit var accountService: AccountService
//
//    private var progressDialog: ProgressDialog? = null
//
//    init {
//        DataApplication.serviceComponent.inject(this)
//    }

    fun facebookLogin(token: String) {
//        accountService.oAuthFB(token)
    }

    fun handleGoogleSignInResult(result: GoogleSignInResult, context: Activity) {
//        if (result.isSuccess) {
//            progressDialog = DialogUtil.getDialog(context, "Aguarde...")
//            progressDialog?.show()
//            val acct = result.signInAccount
//            val scopes = "oauth2:profile email"
//
//            doAsync {
//                val token = GoogleAuthUtil.getToken(context.applicationContext, acct?.email, scopes)
//                progressDialog?.dismiss()
//                googleLogin(token)
//            }
//
//        }
    }

    fun googleLogin(token: String) {
//        accountService.oAuthGoogle(token)
    }


}