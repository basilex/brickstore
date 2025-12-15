#!/usr/bin/env bash
set -euo pipefail

# Cleanup or rotate large files inside VS Code globalStorage (macOS)
# Usage:
#   scripts/cleanup-vscode-globalstorage.sh [GLOBAL_STORAGE_DIR]
# Environment:
#   THRESHOLD_BYTES - files larger than this (default 5242880 = 5MB)
#   BACKUP_DIR - where to move files (default: ~/Desktop/vscode-globalstorage-backup-<ts>)
#   DELETE=1 - if set to 1, delete matching files instead of moving them

DEFAULT_DIR="$HOME/Library/Application Support/Code/User/globalStorage"
GS_DIR="${1:-$DEFAULT_DIR}"

if [ "$(uname -s)" != "Darwin" ]; then
  echo "This script is intended for macOS only (found $(uname -s))." >&2
  exit 1
fi

THRESHOLD_BYTES=${THRESHOLD_BYTES:-5242880}
DELETE=${DELETE:-0}
BACKUP_DIR=${BACKUP_DIR:-"$HOME/Desktop/vscode-globalstorage-backup-$(date +%Y%m%dT%H%M%S)"}

echo "GlobalStorage dir: $GS_DIR"
echo "Threshold (bytes): $THRESHOLD_BYTES"
echo "Delete mode: $DELETE"

if [ ! -d "$GS_DIR" ]; then
  echo "GlobalStorage directory not found: $GS_DIR" >&2
  exit 0
fi

mkdir -p "$BACKUP_DIR"
found=0

# Find files larger than threshold (BSD find on macOS supports 'c' for bytes)
while IFS= read -r -d '' f; do
  found=1
  size=$(stat -f%z "$f")
  rel=${f#"$GS_DIR"/}
  dest="$BACKUP_DIR/$rel"
  mkdir -p "$(dirname "$dest")"
  if [ "$DELETE" = "1" ]; then
    echo "Deleting: $f ($size bytes)"
    rm -f "$f"
  else
    echo "Moving: $f ($size bytes) -> $dest"
    mv "$f" "$dest"
  fi
done < <(find "$GS_DIR" -type f -size +${THRESHOLD_BYTES}c -print0)

if [ "$found" = "0" ]; then
  echo "No files larger than ${THRESHOLD_BYTES} bytes were found in $GS_DIR"
  rmdir --ignore-fail-on-non-empty "$BACKUP_DIR" || true
else
  echo "Backup directory: $BACKUP_DIR"
fi

echo "Done."
