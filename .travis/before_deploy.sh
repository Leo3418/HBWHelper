#!/usr/bin/env bash

# Bash script for pre-deployment tasks during a Travis CI deployment build of
# this project.
#
# This script is designed for execution in a Travis CI build environment only.
# If you want to build an artifact from the source files, please refer to build
# instructions in this project's documentation. If you want to test this
# script, please execute command `export TRAVIS=true` before you start.
#
# Requirements of correct execution of this script:
# - The clone of Git repository of this project used in build and deployment
#   must NOT be a shallow repository.
# - `zip`, `curl` (https://curl.haxx.se/), and `jq`
#   (https://stedolan.github.io/jq/) must have been installed in the
#   environment.
# - Global variable `$TRAVIS_BRANCH` which stores the mod version of the
#   current build with "v" prefix must be defined in the environment.

# Release branches for older clients that use ForgeGradle 3.x.
# When building from these branches, the `build` task can be run directly
# without setting up a workspace.
OLD_FG3_BRANCHES="1.14.4"

# Release branches for older clients that use ForgeGradle 2.x.
# Building from these branches requires setting up a workspace.
OLD_FG2_BRANCHES="1.11-1.12.2 1.9-1.10.2 1.8.9"

# Builds JAR artifact and source archive for an old client branch.
#
# Parameters:
# 1. The name of the branch
# 2. Whether the branch requires ForgeGradle 2.x to build
build_jar() {
  local branch=$1
  local fg2=$2

  git checkout "$branch"
  if [ "$fg2" = true ]; then
    ./gradlew setupCIWorkspace
  fi
  ./gradlew build

  zip_name="build/libs/src-$branch-$TRAVIS_BRANCH.zip"
  zip -r -q "$zip_name" .
  zip -d -q "$zip_name" ".git/*"
  zip -d -q "$zip_name" ".gradle/*"
  zip -d -q "$zip_name" "build/*"
}

# Performs pre-deployment tasks.
pre_deploy() {
  # Builds JAR artifacts and source archives for old client versions
  for branch in ${OLD_FG3_BRANCHES}; do
    build_jar "$branch" false
  done
  for branch in ${OLD_FG2_BRANCHES}; do
    build_jar "$branch" true
  done

  # Deletes source JARs
  (
    cd build/libs || exit
    find . -type f -iregex ".*-sources\.jar" -delete
  )

  # Gets version tags of the latest development build and the latest release
  # build

  # The latest development version would always be the current version being
  # built
  local latest_dev_tag=${TRAVIS_BRANCH}
  local latest_rel_tag=$(curl -s "https://api.github.com/repos/Leo3418/HBWHelper/releases/latest" | jq -r '.tag_name')

  # Removes the "v" prefix of the version tags
  local latest_dev_tag=${latest_dev_tag:1:${#latest_dev_tag}}
  local latest_rel_tag=${latest_rel_tag:1:${#latest_rel_tag}}

  # Replaces placeholders in update JSON file with release information
  sed -i -e "s/<dev-ver>/$latest_dev_tag/g" .travis/pages/promotions.json
  sed -i -e "s/<rel-ver>/$latest_rel_tag/g" .travis/pages/promotions.json

  # Marks completion of pre-deployment tasks
  touch before_deploy_complete
}

main() {
  if ! [ "$TRAVIS" = true ]; then
    echo "This script is intended to be run only in a Travis CI build environment. You should not run this in any other environment."
    echo "If you intend to do so, please run 'export TRAVIS=true' first."
    exit 1
  fi

  if [ -f before_deploy_complete ]; then
    echo "Pre-deployment tasks already done"
  else
    pre_deploy
  fi
}

main
