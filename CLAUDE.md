# CLAUDE.md

Project-specific guidance for working in this repo.

## Branch naming

Use Conventional Commits prefixes for branches, separated from a kebab-case description with a forward slash:

```
<type>/<short-description>
```

Common prefixes:

- `feat/` — new feature or capability (e.g., `feat/hello-world-endpoint`)
- `fix/` — bug fix
- `refactor/` — internal restructuring with no behavior change
- `chore/` — tooling, build, or non-code maintenance
- `docs/` — documentation-only changes
- `test/` — test additions or corrections

Use kebab-case for the description (`feat/topic-crud-endpoints`, not `feat/topicCrudEndpoints` or `feat/topic_crud_endpoints`).

## Commit message format

Follow Tim Pope's 50/72 style with a bullet body when needed:

- **Subject** — ≤50 characters, imperative mood ("Add X", "Fix Y", "Update Z"), no period at the end. Capitalize the first word.
- **Blank line** between subject and body.
- **Body** *(when needed)* — bullet list of what was added, changed, or removed. Wrap at 72 characters per line. Use bullets, not prose paragraphs. Skip the body entirely for trivial commits where the subject already says enough.
- **No Claude co-author trailer.** Omit `Co-Authored-By: Claude …` from commits in this repo unless the user explicitly requests it.

Example:

    Add Ktor server with hello world endpoint

    - Application.kt with embeddedServer on Netty, port 8080
    - GET / returns plain-text status string
    - Logback configuration for console output
