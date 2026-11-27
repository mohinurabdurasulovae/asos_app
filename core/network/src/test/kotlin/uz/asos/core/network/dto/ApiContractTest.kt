package uz.asos.core.network.dto

import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ApiContractTest {

    private val json = Json {
        ignoreUnknownKeys = true
        explicitNulls = false
        encodeDefaults = true
        coerceInputValues = true
        isLenient = true
    }

    private inline fun <reified T> decode(fixture: String): T {
        val text = checkNotNull(
            javaClass.classLoader?.getResourceAsStream("api/$fixture")?.bufferedReader()?.readText(),
        ) { "Missing fixture api/$fixture" }
        return json.decodeFromString(text)
    }

    @Test
    fun `terms list decodes, with article numbers rather than objects`() {
        val response = decode<TermsResponseDto>("terms.json")

        assertTrue(response.ok)
        assertTrue(response.items.isNotEmpty())
        assertEquals("D", response.items.first().letter)
        assertTrue(response.items.any { it.articles.isNotEmpty() })
        assertEquals(29, response.letters.size)
        assertNotNull(response.meta)
    }

    @Test
    fun `one term decodes with its article references`() {
        val response = decode<TermResponseDto>("term.json")

        assertEquals("Daxlsizlik", response.term.term)
        assertEquals("D", response.term.letter)
        assertTrue(response.term.definition.isNotBlank())
        assertTrue(response.term.articles.isNotEmpty())
        assertTrue(response.term.articles.first().number > 0)
    }

    @Test
    fun `term of the day decodes even when the editors left fields out`() {
        val response = decode<TermResponseDto>("term-of-the-day.json")

        assertTrue(response.term.term.isNotBlank())
        assertTrue(response.term.slug.isNotBlank())
    }

    @Test
    fun `posts feed decodes with its author, topic and best answer`() {
        val response = decode<PostsResponseDto>("posts.json")

        assertTrue(response.ok)
        assertTrue(response.items.isNotEmpty())
        assertEquals("popular", response.sort)

        val answered = response.items.first { it.bestAnswer != null }
        assertTrue(answered.bestAnswer!!.authorName.isNotBlank())
        assertNotNull(answered.community)
        assertTrue(answered.author.initials.isNotBlank())
    }

    @Test
    fun `one discussion decodes with its nested comment replies`() {
        val response = decode<PostResponseDto>("post.json")

        assertTrue(response.post.body.isNotBlank())
        assertTrue(response.post.comments.isNotEmpty())

        val best = response.post.comments.first { it.isBest }
        assertTrue(best.isLawyer)
        assertNotNull(best.lawyer)
        assertTrue(best.author.isVerified)

        assertTrue(response.post.comments.all { it.replies.all { reply -> reply.replies.isEmpty() } })
    }

    @Test
    fun `communities decode with their tones and member counts`() {
        val response = decode<CommunitiesResponseDto>("communities.json")

        assertEquals(5, response.items.size)
        val uyJoy = response.items.first { it.slug == "uy-joy" }
        assertEquals("Uy-joy", uyJoy.title)
        assertEquals("pink", uyJoy.tone)
        assertTrue((uyJoy.memberCount ?: 0) > 0)
    }

    @Test
    fun `learn hub decodes`() {
        val response = decode<LearnResponseDto>("learn.json")

        assertTrue(response.ok)
        assertTrue(response.quizzes.isNotEmpty())
        assertTrue(response.quizzes.first().slug.isNotBlank())
    }

    @Test
    fun `legal help overview decodes`() {
        val response = decode<HelpResponseDto>("help.json")

        assertTrue(response.ok)
        assertTrue(response.lawyers.isNotEmpty())
        assertTrue(response.lawyers.first().name.isNotBlank())
    }

    @Test
    fun `constitution tree decodes with all six sections`() {
        val response = decode<ConstitutionResponseDto>("constitution.json")

        assertTrue(response.ok)
        assertEquals(6, response.sections.size)
        assertTrue(response.sections.first().chapters.isNotEmpty())
    }

    @Test
    fun `one article decodes, with its text in the text field and no parts`() {
        val response = decode<ArticleResponseDto>("article.json")

        assertEquals(50, response.article.number)
        assertFalse(response.article.isPreamble)
        assertTrue(response.article.parts.isEmpty())
        assertTrue(response.article.text?.isNotBlank() ?: false)
    }
}
