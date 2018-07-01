package br.com.firstsoft.parlathon.view.activities.abs

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import br.com.firstsoft.parlathon.view.viewmodel.AccountViewModel
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.FacebookSdk
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.GoogleApiClient
import java.util.*

/**
 * Created by danilolemes on 23/12/2017.
 */
abstract class AccountActivity : AppCompatActivity(), FacebookCallback<LoginResult> {

    protected val accountViewModel by lazy { ViewModelProviders.of(this).get(AccountViewModel::class.java) }

    private var callbackManager: CallbackManager? = null
    private var mGoogleApiClient: GoogleApiClient? = null
    private val RC_SIGN_IN = 9001

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            accountViewModel.handleGoogleSignInResult(result, this)
        } else {
            callbackManager?.onActivityResult(requestCode, resultCode, data)
        }
    }

    protected fun setupGoogle() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
        mGoogleApiClient = GoogleApiClient.Builder(this)
                .enableAutoManage(this, {})
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build()
    }

    protected fun setupFacebook() {
        callbackManager = CallbackManager.Factory.create()
        FacebookSdk.setIsDebugEnabled(true)
        LoginManager.getInstance().registerCallback(callbackManager, this)
    }

    fun facebookLogin() {
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"))
    }

    fun googleLogin() {
        val signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient)
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    /**
     * Facebook callback
     */

    override fun onError(error: FacebookException?) {
        Toast.makeText(this, "Não foi possível realizar login", Toast.LENGTH_SHORT).show()
    }

    override fun onSuccess(result: LoginResult?) {
        accountViewModel.facebookLogin(result?.accessToken?.token!!)
    }

    override fun onCancel() {

    }
}