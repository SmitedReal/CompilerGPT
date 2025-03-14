
```markdown
# CompilerGPT

CompilerGPT is a simple yet powerful compiler/interpreter for mathematical expressions and basic functions. It supports arithmetic operations, trigonometric functions (`sin`, `cos`), and multi-line input. It is designed to be easy to use and extend.

---

## Features

- **Arithmetic Operations**: Supports `+`, `-`, `*`, `/`, and parentheses `()`.
- **Trigonometric Functions**: Supports `sin()` and `cos()`.
- **Multi-Line Input**: Process multiple expressions in a single run.
- **Global Command**: Install once, use anywhere via the `cg` command.
- **Cross-Platform**: Works on Windows (via installer) and can be adapted for other platforms.

---

## Installation

### Windows

1. Download the `CompilerGPT_Setup.exe` installer.
2. Run the installer and follow the on-screen instructions.
3. During installation, select the **"Add CompilerGPT to PATH"** option to make the `cg` command globally accessible.

---

## Usage

### Command Line

After installation, open a command prompt and use the `cg` command to run `.cg` files:

```bash
cg Example.cg
```

### Example `.cg` File

Create a file named `Example.cg` with the following content:

```
print 2 + 3
print sin(1.57) + cos(3.14)
print (10 - 2) * 3 / 4
```

Run the file:

```bash
cg Example.cg
```

### Output

```
Result: 5.0
Result: 9.512042950000086E-7
Result: 6.0
```

---

## Supported Syntax

### Arithmetic Operations

- Addition: `2 + 3`
- Subtraction: `10 - 2`
- Multiplication: `3 * 4`
- Division: `12 / 3`
- Parentheses: `(2 + 3) * 4`

### Functions

- `sin(x)`: Computes the sine of `x` (in radians).
- `cos(x)`: Computes the cosine of `x` (in radians).

---

## Building from Source

### Prerequisites

- Java Development Kit (JDK) 8 or later.
- Inno Setup (for creating the Windows installer).

### Steps

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/CompilerGPT.git
   cd CompilerGPT
   ```

2. Compile the Java source code:
   ```bash
   javac CompilerGPT.java
   ```

3. Create a JAR file:
   ```bash
   jar cf