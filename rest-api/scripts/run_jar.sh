#!/bin/sh
set -e

cd "$(dirname "$0")"/.. || exit 1

# Find the executable JAR (exclude *-plain.jar)
JAR_FILE=$(ls -t build/libs/*.jar | grep -v "plain" | head -1)

if [ -z "$JAR_FILE" ]; then
  echo "Error: No executable JAR found. Build the project first."
  exit 1
fi

echo "Starting $JAR_FILE"
java -jar "$JAR_FILE"