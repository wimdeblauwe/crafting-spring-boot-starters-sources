#!/usr/bin/env bash
set -euo pipefail

VERSION="$1"                            # e.g. 1.5.0
MINOR="${VERSION%.*}.x"                 # 1.5.x

mvn -Pdocs install

git fetch origin gh-pages
git checkout gh-pages

rm -rf "request-id/${MINOR}"
cp -R target/generated-docs "request-id/${MINOR}"

rm -rf request-id/latest
cp -R target/generated-docs request-id/latest

git add request-id
git commit -m "docs: publish ${VERSION}"
git push origin gh-pages
