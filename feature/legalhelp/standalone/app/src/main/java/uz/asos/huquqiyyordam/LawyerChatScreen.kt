package uz.asos.huquqiyyordam

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.speech.RecognizerIntent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.io.IOException
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlinx.coroutines.launch
import org.json.JSONException

private const val MessageLimit = 4_000
private val ChatDate = DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.ROOT)
private val ChatClock = DateTimeFormatter.ofPattern("HH:mm", Locale.ROOT)
private val QuickReplies = listOf("Rahmat!", "Keyingi qadam qanday?", "Qanday hujjatlar kerak?")

@Composable
internal fun LawyerChatScreen(
    chat: HelpDialog.Chat,
    busy: Boolean,
    error: String?,
    onBack: () -> Unit,
    onRefresh: () -> Unit,
    onSend: (String, () -> Unit) -> Unit,
    loadArticle: suspend (Int) -> ArticleContent,
    hasSession: Boolean = true,
) {
    var draft by rememberSaveable(chat.lawyer.id) { mutableStateOf("") }
    var selectedArticle by remember { mutableStateOf<ChatAttachment.Article?>(null) }
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val snackbar = remember { SnackbarHostState() }
    val uriHandler = LocalUriHandler.current
    val speechLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val transcript = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                ?.firstOrNull { it.isNotBlank() }
            if (transcript != null) {
                draft = listOf(draft.trim(), transcript).filter(String::isNotBlank)
                    .joinToString(" ").take(MessageLimit)
            }
        }
    }
    val lastMessage = chat.messages.lastOrNull()?.id
    val today = LocalDate.now(ZoneId.of("Asia/Tashkent"))
    LaunchedEffect(lastMessage) {
        if (lastMessage != null) listState.scrollToItem(chat.messages.lastIndex)
    }

    Box(Modifier.fillMaxSize().background(AsosColors.Background), contentAlignment = Alignment.TopCenter) {
        Column(Modifier.widthIn(max = 460.dp).fillMaxSize().navigationBarsPadding().imePadding()) {
            ChatHeader(chat.lawyer, onBack)
            if (busy) LinearProgressIndicator(Modifier.fillMaxWidth())
            LazyColumn(
                modifier = Modifier.fillMaxWidth().weight(1f),
                state = listState,
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                if (chat.messages.isEmpty() && !busy && error == null) {
                    item {
                        val message = if (hasSession) "Advokatga birinchi xabaringizni yozing."
                        else "Xabar yuborish hozircha mavjud emas."
                        Text(message, style = asosTextStyle(14.0, 20.0, 500))
                        if (hasSession) TextButton(onClick = onRefresh) { Text("Yangilash") }
                    }
                }
                items(chat.messages.size, key = { chat.messages[it].id }) { index ->
                    val message = chat.messages[index]
                    val timestamp = message.sentAt?.let(::localChatTime)
                    val day = timestamp?.toLocalDate()
                    val previousDay = chat.messages.getOrNull(index - 1)?.sentAt?.let(::localChatTime)?.toLocalDate()
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        if (day != null && day != previousDay) {
                            ActionBox(
                                onClick = onRefresh,
                                enabled = !busy,
                                modifier = Modifier.align(Alignment.CenterHorizontally).heightIn(min = 26.dp),
                                background = AsosColors.Border,
                                radius = 99.dp,
                                label = "Xabarlarni yangilash",
                            ) {
                                Text(
                                    if (day == today) "Bugun" else ChatDate.format(day),
                                    Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                                    style = asosTextStyle(12.5, 17.0, 700, AsosColors.LawyerSecondary),
                                )
                            }
                        }
                        MessageBubble(
                            message,
                            timestamp?.format(ChatClock),
                            openArticle = { selectedArticle = it },
                            openFile = { url ->
                                try {
                                    uriHandler.openUri(url)
                                } catch (_: IllegalArgumentException) {
                                    scope.launch { snackbar.showSnackbar("Faylni ochib bo‘lmadi.") }
                                }
                            },
                        )
                    }
                }
            }
            if (error != null) {
                Row(
                    Modifier.fillMaxWidth().padding(start = 16.dp, end = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(error, Modifier.weight(1f), style = asosTextStyle(13.0, 18.0, 600))
                    TextButton(onClick = onRefresh, enabled = !busy) { Text("Yangilash") }
                }
            }
            ChatComposer(
                draft = draft,
                onChange = { draft = it.take(MessageLimit) },
                enabled = !busy,
                canSend = chat.id != null && hasSession,
                onSend = { onSend(draft.trim()) { draft = "" } },
                onVoice = {
                    val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
                        .putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                        .putExtra(RecognizerIntent.EXTRA_LANGUAGE, "uz-UZ")
                    try {
                        speechLauncher.launch(intent)
                    } catch (_: ActivityNotFoundException) {
                        scope.launch { snackbar.showSnackbar("Bu qurilmada ovozli kiritish mavjud emas.") }
                    }
                },
            )
        }
        SnackbarHost(snackbar, Modifier.align(Alignment.BottomCenter).navigationBarsPadding().imePadding().padding(bottom = 84.dp))
    }
    selectedArticle?.let {
        ChatArticleDialog(it, loadArticle, onClose = { selectedArticle = null })
    }
}

@Composable
private fun ChatHeader(
    lawyer: Lawyer,
    onBack: () -> Unit,
) {
    Column(Modifier.fillMaxWidth().background(Color.White)) {
        Row(
            Modifier.fillMaxWidth().statusBarsPadding().padding(start = 12.dp, end = 11.dp, top = 8.dp, bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            ActionBox(onClick = onBack, modifier = Modifier.size(44.dp), label = "Orqaga") {
                LegalIcon(AsosIcon.Back, AsosColors.Navy, 22.dp)
            }
            LawyerAvatar(lawyer, 42.dp)
            Column(Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                ) {
                    Text(
                        lawyer.name,
                        Modifier.weight(1f, fill = false),
                        style = asosTextStyle(15.0, 20.0, 800),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    if (lawyer.verified) LegalIcon(AsosIcon.Verified, AsosColors.Blue, 14.dp, "Tasdiqlangan")
                }
                Text(
                    if (lawyer.online) "Onlayn · advokat" else "Advokat",
                    style = asosTextStyle(12.0, 16.0, 600, if (lawyer.online) AsosColors.Green else AsosColors.Secondary),
                )
            }
        }
        HorizontalDivider(color = AsosColors.Border)
    }
}

@Composable
private fun MessageBubble(
    message: LawyerMessage,
    time: String?,
    openArticle: (ChatAttachment.Article) -> Unit,
    openFile: (String) -> Unit,
) {
    val color = if (message.isMine) AsosColors.ActionBlue else Color.White
    val ink = if (message.isMine) Color.White else AsosColors.Navy
    val shape = RoundedCornerShape(
        topStart = 18.dp, topEnd = 18.dp,
        bottomStart = if (message.isMine) 18.dp else 6.dp,
        bottomEnd = if (message.isMine) 6.dp else 18.dp,
    )
    Column(
        Modifier.fillMaxWidth(),
        horizontalAlignment = if (message.isMine) Alignment.End else Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Column(
            Modifier.fillMaxWidth(if (message.isMine) 0.82f else 0.86f)
                .background(color, shape).padding(horizontal = 14.dp, vertical = 11.dp),
            verticalArrangement = Arrangement.spacedBy(9.dp),
        ) {
            if (message.body.isNotBlank()) Text(message.body, style = asosTextStyle(15.0, 21.0, 400, ink))
            message.attachments.forEach {
                when (it) {
                    is ChatAttachment.Article -> ArticleAttachment(it) { openArticle(it) }
                    is ChatAttachment.File -> FileAttachment(it, openFile)
                }
            }
        }
        if (time != null) {
            Text(
                if (message.isMine && message.readAt != null) "$time · O‘qildi" else time,
                style = asosTextStyle(12.0, 16.0, 600, AsosColors.Secondary),
            )
        }
    }
}

@Composable
private fun ArticleAttachment(article: ChatAttachment.Article, open: () -> Unit) {
    ActionBox(onClick = open, modifier = Modifier.fillMaxWidth(), background = AsosColors.Navy, radius = 12.dp) {
        Row(
            Modifier.fillMaxWidth().padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Box(Modifier.size(38.dp).background(AsosColors.Blue, RoundedCornerShape(10.dp)), contentAlignment = Alignment.Center) {
                Text(article.number.toString(), style = asosTextStyle(16.0, 20.0, 700, Color.White, serif = true))
            }
            Column(Modifier.weight(1f)) {
                val label = "${article.number}-modda"
                Text(label, style = asosTextStyle(14.0, 19.0, 800, Color.White))
                article.title?.takeUnless { it.equals(label, ignoreCase = true) }?.let {
                    Text(it, style = asosTextStyle(12.0, 17.0, 600, AsosColors.OnNavy))
                }
            }
            Text("›", style = asosTextStyle(22.0, 24.0, 500, Color.White))
        }
    }
}

@Composable
private fun FileAttachment(file: ChatAttachment.File, openFile: (String) -> Unit) {
    ActionBox(
        onClick = { file.url?.let(openFile) }, enabled = file.url != null,
        modifier = Modifier.fillMaxWidth(), background = AsosColors.Background, radius = 12.dp,
    ) {
        Row(
            Modifier.fillMaxWidth().padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Box(Modifier.size(38.dp).background(AsosColors.BlueLight, RoundedCornerShape(10.dp)), contentAlignment = Alignment.Center) {
                LegalIcon(AsosIcon.File, AsosColors.ActionBlue, 20.dp)
            }
            Column(Modifier.weight(1f)) {
                Text(file.name, style = asosTextStyle(14.0, 18.0, 800), maxLines = 2, overflow = TextOverflow.Ellipsis)
                if (file.bytes > 0) {
                    val size = if (file.bytes < 1024) "${file.bytes} B" else "${file.bytes / 1024} KB"
                    Text(size, style = asosTextStyle(12.0, 16.0, 600, AsosColors.Secondary))
                }
            }
        }
    }
}

@Composable
private fun ChatComposer(
    draft: String,
    onChange: (String) -> Unit,
    enabled: Boolean,
    canSend: Boolean,
    onSend: () -> Unit,
    onVoice: () -> Unit,
) {
    Column(Modifier.fillMaxWidth().background(Color.White)) {
        HorizontalDivider(color = AsosColors.Border)
        Row(
            Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()).padding(horizontal = 12.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            QuickReplies.forEach { reply ->
                ActionBox(
                    onClick = { onChange(reply) }, enabled = enabled,
                    background = AsosColors.Background, border = AsosColors.BlueLight, radius = 99.dp,
                ) {
                    Text(
                        reply, Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                        style = asosTextStyle(13.0, 18.0, 700, AsosColors.ActionBlue),
                    )
                }
            }
        }
        Row(
            Modifier.fillMaxWidth().padding(start = 12.dp, end = 12.dp, bottom = 14.dp),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            ActionBox(
                onClick = {}, enabled = false, modifier = Modifier.size(46.dp),
                background = AsosColors.Background, label = "Fayl biriktirish",
            ) { LegalIcon(AsosIcon.Clip, AsosColors.Secondary, 22.dp) }
            val shape = RoundedCornerShape(15.dp)
            Row(
                Modifier.weight(1f).heightIn(min = 46.dp).background(AsosColors.Background, shape)
                    .border(1.dp, AsosColors.Border, shape).padding(start = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                BasicTextField(
                    value = draft,
                    onValueChange = onChange,
                    modifier = Modifier.weight(1f),
                    enabled = enabled,
                    maxLines = 4,
                    textStyle = asosTextStyle(15.0, 21.0, 400),
                    cursorBrush = SolidColor(AsosColors.ActionBlue),
                    decorationBox = { field ->
                        Box {
                            if (draft.isEmpty()) Text("Xabar yozing…", style = asosTextStyle(15.0, 21.0, 400, AsosColors.Secondary))
                            field()
                        }
                    },
                )
                ActionBox(onClick = onVoice, enabled = enabled, modifier = Modifier.size(38.dp), label = "Ovozli kiritish") {
                    LegalIcon(AsosIcon.Microphone, AsosColors.Secondary, 18.dp)
                }
            }
            ActionBox(
                onClick = onSend, enabled = enabled && canSend && draft.isNotBlank(), modifier = Modifier.size(46.dp),
                background = if (canSend) AsosColors.ActionBlue else AsosColors.BlueLight, label = "Yuborish",
            ) { LegalIcon(AsosIcon.Plane, Color.White, 22.dp) }
        }
    }
}

@Composable
private fun ChatArticleDialog(
    article: ChatAttachment.Article,
    loadArticle: suspend (Int) -> ArticleContent,
    onClose: () -> Unit,
) {
    var content by remember(article.number) { mutableStateOf<ArticleContent?>(null) }
    var error by remember(article.number) { mutableStateOf(false) }
    LaunchedEffect(article.number) {
        try {
            content = loadArticle(article.number)
        } catch (_: IOException) {
            error = true
        } catch (_: JSONException) {
            error = true
        }
    }
    AlertDialog(
        onDismissRequest = onClose,
        title = { Text("${article.number}-modda") },
        text = {
            Column(Modifier.heightIn(max = 360.dp).verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                val detail = content
                when {
                    error -> Text("Moddani yuklab bo‘lmadi.")
                    detail == null -> LinearProgressIndicator(Modifier.fillMaxWidth())
                    else -> {
                        (detail.title ?: article.title)?.let { Text(it, style = asosTextStyle(15.0, 20.0, 800)) }
                        if (detail.parts.isEmpty()) Text("Rasmiy matn hozircha mavjud emas.")
                        detail.parts.forEach { Text(it, style = asosTextStyle(15.0, 22.0, 400, serif = true)) }
                    }
                }
            }
        },
        confirmButton = { TextButton(onClick = onClose) { Text("Yopish") } },
    )
}

@Preview(name = "Advokat bilan chat", widthDp = 390, heightDp = 844, showBackground = true)
@Composable
private fun LawyerChatPreview() {
    val now = LocalDate.now(ZoneId.of("Asia/Tashkent"))
    AsosTheme {
        LawyerChatScreen(
            chat = HelpDialog.Chat(previewLawyers.first(), 1L, listOf(
                LawyerMessage(1L, "Assalomu alaykum. Uy egasi men yo‘qligimda kvartiraga kirib turibdi.", true, "$now 09:02:00", "$now 09:05:00"),
                LawyerMessage(2L, "Va alaykum assalom. Ijara shartnomangiz notarial tasdiqlanganmi?", false, "$now 09:05:00"),
                LawyerMessage(
                    3L, "Quyidagi moddani ko‘rib chiqing.", false, "$now 09:08:00",
                    attachments = listOf(
                        ChatAttachment.File("Ogohlantirish_xati.docx", 24 * 1024, null),
                        ChatAttachment.Article(31, "Shaxsiy hayot va uy-joy daxlsizligi"),
                    ),
                ),
            )),
            busy = false, error = null, onBack = {}, onRefresh = {}, onSend = { _, _ -> },
            loadArticle = { ArticleContent(null, emptyList()) },
        )
    }
}
