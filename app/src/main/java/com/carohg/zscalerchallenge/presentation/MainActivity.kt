package com.carohg.zscalerchallenge.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.carohg.zscalerchallenge.R
import com.carohg.zscalerchallenge.ui.theme.ZscalerChallengeTheme
import com.carohg.zscalerchallenge.utils.SignInErrorCode
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ZscalerChallengeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val viewModel: SignInViewModel by viewModels()
                    SignInScreen(viewModel)
                }
            }
        }
    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SignInScreen(viewModel: SignInViewModel) {

    var username by rememberSaveable { mutableStateOf("") }
    var isUsernameError by rememberSaveable { mutableStateOf(false) }
    var usernameError by rememberSaveable { mutableStateOf("") }

    var password by remember { mutableStateOf("") }
    var isPasswordError by rememberSaveable { mutableStateOf(false) }
    var passwordError by rememberSaveable { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally) {

        Text(text = stringResource(R.string.sign_in))

        OutlinedTextField(
            value = username,
            onValueChange = { it ->
                viewModel.setInitial()
                username = it
            },
            modifier = Modifier,
            enabled = true,
            textStyle = TextStyle(color = Color.Black),
            label = { Text(text = stringResource(R.string.username)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            isError = isUsernameError,
            supportingText = {
                if (isUsernameError) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = usernameError,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            trailingIcon = {
                if (isUsernameError)
                    Icon(Icons.Filled.Warning,
                        stringResource(R.string.error_on_username_field_icon), tint = MaterialTheme.colorScheme.error)
            },
            singleLine = true,
            maxLines = 1,
            minLines = 1,
            interactionSource = MutableInteractionSource(),
            shape = RoundedCornerShape(10.dp),
            )

        OutlinedTextField(
            value = password,
            onValueChange = {
                viewModel.setInitial()
                password = it

            },
            modifier = Modifier,
            enabled = true,
            textStyle = TextStyle(color = Color.Black),
            label = { Text(text = stringResource(R.string.password)) },
            isError = isPasswordError,
            supportingText = {
                if (isPasswordError) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = passwordError,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            trailingIcon = {
                if (isPasswordError)
                    Icon(Icons.Filled.Warning,
                        stringResource(R.string.error_on_password_field_icon), tint = MaterialTheme.colorScheme.error)
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            singleLine = true,
            maxLines = 1,
            minLines = 1,
            interactionSource = MutableInteractionSource(),
            shape = RoundedCornerShape(10.dp),
        )

        Button(onClick = {
            keyboardController?.hide()
            viewModel.signIn(username, password)
        }) {
            Text(text = stringResource(R.string.sig_in))
        }

        when (val result = viewModel.result.collectAsState().value) {
            is SignInUiState.Error -> {
                when (result.code) {
                    SignInErrorCode.EMPTY_USER -> {
                        isUsernameError = true
                        usernameError = stringResource(R.string.username_must_not_be_empty)
                    }

                    SignInErrorCode.EMPTY_PASSWORD -> {
                        isPasswordError = true
                        passwordError = stringResource(R.string.password_must_not_be_empty)
                    }

                    SignInErrorCode.EMPTY_FIELDS -> {
                        isPasswordError = true
                        passwordError = stringResource(R.string.password_must_not_be_empty)
                        isUsernameError = true
                        usernameError = stringResource(R.string.username_must_not_be_empty)
                    }

                    SignInErrorCode.SIGNIN_ERROR -> {
                        ScaffoldSnackbar(
                            result.message ?: stringResource(R.string.something_wrong_happened)
                        )
                    }
                }
            }

            SignInUiState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }

            is SignInUiState.Success -> {
                ScaffoldSnackbar(result.message)
            }

            SignInUiState.Initial -> {
                isUsernameError = false
                isPasswordError = false
            }
        }

    }

}

@Composable
fun ScaffoldSnackbar(message: String) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        floatingActionButton = {
            LaunchedEffect(key1 = message) {
                scope.launch {
                    snackbarHostState.showSnackbar(message)
                }
            }

        },
        content = { }
    )
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ZscalerChallengeTheme {

    }
}