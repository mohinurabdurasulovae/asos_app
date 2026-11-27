package uz.asos.feature.community.post.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import uz.asos.core.designsystem.icon.AsosIcons
import uz.asos.core.designsystem.theme.AsosColors
import uz.asos.core.designsystem.theme.AsosShapes
import uz.asos.core.designsystem.theme.AsosSpacing
import uz.asos.core.designsystem.theme.AsosTextStyles
import uz.asos.core.designsystem.theme.AsosTones
import uz.asos.core.model.community.Post
import uz.asos.feature.community.R
import uz.asos.feature.community.ui.memberCountLabel
import uz.asos.feature.community.ui.shortAge

@Composable
internal fun PostTopBar(
    post: Post,
    onBack: () -> Unit,
    onMembershipToggle: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val community = post.community
    val tone = AsosTones.fromName(community?.tone)

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(AsosSpacing.s),
    ) {
        IconButton(onClick = onBack) {
            Icon(
                imageVector = AsosIcons.ChevronLeft,
                contentDescription = stringResource(R.string.community_post_back),
                tint = AsosColors.Navy,
            )
        }

        if (community != null) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(AsosShapes.Circle)
                    .background(tone.container),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = community.title.take(1),
                    style = AsosTextStyles.Tag,
                    color = tone.content,
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(text = community.title, style = AsosTextStyles.RowTitleRegular)
                community.memberCount?.let { count ->
                    Text(
                        text = memberCountLabel(count),
                        style = AsosTextStyles.Meta,
                        color = AsosColors.Text2,
                    )
                }
            }
            Surface(
                onClick = onMembershipToggle,
                shape = AsosShapes.Pill,
                color = if (community.isMember) AsosColors.SurfaceMuted else AsosColors.Brand600,
            ) {
                Text(
                    text = stringResource(
                        if (community.isMember) {
                            R.string.community_post_leave
                        } else {
                            R.string.community_post_join
                        },
                    ),
                    style = AsosTextStyles.Chip,
                    color = if (community.isMember) AsosColors.Navy else AsosColors.Surface,
                    modifier = Modifier.padding(
                        horizontal = AsosSpacing.l,
                        vertical = AsosSpacing.s,
                    ),
                )
            }
        } else {
            Box(modifier = Modifier.weight(1f))
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun PostHeader(
    post: Post,
    body: String,
    onArticleClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(AsosSpacing.m),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(AsosSpacing.sm),
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(AsosShapes.Circle)
                    .background(AsosColors.SurfaceMuted),
                contentAlignment = Alignment.Center,
            ) {
                if (post.author.isAnonymous) {
                    Icon(
                        imageVector = AsosIcons.EyeOff,
                        contentDescription = null,
                        tint = AsosColors.Text2,
                        modifier = Modifier.size(18.dp),
                    )
                } else {
                    Text(
                        text = post.author.initials,
                        style = AsosTextStyles.Tag,
                        color = AsosColors.Navy,
                    )
                }
            }
            Column {
                Text(text = post.author.name, style = AsosTextStyles.RowTitleRegular)
                Text(
                    text = post.createdAt.shortAge(),
                    style = AsosTextStyles.Meta,
                    color = AsosColors.Text2,
                )
            }
        }

        Text(
            text = post.title,
            style = AsosTextStyles.SectionTitle,
            modifier = Modifier.semantics { heading() },
        )

        Text(text = body, style = AsosTextStyles.Body)

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(AsosSpacing.s),
            verticalArrangement = Arrangement.spacedBy(AsosSpacing.s),
        ) {
            post.tags.forEach { tag ->
                Surface(shape = AsosShapes.R10, color = AsosColors.SurfaceMuted) {
                    Text(
                        text = tag,
                        style = AsosTextStyles.Tag,
                        color = AsosColors.Text2,
                        modifier = Modifier.padding(
                            horizontal = AsosSpacing.sm,
                            vertical = AsosSpacing.xs,
                        ),
                    )
                }
            }
            post.article?.let { article ->
                Surface(
                    onClick = { onArticleClick(article.number) },
                    shape = AsosShapes.R10,
                    color = AsosColors.Brand50,
                    border = BorderStroke(1.dp, AsosColors.Brand100),
                ) {
                    Text(
                        text = stringResource(R.string.community_article_number, article.number),
                        style = AsosTextStyles.Tag,
                        color = AsosColors.Brand600,
                        modifier = Modifier.padding(
                            horizontal = AsosSpacing.sm,
                            vertical = AsosSpacing.xs,
                        ),
                    )
                }
            }
        }
    }
}
