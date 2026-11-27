/*
 * CasesPromoCard.kt
 * Module: :feature:learn   ·   Work package: WP-07
 *
 * Navy "Kazuslar" card with the scales drawing in the corner. Opens Kazuslar.
 */
package uz.asos.feature.learn.hub.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import uz.asos.core.designsystem.icon.AsosIcons
import uz.asos.core.designsystem.theme.AsosColors
import uz.asos.core.designsystem.theme.AsosShapes
import uz.asos.core.designsystem.theme.AsosTextStyles
import uz.asos.core.designsystem.theme.AsosTheme
import uz.asos.feature.learn.R
import uz.asos.feature.learn.hub.CasesPromoUi

@Composable
internal fun CasesPromoCard(
    cases: CasesPromoUi,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(AsosShapes.R26)
            .background(AsosColors.Navy)
            .clickable(
                role = Role.Button,
                onClickLabel = stringResource(R.string.learn_cases_open),
                onClick = onClick,
            ),
    ) {
        Icon(
            imageVector = AsosIcons.Scales,
            contentDescription = null,
            tint = AsosColors.Brand500,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = 24.dp, y = (-14).dp)
                .size(150.dp)
                .alpha(0.55f),
        )
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Text(
                text = stringResource(R.string.learn_cases_overline).uppercase(),
                style = AsosTextStyles.Overline.copy(fontWeight = FontWeight.ExtraBold, letterSpacing = 0.05.em),
                color = AsosColors.BrandOnNavySoft,
                modifier = Modifier
                    .background(AsosColors.BrandOnNavy.copy(alpha = 0.18f), AsosShapes.Pill)
                    .padding(horizontal = 10.dp, vertical = 5.dp),
            )
            Text(
                text = stringResource(R.string.learn_cases_title),
                style = AsosTextStyles.ScreenTitle.copy(fontSize = 22.sp, lineHeight = 28.sp, letterSpacing = (-0.02).em),
                color = AsosColors.Surface,
            )
            Text(
                text = stringResource(R.string.learn_cases_body),
                style = AsosTextStyles.BodySmall.copy(lineHeight = 20.sp),
                color = AsosColors.TextOnNavy,
                modifier = Modifier.widthIn(max = 230.dp),
            )
            Row(
                modifier = Modifier.padding(top = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                val metaStyle = AsosTextStyles.Subtitle.copy(fontWeight = FontWeight.Bold)
                Text(
                    text = stringResource(R.string.learn_cases_solved, cases.solved),
                    style = metaStyle,
                    color = AsosColors.Surface,
                )
                if (cases.new > 0) {
                    Text(
                        text = stringResource(R.string.learn_cases_new, cases.new),
                        style = metaStyle,
                        color = AsosColors.BrandOnNavy,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F8FC)
@Composable
private fun CasesPromoCardPreview() {
    AsosTheme {
        CasesPromoCard(cases = CasesPromoUi(solved = 7, new = 5), onClick = {}, modifier = Modifier.padding(20.dp))
    }
}
