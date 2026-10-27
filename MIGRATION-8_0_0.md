# Migrating the "Using data" chapter to release 8.0.0

The `using-data` branch (PR #6) was written against `main`, which documents GROOVE 7.5.3. When work on `release-8_0_0` resumes and the branch is merged into it, the following has to be reapplied or resolved by hand. The 8.0.0 branch rewrote the same manual pages, so `git merge` will stop on conflicts in most of them.

## 1. Resolve the merge conflicts

| File | What to do |
| :--- | :--- |
| `manual/advanced.md` | Delete the whole `## Data attributes` section (through `### Algebras`) from the 8.0.0 version, as the branch does on main; keep the 8.0.0 text for everything else. Then re-apply the three link edits: the intro sentence pointing to [Using data](manual_data.html); the `algebraFamily` and `userOperations` rows of the system-properties table (the 8.0.0 table has more rows, so the surrounding text differs); and the sentence in *Named nesting levels* that reads `Tests and assignments (see [above](#tests-and-assignments-in-rules))`, which only exists on 8.0.0 and must link to `manual_data.html#tests-and-assignments-in-rules`. |
| `manual.md` | Insert the *Using data* row after *Advanced rule features*, with `8.0.0` in the "Checked against" column, and drop "attributes" from the *Advanced rule features* row. |
| `manual/introduction.md` | Re-apply the two bullets in *How this manual is organised* (the 8.0.0 version words the *Advanced rule features* bullet differently). |
| `manual/control.md` | Re-apply the sentence in *Variables and arguments*: link the rule parameters as "Advanced rule features" instead of "previous chapter", and link `user` to the data chapter. |
| `manual/MakeRefs.java`, `manual/ref-operators.md` | Change "in the [Advanced rule features](manual_advanced.html) chapter" to "in the [Using data](manual_data.html) chapter"; in the 8.0.0 `MakeRefs.java` the sentence is in the operators-page generator (around line 283, not 143). Regenerate the reference pages afterwards. |
| `_data/sidebars/manual_sidebar.yml`, `_config.yml`, `manual/make-figures.sh` | Should merge cleanly. |

## 2. Carry the 8.0.0 additions of the data section into `manual/data.md`

The 8.0.0 version of the former data section differs from main's in two places; both must be ported, since the branch took main's text:

- *Attributes in host graphs*: the sentence on backslash escapes in string constants (`"say \"hi\""`, `"C:\\"`), added for 8.0.0.
- *Algebras*: the link to the system properties already points at `manual_advanced.html#system-properties`; nothing to do.

## 3. Update `manual/data.md` for 8.0.0

- Maven snippet: `<version>8.0.0</version>` instead of 7.5.3.
- *Indeterminate operations*: the bullet now says that 7.5.3 still accepts an indeterminate operation on a quantified level and later releases reject it. For 8.0.0, state that GROOVE reports it as an error on the rule (issue 911), and adjust the matching bullet in *Limitations* ("cannot be used" rather than "should not be used; releases after 7.5.3 reject this").
- *Making the classes available*: the native-access remark can add that the runnable jars of 8.0.0 carry `--enable-native-access` in their manifest, whereas an explicit `-cp` invocation does not (text on the 8.0.0 `installing.md`).
- Optional: the `create` rule can use the 8.0.0 `use=young:test:age > 7000 & age < 15000` prefix on the `age` node instead of the `bool:true` anchor node, with the paragraph describing the rule adjusted accordingly. The prefix is silently ignored by 7.5.x, which is why the branch does not use it.
- The paper is cited without a venue; add it once known.

## 4. Update the example grammar and figures

- `manual/user-ops.gps/system.properties`: `grammarVersion=3.12`, `grooveVersion=8.0.0`, and add `semantics=SPO-multi` (as `manual/graphs.gps/system.properties` has on the 8.0.0 branch).
- Re-render `images/manual/user-ops-*.svg` with the 8.0.0 build, since the 8.0.0 branch re-rendered all manual figures with it; `make-figures.sh` compiles `user-ops-src/user/Date.java` and renders both grammars. On this machine a complete class path is `C:/Groove/code/release/jpackage/target/input/groove-<version>/lib/*`.
- Check the grammar with the 8.0.0 Generator (it explored to 7 states / 6 transitions with the 8.0.0 candidate on 2026-10-27).
