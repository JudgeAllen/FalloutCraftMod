# 💣 辐射纪元 (FalloutCraft)

**辐射纪元** 是一个 Minecraft 1.21.11 Forge 模组，为游戏添加核武器、辐射机制和废土生存元素。

---

## 🔥 功能概览 (Features)

| 功能 | 说明 |
|------|------|
| **💣 核弹** | 可投掷的核弹，落地产生大规模爆炸 |
| **☁️ 蘑菇云** | 爆炸后生成由微型爆炸链构建的蘑菇云视觉特效 |
| **☢️ 辐射区域** | 爆炸后留下持续 5 分钟的辐射云（AreaEffectCloud） |
| **🩸 辐射病** | 处于辐射区会获得辐射效果，每 2 秒造成无视护甲的魔法伤害 |
| **📟 盖革计数器** | 右键检测周围辐射等级（安全/低/中/高），有噼啪音效反馈 |
| **💊 抗辐射药丸** | 立即消除辐射效果 |
| **🖥️ 屏幕覆盖** | 辐射效果下屏幕呈现绿色，等级越高越浓 |

---

## 📦 安装 (Installation)

1. 安装 Minecraft **1.21.11** 和 **Forge 61.1.0**
2. 从 Releases 下载 `FalloutCraft-1.0.0.jar`
3. 放入 `.minecraft/mods/` 文件夹
4. 启动游戏

---

## 🔧 开发 (Development)

### 环境要求

- Java 21
- Gradle（使用项目自带的 wrapper）

### 构建 & 运行

```bash
# 编译
./gradlew build

# 启动客户端
./gradlew runClient

# 清理并重新编译
./gradlew clean build
```

### 项目结构

```
src/main/java/com/falloutcraft/fallout/
├── FalloutCraft.java          — 主类 + 创造标签
├── Config.java                — 配置参数
├── item/
│   ├── FalloutItems.java      — 物品注册
│   ├── NuclearBombItem.java   — 核弹物品
│   ├── GeigerCounterItem.java — 盖革计数器
│   └── AntiRadiationPillItem.java — 抗辐射药丸
├── entity/
│   ├── FalloutEntities.java   — 实体注册
│   └── NuclearBombEntity.java — 核弹实体（爆炸+蘑菇云+辐射区）
├── effect/
│   ├── FalloutEffects.java    — 效果注册
│   └── RadiationEffect.java   — 辐射效果
├── sound/
│   └── FalloutSounds.java     — 音效注册
├── particle/
│   └── FalloutParticles.java  — 粒子注册
└── client/
    └── ClientHandlers.java    — 客户端渲染器 + 屏幕覆盖

src/main/resources/
├── assets/falloutcraft/
│   ├── items/                 — 物品模型定义 (1.21+ 新格式)
│   ├── models/item/           — 物品模型 JSON
│   ├── textures/item/         — 物品贴图 16x16 PNG
│   ├── textures/mob_effect/   — 状态效果图标
│   ├── lang/                  — 中英文语言文件
│   ├── sounds/                — OGG 音效文件
│   └── sounds.json            — 音效注册表
├── data/falloutcraft/
│   ├── recipes/               — 合成配方
│   └── advancements/          — 进度系统
└── META-INF/mods.toml         — 模组元信息
```

---

## 🎮 游戏内操作 (In-Game Usage)

1. 创造模式物品栏找到 **「辐射纪元」** 标签页（在战斗标签后）
2. 手持 **核弹** 右键投掷 → 等待落地爆炸
3. 爆炸后出现辐射云，走入其中会获得 **辐射病** 效果
4. 使用 **盖革计数器** 右键检测辐射等级
5. 使用 **抗辐射药丸** 消除辐射效果

### 合成配方 (Crafting)

| 物品 | 配方 |
|------|------|
| 核弹 | TNT + 钻石块 + 红石块 |
| 抗辐射药丸 | 金苹果 + 荧石粉 + 玻璃瓶 |

> Phase 2 将替换为完整的铀矿加工流程。

---

## ⚙️ 配置 (Configuration)

配置文件路径：`run/config/falloutcraft-common.toml`

| 参数 | 默认值 | 说明 |
|------|--------|------|
| `bomb.explosionRadius` | 25 | 核爆半径（范围 5~100） |
| `radiation.cloudRadius` | 15 | 辐射云半径 |
| `radiation.cloudDuration` | 6000 | 辐射云持续刻数（20刻=1秒） |
| `radiation.damageInterval` | 40 | 辐射伤害间隔（刻） |
| `radiation.damageAmount` | 1.0 | 每次辐射伤害值 |

---

## 🗺️ 未来计划 (Phase 2)

- 铀矿生成、冶炼、加工科技线
- 多级核弹（小型/战术/热核）
- 核反应堆方块
- 防辐射装甲套装
- 导弹发射系统

---

## 📜 许可 (License)

All Rights Reserved

---

## 🤖 致谢 (Credits)

- 基于 [MinecraftForge MDK](https://github.com/MinecraftForge/MDKExamples) 开发
- 由 Claude Code 辅助编写
