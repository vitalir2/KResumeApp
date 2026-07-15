#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
VERSION_FILE="$SCRIPT_DIR/detekt.version"
DETEKT_BIN="$SCRIPT_DIR/detekt-cli.jar"

VERSION="$(head -1 "$VERSION_FILE" | tr -d '[:space:]')"
URL="https://github.com/detekt/detekt/releases/download/v$VERSION/detekt-cli-$VERSION-all.jar"

if [ -f "$DETEKT_BIN" ]; then
  echo "✅ detekt $VERSION is already installed."
  exit 0
fi

echo "⬇️ Downloading detekt $VERSION..."
if ! curl -fsSL -o "$DETEKT_BIN" "$URL"; then
  echo "❌ Error: Failed to download detekt $VERSION" >&2
  rm -f "$DETEKT_BIN"
  exit 1
fi

echo "✅ detekt $VERSION installed successfully."
