# Vice Log - Apple Shortcut Build Guide

A step-by-step guide to building an iPhone shortcut that tracks daily cigarettes
and drinks, with weekly totals, stored in a readable log.

## How It Works

- **Data lives in a text file** (`Shortcuts/Vice Log.txt` in iCloud Drive) that
  gets overwritten each run — this is the source of truth.
- **An Apple Note** named "Vice Log" is also created each run with monospaced
  formatting so you can browse your history in the Notes app.
- Each row is one day: `3/29/2026: 2 smokes, 3 drinks`
- Weekly totals are inserted when a new week begins.
- Weeks run **Sunday – Saturday** (based on your device locale).

> **Why a file + note?** Apple Shortcuts cannot modify the body of an existing
> note — only append to it or create new ones. The text file is overwritable and
> acts as the reliable data store. The note is regenerated from it each time.
> You may accumulate duplicate "Vice Log" notes over time; just delete old ones
> periodically. If your iOS version offers a "Delete Notes" action in Shortcuts,
> the guide includes a step to auto-delete the old note first.

---

## Before You Start

1. Open the **Shortcuts** app on your iPhone.
2. Tap **+** to create a new shortcut.
3. Tap the name at the top and rename it to **Vice Log**.
4. Optionally set an icon (e.g. 🚬 glyph, red color).

---

## Section 1 — Date Setup

These variables are used throughout the shortcut.

| # | Action | Configuration |
|---|--------|---------------|
| 1 | **Format Date** | Input: `Current Date`, Format: **Custom** → `M/d/yyyy`. Tap the result → **Set Variable** → name it `Today`. |
| 2 | **Format Date** | Input: `Current Date`, Format: **Custom** → `M/d`. Set Variable → `TodayShort`. |
| 3 | **Format Date** | Input: `Current Date`, Format: **Custom** → `yyyy`. Set Variable → `Year`. |
| 4 | **Format Date** | Input: `Current Date`, Format: **Custom** → `w`. Set Variable → `WeekNum`. |

---

## Section 2 — Load Data File

Read the existing log from iCloud Drive (or start fresh on first run).

| # | Action | Configuration |
|---|--------|---------------|
| 5 | **Get File** | File Path: `Shortcuts/Vice Log.txt`, Error If Not Found: **OFF**. Set Variable → `LogFile`. |
| 6 | **If** | Input: `LogFile`, Condition: **has any value**. |
| 7 | *(inside If)* **Get Text from** | Input: `LogFile`. Set Variable → `NoteText`. |
| 8 | **Otherwise** | |
| 9 | *(inside Otherwise)* **Text** | Leave empty (literally blank). Set Variable → `NoteText`. |
| 10 | **End If** | |

---

## Section 3 — Choose Category

| # | Action | Configuration |
|---|--------|---------------|
| 11 | **Choose from Menu** | Prompt: `What are you logging?` — Two options: `🚬 Smokes` and `🍺 Drinks`. |
| 12 | *(inside 🚬 Smokes)* **Text** | Type `smokes`. Set Variable → `Category`. |
| 13 | *(inside 🍺 Drinks)* **Text** | Type `drinks`. Set Variable → `Category`. |
| 14 | **End Menu** | |

---

## Section 4 — Parse Today's Entry

Check whether today already has a row in the log and extract current counts.

| # | Action | Configuration |
|---|--------|---------------|
| 15 | **Text** | Type: `` `Today`: (\d+) smokes, (\d+) drinks `` — where `` `Today` `` is the *variable* (tap the variable button and insert `Today`). The rest is literal text including the regex metacharacters `(\d+)`. This builds the regex pattern. Set Variable → `TodayPattern`. |
| 16 | **Match Text** | Input: `NoteText`, Pattern: `TodayPattern`. Set Variable → `Matches`. |
| 17 | **Count** | Input: `Matches`. Set Variable → `MatchCount`. |
| 18 | **If** | Input: `MatchCount`, Condition: **is greater than** `0`. |
| 19 | *(inside If)* **Get Item from List** | Input: `Matches`, Index: **First Item**. Now tap the result variable, choose **Get Match Group**, set Group Index to `1`. Set Variable → `SmokeCount`. |
| 20 | *(inside If)* **Get Item from List** | Same as above but Group Index `2`. Set Variable → `DrinkCount`. |
| 21 | **Otherwise** | |
| 22 | *(inside Otherwise)* **Number** | `0`. Set Variable → `SmokeCount`. |
| 23 | *(inside Otherwise)* **Number** | `0`. Set Variable → `DrinkCount`. |
| 24 | **End If** | |

> **How to access Match Groups:** When you insert a Match Text result into
> another action, tap the blue variable token, then tap **Match Group** in the
> options that appear. Set the **Group Index** to the capture group number
> (1 = first parenthesized group, 2 = second, etc.).

---

## Section 5 — Select Current Count

Set `CurrentCount` to the count for the chosen category.

| # | Action | Configuration |
|---|--------|---------------|
| 25 | **If** | Input: `Category`, Condition: **is** `smokes`. |
| 26 | *(inside If)* **Get Variable** | `SmokeCount`. Set Variable → `CurrentCount`. |
| 27 | **Otherwise** | |
| 28 | *(inside Otherwise)* **Get Variable** | `DrinkCount`. Set Variable → `CurrentCount`. |
| 29 | **End If** | |

---

## Section 6 — Increment Loop

This loop lets you add one or more to your count before saving.

| # | Action | Configuration |
|---|--------|---------------|
| 30 | **Text** | `Yes`. Set Variable → `KeepGoing`. |
| 31 | **Repeat** | Count: `20` (acts as a max — you'll exit early via the flag). |
| 32 | *(inside Repeat)* **If** | Input: `KeepGoing`, Condition: **is** `Yes`. |
| 33 | *(inside If)* **Choose from Menu** | Prompt: `` `Category`: `CurrentCount` `` (insert both variables so the user sees e.g. "smokes: 2"). Options: `+1`, `+2`, `+3`, `Custom`, `Done`. |
| 34 | *(inside +1)* **Calculate** | Input: `CurrentCount` + `1`. Set Variable → `CurrentCount`. |
| 35 | *(inside +2)* **Calculate** | Input: `CurrentCount` + `2`. Set Variable → `CurrentCount`. |
| 36 | *(inside +3)* **Calculate** | Input: `CurrentCount` + `3`. Set Variable → `CurrentCount`. |
| 37 | *(inside Custom)* **Ask for Input** | Prompt: `How many to add?`, Type: **Number**, Default: `1`. Set Variable → `AddNum`. |
| 38 | *(inside Custom)* **Calculate** | Input: `CurrentCount` + `AddNum`. Set Variable → `CurrentCount`. |
| 39 | *(inside Done)* **Text** | `No`. Set Variable → `KeepGoing`. |
| 40 | **End Menu** | |
| 41 | **End If** | |
| 42 | **End Repeat** | |

> After tapping **Done**, the remaining repeat iterations fly by instantly
> because the `If` block is skipped when `KeepGoing` is `No`.

---

## Section 7 — Write Updated Counts Back

Store the updated count in the correct category variable.

| # | Action | Configuration |
|---|--------|---------------|
| 43 | **If** | Input: `Category`, Condition: **is** `smokes`. |
| 44 | *(inside If)* **Get Variable** | `CurrentCount`. Set Variable → `SmokeCount`. |
| 45 | **Otherwise** | |
| 46 | *(inside Otherwise)* **Get Variable** | `CurrentCount`. Set Variable → `DrinkCount`. |
| 47 | **End If** | |
| 48 | **Text** | `` `Today`: `SmokeCount` smokes, `DrinkCount` drinks `` (insert variables for Today, SmokeCount, DrinkCount). Set Variable → `TodayLine`. |

---

## Section 8 — Update Note Text

Either replace today's existing line or append a new one (with weekly total logic).

### 8a — Today already exists → replace the line

| # | Action | Configuration |
|---|--------|---------------|
| 49 | **If** | Input: `MatchCount`, Condition: **is greater than** `0`. |
| 50 | *(inside If)* **Replace Text** | Input: `NoteText`, Find: `TodayPattern` (the regex variable from step 15), Replace with: `TodayLine`. Check **Regular Expression** is ON. Set Variable → `NoteText`. |

### 8b — Today is new → check for weekly total, then append

| 51 | **Otherwise** | *(today is a new day)* |

First detect whether we've entered a new week.

| # | Action | Configuration |
|---|--------|---------------|
| 52 | **Match Text** | Input: `NoteText`, Pattern: `(\d+/\d+/\d+):`. Set Variable → `AllDateMatches`. |
| 53 | **Count** | Input: `AllDateMatches`. Set Variable → `DateCount`. |
| 54 | **If** | Input: `DateCount`, Condition: **is greater than** `0`. |
| 55 | *(inside If)* **Get Item from List** | Input: `AllDateMatches`, Index: **Last Item**. Tap the variable → **Match Group** → Group Index `1`. Set Variable → `LastDateStr`. |
| 56 | *(inside If)* **Date** | Input: `LastDateStr` (Shortcuts auto-parses M/d/yyyy). Set Variable → `LastDate`. |
| 57 | *(inside If)* **Format Date** | Input: `LastDate`, Custom format: `w`. Set Variable → `LastWeekNum`. |
| 58 | *(inside If)* **If** *(nested)* | Input: `LastWeekNum`, Condition: **is not** `WeekNum`. This means we've crossed into a new week. |

If it IS a new week, calculate the previous week's totals:

| # | Action | Configuration |
|---|--------|---------------|
| 59 | **Split Text** | Input: `NoteText`, Separator: **Custom** → type two newlines (tap return twice). Set Variable → `Sections`. |
| 60 | **Get Item from List** | Input: `Sections`, Index: **Last Item**. Set Variable → `WeekSection`. |
| 61 | **Number** | `0`. Set Variable → `WeekSmokes`. |
| 62 | **Number** | `0`. Set Variable → `WeekDrinks`. |
| 63 | **Match Text** | Input: `WeekSection`, Pattern: `(\d+) smokes, (\d+) drinks`. Set Variable → `WeekEntries`. |
| 64 | **Repeat with Each** | Input: `WeekEntries`. |
| 65 | *(inside loop)* Tap **Repeat Item** → **Match Group** → Index `1`. **Calculate**: `WeekSmokes` + this value. Set Variable → `WeekSmokes`. |
| 66 | *(inside loop)* Tap **Repeat Item** → **Match Group** → Index `2`. **Calculate**: `WeekDrinks` + this value. Set Variable → `WeekDrinks`. |
| 67 | **End Repeat with Each** | |
| 68 | **Match Text** | Input: `WeekSection`, Pattern: `^(\d+/\d+)/`. Set Variable → `FirstDateMatch`. (Gets the short date of the first entry in the section.) |
| 69 | **Get Item from List** | Input: `FirstDateMatch`, Index: **First Item** → **Match Group** → Index `1`. Set Variable → `WeekStartShort`. |
| 70 | **Format Date** | Input: `LastDate`, Custom format: `M/d/yyyy`. Set Variable → `WeekEndFull`. |
| 71 | **Text** | `` `WeekStartShort` - `WeekEndFull` total: `WeekSmokes` smokes, `WeekDrinks` drinks `` Set Variable → `TotalLine`. |
| 72 | **Text** | Build the new NoteText by combining: `` `NoteText` `` + newline + `` `TotalLine` `` + **two newlines** (blank line separator) + `` `TodayLine` ``. Set Variable → `NoteText`. |

If it's NOT a new week (still in the same week), just append today:

| # | Action | Configuration |
|---|--------|---------------|
| 73 | **Otherwise** *(of the nested If at step 58)* | |
| 74 | **Text** | `` `NoteText` `` + newline + `` `TodayLine` ``. Set Variable → `NoteText`. |
| 75 | **End If** *(nested — new week check)* | |

Handle the case where this is the very first entry (no previous dates):

| # | Action | Configuration |
|---|--------|---------------|
| 76 | **Otherwise** *(of the If at step 54 — DateCount = 0)* | |
| 77 | **Get Variable** | `TodayLine`. Set Variable → `NoteText`. |
| 78 | **End If** *(DateCount check)* | |
| 79 | **End If** *(outer — MatchCount check from step 49)* | |

---

## Section 9 — Save File

Overwrite the text file in iCloud Drive.

| # | Action | Configuration |
|---|--------|---------------|
| 80 | **Text** | Insert `NoteText` (just the variable — this passes the content to Save File). |
| 81 | **Save File** | Destination: **iCloud Drive**, Sub Path: `Shortcuts/Vice Log.txt`, check **Overwrite If File Exists**: ON. |

---

## Section 10 — Update Apple Note

Delete the old note (if your iOS supports it) and create a fresh one.

| # | Action | Configuration |
|---|--------|---------------|
| 82 | **Find Notes** | Where: **Name** is `Vice Log`. Set Variable → `OldNotes`. |
| 83 | *(Optional)* **Delete Notes** | Input: `OldNotes`. *If this action doesn't exist on your iOS version, skip it — a new note will be created alongside the old one. Periodically delete old copies from the Notes app.* |
| 84 | **Text** | Build an HTML body for monospaced display: |

For step 84, type this (inserting `NoteText` as a variable where shown):

```
<pre style="font-family: Menlo, Courier, monospace; font-size: 14px; line-height: 1.4;">
{NoteText}
</pre>
```

Replace `{NoteText}` with the actual variable (tap the variable button and insert `NoteText`).

| # | Action | Configuration |
|---|--------|---------------|
| 85 | **Create Note** | Body: the HTML text from step 84. Title/Name: `Vice Log`. |

> **Monospaced note:** The `<pre>` HTML tag tells Apple Notes to render the
> text in a monospaced font. If the HTML doesn't render on your iOS version,
> open the note after creation, select all text, and change the format to
> **Monospaced** from the Aa formatting menu.

---

## Section 11 — Confirmation

| # | Action | Configuration |
|---|--------|---------------|
| 86 | **Show Notification** | Title: `Vice Log Updated`. Body: `` `TodayLine` `` (insert the variable). |

---

## Complete Example Output

After a few days of logging, your Vice Log will look like this:

```
3/23/2026: 3 smokes, 2 drinks
3/24/2026: 2 smokes, 4 drinks
3/25/2026: 1 smokes, 3 drinks
3/26/2026: 0 smokes, 2 drinks
3/27/2026: 4 smokes, 3 drinks
3/28/2026: 2 smokes, 5 drinks
3/29/2026: 1 smokes, 3 drinks
3/23 - 3/29/2026 total: 13 smokes, 22 drinks

3/30/2026: 2 smokes, 1 drinks
3/31/2026: 0 smokes, 2 drinks
```

---

## Tips

### Add to Home Screen
In the Shortcuts app, long-press your shortcut → **Add to Home Screen**. This
gives you a one-tap icon to log quickly.

### Siri Integration
You can run it hands-free: *"Hey Siri, Vice Log."*

### Duplicate Notes
If your iOS version doesn't have the "Delete Notes" action, you'll accumulate
old notes named "Vice Log." Just open Notes periodically, search for "Vice Log,"
and delete the older copies.

### Viewing the File
Open the **Files** app → **iCloud Drive** → **Shortcuts** → **Vice Log.txt**
to see the raw log at any time.

### First Run
On the very first run, the `Get File` action won't find `Vice Log.txt` — that's
expected. The shortcut starts with empty text and creates the file on save.

### Customizing the Week Start
The guide uses the `w` (week of year) date format, which follows your device
locale. In the US, weeks start on Sunday. If your locale uses Monday-start weeks
(ISO 8601), the totals will align with Monday–Sunday. Change your device locale
in **Settings → General → Language & Region** if needed.

### Editing Past Entries
If you need to fix a mistake, open `Vice Log.txt` in the Files app and edit the
numbers directly. The shortcut parses the file each time, so manual edits are
respected.
