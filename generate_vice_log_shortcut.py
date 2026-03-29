#!/usr/bin/env python3
"""
Generate 'Vice Log.shortcut' for iPhone habit tracking.

Usage:
    python3 generate_vice_log_shortcut.py

Output:
    Vice Log.shortcut — open on iPhone via AirDrop, Files, or download from GitHub.

Notes:
    The .shortcut plist format is not publicly documented. This script generates
    a best-effort file based on reverse-engineered knowledge of the format. If any
    action doesn't work after import, open the shortcut in the editor and verify
    the action's configuration. The overall structure and logic will be correct.
"""

import plistlib
import uuid
import os

# Unicode Object Replacement Character — marks variable insertion points
# in Shortcuts' WFTextTokenString format.
OBJ = "\ufffc"


def uid():
    """Generate a UUID for GroupingIdentifier (control flow blocks)."""
    return str(uuid.uuid4()).upper()


# ─── Token / variable reference builders ───────────────────────────────

def var(name):
    """Named variable reference."""
    return {"Type": "Variable", "VariableName": name}


def attach(ref):
    """Wrap a reference as a WFTextTokenAttachment (single-value variable)."""
    return {"Value": ref, "WFSerializationType": "WFTextTokenAttachment"}


def tok(s, atts=None):
    """Plain WFTextTokenString (text with optional embedded variable refs)."""
    v = {"string": s}
    if atts:
        v["attachmentsByRange"] = atts
    return {"Value": v, "WFSerializationType": "WFTextTokenString"}


def tpl(template, *names):
    """
    Build a WFTextTokenString from a template with variable placeholders.

    Use \u00ab N \u00bb (guillemets) as placeholders, where N is the index
    into *names.  Example:
        tpl("\u00ab0\u00bb: \u00ab1\u00bb smokes", "Today", "Count")
    """
    out, atts, pos, i = "", {}, 0, 0
    while i < len(template):
        if template[i] == "\u00ab":                       # \u00ab = opening guillemet
            j = template.index("\u00bb", i)                # \u00bb = closing guillemet
            idx = int(template[i + 1 : j])
            atts[f"{{{pos}, 1}}"] = var(names[idx])
            out += OBJ
            pos += 1
            i = j + 1
        else:
            out += template[i]
            pos += 1
            i += 1
    return tok(out, atts if atts else None)


# ─── Action constructors ──────────────────────────────────────────────

def act(identifier, **params):
    """Create a Shortcuts action dict."""
    return {
        "WFWorkflowActionIdentifier": f"is.workflow.actions.{identifier}",
        "WFWorkflowActionParameters": dict(params),
    }


def set_v(name):
    return act("setvariable", WFVariableName=name)


def get_v(name):
    return act("getvariable", WFVariable=attach(var(name)))


def date_f(fmt):
    """Format Current Date with a custom format string."""
    return act(
        "format.date",
        WFDateFormatStyle="Custom",
        WFDateFormat=fmt,
        WFDate=attach({"Type": "CurrentDate"}),
    )


def text(token):
    """Text action — produces a text value."""
    return act("gettext", WFTextActionText=token)


def number(n):
    return act("number", WFNumberActionNumber=n)


def math_add(n):
    """Add n to the current pipeline value."""
    return act("math", WFMathOperation="+", WFMathOperand=n)


# ─── Control flow ─────────────────────────────────────────────────────

def if_start(gid, condition, cmp=None):
    """
    If action.  condition codes:
      0 = is, 1 = is not, 2 = contains, 3 = does not contain,
      4 = begins with, 5 = ends with, 100 = has any value
    cmp: comparison value (tok/tpl or None).
    """
    p = {"GroupingIdentifier": gid, "WFControlFlowMode": 0, "WFCondition": condition}
    if cmp is not None:
        p["WFConditionalActionString"] = cmp
    return act("conditional", **p)


def if_else(gid):
    return act("conditional", GroupingIdentifier=gid, WFControlFlowMode=1)


def if_end(gid):
    return act("conditional", GroupingIdentifier=gid, WFControlFlowMode=2)


def menu_start(gid, prompt, items):
    return act(
        "choosefrommenu",
        GroupingIdentifier=gid,
        WFControlFlowMode=0,
        WFMenuPrompt=prompt,
        WFMenuItems=items,
    )


def menu_item(gid, title):
    return act(
        "choosefrommenu",
        GroupingIdentifier=gid,
        WFControlFlowMode=1,
        WFMenuItemTitle=title,
    )


def menu_end(gid):
    return act("choosefrommenu", GroupingIdentifier=gid, WFControlFlowMode=2)


def repeat_start(gid, count):
    return act(
        "repeat.count",
        GroupingIdentifier=gid,
        WFControlFlowMode=0,
        WFRepeatCount=count,
    )


def repeat_end(gid):
    return act("repeat.count", GroupingIdentifier=gid, WFControlFlowMode=2)


def replace_text(find_tok, repl_tok):
    """Replace Text (regex mode)."""
    return act(
        "text.replace",
        WFReplaceTextFind=find_tok,
        WFReplaceTextReplace=repl_tok,
        WFReplaceTextRegularExpression=True,
    )


# ══════════════════════════════════════════════════════════════════════
#  BUILD THE SHORTCUT ACTIONS
# ══════════════════════════════════════════════════════════════════════

actions = []

# ── Section 1: Date setup ────────────────────────────────────────────

actions += [date_f("M/d/yyyy"), set_v("Today")]

# ── Section 2: Load log file from iCloud Drive ───────────────────────
#    File: iCloud Drive / Shortcuts / Vice Log.txt
#    If the file doesn't exist (first run), start with empty text.

actions.append(
    act("documentpicker.open", WFGetFilePath="Vice Log.txt", WFFileErrorIfNotFound=False)
)
g_load = uid()
actions += [
    if_start(g_load, 100),                  # has any value → file exists
    set_v("LogText"),
    if_else(g_load),                        # otherwise → first run
    text(tok("")),
    set_v("LogText"),
    if_end(g_load),
]

# ── Section 3: Choose category ───────────────────────────────────────

g_cat = uid()
actions += [
    menu_start(g_cat, "What are you logging?", ["\U0001f6ac Smokes", "\U0001f37a Drinks"]),
    menu_item(g_cat, "\U0001f6ac Smokes"),
    text(tok("smokes")),
    set_v("Category"),
    menu_item(g_cat, "\U0001f37a Drinks"),
    text(tok("drinks")),
    set_v("Category"),
    menu_end(g_cat),
]

# ── Section 4: Parse today's counts from log ─────────────────────────
#    Use "contains" to check if today has an entry, then regex-replace
#    to extract individual numbers (avoids match-group complexity).

g_parse = uid()
actions += [
    get_v("LogText"),
    if_start(g_parse, 2, cmp=tpl("\u00ab0\u00bb:", "Today")),  # contains "{Today}:"
    # Extract smoke count: replace entire text with capture group $1
    get_v("LogText"),
    replace_text(
        tpl("[\\s\\S]*?\u00ab0\u00bb: (\\d+) smokes[\\s\\S]*", "Today"),
        tok("$1"),
    ),
    set_v("SmokeCount"),
    # Extract drink count
    get_v("LogText"),
    replace_text(
        tpl("[\\s\\S]*?\u00ab0\u00bb: \\d+ smokes, (\\d+) drinks[\\s\\S]*", "Today"),
        tok("$1"),
    ),
    set_v("DrinkCount"),
    if_else(g_parse),  # no entry for today yet
    number(0),
    set_v("SmokeCount"),
    number(0),
    set_v("DrinkCount"),
    if_end(g_parse),
]

# ── Section 5: Set CurrentCount to the chosen category ───────────────

g_cur = uid()
actions += [
    get_v("Category"),
    if_start(g_cur, 0, cmp=tok("smokes")),  # is "smokes"
    get_v("SmokeCount"),
    set_v("CurrentCount"),
    if_else(g_cur),
    get_v("DrinkCount"),
    set_v("CurrentCount"),
    if_end(g_cur),
]

# ── Section 6: Increment loop ────────────────────────────────────────
#    Repeat up to 20 times.  KeepGoing flag exits early.
#    Menu shows: +1 / +2 / +3 / Done

actions += [text(tok("Yes")), set_v("KeepGoing")]

g_rep = uid()
g_repif = uid()
g_menu = uid()

actions += [
    repeat_start(g_rep, 20),
    get_v("KeepGoing"),
    if_start(g_repif, 0, cmp=tok("Yes")),  # is "Yes"
    # Menu prompt tries to embed variables (Category: CurrentCount).
    # If your iOS doesn't render them, you'll see placeholder text — still works.
    menu_start(
        g_menu,
        tpl("\u00ab0\u00bb: \u00ab1\u00bb", "Category", "CurrentCount"),
        ["+1", "+2", "+3", "Done"],
    ),
    menu_item(g_menu, "+1"),
    get_v("CurrentCount"),
    math_add(1),
    set_v("CurrentCount"),
    menu_item(g_menu, "+2"),
    get_v("CurrentCount"),
    math_add(2),
    set_v("CurrentCount"),
    menu_item(g_menu, "+3"),
    get_v("CurrentCount"),
    math_add(3),
    set_v("CurrentCount"),
    menu_item(g_menu, "Done"),
    text(tok("No")),
    set_v("KeepGoing"),
    menu_end(g_menu),
    if_end(g_repif),  # no else — remaining iterations skip instantly
    repeat_end(g_rep),
]

# ── Section 7: Write updated count back to category variable ─────────

g_wb = uid()
actions += [
    get_v("Category"),
    if_start(g_wb, 0, cmp=tok("smokes")),
    get_v("CurrentCount"),
    set_v("SmokeCount"),
    if_else(g_wb),
    get_v("CurrentCount"),
    set_v("DrinkCount"),
    if_end(g_wb),
]

# ── Section 8: Build today's formatted line ──────────────────────────

actions += [
    text(tpl("\u00ab0\u00bb: \u00ab1\u00bb smokes, \u00ab2\u00bb drinks", "Today", "SmokeCount", "DrinkCount")),
    set_v("TodayLine"),
]

# ── Section 9: Update the log text ───────────────────────────────────
#    If today already has a line → regex-replace it.
#    Otherwise → append (or set if log is empty).

g_upd = uid()
g_empty = uid()

actions += [
    get_v("LogText"),
    if_start(g_upd, 2, cmp=tpl("\u00ab0\u00bb:", "Today")),  # contains → replace
    get_v("LogText"),
    replace_text(
        tpl("\u00ab0\u00bb: \\d+ smokes, \\d+ drinks", "Today"),
        tpl("\u00ab0\u00bb", "TodayLine"),
    ),
    set_v("LogText"),
    if_else(g_upd),  # new day → append
    get_v("LogText"),
    if_start(g_empty, 0, cmp=tok("")),  # log is empty (first ever entry)
    get_v("TodayLine"),
    set_v("LogText"),
    if_else(g_empty),
    text(tpl("\u00ab0\u00bb\n\u00ab1\u00bb", "LogText", "TodayLine")),
    set_v("LogText"),
    if_end(g_empty),
    if_end(g_upd),
]

# ── Section 10: Save log file (overwrite) ────────────────────────────

actions += [
    get_v("LogText"),
    act("documentpicker.save", WFFileDestinationPath="Vice Log.txt", WFSaveFileOverwrite=True),
]

# ── Section 11: Create Apple Note with monospaced formatting ─────────
#    Wraps log in <pre> HTML, converts to rich text, creates a note.
#    NOTE: This creates a NEW note each run.  Periodically delete old
#    "Vice Log" notes from the Notes app.

actions += [
    text(
        tpl(
            '<pre style="font-family: Menlo, Courier, monospace; font-size: 14px; line-height: 1.5;">\u00ab0\u00bb</pre>',
            "LogText",
        )
    ),
    act("getrichtextfromhtml"),
    act("createnote"),
]

# ── Section 12: Confirmation alert ───────────────────────────────────

actions += [
    act(
        "alert",
        WFAlertActionTitle=tok("Vice Log Updated"),
        WFAlertActionMessage=tpl("\u00ab0\u00bb", "TodayLine"),
    ),
]

# ══════════════════════════════════════════════════════════════════════
#  ASSEMBLE PLIST AND WRITE
# ══════════════════════════════════════════════════════════════════════

shortcut_plist = {
    "WFWorkflowActions": actions,
    "WFWorkflowClientVersion": "2302.0.4",
    "WFWorkflowHasOutputFallback": False,
    "WFWorkflowIcon": {
        "WFWorkflowIconGlyphNumber": 59511,
        "WFWorkflowIconStartColor": 4282601983,  # red
    },
    "WFWorkflowImportQuestions": [],
    "WFWorkflowMinimumClientVersion": 900,
    "WFWorkflowMinimumClientVersionString": "900",
    "WFWorkflowTypes": ["NCWidget", "WatchKit"],
    "WFWorkflowInputContentItemClasses": [
        "WFStringContentItem",
    ],
}

script_dir = os.path.dirname(os.path.abspath(__file__))
out_path = os.path.join(script_dir, "Vice Log.shortcut")

with open(out_path, "wb") as f:
    plistlib.dump(shortcut_plist, f, fmt=plistlib.FMT_BINARY)

print(f"Generated: {out_path}")
print(f"Actions:   {len(actions)}")
print()
print("To install on iPhone:")
print("  1. Transfer 'Vice Log.shortcut' to your iPhone (AirDrop, iCloud, email)")
print("  2. Tap the file to open in Shortcuts")
print("  3. Tap 'Add Shortcut' to import")
print("  4. You may need Settings > Shortcuts > Allow Untrusted Shortcuts")
