package com.example.checkboxcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TriStateCheckbox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.unit.dp
import com.example.checkboxcompose.ui.theme.CheckboxComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CheckboxComposeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        Checkboxes()
                    }

                }
            }
        }
    }
}

data class CheckboxInfo(
    val isChecked: Boolean,
    val text: String
)

@Composable
fun Checkboxes() {

    val checkboxes = remember {
        mutableStateListOf(
            CheckboxInfo(
                isChecked = false,
                text = "Checkbox1"
            ),
            CheckboxInfo(
                isChecked = false,
                text = "Checkbox2"
            ),
            CheckboxInfo(
                isChecked = false,
                text = "Checkbox3"
            )
        )
    }

    var isCheckedCount = 0

    var triState by remember {
        mutableStateOf(ToggleableState.Off)
    }

    val toggleTriState = {
        triState = when (triState) {
            ToggleableState.Indeterminate -> ToggleableState.On
            ToggleableState.On -> ToggleableState.Off
            ToggleableState.Off -> ToggleableState.On
        }

        checkboxes.indices.forEach { index ->
            checkboxes[index] = checkboxes[index].copy(
                isChecked = triState == ToggleableState.On
            )
        }
    }

    val parentCheckboxInteractionSource = remember {
        MutableInteractionSource()
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable(
                indication = null,
                onClick = toggleTriState,
                interactionSource = parentCheckboxInteractionSource
            )
    ) {
        TriStateCheckbox(
            state = triState,
            onClick = toggleTriState,
            interactionSource = parentCheckboxInteractionSource
        )

        Text(text = "ParentCheckbox")

    }

    checkboxes.forEachIndexed { index, info ->

        val onCheckChangeFun = {
            checkboxes[index] = info.copy(
                isChecked = !info.isChecked
            )
        }

        if (info.isChecked) {
            isCheckedCount++
        }

        val childCheckboxInteractionSource = remember {
            MutableInteractionSource()
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(start = 32.dp)
                .clickable(
                    indication = null,
                    onClick = onCheckChangeFun,
                    interactionSource = childCheckboxInteractionSource
                )
        ) {
            Checkbox(
                checked = info.isChecked,
                onCheckedChange = { isChecked ->
                    onCheckChangeFun()
                },
                interactionSource = childCheckboxInteractionSource
            )
            Text(text = info.text)

        }

    }

    if (checkboxes.all { it.isChecked }) {
        triState = ToggleableState.On
    } else if (checkboxes.all { !it.isChecked }) {
        triState = ToggleableState.Off
    } else if (checkboxes.any { it.isChecked }) {
        triState = ToggleableState.Indeterminate
    }

    // first way
//    when(isCheckedCount) {
//        0 -> {
//            triState = ToggleableState.Off
//        }
//
//        checkboxes.size -> {
//            triState = ToggleableState.On
//        }
//
//        in 1 until checkboxes.size -> {
//            triState = ToggleableState.Indeterminate
//        }
//    }

}