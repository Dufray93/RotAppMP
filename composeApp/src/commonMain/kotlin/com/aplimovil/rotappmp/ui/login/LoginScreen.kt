package com.aplimovil.rotappmp.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import rotappmp.composeapp.generated.resources.Res
import rotappmp.composeapp.generated.resources.bg
import rotappmp.composeapp.generated.resources.login_button
import rotappmp.composeapp.generated.resources.login_email_hint
import rotappmp.composeapp.generated.resources.login_forgot_password
import rotappmp.composeapp.generated.resources.login_password_hint
import rotappmp.composeapp.generated.resources.login_title
import rotappmp.composeapp.generated.resources.rotapp_logo

// Paleta base del fondo.
private val LoginBackgroundColor = Color(0xFFFBEFED)
// Color de la tarjeta que contiene el formulario.
private val LoginCardColor = Color(0xFFF6DCD7)
// Tono principal utilizado en botones y cursor.
private val LoginPrimary = Color(0xFF8C3A33)
// Texto secundario reutilizado en títulos y enlaces.
private val LoginTextSecondary = Color(0xFF4C2D27)
// Altura máxima del bloque hero antes de comenzar a reducirse en pantallas chicas.
private val LoginHeroHeight = 360.dp
// Distancia con la que la tarjeta se solapa sobre el hero para recrear el mockup.
private val LoginCardTopOverlap = 330.dp
// Zoom aplicado a la imagen del hero para centrar el detalle del fondo.
private const val LoginHeroZoom = 1.45f
// Desplazamiento vertical extra del hero tras aplicar el zoom.
private val LoginHeroVerticalOffset = (-35).dp

/**
 * Pantalla de inicio de sesión multiplataforma. Recibe el estado del `LoginViewModel` y reenvía las
 * acciones de usuario mediante lambdas para mantener la lógica fuera del composable.
 */
@OptIn(ExperimentalResourceApi::class, ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    email: String,
    password: String,
    onBackClick: () -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    onForgotPasswordClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                            tint = LoginTextSecondary,
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    navigationIconContentColor = LoginTextSecondary,
                ),
            )
        },
        containerColor = LoginBackgroundColor,
    ) { innerPadding ->
        val topInset = innerPadding.calculateTopPadding()
        val bottomInset = innerPadding.calculateBottomPadding()
        BoxWithConstraints(
            modifier = modifier
                .fillMaxSize()
                .padding(bottom = bottomInset)
                .background(LoginBackgroundColor),
        ) {
            val availableHeight = maxHeight - bottomInset
            val adaptiveHeroHeight = minOf(LoginHeroHeight, availableHeight * 0.45f)
            val adaptiveCardTopOverlap = minOf(LoginCardTopOverlap, adaptiveHeroHeight - 40.dp)

            LoginHero(topInset = topInset, heroHeight = adaptiveHeroHeight)
            LoginContent(
                email = email,
                password = password,
                onEmailChange = onEmailChange,
                onPasswordChange = onPasswordChange,
                onLoginClick = onLoginClick,
                onForgotPasswordClick = onForgotPasswordClick,
                topInset = topInset,
                cardTopOverlap = adaptiveCardTopOverlap,
            )
        }
    }
}

/**
 * Bloque superior que renderiza el fondo + logo aplicando zoom y desplazamiento para mantener la composición en escritorio/web.
 *
 * @param topInset Desfase superior que respeta barras del sistema.
 * @param heroHeight Altura calculada para el bloque hero según el alto disponible.
 */
@OptIn(ExperimentalResourceApi::class)
@Composable
private fun LoginHero(topInset: Dp, heroHeight: Dp) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = topInset),
    ) {
        Image(
            painter = painterResource(Res.drawable.bg),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(heroHeight)
                .align(Alignment.TopCenter)
                .graphicsLayer {
                    scaleX = LoginHeroZoom
                    scaleY = LoginHeroZoom
                    translationY = LoginHeroVerticalOffset.toPx()
                },
            contentScale = ContentScale.Crop,
        )
        Image(
            painter = painterResource(Res.drawable.rotapp_logo),
            contentDescription = null,
            modifier = Modifier
                .size(180.dp)
                .align(Alignment.TopCenter)
                .offset(y = 32.dp),
        )
    }
}

/**
 * Contenedor de la tarjeta con los campos y acciones; permite scroll cuando el alto disponible es reducido.
 *
 * @param email Valor actual del campo de correo.
 * @param password Valor actual del campo de contraseña.
 * @param onEmailChange Callback para propagar cambios en el correo.
 * @param onPasswordChange Callback para propagar cambios en la contraseña.
 * @param onLoginClick Acción principal del botón.
 * @param onForgotPasswordClick Acción secundaria "Olvidé mi contraseña".
 * @param topInset Desfase superior que aporta Scaffold.
 * @param cardTopOverlap Distancia de superposición entre hero y tarjeta.
 */
@Composable
private fun LoginContent(
    email: String,
    password: String,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    topInset: Dp,
    cardTopOverlap: Dp,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
            .padding(top = topInset + cardTopOverlap, bottom = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(36.dp),
            colors = CardDefaults.cardColors(containerColor = LoginCardColor),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 28.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = stringResource(Res.string.login_title),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = LoginTextSecondary,
                )

                Spacer(modifier = Modifier.height(32.dp))

                LoginTextField(
                    value = email,
                    onValueChange = onEmailChange,
                    placeholder = stringResource(Res.string.login_email_hint),
                    keyboardType = KeyboardType.Email,
                )

                Spacer(modifier = Modifier.height(24.dp))

                LoginTextField(
                    value = password,
                    onValueChange = onPasswordChange,
                    placeholder = stringResource(Res.string.login_password_hint),
                    keyboardType = KeyboardType.Password,
                    isPassword = true,
                )

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = onLoginClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .shadow(elevation = 10.dp, shape = RoundedCornerShape(28.dp), clip = false),
                    shape = RoundedCornerShape(28.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = LoginPrimary,
                        contentColor = Color.White,
                    ),
                ) {
                    Text(text = stringResource(Res.string.login_button))
                }

                Spacer(modifier = Modifier.height(16.dp))

                TextButton(onClick = onForgotPasswordClick) {
                    Text(
                        text = stringResource(Res.string.login_forgot_password),
                        color = LoginTextSecondary,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }
        }
    }
}

/**
 * Campo reutilizable para correo/contraseña con el mismo estilo de subrayado y paleta.
 *
 * @param value Texto actual del campo.
 * @param onValueChange Callback que entrega el nuevo texto.
 * @param placeholder Texto guía que se muestra cuando el campo está vacío.
 * @param keyboardType Tipo de teclado sugerido.
 * @param isPassword Define si aplica transformación de contraseña.
 */
@Composable
private fun LoginTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    keyboardType: KeyboardType,
    isPassword: Boolean = false,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .drawBehind {
                val strokeWidth = 1.dp.toPx()
                drawLine(
                    color = Color(0xFFB9A8A3),
                    start = androidx.compose.ui.geometry.Offset(0f, size.height),
                    end = androidx.compose.ui.geometry.Offset(size.width, size.height),
                    strokeWidth = strokeWidth,
                )
            },
        placeholder = { Text(text = placeholder, color = Color(0xFF9C8C87)) },
        textStyle = MaterialTheme.typography.bodyLarge.copy(color = LoginTextSecondary),
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        singleLine = true,
        shape = RoundedCornerShape(8.dp),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = Color.Transparent,
            focusedBorderColor = Color.Transparent,
            disabledBorderColor = Color.Transparent,
            errorBorderColor = Color.Transparent,
            cursorColor = LoginPrimary,
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
        ),
    )
}
