#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(cd "$SCRIPT_DIR/../.." && pwd)"
ADR_DIR="$PROJECT_ROOT/history/adr"
INDEX_FILE="$ADR_DIR/INDEX.md"

# --- Scan ADR files ---
ADRS=()
while IFS= read -r -d '' f; do
  ADRS+=("$f")
done < <(find "$ADR_DIR" -maxdepth 1 -name '[0-9]*.md' ! -name 'README.md' ! -name 'INDEX.md' -print0 | sort -zV)

# --- Build table rows ---
ROWS=""
if [ ${#ADRS[@]} -eq 0 ]; then
  ROWS="| — | No ADRs yet | — | — |"
else
  for f in "${ADRS[@]}"; do
    BASENAME=$(basename "$f")
    NNN=$(echo "$BASENAME" | sed -E 's/^([0-9]+)-.*/\1/')
    TITLE=$(grep -m1 '^# ' "$f" | sed 's/^# //' | sed 's/ $//')
    STATUS=$(grep -m1 -oE '\*\*Status:\*\* .+' "$f" | sed 's/\*\*Status:\*\* //' || echo "unknown")
    DATE=$(grep -m1 -oE '\*\*Date:\*\* .+' "$f" | sed 's/\*\*Date:\*\* //' || echo "—")

    if [ -z "$TITLE" ]; then
      TITLE="(no title)"
    fi

    ROWS="$ROWS| $NNN | [$TITLE]($BASENAME) | $STATUS | $DATE |
"
  done
fi

# --- Generate INDEX.md ---
cat > "$INDEX_FILE" <<EOF
# ADR Index

| # | Title | Status | Date |
|---|-------|--------|------|
${ROWS}
EOF

COUNT=${#ADRS[@]}
PLURAL="s"
if [ "$COUNT" -eq 1 ]; then
  PLURAL=""
fi
echo "✅ Updated INDEX.md ($COUNT ADR$PLURAL)"
