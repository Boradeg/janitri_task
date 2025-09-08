package com.example.janitri_task.presentation.home_screen

import android.content.Intent
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import com.example.janitri_task.R
import com.example.janitri_task.domain.model.VitalRecord
import com.example.janitri_task.presentation.service.TimerService
import com.example.janitri_task.ui.theme.AppTypography
import com.example.janitri_task.ui.theme.BlueDark
import com.example.janitri_task.ui.theme.DarkPink
import com.example.janitri_task.ui.theme.Green
import com.example.janitri_task.ui.theme.Grey
import com.example.janitri_task.ui.theme.LightPink
import com.example.janitri_task.ui.theme.PinkTopBar
import com.example.janitri_task.ui.theme.Red
import com.example.janitri_task.ui.theme.TopBarTextColor
import com.example.janitri_task.utils.formatTimestamp


@Composable
fun MainScreen(viewModel: TimerViewModel) {
    val context = LocalContext.current
    val time by viewModel.time
    var isLoading by rememberSaveable  { mutableStateOf(false) }

    val isServiceRunning by viewModel.isServiceRunning
    var showDialog by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is VitalsUiEvent.ShowError ->
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()

                VitalsUiEvent.Success -> {
                    Toast.makeText(
                        context,
                        context.getString(R.string.vitals_saved), Toast.LENGTH_SHORT
                    ).show()
                    showDialog = false
                }

                VitalsUiEvent.Loading -> {
                    isLoading = true
                }
            }
        }
    }
    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
    if (showDialog) {
        VitalsInputDialog(
            onDismissRequest = { showDialog = false },
            onSubmit = { sys, dia, weight, kicks ->
                viewModel.validateAndAddVitals(sys, dia, weight, kicks)
            }
        )
    }
    Scaffold(
        floatingActionButton = {
            AddFloatingActionButton(onClick = {
                showDialog = true
            })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = innerPadding.calculateBottomPadding())
        ) {
            TopBar(
                timerText = time,
                buttonText = if (isServiceRunning) stringResource(R.string.stop) else stringResource(
                    R.string.start
                ), isServiceRunning
            ) {
                val intent = Intent(context, TimerService::class.java)
                if (isServiceRunning) {
                    context.stopService(intent)
                    viewModel.setServiceRunning(false)
                } else {
                    ContextCompat.startForegroundService(context, intent)
                    viewModel.setServiceRunning(true)
                }
            }
            VitalsListScreen(viewModel)
        }
    }
}

@Composable
fun VitalsListScreen(viewModel: TimerViewModel) {
    val uiState by viewModel.vitalsUiState.collectAsState()

    when (uiState) {
        is VitalsUiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is VitalsUiState.Empty -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.no_vitals_found_please_add_one),
                    color = Color.Red
                )
            }
        }

        is VitalsUiState.Success -> {
            val vitals = (uiState as VitalsUiState.Success).list
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
                    .padding(horizontal = 11.dp),
            ) {
                items(vitals, key = { it.dateTime }) { record ->
                    Spacer(modifier = Modifier.height(14.dp))
                    VitalCard(record)
                }
            }
        }

        is VitalsUiState.Error -> {
            val message = (uiState as VitalsUiState.Error).message
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = message,
                    color = Color.Red
                )
            }
        }
    }
}


@Composable
fun VitalCard(record: VitalRecord) {
    Card(
        shape = RoundedCornerShape(4.dp),
        colors = CardDefaults.cardColors(containerColor = LightPink),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    VitalItem(
                        iconRes = R.drawable.ic_heart_rate,
                        text = stringResource(R.string.bpm, record.heartRate),
                        contentDescription = stringResource(R.string.heart_rate)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    VitalItem(
                        iconRes = R.drawable.ic_weight,
                        text = stringResource(R.string.kg, record.weight),
                        contentDescription = stringResource(R.string.weight)
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Column {
                    VitalItem(
                        iconRes = R.drawable.ic_blood_pressure,
                        text = stringResource(R.string._80_mmhg, record.bloodPressure),
                        contentDescription = stringResource(R.string.blood_pressure)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    VitalItem(
                        iconRes = R.drawable.ic_newborn,
                        text = stringResource(R.string.kicks, record.kicks),
                        contentDescription = stringResource(R.string.baby_kicks)
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DarkPink)
                    .padding(vertical = 6.dp, horizontal = 9.dp)
            ) {
                Text(
                    text = formatTimestamp(record.dateTime),
                    color = White,
                    style = AppTypography.labelLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.End)
                )
            }
        }
    }
}

@Composable
fun VitalsInputDialog(
    onDismissRequest: () -> Unit,
    onSubmit: (String, String, String, String) -> Unit
) {
    var sysBP by rememberSaveable { mutableStateOf("") }
    var diaBP by rememberSaveable { mutableStateOf("") }
    var weight by rememberSaveable { mutableStateOf("") }
    var kicks by rememberSaveable { mutableStateOf("") }

    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            shape = RoundedCornerShape(8.dp),
            color = Color.White,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 18.dp, vertical = 15.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.add_vitals),
                    style = AppTypography.titleLarge.copy(fontWeight = SemiBold, fontSize = 16.sp),
                    color = BlueDark,
                    modifier = Modifier.align(Alignment.Start)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Sys + Dia BP
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    VitalInputField(
                        value = sysBP,
                        onValueChange = { sysBP = it },
                        label = stringResource(R.string.sys_bp),
                        imeAction = ImeAction.Next,
                        modifier = Modifier.weight(1f)
                    )
                    VitalInputField(
                        value = diaBP,
                        onValueChange = { diaBP = it },
                        label = stringResource(R.string.dia_bp),
                        imeAction = ImeAction.Next,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Weight
                VitalInputField(
                    value = weight,
                    onValueChange = { weight = it },
                    label = stringResource(R.string.weight_in_kg),
                    imeAction = ImeAction.Next,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Baby Kicks
                VitalInputField(
                    value = kicks,
                    onValueChange = { kicks = it },
                    label = stringResource(R.string.baby_kicks),
                    imeAction = ImeAction.Done,
                    modifier = Modifier.fillMaxWidth(),
                    onImeAction = {
                        onSubmit(sysBP, diaBP, weight, kicks)
                        onDismissRequest()
                    }
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = { onSubmit(sysBP, diaBP, weight, kicks) },
                    shape = RoundedCornerShape(4.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = DarkPink),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text(
                        stringResource(R.string.submit),
                        style = AppTypography.labelLarge.copy(fontSize = 16.sp),
                        color = White,
                        modifier = Modifier.padding(horizontal = 44.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun VitalInputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    imeAction: ImeAction,
    modifier: Modifier = Modifier,
    onImeAction: (() -> Unit)? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, color = Grey) },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = imeAction
        ),
        keyboardActions = if (onImeAction != null) {
            KeyboardActions(onDone = { onImeAction() })
        } else KeyboardActions.Default,
        modifier = modifier
    )
}


@Composable
fun VitalItem(@DrawableRes iconRes: Int, text: String, contentDescription: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,

        ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = contentDescription,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(text = text, style = AppTypography.titleLarge.copy(fontSize = 12.sp), color = BlueDark)
    }
}

@Composable
fun TopBar(
    timerText: String,
    buttonText: String,
    isServiceRunning: Boolean,
    onStartClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(PinkTopBar)
            .statusBarsPadding()
    ) {
        val modifier = if (isServiceRunning) {
            Modifier.background(Red, RoundedCornerShape(8.dp))
        } else {
            Modifier.background(Green, RoundedCornerShape(8.dp))
        }
        // Title
        Text(
            text = stringResource(R.string.track_my_pregnancy),
            style = AppTypography.titleLarge.copy(fontWeight = SemiBold),
            color = TopBarTextColor,
            modifier = Modifier
                .fillMaxWidth()
                .background(PinkTopBar)
                .padding(horizontal = 10.dp, vertical = 14.dp)
        )

        // Timer + Start Button Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(DarkPink)
                .padding(horizontal = 10.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(R.string.timer, timerText),
                style = AppTypography.labelLarge,
                color = White,
            )

            SmallBoxButton(
                text = buttonText,
                modifier = modifier.padding(end = 5.dp),
                onClick = {
                    onStartClick()
                }
            )
        }
    }
}

@Composable
fun SmallBoxButton(
    text: String,
    modifier: Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clickable { onClick() }
            .padding(horizontal = 17.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = AppTypography.labelLarge.copy(
                color = White,
                fontSize = 12.sp, // smaller text
                fontWeight = SemiBold
            )
        )
    }
}

@Composable
fun AddFloatingActionButton(onClick: () -> Unit) {
    FloatingActionButton(
        onClick = {
            onClick()
            // Handle FAB click (e.g., open form, navigate to new screen, etc.)
        },
        containerColor = DarkPink, // Purple color from Figma
        contentColor = White,
        shape = CircleShape,
        elevation = FloatingActionButtonDefaults.elevation(
            defaultElevation = 6.dp,
            pressedElevation = 10.dp
        )
    ) {
        Icon(
            imageVector = Icons.Default.Add, contentDescription = stringResource(R.string.add),
            modifier = Modifier.size(32.dp)
        )
    }
}

