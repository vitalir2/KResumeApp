#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(cd "$SCRIPT_DIR/../.." && pwd)"
ADR_DIR="$PROJECT_ROOT/history/adr"

VALID_STATUSES=("proposed" "accepted" "implemented" "deprecated")

usage() {
  echo "Usage: $(basename "$0") <nnn> <status> [--superseded-by <mmm>]"
  echo ""
  echo "Update the status of an ADR."
  echo "  <nnn>     ADR number (e.g. 1)"
  echo "  <status>  One of: proposed, accepted, implemented, deprecated"
  echo "  --superseded-by <mmm>  New ADR that replaces this one (only for 'deprecated')"
  echo ""
  echo "Examples:"
  echo "  $(basename "$0") 2 accepted"
  echo "  $(basename "$0") 2 implemented"
  echo "  $(basename "$0") 2 deprecated --superseded-by 5"
  exit 1
}

# --- Parse args ---
SUPERSEDED_BY=""

if [ $# -eq 2 ]; then
  NNN="$1"
  NEW_STATUS="$2"
elif [ $# -eq 4 ] && [ "$3" = "--superseded-by" ]; then
  NNN="$1"
  NEW_STATUS="$2"
  SUPERSEDED_BY="$4"
else
  usage
fi

# --- Validate NNN ---
if ! echo "$NNN" | grep -qE '^[1-9][0-9]*$'; then
  echo "❌ Error: ADR number must be a positive integer (e.g., 1, 42, not $NNN)." >&2
  exit 1
fi

# --- Find ADR file ---
# shellcheck disable=SC2206
ADR_FILES=("$ADR_DIR"/"$NNN"-*.md)
if [ ${#ADR_FILES[@]} -eq 0 ] || [ ! -f "${ADR_FILES[0]}" ]; then
  echo "❌ Error: No ADR #$NNN found in $ADR_DIR" >&2
  echo "   Existing ADRs: $(ls "$ADR_DIR"/[0-9]*-*.md 2>/dev/null | sed 's/.*\///' | tr '\n' ' ' | head -c 200)" >&2
  exit 1
fi
ADR_FILE="${ADR_FILES[0]}"

# --- Validate new status ---
IS_VALID=false
for s in "${VALID_STATUSES[@]}"; do
  if [ "$s" = "$NEW_STATUS" ]; then
    IS_VALID=true
    break
  fi
done

if [ "$IS_VALID" = false ]; then
  echo "❌ Error: Invalid status '$NEW_STATUS'. Valid: ${VALID_STATUSES[*]}" >&2
  exit 1
fi

# --- Read current status ---
CURRENT_STATUS=$(grep -oE '\*\*Status:\*\* .+' "$ADR_FILE" | sed 's/\*\*Status:\*\* //' | head -1)

if [ -z "$CURRENT_STATUS" ]; then
  echo "❌ Error: ADR $NNN has no Status line." >&2
  exit 1
fi

# --- Validate transition ---
transition_valid() {
  local from="$1" to="$2"
  if [ "$from" = "$to" ]; then
    return 2  # no-op
  fi
  case "$from" in
    proposed)     [ "$to" = "accepted" ] || [ "$to" = "deprecated" ] ;;
    accepted)     [ "$to" = "implemented" ] || [ "$to" = "deprecated" ] ;;
    implemented)  [ "$to" = "deprecated" ] ;;
    deprecated)   return 1 ;;
    *)            return 1 ;;
  esac
}

set +e
transition_valid "$CURRENT_STATUS" "$NEW_STATUS"
TRANSITION_EXIT=$?
set -e

if [ $TRANSITION_EXIT -eq 1 ]; then
  echo "❌ Error: Cannot transition from '$CURRENT_STATUS' to '$NEW_STATUS'." >&2
  echo "   Valid transitions from '$CURRENT_STATUS':"
  case "$CURRENT_STATUS" in
    proposed)     echo "   → accepted, deprecated" ;;
    accepted)     echo "   → implemented, deprecated" ;;
    implemented)  echo "   → deprecated" ;;
    deprecated)   echo "   (none — once deprecated, status is frozen)" ;;
  esac
  exit 1
fi

if [ $TRANSITION_EXIT -eq 2 ]; then
  echo "ℹ️  Already '$CURRENT_STATUS', nothing to do."
  exit 0
fi

# --- Check --superseded-by semantics ---
if [ "$NEW_STATUS" = "deprecated" ] && [ -n "$SUPERSEDED_BY" ]; then
  if ! echo "$SUPERSEDED_BY" | grep -qE '^[1-9][0-9]*$'; then
    echo "❌ Error: Superseding ADR number must be a positive integer (e.g., 5, not $SUPERSEDED_BY)." >&2
    exit 1
  fi
  SUP_FILES=("$ADR_DIR"/"$SUPERSEDED_BY"-*.md)
  if [ ${#SUP_FILES[@]} -eq 0 ] || [ ! -f "${SUP_FILES[0]}" ]; then
    echo "❌ Error: Superseding ADR #$SUPERSEDED_BY not found." >&2
    echo "   Existing ADRs: $(ls "$ADR_DIR"/[0-9]*-*.md 2>/dev/null | sed 's/.*\///' | tr '\n' ' ' | head -c 200)" >&2
    exit 1
  fi
  SUP_FILE="${SUP_FILES[0]}"
  SUP_SLUG=$(basename "$SUP_FILE")
fi

if [ "$NEW_STATUS" = "deprecated" ] && [ -z "$SUPERSEDED_BY" ]; then
  echo "⚠️  Warning: Deprecating without --superseded-by. Consider linking to the replacement ADR." >&2
fi

if [ "$NEW_STATUS" != "deprecated" ] && [ -n "$SUPERSEDED_BY" ]; then
  echo "⚠️  Warning: --superseded-by is only meaningful for 'deprecated' status. Ignoring." >&2
  SUPERSEDED_BY=""
fi

# --- Apply status change ---
sed -i '' "s/\*\*Status:\*\* .*/\*\*Status:\*\* $NEW_STATUS/" "$ADR_FILE"
echo "✅ Status of $NNN: $CURRENT_STATUS → $NEW_STATUS"

# --- Add superseded-by link ---
if [ -n "$SUPERSEDED_BY" ]; then
  # Determine which section to append to: More Information (preferred) or Links (legacy)
  if grep -q "^## More Information" "$ADR_FILE"; then
    sed -i '' "/^## More Information/a\\
- Superseded by [$SUP_SLUG]($SUP_SLUG)
" "$ADR_FILE"
  elif grep -q "^## Links" "$ADR_FILE"; then
    sed -i '' "/^## Links/a\\
- Superseded by [$SUP_SLUG]($SUP_SLUG)
" "$ADR_FILE"
  else
    cat >> "$ADR_FILE" <<EOF

## More Information

- Superseded by [$SUP_SLUG]($SUP_SLUG)
EOF
  fi
  echo "✅ Added link: Superseded by $SUP_SLUG"
fi

# --- Update index ---
"$SCRIPT_DIR/index.sh" > /dev/null 2>&1 || true
