package uz.asos.huquqiyyordam

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.speech.RecognizerIntent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

private val pageInset = Modifier.padding(horizontal = 20.dp)
internal const val PrototypeHotline = "+998 (71) 000-00-00"
private const val QuestionHint =
    "Vaziyatingizni yozing, masalan: ish beruvchi ikki oydan beri maoshni kechiktirmoqda…"

@Composable
fun LegalHelpScreen(
    onAction: (LegalHelpAction) -> Unit,
    modifier: Modifier = Modifier,
    hotlineNumber: String = PrototypeHotline,
    assistantBusy: Boolean = false,
    assistantContent: (@Composable () -> Unit)? = null,
) = LegalHelpScreenContent(
    onAction, modifier, hotlineNumber, assistantBusy, assistantContent,
)

@Composable
internal fun LegalHelpScreenContent(
    onAction: (LegalHelpAction) -> Unit,
    modifier: Modifier = Modifier,
    hotlineNumber: String = PrototypeHotline,
    assistantBusy: Boolean = false,
    assistantContent: (@Composable () -> Unit)? = null,
    live: Boolean = false,
    help: HelpPage? = null,
    helpError: String? = null,
    onReload: () -> Unit = {},
    lawyers: List<Lawyer>? = null,
    lawyersError: String? = null,
    onReloadLawyers: () -> Unit = {},
) {
    var question by rememberSaveable { mutableStateOf("") }
    var specialty by rememberSaveable { mutableStateOf(Specialty.All) }
    val keyboardFocus = LocalFocusManager.current
    val questionFocus = remember { FocusRequester() }
    val scope = rememberCoroutineScope()
    val messages = remember { SnackbarHostState() }
    val speechLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val transcript = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                ?.firstOrNull { it.isNotBlank() }
            if (transcript != null) {
                question = listOf(question.trim(), transcript).filter(String::isNotEmpty)
                    .joinToString(" ").take(AssistantQuestionLimit)
            }
        }
    }
    val topInset = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()

    Box(
        modifier.fillMaxSize().background(AsosColors.Background)
            .statusBarsPadding().navigationBarsPadding().imePadding(),
        contentAlignment = Alignment.TopCenter,
    ) {
        Column(
            Modifier.widthIn(max = 460.dp).fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(top = maxOf(52.dp - topInset, 8.dp), bottom = 40.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            PageHeader(onBack = { onAction(LegalHelpAction.Back) })
            ArticleQuote(help?.quote?.first ?: 29, help?.quote?.second)
            if (live && helpError != null) {
                Column(pageInset.fillMaxWidth()) {
                    Text(helpError, style = asosTextStyle(13.0, 18.0, 600))
                    TextButton(onClick = onReload) { Text("Qayta urinish") }
                }
            }
            AiCard(
                question = question,
                help = help,
                usesGemini = live,
                onQuestionChange = { question = it.take(AssistantQuestionLimit) },
                assistantBusy = assistantBusy,
                assistantContent = assistantContent,
                questionFocus = questionFocus,
                onVoice = {
                    val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
                        .putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                        .putExtra(RecognizerIntent.EXTRA_LANGUAGE, "uz-UZ")
                        .putExtra(RecognizerIntent.EXTRA_PROMPT, "Vaziyatingizni ayting")
                    try {
                        speechLauncher.launch(intent)
                    } catch (_: ActivityNotFoundException) {
                        scope.launch {
                            messages.showSnackbar("Bu qurilmada ovozli kiritish mavjud emas.")
                        }
                    }
                },
                onSubmit = {
                    val message = question.trim()
                    if (message.isEmpty()) {
                        questionFocus.requestFocus()
                    } else {
                        keyboardFocus.clearFocus()
                        onAction(LegalHelpAction.SubmitQuestion(message))
                        if (assistantContent != null) question = ""
                    }
                },
            )
            val hotline = help?.hotline
            HotlineCard(
                number = hotline?.phone ?: hotlineNumber,
                onAction = onAction,
                hotline = hotline,
                live = live,
            )
            val entries = if (live) lawyers else previewLawyers
            when {
                entries != null -> LawyerSection(
                    specialty = specialty,
                    onSpecialtyChange = { specialty = it },
                    onAction = onAction,
                    entries = entries,
                    live = live,
                )
                lawyersError != null -> Column(pageInset.fillMaxWidth()) {
                    Text("Advokatlar ro‘yxatini yuklab bo‘lmadi.", style = asosTextStyle(13.0, 18.0, 600))
                    Text(lawyersError, style = asosTextStyle(13.0, 18.0, 400))
                    TextButton(onClick = onReloadLawyers) { Text("Qayta urinish") }
                }
                else -> Text("Advokatlar yuklanmoqda…", pageInset, style = asosTextStyle(13.0, 18.0, 600))
            }
        }
        SnackbarHost(messages, Modifier.align(Alignment.BottomCenter).padding(12.dp))
    }
}

@Composable
private fun PageHeader(onBack: () -> Unit) {
    Row(
        Modifier.fillMaxWidth().padding(start = 12.dp, end = 20.dp).heightIn(min = 44.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ActionBox(
            onClick = onBack,
            modifier = Modifier.size(44.dp),
            label = "Orqaga",
        ) {
            LegalIcon(AsosIcon.Back, AsosColors.Navy, 22.dp)
        }
        Text(
            "Huquqiy yordam",
            Modifier.semantics { heading() },
            style = asosTextStyle(24.0, 30.0, 800, spacing = -0.6),
        )
    }
}

@Composable
private fun ArticleQuote(number: Int, body: String?) {
    val quote = body?.substringBefore(". ")?.trim('“', '”', ' ', '.')
        ?: "Har kimga malakali yuridik yordam olish huquqi kafolatlanadi"
    val shape = RoundedCornerShape(20.dp)
    Row(
        pageInset.fillMaxWidth()
            .background(Color.White, shape)
            .border(1.dp, AsosColors.Border, shape)
            .padding(horizontal = 19.dp, vertical = 17.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        Box(
            Modifier.width(35.dp).height(30.dp)
                .background(AsosColors.Navy, RoundedCornerShape(9.dp)),
            contentAlignment = Alignment.Center,
        ) {
            Text(number.toString(), style = asosTextStyle(15.0, 19.0, 700, Color.White, serif = true))
        }
        Text(
            "“$quote.”",
            style = asosTextStyle(16.5, 23.925, 400, serif = true),
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun AiCard(
    question: String,
    help: HelpPage?,
    usesGemini: Boolean,
    onQuestionChange: (String) -> Unit,
    questionFocus: FocusRequester,
    onVoice: () -> Unit,
    onSubmit: () -> Unit,
    assistantBusy: Boolean,
    assistantContent: (@Composable () -> Unit)?,
) {
    Column(
        pageInset.fillMaxWidth()
            .background(AsosColors.Navy, RoundedCornerShape(26.dp))
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                Modifier.width(41.dp).height(44.dp)
                    .background(AsosColors.Blue, RoundedCornerShape(14.dp)),
                contentAlignment = Alignment.Center,
            ) {
                LegalIcon(AsosIcon.Sparkles, Color.White, 22.dp)
            }
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(help?.aiTitle ?: "Asos AI yordamchi", style = asosTextStyle(17.0, 22.0, 800, Color.White))
                Text(
                    if (usesGemini) "Huquqiy savollarni moddalar asosida tushuntiradi"
                    else help?.aiSubtitle ?: "Tegishli moddalarni topib, sodda tushuntiradi",
                    style = asosTextStyle(13.0, 16.0, 400, AsosColors.OnNavy),
                )
            }
        }
        QuestionInput(question, onQuestionChange, questionFocus, onVoice, onSubmit, assistantBusy)
        FlowRow(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            val suggested = help?.suggestions?.map { it to (questionSuggestions[it] ?: it) } ?: questionSuggestions.toList()
            suggested.forEach { (label, suggestion) ->
                ActionBox(
                    onClick = { onQuestionChange(suggestion) },
                    modifier = Modifier.height(34.dp),
                    border = Color.White.copy(alpha = 0.18f),
                    radius = 99.dp,
                ) {
                    Text(
                        label,
                        Modifier.padding(horizontal = 13.dp),
                        style = asosTextStyle(13.0, 16.0, 700, Color.White),
                        maxLines = 1,
                    )
                }
            }
        }
        val disclosure = help?.disclaimer
            ?: "AI umumiy huquqiy ma’lumot beradi va advokat maslahati o‘rnini bosmaydi."
        Text(
            if (usesGemini) "$disclosure Savol Gemini’ga yuboriladi." else disclosure,
            style = asosTextStyle(12.0, 17.4, 400, AsosColors.OnNavyMuted),
        )
        assistantContent?.invoke()
    }
}

@Composable
private fun QuestionInput(
    question: String,
    onQuestionChange: (String) -> Unit,
    questionFocus: FocusRequester,
    onVoice: () -> Unit,
    onSubmit: () -> Unit,
    assistantBusy: Boolean,
) {
    val shape = RoundedCornerShape(18.dp)
    Column(
        Modifier.fillMaxWidth()
            .background(Color.White.copy(alpha = 0.07f), shape)
            .border(1.dp, Color.White.copy(alpha = 0.14f), shape)
            .padding(15.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        val inputStyle = asosTextStyle(15.0, 22.5, 400, Color.White)
        BasicTextField(
            value = question,
            onValueChange = onQuestionChange,
            modifier = Modifier.fillMaxWidth().focusRequester(questionFocus).semantics {
                contentDescription = "Vaziyatingizni yozing"
            },
            textStyle = inputStyle,
            cursorBrush = SolidColor(Color.White),
            minLines = 3,
            maxLines = 3,
            decorationBox = { field ->
                Box(Modifier.padding(start = 2.dp, top = 2.dp, end = 2.dp)) {
                    if (question.isEmpty()) {
                        Text(
                            QuestionHint,
                            style = inputStyle.copy(color = AsosColors.OnNavyMuted),
                            maxLines = 3,
                        )
                    }
                    field()
                }
            },
        )
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(
                Modifier.height(40.dp).clip(RoundedCornerShape(10.dp))
                    .clickable(role = Role.Button, onClick = onVoice),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                LegalIcon(AsosIcon.Microphone, AsosColors.OnNavy, 15.dp)
                Text(
                    "Ovozli kiritish",
                    style = asosTextStyle(12.0, 15.0, 600, AsosColors.OnNavy),
                )
            }
            ActionBox(
                onClick = onSubmit,
                enabled = !assistantBusy,
                modifier = Modifier.size(40.dp),
                background = Color.White.copy(alpha = if (assistantBusy) 0.5f else 1f),
                radius = 12.dp,
                label = "Yuborish",
            ) {
                LegalIcon(AsosIcon.Send, AsosColors.ActionBlue, 18.dp)
            }
        }
    }
}

@Composable
private fun HotlineCard(
    number: String,
    onAction: (LegalHelpAction) -> Unit,
    hotline: Hotline?,
    live: Boolean,
) {
    val canDial = hotline?.dialable ?: (!live && number != PrototypeHotline)
    Column(
        pageInset.fillMaxWidth().whiteCard(24.dp).padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                Modifier.size(44.dp).background(AsosColors.GreenLight, RoundedCornerShape(14.dp)),
                contentAlignment = Alignment.Center,
            ) {
                LegalIcon(AsosIcon.Phone, AsosColors.Green, 21.dp)
            }
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text("Ishonch telefoni", style = asosTextStyle(16.0, 21.0, 800))
                Text(
                    if (hotline == null) {
                        if (live) "Ish vaqti ma’lum qilinmagan" else "Har kuni 09:00–21:00 · bepul"
                    } else listOfNotNull(
                        "Har kuni".takeIf { hotline.everyDay },
                        hotline.hours.takeIf(String::isNotBlank),
                        "bepul".takeIf { hotline.isFree },
                    ).joinToString(" · "),
                    style = asosTextStyle(12.5, 16.0, 600, AsosColors.Secondary),
                )
            }
            if (hotline?.isOpen == true || !live) {
                StatusLabel("Ochiq", size = 12.0, weight = 800, gap = 5.dp)
            } else if (hotline != null) {
                Text("Yopiq", style = asosTextStyle(12.0, 16.0, 700, AsosColors.Secondary))
            }
        }
        Text(number, style = asosTextStyle(28.0, 38.0, 700, serif = true, spacing = 0.28))
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.Top,
        ) {
            TextAction(
                text = "Qo‘ng‘iroq qilish",
                onClick = { onAction(LegalHelpAction.CallHotline(number)) },
                modifier = Modifier.weight(1f).heightIn(min = 48.dp),
                primary = true,
                enabled = canDial,
            )
            TextAction(
                text = "Menga qo‘ng‘iroq",
                onClick = { onAction(LegalHelpAction.RequestCallback) },
                modifier = Modifier.weight(1f).heightIn(min = 50.dp),
            )
        }
    }
}

@Composable
private fun LawyerSection(
    specialty: Specialty,
    onSpecialtyChange: (Specialty) -> Unit,
    onAction: (LegalHelpAction) -> Unit,
    entries: List<Lawyer>,
    live: Boolean,
) {
    Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Row(
            pageInset.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom,
        ) {
            Text(
                "Advokatlar",
                Modifier.semantics { heading() },
                style = asosTextStyle(19.0, 24.0, 800),
            )
            StatusLabel("${entries.count { it.online }} nafari onlayn", size = 13.0, weight = 700, gap = 6.dp)
        }
        LawyerFilters(specialty, onSpecialtyChange)
        val visible = if (specialty == Specialty.All) entries else entries.filter { specialty in it.specialties }
        if (visible.isEmpty()) Text("Advokat topilmadi.", pageInset)
        visible.forEach { lawyer -> LawyerCard(lawyer, onAction, live) }
    }
}

@Composable
private fun LawyerFilters(selected: Specialty, onSelected: (Specialty) -> Unit) {
    val scroll = rememberScrollState()
    Column(Modifier.fillMaxWidth()) {
        Row(
            Modifier.fillMaxWidth().horizontalScroll(scroll).selectableGroup()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Specialty.entries.forEach { item ->
                val isSelected = item == selected
                val shape = RoundedCornerShape(99.dp)
                val outline = if (isSelected) Modifier else Modifier.border(1.dp, AsosColors.Border, shape)
                Box(
                    Modifier.height(38.dp).clip(shape)
                        .background(if (isSelected) AsosColors.Navy else Color.White)
                        .then(outline)
                        .selectable(selected = isSelected, role = Role.Tab) { onSelected(item) }
                        .padding(horizontal = if (isSelected) 16.dp else 17.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        item.label,
                        style = asosTextStyle(13.5, 17.0, 700, if (isSelected) Color.White else AsosColors.Navy),
                        maxLines = 1,
                    )
                }
            }
        }
    }
}

@Composable
private fun LawyerCard(lawyer: Lawyer, onAction: (LegalHelpAction) -> Unit, live: Boolean) {
    Column(
        pageInset.fillMaxWidth().whiteCard(22.dp).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            LawyerAvatar(lawyer)
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(lawyer.name, style = asosTextStyle(15.5, 19.0, 800))
                    if (lawyer.verified) LegalIcon(AsosIcon.Verified, AsosColors.Blue, 16.dp, "Tasdiqlangan")
                }
                Text(
                    lawyer.experience,
                    style = asosTextStyle(13.0, 16.0, 600, AsosColors.LawyerSecondary),
                )
                val slot = lawyer.nextFreeAt?.takeIf(::isFutureSlot)
                val availability = lawyer.availability ?: slot?.let { "Bo‘sh vaqt · ${localSlotLabel(it)}" }
                val rating = lawyer.rating
                if (availability != null) {
                    Text(
                        availability,
                        style = asosTextStyle(12.5, 16.0, 700, AsosColors.Amber),
                    )
                } else if (rating != null) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        LegalIcon(AsosIcon.Star, AsosColors.Gold, 14.dp)
                        Text(rating, style = asosTextStyle(12.5, 16.0, 700, AsosColors.LawyerSecondary))
                    }
                } else if (live) {
                    Text("Bo‘sh vaqt e’lon qilinmagan", style = asosTextStyle(12.5, 16.0, 700, AsosColors.Secondary))
                }
            }
        }
        if (!lawyer.online && (live || lawyer.rating == null)) {
            TextAction(
                text = "Vaqt band qilish",
                onClick = { onAction(LegalHelpAction.Book(lawyer.id)) },
                modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp),
                icon = AsosIcon.Calendar,
                fontSize = 14.5,
            )
        } else {
            TextAction(
                text = "Yozish",
                onClick = { onAction(LegalHelpAction.Chat(lawyer.id)) },
                modifier = Modifier.fillMaxWidth().heightIn(min = 46.dp),
                primary = true,
                icon = AsosIcon.Chat,
            )
        }
    }
}

@Composable
internal fun LawyerAvatar(lawyer: Lawyer, size: Dp = 54.dp) {
    val (background, ink) = when (lawyer.id) {
        "1", "sardor-rahimov" -> AsosColors.Navy to Color.White
        "2", "malika-yusupova" -> AsosColors.BlueLight to AsosColors.ActionBlue
        else -> AsosColors.AmberLight to AsosColors.Amber
    }
    Box(
        Modifier.size(size).background(background, RoundedCornerShape(size / 3)),
        contentAlignment = Alignment.Center,
    ) {
        Text(lawyer.initials, style = asosTextStyle(17.0, 22.0, 800, ink))
        if (lawyer.online) {
            Box(
                Modifier.align(Alignment.BottomEnd).offset(x = 2.dp, y = 2.dp)
                    .size(size / 3).background(Color.White, CircleShape).padding(2.dp)
                    .background(AsosColors.GreenDot, CircleShape),
            )
        }
    }
}

@Composable
private fun StatusLabel(text: String, size: Double, weight: Int, gap: Dp) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(gap),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(Modifier.size(8.dp).background(AsosColors.GreenDot, CircleShape))
        Text(text, style = asosTextStyle(size, size + 3, weight, AsosColors.Green))
    }
}

@Composable
private fun TextAction(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier,
    primary: Boolean = false,
    icon: AsosIcon? = null,
    fontSize: Double = if (primary) 14.5 else 14.0,
    enabled: Boolean = true,
) {
    val ink = when {
        !enabled -> AsosColors.Secondary
        primary -> Color.White
        else -> AsosColors.Navy
    }
    ActionBox(
        modifier = modifier,
        onClick = onClick,
        background = when {
            primary && enabled -> AsosColors.ActionBlue
            primary -> AsosColors.ButtonBorder
            else -> Color.Transparent
        },
        border = if (primary) null else AsosColors.ButtonBorder,
        enabled = enabled,
    ) {
        Row(
            Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (icon != null) LegalIcon(icon, ink, 18.dp)
            Text(text, style = asosTextStyle(fontSize, 19.0, 800, ink))
        }
    }
}

@Composable
internal fun ActionBox(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    background: Color = Color.Transparent,
    border: Color? = null,
    radius: Dp = 14.dp,
    label: String? = null,
    enabled: Boolean = true,
    content: @Composable BoxScope.() -> Unit,
) {
    val shape = RoundedCornerShape(radius)
    val borderModifier = if (border == null) Modifier else Modifier.border(1.dp, border, shape)
    val labelModifier = if (label == null) Modifier else Modifier.semantics {
        contentDescription = label
    }
    Box(
        modifier.clip(shape).background(background).then(borderModifier)
            .clickable(enabled = enabled, role = Role.Button, onClick = onClick).then(labelModifier),
        contentAlignment = Alignment.Center,
        content = content,
    )
}

private fun Modifier.whiteCard(radius: Dp): Modifier {
    val shape = RoundedCornerShape(radius)
    val shadowColor = AsosColors.Navy.copy(alpha = 0.15f)
    return shadow(8.dp, shape, clip = false, ambientColor = shadowColor, spotColor = shadowColor)
        .background(Color.White, shape)
}

@Preview(name = "Huquqiy yordam · 390 dp", widthDp = 390, heightDp = 900, showBackground = true)
@Preview(name = "Huquqiy yordam · to‘liq sahifa", widthDp = 390, heightDp = 1510, showBackground = true)
@Composable
private fun LegalHelpPreview() {
    AsosTheme {
        LegalHelpScreen(onAction = {})
    }
}
