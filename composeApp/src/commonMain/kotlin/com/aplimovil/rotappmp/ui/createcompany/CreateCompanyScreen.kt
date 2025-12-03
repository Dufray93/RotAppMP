package com.aplimovil.rotappmp.ui.createcompany

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.aplimovil.rotappmp.domain.model.CompanyCategory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

private val CompanyBackground = Color(0xFFFBEFED)
private val CompanyCardColor = Color(0xFFF6DCD7)
private val CompanyPrimary = Color(0xFF8C3A33)
private val CompanyText = Color(0xFF4C2D27)

/**
 * UI compartida para el flujo de creación de empresa. Consume [CreateCompanyUiState] y envía las
 * interacciones de regreso al ViewModel mediante lambdas para mantener el código de plataforma liviano.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateCompanyScreen(
    state: CreateCompanyUiState,
    onNameChange: (String) -> Unit,
    onCategorySelected: (CompanyCategory) -> Unit,
    onEmployeesChange: (Int) -> Unit,
    onSubmit: () -> Unit,
    onBackClick: () -> Unit,
    events: Flow<CreateCompanyEvent>?,
    onNavigateHome: () -> Unit,
) {
    LaunchedEffect(events) {
        events?.collect { event ->
            if (event is CreateCompanyEvent.NavigateHome) onNavigateHome()
        }
    }

    Scaffold(
        containerColor = CompanyBackground,
        topBar = {
            TopAppBar(
                title = { Text(text = "Registrar empresa", color = CompanyText) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                            tint = CompanyText,
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            Text(
                text = "Configura tu empresa",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = CompanyText,
            )
            Text(
                text = "Usaremos estos datos para darte recomendaciones y distribuir mejor los turnos.",
                style = MaterialTheme.typography.bodyMedium,
                color = CompanyText.copy(alpha = 0.75f),
            )

            CompanyTextField(
                value = state.name,
                onValueChange = onNameChange,
            )

            Text(
                text = "¿A qué se dedica?",
                style = MaterialTheme.typography.titleMedium,
                color = CompanyText,
            )

            CategorySelector(
                selected = state.category,
                onCategorySelected = onCategorySelected,
            )

            EmployeesCard(
                count = state.employeesCount,
                onEmployeesChange = onEmployeesChange,
            )

            if (state.errorMessage != null) {
                Text(text = state.errorMessage, color = Color(0xFFB00020))
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = onSubmit,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(containerColor = CompanyPrimary, contentColor = Color.White),
                enabled = !state.isLoading,
            ) {
                Text(text = "Crear empresa")
            }
        }
    }
}

@Composable
private fun CompanyTextField(value: String, onValueChange: (String) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(text = "Nombre de la empresa", color = CompanyText, style = MaterialTheme.typography.titleMedium)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(CompanyCardColor)
                .padding(horizontal = 20.dp, vertical = 14.dp),
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                textStyle = MaterialTheme.typography.bodyLarge.copy(color = CompanyText),
                modifier = Modifier.fillMaxWidth(),
                decorationBox = { innerTextField ->
                    if (value.isEmpty()) {
                        Text(text = "RotApp Corp", color = CompanyText.copy(alpha = 0.4f))
                    }
                    innerTextField()
                },
            )
        }
    }
}

@Composable
private fun CategorySelector(
    selected: CompanyCategory,
    onCategorySelected: (CompanyCategory) -> Unit,
) {
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        CompanyCategory.entries.forEach { category ->
            val isSelected = category == selected
            val background = if (isSelected) CompanyPrimary else Color.Transparent
            val border = if (isSelected) CompanyPrimary else CompanyCardColor
            Text(
                text = category.displayName,
                color = if (isSelected) Color.White else CompanyText,
                modifier = Modifier
                    .clip(RoundedCornerShape(24.dp))
                    .background(background)
                    .border(width = 1.5.dp, color = border, shape = RoundedCornerShape(24.dp))
                    .padding(horizontal = 16.dp, vertical = 10.dp)
                    .clickable { onCategorySelected(category) },
            )
        }
    }
}

@Composable
private fun EmployeesCard(count: Int, onEmployeesChange: (Int) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(CompanyCardColor)
            .padding(horizontal = 20.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "Número de colaboradores", color = CompanyText, fontWeight = FontWeight.SemiBold)
                Text(text = "Puedes ajustarlo más adelante en ajustes.", color = CompanyText.copy(alpha = 0.7f))
            }
            Text(text = count.toString(), color = CompanyText, fontWeight = FontWeight.Bold)
        }
        Slider(
            value = count.toFloat(),
            onValueChange = { onEmployeesChange(it.toInt()) },
            valueRange = 5f..500f,
            colors = SliderDefaults.colors(
                thumbColor = CompanyPrimary,
                activeTrackColor = CompanyPrimary,
                inactiveTrackColor = CompanyPrimary.copy(alpha = 0.3f),
            ),
        )
    }
}
