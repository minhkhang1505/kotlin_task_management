#!/bin/bash
# Automated iOS Framework Build & Xcode Setup Script

set -e

PROJECT_ROOT="/Users/nguyenminhkhang/Documents/Kotlin/kotlin_task_management"
SHARED_MODULE="$PROJECT_ROOT/shared"
FRAMEWORK_OUTPUT="$SHARED_MODULE/build/XCFrameworks/release/sharedKit.xcframework"
IOS_APP_DIR="$PROJECT_ROOT/iosApp/TaskManagementApp"

echo "🚀 Starting iOS Framework Setup..."
echo "=================================="

# Step 1: Ensure gradle wrapper exists
echo "✓ Step 1: Checking Gradle..."
cd "$PROJECT_ROOT"

# Step 2: Build XCFramework
echo "✓ Step 2: Building iOS Framework (sharedKit.xcframework)..."
echo "   This may take 2-3 minutes on first build..."
./gradlew shared:assembleXCFramework

# Verify framework was created
if [ ! -d "$FRAMEWORK_OUTPUT" ]; then
    echo "❌ Framework build failed!"
    exit 1
fi

echo "✅ Framework built: $FRAMEWORK_OUTPUT"

# Step 3: List framework architectures
echo ""
echo "✓ Step 3: Framework Contents:"
ls -la "$FRAMEWORK_OUTPUT"

# Step 4: Create iOS app directory structure if it doesn't exist
echo ""
echo "✓ Step 4: Setting up iOS app structure..."
mkdir -p "$IOS_APP_DIR"/Sources
mkdir -p "$IOS_APP_DIR"/Resources

# Step 5: Create Info.plist template
if [ ! -f "$IOS_APP_DIR/Resources/Info.plist" ]; then
    cat > "$IOS_APP_DIR/Resources/Info.plist" << 'EOF'
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE plist PUBLIC "-//Apple//DTD PLIST 1.0//EN" "http://www.apple.com/DTDs/PropertyList-1.0.dtd">
<plist version="1.0">
<dict>
    <key>CFBundleExecutable</key>
    <string>$(EXECUTABLE_NAME)</string>
    <key>CFBundleIdentifier</key>
    <string>$(PRODUCT_BUNDLE_IDENTIFIER)</string>
    <key>CFBundleInfoDictionaryVersion</key>
    <string>6.0</string>
    <key>CFBundleName</key>
    <string>$(PRODUCT_NAME)</string>
    <key>CFBundlePackageType</key>
    <string>APPL</string>
    <key>CFBundleShortVersionString</key>
    <string>1.0</string>
    <key>CFBundleVersion</key>
    <string>1</string>
    <key>LSRequiresIPhoneOS</key>
    <true/>
    <key>UIMainStoryboardFile</key>
    <string></string>
    <key>UIRequiredDeviceCapabilities</key>
    <array>
        <string>armv7</string>
    </array>
    <key>UISupportedInterfaceOrientations</key>
    <array>
        <string>UIInterfaceOrientationPortrait</string>
        <string>UIInterfaceOrientationLandscapeLeft</string>
        <string>UIInterfaceOrientationLandscapeRight</string>
    </array>
</dict>
</plist>
EOF
    echo "   Created Info.plist template"
fi

# Step 6: Print next steps
echo ""
echo "✅ iOS Framework Setup Complete!"
echo ""
echo "📍 Next Steps:"
echo "1. Install xcodegen: brew install xcodegen"
echo "2. Navigate to: cd $IOS_APP_DIR"
echo "3. Generate Xcode project: xcodegen generate"
echo "4. Open: open TaskManagement.xcodeproj"
echo "5. Link framework in Xcode (see PHASE_4_iOS_SETUP.md for details)"
echo ""
echo "📚 Full guide: $PROJECT_ROOT/docs/PHASE_4_iOS_SETUP.md"
echo ""
