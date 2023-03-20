package com.kazak.kirill.realestatecatalog.domain.authorization.useCases

import android.util.Patterns
import com.kazak.kirill.realestatecatalog.domain.authorization.AuthorizationRepository
import com.kazak.kirill.realestatecatalog.domain.authorization.AuthorizationState
import com.kazak.kirill.realestatecatalog.domain.authorization.module.UserCredentialsModule

class CheckSignInCredentialsUseCase(private val repository: AuthorizationRepository) {

    suspend fun execute(
        email: String,
        password: String,
        errorMessage: (String) -> Unit
    ): AuthorizationState {
        return if (checkEmailAddress(email, errorMessage) == AuthorizationState.SUCCESS &&
            checkPassword(password, errorMessage) == AuthorizationState.SUCCESS) {
            repository.saveUserCredentials(UserCredentialsModule(email, password))
            AuthorizationState.SUCCESS
        } else {
            AuthorizationState.ERROR
        }
    }

    private suspend fun checkEmailAddress(
        email: String,
        errorMessage: (String) -> Unit
    ): AuthorizationState {
        return when {
            email.isEmpty() -> {
                errorMessage.invoke(ENTER_EMAIL_MESSAGE)
                AuthorizationState.ERROR
            }
            !email.isEmailValid() -> {
                errorMessage.invoke(INCORRECT_EMAIL_MESSAGE)
                AuthorizationState.ERROR
            }
            email.isUserWithThisEmailExist() -> {
                errorMessage.invoke(USER_WITH_THIS_EMAIL_EXISTS_MESSAGE)
                AuthorizationState.ERROR
            }
            else -> {
                AuthorizationState.SUCCESS
            }
        }
    }

    private suspend fun checkPassword(
        password: String,
        errorMessage: (String) -> Unit
    ): AuthorizationState {
        return if (password.isEmpty()) {
            errorMessage.invoke(ENTER_PASSWORD_MESSAGE)
            AuthorizationState.ERROR
        } else {
            AuthorizationState.SUCCESS
        }
    }

    private fun String.isEmailValid(): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(this).matches()
    }

    //true if exist, false if not exist
    private fun String.isUserWithThisEmailExist(): Boolean {
        return repository.checkIfUserWithThisEmailExist(this)
    }

    private companion object {
        const val ENTER_EMAIL_MESSAGE = "Please, enter your email"
        const val INCORRECT_EMAIL_MESSAGE = "You have entered email incorrect"
        const val USER_WITH_THIS_EMAIL_EXISTS_MESSAGE = "A user with the same email already exists"
        const val ENTER_PASSWORD_MESSAGE = "Please, enter your password"
    }
}