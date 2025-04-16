#!/bin/sh
set -e

cd "$(dirname "$0")"/.. || exit 1

# Find the executable JAR (exclude *-original.jar)
JAR_FILE=$(ls -t target/*.jar | grep -v "original" | head -1)

if [ -z "$JAR_FILE" ]; then
  echo "Error: No executable JAR found. Build the project first with 'mvn package'."
  exit 1
fi

echo "Starting $JAR_FILE"
java -jar "$JAR_FILE"