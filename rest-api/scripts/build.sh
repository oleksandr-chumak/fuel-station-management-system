#!/bin/sh
set -e

# Navigate to the project root (where gradlew is located)
cd "$(dirname "$0")"/.. || exit 1

./gradlew clean build
echo "Build successful. JAR location: build/libs/"