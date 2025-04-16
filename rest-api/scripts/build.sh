#!/bin/sh
set -e

# Navigate to the project root (where pom.xml is located)
cd "$(dirname "$0")"/.. || exit 1

mvn clean package -e
echo "Build successful. JAR location: target/"