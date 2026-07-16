#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(cd "$SCRIPT_DIR/../.." && pwd)"
BRAIN_DIR="$PROJECT_ROOT/history/brainstorms"
INDEX_FILE="$BRAIN_DIR/INDEX.md"

mkdir -p "$BRAIN_DIR"

# --- Scan brainstorm files ---
BRAINS=()
while IFS= read -r -d '' f; do
  BRAINS+=("$f")
done < <(find "$BRAIN_DIR" -maxdepth 1 -name '*.md' ! -name 'README.md' ! -name 'INDEX.md' -print0 | sort)

# --- Build table rows ---
ROWS=""
if [ ${#BRAINS[@]} -eq 0 ]; then
  ROWS="| — | No brainstorms yet | — |"
else
  NUM=0
  for f in "${BRAINS[@]}"; do
    NUM=$((NUM + 1))
    BASENAME=$(basename "$f")
    TOPIC=$(grep -m1 '^# ' "$f" | sed 's/^# //' | sed 's/ $//')
    DATE=$(grep -m1 -oE '\*\*Date:\*\* .+' "$f" | sed 's/\*\*Date:\*\* //' || echo "—")

    if [ -z "$TOPIC" ]; then
      TOPIC="(no title)"
    fi

    ROWS="$ROWS| $NUM | [$TOPIC]($BASENAME) | $DATE |
"
  done
fi

# --- Generate INDEX.md ---
cat > "$INDEX_FILE" <<EOF
# Brainstorm Index

| # | Topic | Date |
|---|-------|------|
${ROWS}
EOF

COUNT=${#BRAINS[@]}
PLURAL="s"
if [ "$COUNT" -eq 1 ]; then
  PLURAL=""
fi
echo "✅ Updated brainstorms/INDEX.md ($COUNT session$PLURAL)"
