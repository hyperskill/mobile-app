package org.hyperskill.app.android.debug.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.themeadapter.material.MdcTheme
import org.hyperskill.app.R
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillTopAppBar
import org.hyperskill.app.debug.domain.model.EndpointConfigType
import org.hyperskill.app.debug.presentation.DebugFeature
import org.hyperskill.app.debug.presentation.DebugViewModel

@Composable
fun DebugScreen(
    viewModel: DebugViewModel,
    onBackClick: (() -> Unit)?
) {
    val viewState by viewModel.state.collectAsStateWithLifecycle()
    when (val state = viewState) {
        is DebugFeature.ViewState.Content -> {
            DebugScreen(
                options = state.availableEndpointConfigs,
                selectedOption = state.selectedEndpointConfig,
                onBackClick = onBackClick,
                onOptionClick = viewModel::onEndpointConfigClicked,
                isApplyEndpointButtonVisible = state.isApplySettingsButtonAvailable,
                onApplyEndpointClick = viewModel::onApplyConfigClicked,
                onOpenStepClick = viewModel::onOpenStepClicked,
                onStepIdChanged = viewModel::onStepIdChanged,
                onFindStageInputChanged = { input ->
                    viewModel.onFindStageInputChanged(projectId = input.projectId, stageId = input.stageId)
                },
                onOpenStageClick = viewModel::onOpenStageClick
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
    onBackClick: (() -> Unit)?,
    onApplyEndpointClick: () -> Unit,
    onOpenStepClick: () -> Unit,
    onStepIdChanged: (String) -> Unit,
    onFindStageInputChanged: (FindStageInput) -> Unit,
    onOpenStageClick: () -> Unit
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        HyperskillTopAppBar(
            title = stringResource(id = R.string.debug_menu_title),
            onNavigationIconClick = onBackClick,
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = MaterialTheme.colors.background
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(35.dp)
        ) {
            EndpointSwitcher(
                options = options,
                selectedOption = selectedOption,
                isApplyButtonVisible = isApplyEndpointButtonVisible,
                onOptionClick = onOptionClick,
                onApplyClick = onApplyEndpointClick
            )
            FindStep(onApplyClick = onOpenStepClick, onStepIdChanged = onStepIdChanged)
            FindStage(onInputChanged = onFindStageInputChanged, onApplyClick = onOpenStageClick)
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun DebugScreenPreview() {
    var selectedOption by remember {
        mutableStateOf(EndpointConfigType.PRODUCTION)
    }
    MdcTheme {
        DebugScreen(
            options = EndpointConfigType.values().toList(),
            selectedOption = selectedOption,
            onBackClick = {},
            onOptionClick = { newSelectedOption ->
                selectedOption = newSelectedOption
            },
            onOpenStepClick = {},
            onStepIdChanged = {},
            onApplyEndpointClick = {},
            isApplyEndpointButtonVisible = true,
            onFindStageInputChanged = {},
            onOpenStageClick = {}
        )
    }
}