# Walkthrough: Gesture Priority Correction (`PointerEventPass.Main`)

Corrected gesture priority by changing `swipeToBack` to process at `PointerEventPass.Main`, enabling child `SwipeableTaskRow` to consume task row drags before parent `swipeToBack` evaluates them.

---

## Key Changes Implemented

### 1. Gesture Priority Correction ([`SwipeToBack.kt`](file:///Users/barathiraja/AndroidStudioProjects/Dinam/app/src/main/java/com/barathiraja/dinam/ui/components/common/SwipeToBack.kt))
- **`PointerEventPass.Main` Shift:** Updated `awaitFirstDown` and `awaitPointerEvent` in `SwipeToBack.kt` from `PointerEventPass.Initial` to `PointerEventPass.Main`.
- **Pass Propagation Rule:** In Compose, `PointerEventPass.Main` flows from child up to parent.
- **Child Claim First:** Child `SwipeableTaskRow` (`detectHorizontalDragGestures` on `Main` pass) receives events first and consumes horizontal drags (`change.consume()`).
- **Parent Back Off:** Parent `swipeToBack()` inspects `change.isConsumed`. Since `change.isConsumed == true`, `swipeToBack()` breaks and backs off immediately, preventing screen back-navigation during item swiping.

### 2. Main Pass Child Drag Handling ([`SwipeTaskRow.kt`](file:///Users/barathiraja/AndroidStudioProjects/Dinam/app/src/main/java/com/barathiraja/dinam/ui/components/common/SwipeTaskRow.kt))
- Kept `SwipeableTaskRow` on `detectHorizontalDragGestures` at `PointerEventPass.Main` with the $40\text{dp}$ threshold.
- Left-to-right swipes $\ge 40\text{dp}$ trigger `onSwipeRight` (Delete Action).
- Right-to-left swipes $\le -40\text{dp}$ trigger `onSwipeLeft` (Inline Edit Action).

---

## Verification Results

- `app:assembleDebug`: **SUCCESS** (0 compilation errors)
- `app:testDebugUnitTest`: **SUCCESS** (13/13 unit tests passed)
