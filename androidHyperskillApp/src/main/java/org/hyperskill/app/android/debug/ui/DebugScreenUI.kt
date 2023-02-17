package org.hyperskill.app.android.debug.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.themeadapter.material.MdcTheme
import org.hyperskill.app.R
import org.hyperskill.app.debug.domain.model.EndpointConfigType
import org.hyperskill.app.debug.presentation.DebugFeature
import org.hyperskill.app.debug.presentation.DebugViewModel

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun DebugScreen(viewModel: DebugViewModel) {
    val viewState by viewModel.state.collectAsStateWithLifecycle()
    when (val state = viewState) {
        is DebugFeature.ViewState.Content -> {
            DebugScreen(
                options = state.availableEndpointConfigs,
                selectedOption = state.selectedEndpointConfig,
                onOptionClick = viewModel::onEndpointConfigClicked,
                isApplyEndpointButtonVisible = state.isApplySettingsButtonAvailable,
                onApplyEndpointClick = viewModel::onApplyConfigClicked,
                onOpenStepClick = viewModel::onOpenStepClicked,
                onStepIdChanged = viewModel::onStepIdChanged
            )
        }
        DebugFeature.ViewState.Error,
        DebugFeature.ViewState.Idle,
        DebugFeature.ViewState.Loading -> {
            // no op
        }
    }
}

@Composable
fun DebugScreen(
    options: List<EndpointConfigType>,
    selectedOption: EndpointConfigType,
    onOptionClick: (EndpointConfigType) -> Unit,
    isApplyEndpointButtonVisible: Boolean,
    onApplyEndpointClick: () -> Unit,
    onOpenStepClick: () -> Unit,
    onStepIdChanged: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TopAppBar(
            title = {
                Text(text = stringResource(id = R.string.debug_menu_title))
            },
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = MaterialTheme.colors.background
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(Dp(20f)),
            verticalArrangement = Arrangement.spacedBy(Dp(35f))
        ) {
            EndpointSwitcher(
                options = options,
                selectedOption = selectedOption,
                isApplyButtonVisible = isApplyEndpointButtonVisible,
                onOptionClick = onOptionClick,
                onApplyClick = onApplyEndpointClick
            )
            FindStep(onApplyClick = onOpenStepClick, onStepIdChanged = onStepIdChanged)
        }
    }
}

@Composable
@Preview(showBackground = true)
fun DebugScreenPreview() {
    var selectedOption by remember {
        mutableStateOf(EndpointConfigType.PRODUCTION)
    }
    MdcTheme {
        DebugScreen(
            options = EndpointConfigType.values().toList(),
            selectedOption = selectedOption,
            onOptionClick = { newSelectedOption ->
                selectedOption = newSelectedOption
            },
            onOpenStepClick = {},
            onStepIdChanged = {},
            onApplyEndpointClick = {},
            isApplyEndpointButtonVisible = true
        )
    }
}