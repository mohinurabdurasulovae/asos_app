/*
 * LegalHelpSampleData.kt
 * Module: :core:testing   ·   Work package: WP-02 (legal-help slice written with WP-10)
 *
 * Sample content from the design canvas (Yordam, Chat) for tests.
 */
package uz.asos.core.testing.data

import uz.asos.core.model.legalhelp.AssistantAnswer
import uz.asos.core.model.legalhelp.AssistantArticle
import uz.asos.core.model.legalhelp.AssistantIntro
import uz.asos.core.model.legalhelp.ChatMessage
import uz.asos.core.model.legalhelp.Conversation
import uz.asos.core.model.legalhelp.ConversationDetail
import uz.asos.core.model.legalhelp.HelpOverview
import uz.asos.core.model.legalhelp.Hotline
import uz.asos.core.model.legalhelp.Lawyer
import uz.asos.core.model.legalhelp.MessageAttachment
import uz.asos.core.model.legalhelp.QuotedArticle

object LegalHelpSampleData {

    val sardor = Lawyer(
        id = 1,
        name = "Sardor Rahimov",
        initials = "SR",
        specialties = listOf("Uy-joy", "Fuqarolik ishlari"),
        years = 9,
        rating = 4.9,
        reviewsCount = 210,
        isVerified = true,
        isOnline = true,
        nextFreeAt = null,
    )

    val malika = Lawyer(
        id = 2,
        name = "Malika Yusupova",
        initials = "MY",
        specialties = listOf("Oila", "Ta’lim"),
        years = 12,
        rating = 4.8,
        reviewsCount = 164,
        isVerified = true,
        isOnline = true,
        nextFreeAt = null,
    )

    val bekzod = Lawyer(
        id = 3,
        name = "Bekzod Karimov",
        initials = "BK",
        specialties = listOf("Mehnat"),
        years = 6,
        rating = null,
        reviewsCount = 0,
        isVerified = true,
        isOnline = false,
        nextFreeAt = "2026-11-21 15:00",
    )

    val hotline = Hotline(
        phone = "+998 (71) 000-00-00",
        hours = "09:00–21:00",
        isFree = true,
        everyDay = true,
        isOpen = true,
        note = null,
    )

    val help = HelpOverview(
        quote = QuotedArticle(
            number = 29,
            title = "Malakali yuridik yordam olish huquqi",
            slug = "29",
            body = "Har kimga malakali yuridik yordam olish huquqi kafolatlanadi.",
        ),
        assistant = AssistantIntro(
            title = "Asos AI yordamchi",
            subtitle = "Tegishli moddalarni topib, sodda tushuntiradi",
            suggestions = listOf("Ish haqi kechikdi", "Ijara muammosi", "Talaba huquqlari"),
            disclaimer = "AI umumiy huquqiy ma’lumot beradi va advokat maslahati o‘rnini bosmaydi.",
        ),
        hotline = hotline,
        lawyers = listOf(sardor, malika, bekzod),
        lawyersOnline = 2,
        specialties = listOf("Uy-joy", "Oila", "Mehnat", "Ta’lim"),
    )

    val rentAnswer = AssistantAnswer(
        threadId = 11,
        answer = "Uy-joy daxlsiz: uy egasi ham sizning roziligingizsiz kvartiraga kira olmaydi.",
        articles = listOf(
            AssistantArticle(
                number = 31,
                title = "Shaxsiy hayot va uy-joy daxlsizligi",
                slug = "31",
                bullets = listOf("Har kim uy-joy daxlsizligi huquqiga ega."),
            ),
        ),
        disclaimer = null,
    )

    val conversation = Conversation(
        id = 42,
        subject = null,
        lawyer = sardor,
        unreadCount = 1,
        createdAt = "2026-11-21 14:02:00",
        lastMessageAt = "2026-11-21 14:08:00",
    )

    val conversationDetail = ConversationDetail(
        conversation = conversation,
        messages = listOf(
            ChatMessage(
                id = 1,
                body = "Assalomu alaykum. Uy egasi men yo‘qligimda kvartiraga kirib turibdi.",
                isMine = true,
                createdAt = "2026-11-21 14:02:00",
                readAt = "2026-11-21 14:03:00",
                attachments = emptyList(),
            ),
            ChatMessage(
                id = 2,
                body = "Va alaykum assalom, Aziza. Ijara shartnomangiz notarial tasdiqlanganmi?",
                isMine = false,
                createdAt = "2026-11-21 14:05:00",
                readAt = null,
                attachments = emptyList(),
            ),
            ChatMessage(
                id = 3,
                body = "Yo‘q, oddiy yozma shartnoma.",
                isMine = true,
                createdAt = "2026-11-21 14:06:00",
                readAt = "2026-11-21 14:06:30",
                attachments = emptyList(),
            ),
            ChatMessage(
                id = 4,
                body = "Tushunarli. Birinchi qadam — uy egasiga yozma ogohlantirish. Namunani va tayanadigan moddani yubordim:",
                isMine = false,
                createdAt = "2026-11-21 14:08:00",
                readAt = null,
                attachments = listOf(
                    MessageAttachment.File(
                        fileName = "Ogohlantirish_xati.docx",
                        mime = "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                        bytes = 24_576,
                        url = null,
                    ),
                    MessageAttachment.ArticleLink(number = 31, title = "Shaxsiy hayot va uy-joy daxlsizligi", slug = "31"),
                ),
            ),
        ),
    )
}
