# AI Usage Guide — Tips to save tokens

## Before writing a message
- Batch everything — each reply burns ~2K tokens overhead
- Point to exact file + line: `"edit src/Foo.kt:42"`
- Say "unchanged" before I re-read files you know are stable
- Skip pleasantries — `"Implement X"` works
- State scope up front: `"scope: AGENTS.md + code-style.md only"` — prevents over-exploration
- If you know the answer, say it — don't make me propose and reject
- Drop the approve cycle for small things: `"Add X. Then do it."` not `"Should I add X?"`

## During work
- Say "skip verification" or "skip tests" when you trust the change
- Say "move on" if analysis too long — I'll stop and execute
- Drop round-trips: say all constraints up front

## Exploration (finding code)
- Say **"just paths"** or **"no summary"** — I return file:line only, no prose
- Start with file inventory: `"what's in feature/X/"` = 1 glob, 0 reads
- Say **"same pattern as X"** — saves me reading a reference file

## Feature dev
- Say **"no re-read"** — I skip re-reading files I already know
- Batch edits across files — 3 files in one message cheaper than 3 separate
- Skip round-trips: say all constraints up front

## What not to do
- Open-ended "what do you think?" — triggers exploration + analysis reads
- Serial requests (write → review → fix → review) — wastes overhead each turn
- Repeating same context — I already have it
- Multiple small edits on same file — 2-3 large block replacements are cheaper
- Incremental scope creep — each "also add X" costs a round trip. Say everything in one message
