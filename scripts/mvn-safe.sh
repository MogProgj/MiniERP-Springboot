#!/usr/bin/env bash
set -euo pipefail

# Usage:
#   scripts/mvn-safe.sh validate
#   scripts/mvn-safe.sh test
#
# Uses project-local .mvn/settings.xml if present, otherwise default Maven settings.
# This helps teams pin a known-good mirror/proxy setup per project.

if [[ -f ".mvn/settings.xml" ]]; then
  echo "Using project settings: .mvn/settings.xml"
  mvn -s .mvn/settings.xml "$@"
else
  echo "No .mvn/settings.xml found; using default Maven settings"
  mvn "$@"
fi
