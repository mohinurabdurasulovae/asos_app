package uz.asos.huquqiyyordam

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModelProvider

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(android.graphics.Color.TRANSPARENT, android.graphics.Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.light(android.graphics.Color.TRANSPARENT, android.graphics.Color.TRANSPARENT),
        )
        val assistant = ViewModelProvider(this)[AssistantViewModel::class.java]
        setContent {
            AsosTheme {
                StandaloneLegalHelp(
                    onBack = ::finish,
                    assistant = assistant,
                    onDial = { number ->
                        val digits = number.filter { it == '+' || it in '0'..'9' }
                        if (!openIntent(Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", digits, null)))) {
                            assistant.show(HelpDialog.Notice("Ishonch telefoni", number))
                        }
                    },
                )
            }
        }
    }
    private fun openIntent(intent: Intent): Boolean = try {
        startActivity(intent)
        true
    } catch (_: ActivityNotFoundException) {
        false
    } catch (_: SecurityException) {
        false
    }

}

private val AsosScheme = lightColorScheme(
    primary = AsosColors.ActionBlue,
    onPrimary = Color.White,
    background = AsosColors.Background,
    onBackground = AsosColors.Navy,
    surface = Color.White,
    onSurface = AsosColors.Navy,
)

@Composable
internal fun AsosTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = AsosScheme, content = content)
}

@Composable
private fun StandaloneLegalHelp(
    onBack: () -> Unit,
    assistant: AssistantViewModel,
    onDial: (String) -> Unit,
) {
    val screenState = rememberSaveableStateHolder()
    val chat = assistant.dialog as? HelpDialog.Chat
    if (chat != null) {
        BackHandler(onBack = assistant::closeDialog)
        LawyerChatScreen(
            chat = chat,
            hasSession = assistant.chatHasSession,
            busy = assistant.busy,
            error = assistant.dialogError,
            onBack = assistant::closeDialog,
            onRefresh = assistant::refreshChat,
            onSend = assistant::sendMessage,
            loadArticle = assistant::loadArticle,
        )
        return
    }
    screenState.SaveableStateProvider("help") {
        LegalHelpScreenContent(
            live = true,
            help = assistant.help,
            helpError = assistant.helpError,
            onReload = assistant::loadHelp,
            lawyers = assistant.lawyers,
            lawyersError = assistant.lawyersError,
            onReloadLawyers = assistant::loadLawyers,
            assistantBusy = assistant.state.sending,
            assistantContent = {
                AssistantConversation(assistant.state, assistant::retry)
            },
            onAction = { action ->
                when (action) {
                    LegalHelpAction.Back -> onBack()
                    is LegalHelpAction.SubmitQuestion -> assistant.send(action.question)
                    is LegalHelpAction.CallHotline -> onDial(action.number)
                    LegalHelpAction.RequestCallback -> assistant.openCallback()
                    is LegalHelpAction.Chat ->
                        assistant.lawyers?.find { it.id == action.lawyerId }?.let(assistant::openChat)
                    is LegalHelpAction.Book ->
                        assistant.lawyers?.find { it.id == action.lawyerId }?.let(assistant::openBooking)
                }
            },
        )
    }
    HelpDialogs(assistant)
}
