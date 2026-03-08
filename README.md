[**简体中文**](https://github.com/coderxi1/PaidFly/blob/master/README.zh.md)

# PaidFly

**PaidFly** is a plugin designed for Minecraft servers that allows players to fly by consuming (in-game currency/experience points)!

## Features

- Supports players flying by paying with in-game currency/experience points
- Supports Title, Action, and sound notifications
- Supports countdown notifications

## Installation

1. Download the latest version of **`PaidFly.jar`** from the [plugin release page](https://github.com/coderxi1/PaidFly/releases) (if the server has `kotlin-stdlib`, you can use `PaidFly-kotlin.jar`)
2. Place the plugin in the server's `plugins/` folder
3. To allow players to use it, set the `paidfly.fly` permission for the default user group
4. If using experience for flying, ensure the **`Vault`** economy plugin is installed on the server.

## Configuration

```yaml
Main:
  PayType: Money        # Payment mode
  PayInterval: 3s       # Payment interval
  PayCost: 0.5          # Cost per interval
  AutoOffThreshold: 0.5 # Turn off flight if below this value (recommended to set same as PayCost)
```

## Commands

All commands start with `/paidfly`, alias `/fly`:

| Command | Permission | Description |
|---------|------------|-------------|
| `/fly` | `paidfly.fly` | Equivalent to `/fly toggle` |
| `/fly on/off/toggle` | `paidfly.fly` | Toggle flight status |
| `/fly on/off/toggle [player]` | `paidfly.others` | Toggle flight status for other players, requires `paidfly.others` permission |
| `/fly help` | `paidfly.use` | Display plugin help information (shows different content based on permissions) |
| `/fly reload` | `paidfly.reload` | Reload plugin configuration |

## Permissions

| Permission Node | Function |
|-----------------|----------|
| `paidfly.use` | Basic usage permission, view help information |
| `paidfly.fly` | Enable, disable, or toggle flight |
| `paidfly.others` | Operate flight for other players |
| `paidfly.reload` | Reload plugin configuration |
| `paidfly.admin` | Access admin subcommands (e.g., `/paidfly reload`) |

## PlaceholderAPI

| Placeholder | Description | Example |
|-------------|-------------|---------|
| `%paidfly_status%` | Whether the player is currently in paid flight mode | `true` / `false` |
| `%paidfly_remaining_seconds%` | Remaining flight time in seconds | `120` |
| `%paidfly_pay_type%` | Localized name of the current payment type | `Gold` |
| `%paidfly_pay_type_value%` | Raw calculated value of the current payment type for the player | `12.3333333334` |
| `%paidfly_pay_type_value_fixed%` | Current payment value, default to **1 decimal place** | `12.3` |
| `%paidfly_pay_type_value_fixed_<digits>%` | Current payment value, with specified decimal places | `%paidfly_pay_type_value_fixed_2%` → `12.34` |