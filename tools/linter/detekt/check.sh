#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(cd "$SCRIPT_DIR/../.." && pwd)"
DETEKT_BIN="$SCRIPT_DIR/detekt-cli.jar"
CONFIG="$SCRIPT_DIR/detekt.yml"
BASELINE="$SCRIPT_DIR/baseline.xml"

cd "$PROJECT_ROOT"

if [ ! -f "$DETEKT_BIN" ]; then
  echo "⚠️  detekt not found. Run ./tools/linter/detekt/install.sh first, or skip detekt."
  exit 0
fi

echo "🔍 Running detekt..."
java -jar "$DETEKT_BIN" \
  --config "$CONFIG" \
  --baseline "$BASELINE" \
  --input "$PROJECT_ROOT" \
  --excludes "**/build/**,**/generated/**" \
  ${CREATE_BASELINE:+--build-upon-default-config --baseline "$BASELINE"}

RESULT=$?
if [ $RESULT -eq 0 ]; then
  echo "✅ detekt passed."
else
  echo "❌ detekt failed. Run CREATE_BASELINE=true ./tools/linter/detekt/check.sh to update baseline."
fi
exit $RESULT
