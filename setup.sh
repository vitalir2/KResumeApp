#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$SCRIPT_DIR"

echo "=== Setup: kmp-general ==="

# 1. Install ktlint
echo "--- Installing ktlint ---"
bash tools/formatter/install.sh

# 2. Install detekt
echo "--- Installing detekt ---"
if [ -f tools/linter/detekt/install.sh ]; then
  bash tools/linter/detekt/install.sh
fi

# 3. Configure git hooks
echo "--- Configuring git hooks ---"
git config core.hooksPath .githooks

# 4. Generate Gradle wrapper
echo "--- Generating Gradle wrapper ---"
if command -v gradle &> /dev/null; then
    gradle wrapper --gradle-version=8.10
elif command -v java &> /dev/null; then
    echo "Downloading Gradle wrapper..."
    curl -fsSL "https://services.gradle.org/distributions/gradle-8.10-bin.zip" -o /tmp/gradle-8.10-bin.zip
    unzip -q /tmp/gradle-8.10-bin.zip -d /tmp/gradle-8.10
    /tmp/gradle-8.10/gradle-8.10/bin/gradle wrapper
    rm -rf /tmp/gradle-8.10 /tmp/gradle-8.10-bin.zip
else
    echo "⚠️  Install JDK 17+ and Gradle, then run: gradle wrapper"
fi

echo "=== Setup complete ==="
echo ""
echo "Next steps:"
echo "  ./gradlew build        # Build all targets"
echo "  ./gradlew test         # Run unit tests"
echo "  ./gradlew jvmRun       # Run desktop app (JVM)"
