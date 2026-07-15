#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
VERSION_FILE="$SCRIPT_DIR/ktlint.version"
KTLINT_BIN="$SCRIPT_DIR/ktlint"

VERSION="$(head -1 "$VERSION_FILE" | tr -d '[:space:]')"
URL="https://github.com/pinterest/ktlint/releases/download/$VERSION/ktlint"

if [ -f "$KTLINT_BIN" ]; then
  INSTALLED_VERSION="$("$KTLINT_BIN" --version 2>/dev/null | grep -oE '[0-9]+\.[0-9]+\.[0-9]+' | head -1)"
  if [ "$INSTALLED_VERSION" = "$VERSION" ]; then
    echo "✅ ktlint $VERSION is already installed."
    exit 0
  fi
fi

echo "⬇️ Downloading ktlint $VERSION..."
if ! curl -fsSL -o "$KTLINT_BIN" "$URL"; then
  echo "❌ Error: Failed to download ktlint $VERSION from $URL" >&2
  echo "Check your network connection and verify the version in $VERSION_FILE." >&2
  rm -f "$KTLINT_BIN"
  exit 1
fi

chmod +x "$KTLINT_BIN"
echo "✅ ktlint $VERSION installed successfully."
