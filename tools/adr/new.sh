#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(cd "$SCRIPT_DIR/../.." && pwd)"
ADR_DIR="$PROJECT_ROOT/history/adr"
TEMPLATE_FILE="$ADR_DIR/template.md"

usage() {
  echo "Usage: $(basename "$0") '<title>'"
  echo ""
  echo "Create a new Architecture Decision Record."
  echo "  <title>  Short decision title (used for filename slug)"
  echo ""
  echo "Examples:"
  echo "  $(basename "$0") 'Choose serialization library'"
  echo "  $(basename "$0") 'Rendering pipeline approach'"
  exit 1
}

# --- Validate argument ---
if [ $# -ne 1 ]; then
  usage
fi

RAW_TITLE="$1"

if [ -z "$(echo "$RAW_TITLE" | tr -d '[:space:]')" ]; then
  echo "❌ Error: Title cannot be empty." >&2
  exit 1
fi

# --- Check template exists ---
if [ ! -f "$TEMPLATE_FILE" ]; then
  echo "❌ Error: Template file not found at $TEMPLATE_FILE" >&2
  echo "   The canonical template (history/adr/template.md) must exist." >&2
  echo "   Recreate it from the project repository or restore the file." >&2
  exit 1
fi

if [ ! -s "$TEMPLATE_FILE" ]; then
  echo "❌ Error: Template file is empty at $TEMPLATE_FILE" >&2
  exit 1
fi

# --- Compute NNN ---
mkdir -p "$ADR_DIR"

LAST_NUM=$(ls "$ADR_DIR"/[0-9]*-*.md 2>/dev/null \
  | sed -n 's/.*\/\([0-9][0-9]*\)-.*/\1/p' \
  | sort -n \
  | tail -1)

if [ -z "$LAST_NUM" ]; then
  NEXT_NUM=1
else
  NEXT_NUM=$((LAST_NUM + 1))
fi

NNN=$NEXT_NUM

# --- Slugify title ---
SLUG=$(echo "$RAW_TITLE" \
  | tr '[:upper:]' '[:lower:]' \
  | sed -E 's/[^a-z0-9]+/-/g' \
  | sed 's/^-//; s/-$//')

if [ -z "$SLUG" ]; then
  echo "❌ Error: Title must contain at least one letter or digit to form a filename." >&2
  exit 1
fi

FILENAME="$ADR_DIR/$NNN-$SLUG.md"

# --- Check for collision ---
if [ -f "$FILENAME" ]; then
  echo "❌ Error: ADR already exists at $FILENAME" >&2
  exit 1
fi

# --- Generate file from template ---
TODAY=$(date +%Y-%m-%d)

awk -v title="$RAW_TITLE" -v date="$TODAY" '
  NR == 1 { print "# " title; next }
  index($0, "**Status:**") == 1 { print "**Status:** proposed"; next }
  { count += gsub("YYYY-MM-DD", date); print }
  ENDFILE {
    if (count == 0) {
      print "⚠️ Warning: YYYY-MM-DD not found in template — date was not replaced. Fix template.md." > "/dev/stderr"
    }
  }
' "$TEMPLATE_FILE" > "$FILENAME"

echo "✅ Created $FILENAME (proposed)"

# --- Update index ---
"$SCRIPT_DIR/index.sh" > /dev/null 2>&1 || true
