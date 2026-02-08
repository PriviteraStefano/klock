package org.stefanoprivitera.klock.features.auth.presentation
//
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.dp
//
////package org.stefanoprivitera.klock.features.auth.presentation
//
//@Composable
//fun AuthScreen(authViewModel: AuthViewModel) {
//    val state by authViewModel.state
//
//    var email by remember { mutableStateOf("") }
//    var password by remember { mutableStateOf("") }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        when (state) {
//            AuthState.Idle, AuthState.Unauthenticated -> {
//                TextField(
//                    value = email,
//                    onValueChange = { email = it },
//                    label = { Text("Email") },
//                    modifier = Modifier.fillMaxWidth()
//                )
//
//                TextField(
//                    value = password,
//                    onValueChange = { password = it },
//                    label = { Text("Password") },
//                    visualTransformation = PasswordVisualTransformation(),
//                    modifier = Modifier.fillMaxWidth()
//                )
//
//                Button(
//                    onClick = {
//                        authViewModel.processIntent(
//                            AuthIntent.Login(email, password)
//                        )
//                    }
//                ) {
//                    Text("Login")
//                }
//            }
//
//            AuthState.Loading -> {
//                CircularProgressIndicator()
//            }
//
//            is AuthState.Authenticated -> {
//                val user = (state as AuthState.Authenticated).user
//                Text("Welcome, ${user.fullName}!")
//
//                Button(
//                    onClick = {
//                        authViewModel.processIntent(AuthIntent.Logout)
//                    }
//                ) {
//                    Text("Logout")
//                }
//            }
//
//            is AuthState.Error -> {
//                val error = state as AuthState.Error
//                Text(error.message, color = Color.Red)
//
//                Button(
//                    onClick = {
//                        authViewModel.processIntent(AuthIntent.DismissError)
//                    }
//                ) {
//                    Text("Dismiss")
//                }
//            }
//
//            else -> {}
//        }
//    }
//}
