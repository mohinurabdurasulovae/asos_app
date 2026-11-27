package uz.asos.feature.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uz.asos.core.designsystem.component.AsosCard
import uz.asos.core.designsystem.component.AsosTag
import uz.asos.core.designsystem.component.VerifiedBadge
import uz.asos.core.designsystem.icon.AsosIcons
import uz.asos.core.designsystem.theme.AsosColors
import uz.asos.core.designsystem.theme.AsosShapes
import uz.asos.core.designsystem.theme.AsosTextStyles
import uz.asos.core.designsystem.theme.AsosTheme
import uz.asos.core.designsystem.theme.AsosTones
import uz.asos.feature.home.PostBadge
import uz.asos.feature.home.PostUi
import uz.asos.feature.home.R

@Composable
internal fun CommunityHighlightsSection(
    posts: List<PostUi>,
    onPostClick: (Int) -> Unit,
    onSeeAllClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    HomeSection(
        title = stringResource(R.string.home_community_title),
        actionLabel = stringResource(R.string.home_see_all),
        onAction = onSeeAllClick,
        modifier = modifier,
    ) {
        posts.forEach { post ->
            PostCard(post = post, onClick = { onPostClick(post.id) })
        }
    }
}

@Composable
private fun PostCard(post: PostUi, onClick: () -> Unit) {
    AsosCard(modifier = Modifier.fillMaxWidth(), shape = AsosShapes.R22, onClick = onClick) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            PostMeta(post)
            Text(text = post.title, style = AsosTextStyles.RowTitle.copy(fontSize = 15.5.sp, lineHeight = 21.sp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                Counter(
                    icon = AsosIcons.Upvote,
                    value = post.upvotes,
                    description = stringResource(R.string.home_post_upvotes, post.upvotes),
                )
                Counter(
                    icon = AsosIcons.Comment,
                    value = post.comments,
                    description = stringResource(R.string.home_post_comments, post.comments),
                )
                Spacer(Modifier.weight(1f))
                PostBadgeTag(post.badge)
            }
        }
    }
}

@Composable
private fun PostMeta(post: PostUi) {
    val tone = AsosTones.fromName(post.communityTone)
    Text(
        text = buildAnnotatedString {
            post.communityTitle?.let { title ->
                withStyle(SpanStyle(color = tone.content, fontWeight = FontWeight.ExtraBold)) { append(title) }
                if (post.meta.isNotBlank()) append(" · ")
            }
            append(post.meta)
        },
        style = AsosTextStyles.Meta.copy(fontSize = 12.sp, fontWeight = FontWeight.SemiBold),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
    )
}

@Composable
private fun Counter(icon: ImageVector, value: String, description: String) {
    Row(
        modifier = Modifier.clearAndSetSemantics { contentDescription = description },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Icon(icon, contentDescription = null, tint = AsosColors.Text2, modifier = Modifier.size(16.dp))
        Text(text = value, style = AsosTextStyles.Subtitle.copy(fontWeight = FontWeight.Bold))
    }
}

@Composable
private fun PostBadgeTag(badge: PostBadge) {
    when (badge) {
        PostBadge.LAWYER_ANSWERED -> Row(
            modifier = Modifier
                .background(AsosColors.Brand50, AsosShapes.Pill)
                .padding(horizontal = 9.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp),
        ) {
            VerifiedBadge(size = 14.dp, contentDescription = null)
            Text(
                text = stringResource(R.string.home_post_lawyer_answered),
                style = AsosTextStyles.Tag,
                color = AsosColors.Brand600,
            )
        }
        PostBadge.IN_DISCUSSION -> AsosTag(
            text = stringResource(R.string.home_post_in_discussion),
            tone = AsosTones.Orange,
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F8FC, widthDp = 390)
@Composable
private fun CommunityHighlightsSectionPreview() {
    AsosTheme {
        CommunityHighlightsSection(
            posts = listOf(
                PostUi(1, "Uy-joy", "pink", "Anonim · 2 soat oldin", "Uy egasi ruxsatsiz kvartiraga kirib turibdi. Bu qonuniymi?", "128", "34", PostBadge.LAWYER_ANSWERED),
                PostUi(2, "Ta’lim", "teal", "Jasur Tursunov · 5 soat oldin", "Kontrakt to‘lovi kechiksa, talabani darsga kiritmaslik mumkinmi?", "76", "19", PostBadge.IN_DISCUSSION),
            ),
            onPostClick = {},
            onSeeAllClick = {},
            modifier = Modifier.padding(20.dp),
        )
    }
}
