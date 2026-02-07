#!/bin/sh
set -e

cd "$(dirname "$0")"/.. || exit 1

mvn clean package

# Find the executable JAR (typically in target/ directory)
JAR_FILE=$(ls -t target/*.jar | grep -v "original" | head -1)

if [ -z "$JAR_FILE" ]; then
  echo "Error: No executable JAR found in target/"
  exit 1
fi

echo "Running $JAR_FILE"
java -jar "$JAR_FILE"