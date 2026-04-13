#!/bin/bash
set -e

cd /Users/nguyenminhkhang/Documents/Kotlin/kotlin_task_management

echo "🧹 Cleaning old XCFramework..."
rm -rf shared/build/XCFrameworks/release

echo "📁 Creating output directory..."
mkdir -p shared/build/XCFrameworks/release

echo "🔗 Building XCFramework..."
xcodebuild -create-xcframework \
  -framework "shared/build/bin/iosArm64/debugFramework/sharedKit.framework" \
  -framework "shared/build/bin/iosSimulatorArm64/debugFramework/sharedKit.framework" \
  -output "shared/build/XCFrameworks/release/sharedKit.xcframework"

echo ""
echo "✅ XCFramework created successfully!"
echo "📍 Path: shared/build/XCFrameworks/release/sharedKit.xcframework"
echo ""
ls -la shared/build/XCFrameworks/release/sharedKit.xcframework/
