# Asos icons

All icons are in [`core/designsystem/.../icon/AsosIcons.kt`](../../core/designsystem/src/main/kotlin/uz/asos/core/designsystem/icon/AsosIcons.kt), taken from the design canvas. Visual sheet: [`icons-preview.html`](icons-preview.html).

**Style:** 24×24 line icons, rounded caps and joins, 1.8–2 px strokes. Monochrome icons are drawn in Navy and tinted with `AsosIconTint`. Multicolour ones use `tint = Color.Unspecified`.

## Tints

| Token | Hex | Use |
|---|---|---|
| `AsosIconTint.Navy` | `#172B4D` | Default icon colour, main text |
| `AsosIconTint.Muted` | `#5E6A80` | Secondary icons, inactive tab |
| `AsosIconTint.Text3` | `#3A4A66` | Tertiary icons |
| `AsosIconTint.SoftGray` | `#8A94A6` | Decoration only, locked states |
| `AsosIconTint.Active` | `#1C64C0` | Selected tab, links, icon buttons on Brand50 |
| `AsosIconTint.Brand` | `#2478DE` | Large brand surfaces, progress |
| `AsosIconTint.OnBrand` | `#FFFFFF` | Icons on Brand600/Navy surfaces |
| `AsosIconTint.Success` | `#1E9E63` | Correct, read, online |
| `AsosIconTint.Error` | `#D9433A` | Wrong, logout |
| `AsosIconTint.Warning` | `#D9780A` | Highlights, partial score |
| `AsosIconTint.Streak` | `#D9780A` | Streak flame |
| `AsosIconTint.Dictionary` | `#5B3CC4` | Dictionary accent |
| `AsosIconTint.Achievement` | `#0F7A68` | Achievement accent |

## Navigation

| Icon | Used for | Tint |
|---|---|---|
| `AsosIcons.ChevronLeft` | Back (Orqaga) on every pushed screen | Navy |
| `AsosIcons.ChevronRight` | Row / card disclosure | Muted |
| `AsosIcons.ArrowRight` | Primary CTA arrow (Kirish, Home cards) | OnBrand |
| `AsosIcons.ChevronDown` | Expand section, show more replies | Muted |
| `AsosIcons.ChevronUp` | Collapse section | Muted |
| `AsosIcons.Close` | Close quiz (Testni yopish) | Navy |
| `AsosIcons.ArrowUp` | Send to AI assistant | OnBrand |

## Status

| Icon | Used for | Tint |
|---|---|---|
| `AsosIcons.Verified` | Verified lawyer (Tasdiqlangan) | multicolor (Brand500) |
| `AsosIcons.CheckCircleFilled` | Read (O‘qilgan), correct answer, completed, solved | multicolor (Success) |
| `AsosIcons.CircleOutline` | Not started (Boshlanmagan) | multicolor (Border) |
| `AsosIcons.XCircleFilled` | Wrong answer (Noto‘g‘ri javob) | multicolor (Error) |

## Communication

| Icon | Used for | Tint |
|---|---|---|
| `AsosIcons.Phone` | Call lawyer / hotline | Active |
| `AsosIcons.Video` | Video call | Active |
| `AsosIcons.EyeOff` | Anonymous posting (Anonim) | Muted |
| `AsosIcons.Comment` | Comments count, "Muhokamada" | Muted |
| `AsosIcons.Sparkles` | Asos AI yordamchi | OnBrand |
| `AsosIcons.Calendar` | Book a time (Vaqt band qilish) | Active |

## Content

| Icon | Used for | Tint |
|---|---|---|
| `AsosIcons.DocumentLines` | File attachment (.docx) | Active |
| `AsosIcons.BookOpen` | Dictionary (Lug‘at) | Dictionary |
| `AsosIcons.Document` | Official text (Rasmiy matn) | Active |
| `AsosIcons.Lightbulb` | Plain summary (Sodda qilib aytganda) | Warning |
| `AsosIcons.Book` | Achievement: Birinchi qadam | Active |
| `AsosIcons.Globe` | Language setting | Navy |
| `AsosIcons.Clock` | Quiz timer, reading time | Muted |

## Actions

| Icon | Used for | Tint |
|---|---|---|
| `AsosIcons.Attach` | Attach file in chat | Text3 |
| `AsosIcons.Microphone` | Voice search, chat voice input | Muted |
| `AsosIcons.Send` | Send message / reply (Yuborish) | OnBrand |
| `AsosIcons.Search` | Search fields (Qidirish) | Muted |
| `AsosIcons.Bookmark` | Save / saved (Saqlash, Saqlanganlar) | Navy |
| `AsosIcons.BookmarkFilled` | Saved state (Saqlangan) on the reader | Active |
| `AsosIcons.ListAll` | "Barchasi" community chip | Navy |
| `AsosIcons.Upvote` | Vote up | Muted / Active when voted |
| `AsosIcons.Downvote` | Vote down | Muted |
| `AsosIcons.Share` | Share (Ulashish) | Navy |
| `AsosIcons.Edit` | Ask a question (Savol berish) | OnBrand |
| `AsosIcons.Check` | Checkbox tick | OnBrand |
| `AsosIcons.Sliders` | Sort (Saralash), settings (Sozlamalar) | Navy |
| `AsosIcons.Play` | Continue reading | OnBrand |
| `AsosIcons.VolumeUp` | Listen to term (Tinglash) | Active |
| `AsosIcons.Bell` | Notifications, daily reminders | Navy |
| `AsosIcons.Logout` | Chiqish | Error |

## Tab bar

| Icon | Used for | Tint |
|---|---|---|
| `AsosIcons.Home` | Tab: Bosh sahifa; community Uy-joy chip | Active/Inactive |
| `AsosIcons.Person` | Tab: Profil; personal info row | Active/Inactive |
| `AsosIcons.TabConstitution` | Tab: Konstitutsiya | Active/Inactive |
| `AsosIcons.TabLearn` | Tab: O‘rganish | Active/Inactive |
| `AsosIcons.TabCommunity` | Tab: Hamjamiyat | Active/Inactive |

## Topics

| Icon | Used for | Tint |
|---|---|---|
| `AsosIcons.GraduationCap` | Community chip: Ta’lim; quiz CTA | Navy |
| `AsosIcons.BriefcaseSimple` | Community chip: Mehnat | Navy |
| `AsosIcons.People` | Community chip: Oila | Navy |
| `AsosIcons.Scales` | Legal help / Sud | Active |
| `AsosIcons.ShieldCheck` | Topic: Shaxsiy huquqlar | Active |
| `AsosIcons.Ballot` | Topic: Siyosiy huquqlar | Active |
| `AsosIcons.Briefcase` | Topic: Ijtimoiy huquqlar | Active |
| `AsosIcons.ClipboardCheck` | Topic: Burchlar (duties) | Active |
| `AsosIcons.Family` | Topic: Oila va yoshlar | Active |
| `AsosIcons.Government` | Topic: Davlat hokimiyati | Active |

## Learning

| Icon | Used for | Tint |
|---|---|---|
| `AsosIcons.Trophy` | Case score banner | OnBrand |
| `AsosIcons.Flame` | Streak (kunlik seriya) | Streak |
| `AsosIcons.Star` | Points, rating, level | Warning |
| `AsosIcons.TrophyLarge` | Achievement: Burchlar bilimdoni | Achievement |
| `AsosIcons.Lock` | Locked achievement | SoftGray |

## Brand

| Icon | Used for | Tint |
|---|---|---|
| `AsosIcons.LogoPillars` | Logo glyph without the tile (watermarks, profile) | Navy / OnBrand |
| `AsosIcons.LogoMark` | Asos logo: app icon, login, design system | multicolor (Brand500 + white) |
| `AsosIcons.OneId` | OneID login button | Navy |
| `AsosIcons.Google` | Google login button (official colours) | multicolor (Google) |

Not icons: the progress rings (36 and 104 dp) are the `ProgressRing` component.
