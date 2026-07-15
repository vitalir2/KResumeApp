# Linter

This project uses [ktlint](https://pinterest.github.io/ktlint/) for formatting and style enforcement.

## ktlint

ktlint handles formatting and style checks. It uses the `ktlint_official` style with experimental rules enabled.

### Key configuration

- **Max line length**: 120 characters
- **Indentation**: 4 spaces
- **Trailing commas**: Allowed on declarations and call sites
- **Full rule set**: See `.editorconfig` at the project root

### Running ktlint

```bash
# Check only — reports violations without modifying files
./tools/formatter/check.sh

# Auto-format — fixes fixable violations in place
./tools/formatter/format.sh
```

## Pre-commit Hook

The pre-commit hook runs `tools/formatter/pre-commit`, which executes ktlint on all staged `.kt` files. It must pass before a commit can be made. Fix any reported violations — do not bypass the hook.

## Detekt

For additional static analysis beyond formatting (complexity, naming, potential bugs), the project may also use [Detekt](https://detekt.dev/). Detekt rules are configured in the Gradle build and run as part of the build process. See the project's `build.gradle.kts` for Detekt configuration details.

## Architecture Linting

For architecture and layer dependency rules, see `tools/linter/README.md`. The project may use Konsist for architectural rule enforcement (e.g., layer dependencies, package structure).
