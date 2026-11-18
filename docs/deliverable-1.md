# Software Architecture project - Deliverable 1

Ka Man Fung Ng

14 November 2026


## 1. Use cases

For this project, the user of the spreadsheet is the only actor. The program starts with an empty spreadsheet, so there is always a current spreadsheet. It is replaced by *Create spreadsheet* and *Load spreadsheet*.

| Use case | Description | Primary flow | Alternative flow |
|---|---|---|---|
| Create spreadsheet | Starts a new spreadsheet. | The user chooses the option, the current spreadsheet is discarded and an empty one takes its place. | None. |
| Load spreadsheet | Loads a spreadsheet from an S2V file. | See 1.1. | See 1.1. |
| Save spreadsheet | Saves the current spreadsheet to an S2V file. | See 1.2. | See 1.2. |
| Edit cell | Sets the content of a cell (text, a number or a formula) or clears the cell if the content is empty. | See 1.3. | See 1.3. |
| Process formula | Extends *Edit cell* when the new content starts with `=` and is included by *Load spreadsheet* for each formula of the file. Parses the formula, finds the cells it uses and computes its value or its error. | See 1.4. | See 1.4. |
| Show cell content | Shows the content of a cell as it was written. | The user gives the coordinate and the content is printed as it was written, for example `TOTAL`, `4` or `=A1+B1`. An empty cell prints an empty string. | The coordinate is not valid. |
| Show cell value | Shows the computed value of a cell. | The user gives the coordinate and the value is printed. | The coordinate is not valid. An error is printed if the cell holds a formula that could not be computed.  |
| Show spreadsheet | Shows the values of all the cells at once. | The spreadsheet is printed as a table, with the column letters on top and the row numbers on the left, up to the last non-empty row and column. | If the spreadsheet is empty, only the headers are printed. |
| Run commands from file | Runs a list of commands stored in a text file. | The user gives the path of the file, and each line is run as one of the menu options with its arguments. | The file is not found, or if a line is not a valid command or fails, the error is printed and the next line runs. |
| Exit | Leaves the program. | The program ends, nothing is saved unless the user saved it before. | None. |

### Use case descriptions

#### 1.1 Load spreadsheet

**ID:** UC-01

**Description:** The user replaces the current spreadsheet with one read from a file in S2V format. Each line of the file is a row and the cells of the row are separated by `;`. Inside a formula, the `;` between the arguments of a function is written as `,` in the file.

**Primary actor:** user.

**Secondary actors:** none.

**Pre-conditions:**
1. There is a current spreadsheet (the program starts with an empty one).

**Minimum guarantees:**
1. Either the spreadsheet of the file is the current spreadsheet with every cell holding its value or its error, or an error is shown and the previous spreadsheet is kept.

**Success guarantees:**
1. The current spreadsheet has one cell for each non-empty field of the file, at the row of the line and the column of the field.
2. Every formula has been processed and every cell has its value, or its error when the formula cannot be evaluated.

**Primary flow:**
1. The user gives the path of the S2V file.
2. The system opens the file.
3. For each line, the system splits it by `;`. Each non-empty field becomes the content of the cell at that row and column. In a field that starts with `=`, the `,` between the arguments of a function are turned back into `;`.
4. The system computes the cells in the order of the file. Numbers and text are stored directly and formulas are processed (*Process formula*), so each formula cell gets its value or its error. If a formula uses a formula cell that has not been computed yet, that cell is computed first. A formula cell that is reached again while it is being computed forms a cycle (*Process formula*, 2a).
5. The system replaces the current spreadsheet with the loaded one and says how many rows were read.
6. The use case ends.

**Alternative flows:**
- 2a. The file does not exist or cannot be read
  1. The system shows an error. The current spreadsheet is kept.
  2. The use case ends.
- 4a. A field contains a formula that *Process formula* rejects (syntax error or circular dependency)
  1. The system shows the coordinate of the cell and the error it returned. The current spreadsheet is kept.
  2. The use case ends.

#### 1.2 Save spreadsheet

**ID:** UC-02

**Description:** The user stores the current spreadsheet in a file in S2V format, so it can be loaded again later.

**Primary actor:** user.

**Secondary actors:** none.

**Pre-conditions:**
1. There is a current spreadsheet.

**Minimum guarantees:**
1. Either the file contains the whole spreadsheet, or an error is shown.

**Success guarantees:**
1. The file has one line per row, up to the last non-empty row, and loading it gives back the same contents.

**Primary flow:**
1. The user gives the path of the file to write.
2. For each row up to the last non-empty one, the system builds a line with the contents of its cells separated by `;`, up to the last non-empty cell of the row. Empty cells leave an empty field.
3. Numbers and text are written as they are, and formulas are written as their text, even when their value is an error, with the `;` between function arguments replaced by `,`.
4. The system writes the lines to the file, replacing it if it already exists, and confirms it.
5. The use case ends.

**Alternative flows:**
- 4a. The file cannot be written (the folder does not exist or there is no permission)
  1. The system shows an error.
  2. The use case ends.

#### 1.3 Edit cell

**ID:** UC-03

**Description:** The user sets the content of a cell. If the text starts with `=` it is a formula, if it looks like a number it is a number, and anything else is text. If the content is empty, the cell is cleared. After the change, the cells that depend on the edited one are recomputed.

**Primary actor:** user.

**Secondary actors:** none.

**Pre-conditions:**
1. There is a current spreadsheet (new, or loaded from a file).

**Minimum guarantees:**
1. Either the cell holds the new content and every dependent cell has been recomputed, each one with a value or an error, or the content is rejected, an error is shown and nothing changes.

**Success guarantees:**
1. The cell holds the new content and its value, or its error when the formula cannot be evaluated.
2. Every cell that depends on the edited cell, directly or through other cells, has been recomputed from the new content and holds either its new value or an error.

**Primary flow:**
1. The user gives the coordinate of the cell and the new content.
2. The system checks that the coordinate is valid (one or more letters followed by a row number from 1) and that the content, if it is text, does not contain `;`.
3. The system determines the type of the content (extension point *content type*): a formula if it starts with `=`, a number if it looks like a number, and text otherwise.
4. The system stores the new content in the cell, with its value or its error, and records the cells it depends on. If the new content is empty, the system removes the content of the cell instead, so the cell is empty again.
5. The system recomputes only the cells that depend on the edited cell, directly or through other cells. Each one is recomputed after the cells it uses.
6. The system shows the new value of the cell, or its error.
7. The use case ends.

**Alternative flows:**
- 2a. Invalid coordinate
  1. The system shows an error and nothing changes.
  2. The use case ends.
- 2b. The content is text that contains `;`
  1. The system shows an error, because the S2V format could not store that text and nothing changes.
  2. The use case ends.
- 3a. The content starts with `=` (*Process formula*)
  1. The system processes the formula and gets its expression, the cells it depends on and its value or its error.
  2. The use case resumes at step 4 (of Primary flow).
- 3a.1a. *Process formula* rejects the formula (syntax error or circular dependency)
  1. The system shows the error it returned and nothing changes.
  2. The use case ends.
- 5a. A dependent formula can no longer be evaluated with the new content
  1. The edit is kept. The dependent formula is evaluated again with the rules of *Process formula*, and since it cannot be evaluated the system replaces the value of that cell, and of the cells that depend on it, by an error and reports it.
  2. The use case resumes at step 6 (of Primary flow).

#### 1.4 Process formula

**ID:** UC-04

**Description:** The system turns the text of a formula into an expression, finds the cells it depends on and computes its value. It is included by *Load spreadsheet* for each formula of the file and extends *Edit cell* when the new content starts with `=`.

**Primary actor:** none (included and extending use case, started by *Load spreadsheet* or *Edit cell*).

**Secondary actors:** none.

**Pre-conditions:**
1. There is a text that starts with `=` and the coordinate of the cell it is going to be set in.

**Minimum guarantees:**
1. Either the formula is accepted with its value or its error, or it is rejected and the use case that asked for it does not change the cell.

**Success guarantees:**
1. The expression of the formula, the cells it depends on and its value, or its error, are returned to the use case that asked for it.

**Primary flow:**
1. The system parses the text into an expression. A formula can contain numbers, the operators `+`, `-`, `*` and `/`, parentheses, cell references, ranges such as `A1:B3` and the functions `SUMA`, `MIN`, `MAX` and `PROMEDIO`, whose arguments are separated by `;`. Any expression can be a function argument, for example `SUMA(A1*2;3)`.
2. The system collects the cells referenced by the expression, directly or through its ranges, and checks that none of them depends, directly or through other cells, on the cell the formula is being set in.
3. The system evaluates the expression, each referenced cell gives a number. A numerical content gives its number, an empty cell gives 0, a text content gives the number it represents and a formula gives its value, or its error (3a).
4. The system returns the expression, the cells it depends on and the value.
5. The use case ends.

**Alternative flows:**
- 1a. The text has a syntax error
  1. The formula is rejected and the error, with where it was found, is returned to the use case that asked for it.
  2. The use case ends.
- 2a. The formula introduces a circular dependency
  1. The formula is rejected and the error, with the cells that form the cycle, is returned to the use case that asked for it.
  2. The use case ends.
- 3a. The expression cannot be evaluated (a referenced cell holds text that is not a number, a referenced formula holds an error, or there is a division by zero)
  1. The formula is accepted anyway, the system returns the expression and the cells it depends on with an error instead of the value, so the cell will show the error (*Show cell value*) and will be recomputed when one of those cells changes.
  2. The use case ends.

## 2. Use case diagram

![Use case diagram](use-case-diagram.drawio.png)

- The menu options are the use cases linked to the actor.
- When the new content starts with `=`, the formula has to be parsed, checked and evaluated. That is `Process formula`, an extension of `Edit cell` at the extension point `content type`, which only runs under that condition. A formula is one of the three normal kinds of content, so the extension is conditional rather than exceptional.
- `Load spreadsheet` includes `Process formula`, because every non-empty cell has to be processed right after loading a spreadsheet from a file. Both uses share the same rules, defined once in 1.4.
- Rejecting a circular dependency, recomputing the cells that depend on an edited cell and computing a loaded spreadsheet are not use cases of their own. The user never asks for them separately, so they are steps and alternative flows of `Edit cell` and `Load spreadsheet` (see 1.3 and 1.1).
- `Run commands from file` runs the options listed in a text file, one per line. Which options run depends on the file, and an included use case runs every time its base use case runs, so it is not drawn as including them. It is there to test the program without typing the commands by hand.

## 3. Domain model diagram

![Domain model](domain-model.drawio.png)

A spreadsheet is a set of cells, each one at a coordinate and holding text, a number or a formula. On top of that, the model represents a parsed formula as a tree and keeps the dependencies between cells.

- `Spreadsheet` is composed of its `Cell`s, which do not exist outside it. `S2VFile` is the file it is stored in, and a new spreadsheet has none yet.
- Each `Cell` has one `Coordinate` (column letters and row number) and is composed of at most one `Content`, which does not exist outside its cell. In particular, an empty cell has no content, so its string is empty and its number is 0.
- `Content` is abstract, with `TextContent`, `NumericalContent` and `FormulaContent`. A formula keeps its text and either the value computed from it or an error when it cannot be evaluated (*Process formula*, 3a), never both.
- The reflexive association on `Cell` records which cells a formula depends on. It is derived (marked with `/`) from the cell references and ranges of the formula, and is kept in the model because only the dependents are recomputed after an edit, and it is also what detects cycles.
- `FormulaContent` is composed of its `Expression` tree, which does not exist outside the formula. `NumberLiteral` and `CellReference` are the leaves, and a `CellReference` refers to the `Coordinate` of the cell it reads. `BinaryOperation` (`+`, `-`, `*`, `/`) is composed of two ordered operand expressions, since the order matters for `-` and `/`, and `FunctionCall` (`SUMA`, `MIN`, `MAX`, `PROMEDIO`) of one or more arguments.
- An `Argument` is either an `Expression` or a `Range`, a `Range` is the rectangle between two coordinates and is not an `Expression`, so it can be an argument of a function but not an operand or a whole formula. A single cell is a `CellReference`, which as an argument behaves like a range whose two corners are the same cell.
