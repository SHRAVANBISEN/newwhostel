import android.app.Application
import android.content.ContentValues.TAG
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import eu.tutorials.chatroomapp.Injection
import eu.tutorials.chatroomapp.data.Result
import eu.tutorials.chatroomapp.data.User
import eu.tutorials.chatroomapp.data.UserRepository
import kotlinx.coroutines.launch

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val userRepository: UserRepository = UserRepository(
        FirebaseAuth.getInstance(),
        Injection.instance()
    )

    private val sharedPreferences: SharedPreferences =
        application.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    val isLoading: MutableLiveData<Boolean> = MutableLiveData(false)

    fun setLoading(loading: Boolean) {
        isLoading.value = loading
    }

    private val _authResult = MutableLiveData<Result<Boolean>?>()
    val authResult: MutableLiveData<Result<Boolean>?> get() = _authResult

    private val _currentUser = MutableLiveData<Result<User>>()
    val currentUser: LiveData<Result<User>> get() = _currentUser

    private val _isUserLoggedIn = MutableLiveData<Boolean>()
    val isUserLoggedIn: LiveData<Boolean> get() = _isUserLoggedIn

    init {
        checkUserLoggedIn()
    }

    fun getCurrentUser() {
        viewModelScope.launch {
            _currentUser.value = userRepository.getCurrentUser()
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                setLoading(true)
                val result = userRepository.login(email, password)
                if (result is Result.Success) {
                    _isUserLoggedIn.value = true
                    val isPrincipal = isPrincipalUser(email, password)
                    setPrincipalUserStatus(isPrincipal)
                }
                _authResult.value = result
            } catch (e: Exception) {
                _authResult.value = Result.Error(e)
                Log.e(TAG, "Login failed", e)
            } finally {
                setLoading(false)
            }
        }
    }

    fun clearAuthResult() {
        _authResult.value = null
    }

    fun signUp(email: String, password: String, firstName: String, hostelName: String, roomNumber: String) {
        viewModelScope.launch {
            setLoading(true)
            val result = userRepository.signUp(email, password, firstName, hostelName, roomNumber)
            _authResult.value = result
            setLoading(false)
        }
    }

    fun checkUserLoggedIn() {
        _isUserLoggedIn.value = userRepository.isUserLoggedIn()
    }

    fun logout() {
        userRepository.logout()
        _isUserLoggedIn.value = false
        setPrincipalUserStatus(false) // Clear principal status on logout
    }

    private fun setPrincipalUserStatus(isPrincipal: Boolean) {
        with(sharedPreferences.edit()) {
            putBoolean("isPrincipalUser", isPrincipal)
            apply()
        }
    }

    fun isPrincipalUser(email: String, password: String): Boolean {
        return email == principalUserId && password == principalUserPassword
    }

    fun isPrincipalUserOnLaunch(): Boolean {
        return sharedPreferences.getBoolean("isPrincipalUser", false)
    }

    companion object {
        private const val principalUserId = "user4@gmail.com"
        private const val principalUserPassword = "user444"
    }
}
