package uz.asos.huquqiyyordam

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
internal fun HelpDialogs(model: AssistantViewModel) {
    when (val dialog = model.dialog) {
        null -> Unit
        is HelpDialog.Login -> LoginDialog(model, dialog)
        HelpDialog.Callback -> CallbackDialog(model)
        is HelpDialog.Chat -> Unit
        is HelpDialog.BookingUnavailable -> AlertDialog(
            onDismissRequest = model::closeDialog,
            title = { Text("Vaqt band qilish") },
            text = { Text("Advokatning bo‘sh vaqti hozircha e’lon qilinmagan.") },
            confirmButton = {
                TextButton(onClick = { model.openChat(dialog.lawyer) }) { Text("Advokatga yozish") }
            },
            dismissButton = { TextButton(onClick = model::closeDialog) { Text("Yopish") } },
        )
        is HelpDialog.Booking -> HelpAlert(
            model, "Vaqt band qilish", "Tasdiqlash", true, model::book,
        ) {
            Text(dialog.lawyer.name)
            Text("${localSlotLabel(dialog.slot)} · Toshkent vaqti")
        }
        is HelpDialog.Notice -> AlertDialog(
            onDismissRequest = model::closeDialog,
            title = { Text(dialog.title) },
            text = { Text(dialog.text) },
            confirmButton = { TextButton(onClick = model::closeDialog) { Text("Yopish") } },
        )
    }
}

@Composable
private fun LoginDialog(model: AssistantViewModel, login: HelpDialog.Login) {
    var phone by rememberSaveable { mutableStateOf("") }
    var code by remember(login.phone) { mutableStateOf("") }
    val sent = login.phone != null
    HelpAlert(
        model,
        title = "Telefon orqali kirish",
        action = if (sent) "Tasdiqlash" else "Kod yuborish",
        canSubmit = if (sent) code.length == 6 else validPhone(phone),
        onSubmit = { if (sent) model.verify(code) else model.requestCode(normalizePhone(phone)) },
    ) {
        if (!sent) {
            PhoneField(phone, { phone = it.take(24) }, !model.busy)
        } else {
            Text("SMS kodi ${login.phone} raqamiga yuborildi.")
            login.devCode?.let { Text("Sinov kodi: $it") }
            OutlinedTextField(
                value = code,
                onValueChange = { code = it.filter { digit -> digit in '0'..'9' }.take(6) },
                label = { Text("SMS kodi") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                enabled = !model.busy,
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )
            TextButton(onClick = model::changePhone, enabled = !model.busy) { Text("Raqamni o‘zgartirish") }
        }
    }
}

@Composable
private fun CallbackDialog(model: AssistantViewModel) {
    var phone by rememberSaveable { mutableStateOf("") }
    var topic by rememberSaveable { mutableStateOf("") }
    HelpAlert(model, "Menga qo‘ng‘iroq", "Yuborish", validPhone(phone), {
        model.requestCallback(normalizePhone(phone), topic.trim())
    }) {
        PhoneField(phone, { phone = it.take(24) }, !model.busy)
        OutlinedTextField(
            value = topic,
            onValueChange = { topic = it.take(190) },
            label = { Text("Mavzu (ixtiyoriy)") },
            enabled = !model.busy,
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun PhoneField(phone: String, onChange: (String) -> Unit, enabled: Boolean) {
    OutlinedTextField(
        value = phone,
        onValueChange = onChange,
        label = { Text("Telefon raqami") },
        placeholder = { Text("+998 90 123 45 67") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
        singleLine = true,
        enabled = enabled,
        modifier = Modifier.fillMaxWidth(),
    )
}

private fun normalizePhone(value: String): String =
    value.filterNot { it == ' ' || it == '-' || it == '(' || it == ')' }

private fun validPhone(value: String): Boolean =
    Regex("(?:\\+?998)?[0-9]{9}").matches(normalizePhone(value))

@Composable
private fun HelpAlert(
    model: AssistantViewModel,
    title: String,
    action: String,
    canSubmit: Boolean,
    onSubmit: () -> Unit,
    content: @Composable () -> Unit,
) {
    AlertDialog(
        onDismissRequest = model::closeDialog,
        title = { Text(title) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                content()
                if (model.busy) LinearProgressIndicator(Modifier.fillMaxWidth())
                model.dialogError?.let { Text(it, color = MaterialTheme.colorScheme.error) }
            }
        },
        confirmButton = {
            TextButton(onClick = onSubmit, enabled = canSubmit && !model.busy) { Text(action) }
        },
        dismissButton = {
            TextButton(onClick = model::closeDialog, enabled = !model.busy) { Text("Yopish") }
        },
    )
}
