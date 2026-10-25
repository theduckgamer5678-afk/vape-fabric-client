name: Build Mod Jar
on: [push, workflow_dispatch]

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - name: Checkout
        uses: actions/checkout@v4

      - name: Set up Java 25
        uses: actions/setup-java@v4
        with:
          distribution: 'temurin'
          java-version: '25'

      - name: Set up Gradle
        uses: gradle/actions/setup-gradle@v4
        with:
          gradle-version: '9.7.1'

      - name: Build
        run: gradle build

      - name: Dump names 2
        if: always()
        run: |
          set +e
          J=/home/runner/.gradle/caches/fabric-loom/26.3/minecraft-merged.jar
          echo "=== Minecraft"
          javap -public -cp $J net.minecraft.client.Minecraft | grep -E "getInstance|getWindow|setScreen|Screen screen|Font font"
          echo "=== Screen"
          javap -public -cp $J net.minecraft.client.gui.screens.Screen | grep -E "Screen\(|mouseClicked|keyPressed|isPauseScreen|shouldPause|void init\(|onClose|extractRenderState\("
          echo "=== GuiGraphicsExtractor"
          javap -public -cp $J net.minecraft.client.gui.GuiGraphicsExtractor | grep -E " text\(| fill\(| centeredText\(| string\(|drawString|drawText"
          echo "=== Component"
          javap -public -cp $J net.minecraft.network.chat.Component | grep -E "literal|nullToEmpty"
          echo "=== KeyMapping"
          javap -public -cp $J net.minecraft.client.KeyMapping | grep -E "KeyMapping\(|isDown|consumeClick|matches"
          echo "=== KeyMapping.Category"
          javap -public -cp $J 'net.minecraft.client.KeyMapping$Category'
          echo "=== InputConstants"
          javap -public -cp $J com.mojang.blaze3d.platform.InputConstants | grep -E "isKeyDown|KEY_RSHIFT|KEY_RIGHT|KEY_LSHIFT"
          echo "=== MouseButtonEvent"
          unzip -l $J | awk '{print $4}' | grep -E "MouseButtonEvent|KeyEvent"
          javap -public -cp $J net.minecraft.client.input.MouseButtonEvent | head -15
          echo "=== Fabric key jars"
          for f in $(find /home/runner/.gradle -name "fabric-key*.jar"); do echo "JAR: $f"; unzip -l "$f" | awk '{print $4}' | grep -E "\.class$" | grep -iE "Helper|KeyMapping|KeyBinding" | head -10; done
          echo "=== Fabric tick events"
          for f in $(find /home/runner/.gradle -name "fabric-lifecycle-events*.jar"); do unzip -l "$f" | awk '{print $4}' | grep ClientTickEvents | head -3; done

      - name: Upload jar
        uses: actions/upload-artifact@v4
        with:
          name: Vape-Client-Jar
          path: build/libs/*.jar
          if-no-files-found: error