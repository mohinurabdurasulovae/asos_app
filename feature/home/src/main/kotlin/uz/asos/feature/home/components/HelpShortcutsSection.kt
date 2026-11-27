package uz.asos.feature.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uz.asos.core.designsystem.component.AsosCard
import uz.asos.core.designsystem.icon.AsosIcons
import uz.asos.core.designsystem.theme.AsosColors
import uz.asos.core.designsystem.theme.AsosShapes
import uz.asos.core.designsystem.theme.AsosTextStyles
import uz.asos.core.designsystem.theme.AsosTheme
import uz.asos.core.designsystem.theme.AsosTone
import uz.asos.core.designsystem.theme.AsosTones
import uz.asos.feature.home.HelpUi
import uz.asos.feature.home.R

@Composable
internal fun HelpShortcutsSection(
    help: HelpUi,
    onLegalHelpClick: () -> Unit,
    onDictionaryClick: () -> Unit,
    onAssistantClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    HomeSection(title = stringResource(R.string.home_help_title), modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Max),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            ShortcutCard(
                icon = AsosIcons.Scales,
                tone = AsosTones.Brand,
                title = stringResource(R.string.home_legal_help_title),
                text = stringResource(R.string.home_legal_help_text),
                onClick = onLegalHelpClick,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
            ) {
                help.lawyersOnlineLabel?.let { OnlineStatus(it) }
            }
            ShortcutCard(
                icon = AsosIcons.BookOpen,
                tone = AsosTones.Purple,
                title = stringResource(R.string.home_dictionary_title),
                text = stringResource(R.string.home_dictionary_text),
                onClick = onDictionaryClick,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
            ) {
                help.termOfDayLabel?.let {
                    Text(text = it, style = AsosTextStyles.Tag, color = AsosColors.Purple)
                }
            }
        }
        AssistantRow(onClick = onAssistantClick)
    }
}

@Composable
private fun ShortcutCard(
    icon: ImageVector,
    tone: AsosTone,
    title: String,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    footer: @Composable () -> Unit,
) {
    AsosCard(modifier = modifier, shape = AsosShapes.R22, onClick = onClick) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            IconTile(icon = icon, container = tone.container, content = tone.content, size = 42)
            Text(text = title, style = AsosTextStyles.RowTitle)
            Text(text = text, style = AsosTextStyles.Meta.copy(lineHeight = 17.5.sp))
            Spacer(Modifier.weight(1f))
            footer()
        }
    }
}

@Composable
private fun OnlineStatus(label: String) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .background(AsosColors.Success, AsosShapes.Circle),
        )
        Text(text = label, style = AsosTextStyles.Tag, color = AsosColors.SuccessOnLight)
    }
}

@Composable
private fun AssistantRow(onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(AsosShapes.R20)
            .background(AsosColors.Navy)
            .clickable(role = Role.Button, onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        IconTile(icon = AsosIcons.Sparkles, container = AsosColors.Brand500, content = AsosColors.Surface, size = 40)
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                text = stringResource(R.string.home_assistant_title),
                style = AsosTextStyles.RowTitle.copy(fontSize = 14.5.sp),
                color = AsosColors.Surface,
            )
            Text(
                text = stringResource(R.string.home_assistant_text),
                style = AsosTextStyles.Meta.copy(fontWeight = FontWeight.Normal),
                color = AsosColors.TextOnNavy,
            )
        }
        Icon(AsosIcons.ChevronRight, contentDescription = null, tint = AsosColors.Surface, modifier = Modifier.size(20.dp))
    }
}

@Composable
private fun IconTile(icon: ImageVector, container: Color, content: Color, size: Int) {
    Box(
        modifier = Modifier
            .size(size.dp)
            .background(container, AsosShapes.R12),
        contentAlignment = Alignment.Center,
    ) {
        Icon(icon, contentDescription = null, tint = content, modifier = Modifier.size(21.dp))
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F8FC, widthDp = 390)
@Composable
private fun HelpShortcutsSectionPreview() {
    AsosTheme {
        HelpShortcutsSection(
            help = HelpUi(lawyersOnlineLabel = "3 advokat onlayn", termOfDayLabel = "Kun atamasi: Daxlsizlik"),
            onLegalHelpClick = {},
            onDictionaryClick = {},
            onAssistantClick = {},
            modifier = Modifier.padding(20.dp),
        )
    }
}
