package com.aplimovil.rotappmp.ui.roleselection

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

private val RoleBackground = Color(0xFFFBEFED)
private val RolePrimary = Color(0xFF8C3A33)
private val RoleText = Color(0xFF4C2D27)
private val RoleCardBorder = Color(0xFFE4BDB7)
private val RoleSelectedBackground = Color(0xFFF4D7D1)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoleSelectionScreen(
    state: RoleSelectionUiState,
    onRoleSelected: (RoleOption) -> Unit,
    onContinue: () -> Unit,
    onBackClick: () -> Unit,
    events: Flow<RoleSelectionEvent>?,
    onNavigateNext: () -> Unit,
) {
    LaunchedEffect(events) {
        events?.collectLatest { event ->
            if (event is RoleSelectionEvent.NavigateNext) onNavigateNext()
        }
    }

    Scaffold(
        containerColor = RoleBackground,
        topBar = {
            TopAppBar(
                title = { Text(text = "Selecciona tu rol", color = RoleText) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                            tint = RoleText,
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            Text(
                text = "¿Cómo quieres usar RotApp?",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = RoleText,
            )
            Text(
                text = "Elige el rol que mejor refleje tu día a día para personalizar la experiencia.",
                style = MaterialTheme.typography.bodyMedium,
                color = RoleText.copy(alpha = 0.75f),
            )

            RoleCard(
                title = "Administrador",
                description = "Gestiona turnos, organiza equipos y supervisa la operación completa.",
                selected = state.selectedRole == RoleOption.ADMIN,
                onClick = { onRoleSelected(RoleOption.ADMIN) },
            )
            RoleCard(
                title = "Colaborador",
                description = "Consulta tus turnos, solicita cambios y mantente al día con tus responsabilidades.",
                selected = state.selectedRole == RoleOption.COLLABORATOR,
                onClick = { onRoleSelected(RoleOption.COLLABORATOR) },
            )

            if (state.errorMessage != null) {
                Text(
                    text = state.errorMessage,
                    color = Color(0xFFB00020),
                    style = MaterialTheme.typography.bodyMedium,
                )
            }

            Button(
                onClick = onContinue,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = RolePrimary,
                    contentColor = Color.White,
                ),
            ) {
                Text(text = "Continuar")
            }
        }
    }
}

@Composable
private fun RoleCard(
    title: String,
    description: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val background = if (selected) RoleSelectedBackground else Color.White
    val borderColor = if (selected) RolePrimary else RoleCardBorder

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(background, RoundedCornerShape(24.dp))
            .border(width = 1.5.dp, color = borderColor.copy(alpha = if (selected) 0.7f else 0.4f), shape = RoundedCornerShape(24.dp))
            .clickable { onClick() }
            .padding(horizontal = 20.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(text = title, fontWeight = FontWeight.SemiBold, color = RoleText)
        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium,
            color = RoleText.copy(alpha = 0.85f),
        )
        if (selected) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .background(RolePrimary, RoundedCornerShape(20.dp))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
                    .align(Alignment.End),
            ) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(16.dp),
                )
                Text(
                    text = "Seleccionado",
                    color = Color.White,
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.padding(start = 6.dp),
                )
            }
        }
    }
}
