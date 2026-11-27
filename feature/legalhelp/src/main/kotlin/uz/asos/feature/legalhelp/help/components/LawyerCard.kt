package uz.asos.feature.legalhelp.help.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import uz.asos.core.designsystem.component.AsosButton
import uz.asos.core.designsystem.component.AsosButtonVariant
import uz.asos.core.designsystem.component.AsosCard
import uz.asos.core.designsystem.component.VerifiedBadge
import uz.asos.core.designsystem.icon.AsosIcons
import uz.asos.core.designsystem.theme.AsosColors
import uz.asos.core.designsystem.theme.AsosShapes
import uz.asos.core.designsystem.theme.AsosSpacing
import uz.asos.core.designsystem.theme.AsosTextStyles
import uz.asos.core.designsystem.theme.AsosTheme
import uz.asos.feature.legalhelp.R
import uz.asos.feature.legalhelp.help.FreeSlotUi
import uz.asos.feature.legalhelp.help.LawyerUi
import uz.asos.feature.legalhelp.ui.AvatarTone
import uz.asos.feature.legalhelp.ui.LawyerAvatar

private val ActionHeight = 48.dp

@Composable
internal fun LawyerCard(
    lawyer: LawyerUi,
    onWrite: () -> Unit,
    onCall: () -> Unit,
    onVideoCall: () -> Unit,
    onBook: () -> Unit,
    modifier: Modifier = Modifier,
    isOpeningChat: Boolean = false,
) {
    val online = stringResource(R.string.legalhelp_online)
    AsosCard(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(AsosSpacing.l),
            verticalArrangement = Arrangement.spacedBy(AsosSpacing.l),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                LawyerAvatar(
                    initials = lawyer.initials,
                    tone = lawyer.avatarTone,
                    isOnline = lawyer.isOnline,
                    contentDescription = if (lawyer.isOnline) "${lawyer.name}, $online" else lawyer.name,
                )
                Column(
                    Modifier
                        .weight(1f)
                        .padding(start = AsosSpacing.mm),
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = lawyer.name, style = AsosTextStyles.RowTitle)
                        if (lawyer.isVerified) {
                            VerifiedBadge(modifier = Modifier.padding(start = AsosSpacing.xs))
                        }
                    }
                    Text(
                        text = if (lawyer.specialtyLine.isNotEmpty()) {
                            stringResource(R.string.legalhelp_lawyer_experience, lawyer.specialtyLine, lawyer.experienceYears)
                        } else {
                            stringResource(R.string.legalhelp_lawyer_years, lawyer.experienceYears)
                        },
                        style = AsosTextStyles.Meta.copy(color = AsosColors.Text3),
                    )
                    when {
                        lawyer.nextFree != null -> Text(
                            text = if (lawyer.nextFree.isToday) {
                                stringResource(R.string.legalhelp_lawyer_free_today, lawyer.nextFree.time)
                            } else {
                                stringResource(R.string.legalhelp_lawyer_free_on, lawyer.nextFree.dateLabel, lawyer.nextFree.time)
                            },
                            style = AsosTextStyles.Tag,
                            color = AsosColors.WarningOnLight,
                        )
                        lawyer.ratingLabel != null -> Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                AsosIcons.Star,
                                contentDescription = null,
                                tint = AsosColors.Warning,
                                modifier = Modifier.size(14.dp),
                            )
                            Text(
                                text = stringResource(R.string.legalhelp_lawyer_rating, lawyer.ratingLabel, lawyer.reviewCount),
                                style = AsosTextStyles.Meta.copy(color = AsosColors.Text3),
                                modifier = Modifier.padding(start = AsosSpacing.xxs),
                            )
                        }
                    }
                }
            }

            if (lawyer.isOnline) {
                Row(horizontalArrangement = Arrangement.spacedBy(AsosSpacing.s)) {
                    AsosButton(
                        text = stringResource(R.string.legalhelp_lawyer_write),
                        onClick = onWrite,
                        enabled = !isOpeningChat,
                        modifier = Modifier.weight(1f),
                        height = ActionHeight,
                        leadingIcon = AsosIcons.Comment,
                    )
                    SquareIconButton(
                        icon = AsosIcons.Phone,
                        contentDescription = stringResource(R.string.legalhelp_call),
                        onClick = onCall,
                    )
                    SquareIconButton(
                        icon = AsosIcons.Video,
                        contentDescription = stringResource(R.string.legalhelp_video_call),
                        onClick = onVideoCall,
                    )
                }
            } else {
                AsosButton(
                    text = stringResource(R.string.legalhelp_lawyer_book),
                    onClick = onBook,
                    modifier = Modifier.fillMaxWidth(),
                    variant = AsosButtonVariant.Secondary,
                    height = ActionHeight,
                    leadingIcon = AsosIcons.Calendar,
                )
            }
        }
    }
}

@Composable
private fun SquareIconButton(icon: ImageVector, contentDescription: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(ActionHeight)
            .clip(AsosShapes.R14)
            .border(1.dp, AsosColors.Border, AsosShapes.R14)
            .clickable(role = Role.Button, onClickLabel = contentDescription, onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Icon(icon, contentDescription = contentDescription, tint = AsosColors.Navy, modifier = Modifier.size(20.dp))
    }
}

internal val PreviewLawyers = listOf(
    LawyerUi(
        id = 1, name = "Sardor Rahimov", initials = "SR", specialties = listOf("Uy-joy", "Fuqarolik ishlari"),
        specialtyLine = "Uy-joy va fuqarolik ishlari", experienceYears = 9, ratingLabel = "4,9", reviewCount = 210,
        isOnline = true, isVerified = true, nextFree = null, avatarTone = AvatarTone.NAVY,
    ),
    LawyerUi(
        id = 2, name = "Malika Yusupova", initials = "MY", specialties = listOf("Oila", "Ta’lim"),
        specialtyLine = "Oila va ta’lim", experienceYears = 12, ratingLabel = "4,8", reviewCount = 164,
        isOnline = true, isVerified = true, nextFree = null, avatarTone = AvatarTone.BRAND,
    ),
    LawyerUi(
        id = 3, name = "Bekzod Karimov", initials = "BK", specialties = listOf("Mehnat"),
        specialtyLine = "Mehnat", experienceYears = 6, ratingLabel = null, reviewCount = 0,
        isOnline = false, isVerified = true,
        nextFree = FreeSlotUi(raw = "2026-11-21 15:00", isToday = true, dateLabel = "21.11", time = "15:00"),
        avatarTone = AvatarTone.ORANGE,
    ),
)

@Preview(showBackground = true, backgroundColor = 0xFFF5F8FC, widthDp = 390)
@Composable
private fun LawyerCardPreview() {
    AsosTheme {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            PreviewLawyers.forEach { LawyerCard(it, onWrite = {}, onCall = {}, onVideoCall = {}, onBook = {}) }
        }
    }
}
