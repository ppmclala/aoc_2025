
ownerproof-5388755-1764735318-01efb670309b

# Java LSP Keybindings Cheat Sheet

## Standard LSP Commands

| Keybinding | Command | Description |
|------------|---------|-------------|
| `gd` | Go to Definition | Jump to where a symbol is defined |
| `gr` | Go to References | List all references to symbol under cursor |
| `gi` | Go to Implementation | Jump to implementation of interface/abstract method |
| `K` | Hover Documentation | Show documentation for symbol under cursor |
| `<leader>rn` | Rename | Rename symbol across entire project |
| `<leader>ca` | Code Action | Show available code actions (quick fixes, refactors) |

## Diagnostics (Errors/Warnings)

| Keybinding | Command | Description |
|------------|---------|-------------|
| `<leader>e` | Show Error | Show error/warning message in floating window |
| `[d` | Previous Diagnostic | Jump to previous error/warning |
| `]d` | Next Diagnostic | Jump to next error/warning |
| `<leader>q` | List All | Show all diagnostics in location list |

## Java-Specific Commands

| Keybinding | Command | Description | Mode |
|------------|---------|-------------|------|
| `<leader>jo` | Organize Imports | Remove unused and sort imports | Normal |
| `<leader>jv` | Extract Variable | Extract expression to a variable | Normal/Visual |
| `<leader>jc` | Extract Constant | Extract value to a constant | Normal/Visual |
| `<leader>jm` | Extract Method | Extract selection to a new method | Visual |

## Tips

- Default `<leader>` key is `\` (backslash), but many users remap it to `,` or `<Space>`
- Code actions (`<leader>ca`) provide context-aware refactorings and quick fixes
- Visual mode keybindings: Select text in visual mode first, then press the keybinding
- The LSP automatically starts when you open a `.java` file in the project root

## Setup Requirements

1. **Plugin**: `mfussenegger/nvim-jdtls`
2. **Language Server**: Install via `:MasonInstall jdtls`
3. **Config Location**: `~/.config/nvim/ftplugin/java.lua`
4. **Java Version**: Java 17+ required to run jdtls
