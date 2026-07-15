#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(cd "$SCRIPT_DIR/../.." && pwd)"
KTLINT_BIN="$SCRIPT_DIR/ktlint"
VERSION_FILE="$SCRIPT_DIR/ktlint.version"

cd "$PROJECT_ROOT"

if [ ! -f "$KTLINT_BIN" ]; then
  echo "❌ Error: ktlint not found. Run ./setup.sh first." >&2
  exit 1
fi

EXPECTED_VERSION="$(head -1 "$VERSION_FILE" | tr -d '[:space:]')"
INSTALLED_VERSION="$("$KTLINT_BIN" --version 2>/dev/null | grep -oE '[0-9]+\.[0-9]+\.[0-9]+' | head -1)"

if [ "$INSTALLED_VERSION" != "$EXPECTED_VERSION" ]; then
  echo "⚠️ Warning: ktlint version mismatch. Installed: $INSTALLED_VERSION, expected: $EXPECTED_VERSION. Run ./setup.sh to update." >&2
fi

echo "🔍 Checking Kotlin files with ktlint $INSTALLED_VERSION..."
"$KTLINT_BIN" --relative .
RESULT=$?
if [ $RESULT -eq 0 ]; then
  echo "✅ Check passed. No violations found."
else
  echo "❌ Check failed with exit code $RESULT. Run ./tools/formatter/format.sh to auto-correct." >&2
fi
exit $RESULT
