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

# Release branches for older clients
OLD_CLIENT_BRANCHES="1.9-1.10.2 1.8.9"

if ! [ "$TRAVIS" = true ]; then
    echo "This script is intended to be run only in a Travis CI build environment. You should not run this in any other environment."
    echo "If you intend to do so, please run 'export TRAVIS=true' first."
    exit
fi

if [ -f before_deploy_complete ]; then
    echo "Pre-deployment tasks already done"
else # Perform pre-deployment tasks

# Builds JAR artifacts and source archives for old client versions
for branch in ${OLD_CLIENT_BRANCHES}; do
    git checkout ${branch}
    ./gradlew setupCiWorkspace
    ./gradlew build
    zip_name="build/libs/src-$branch-$TRAVIS_BRANCH.zip"
    zip -r -q ${zip_name} .
    zip -d -q ${zip_name} ".git/*"
    zip -d -q ${zip_name} ".gradle/*"
    zip -d -q ${zip_name} "build/*"
done

# Deletes source JARs
(
    cd build/libs
    find . -type f -iregex ".*-sources\.jar" -delete
)

# Gets version tags of the latest development build and the latest release build
LATEST_DEV_TAG=$(curl -s "https://api.github.com/repos/Leo3418/HBWHelper/releases/tags/$TRAVIS_BRANCH" | jq -r '.tag_name')
LATEST_REL_TAG=$(curl -s "https://api.github.com/repos/Leo3418/HBWHelper/releases/latest" | jq -r '.tag_name')

# Removes the "v" prefix of the version tags
LATEST_DEV_TAG=${LATEST_DEV_TAG:1:${#LATEST_DEV_TAG}}
LATEST_REL_TAG=${LATEST_REL_TAG:1:${#LATEST_REL_TAG}}

# Replaces placeholders in update JSON file with release information
sed -i -e "s/<dev-ver>/$LATEST_DEV_TAG/g" travis/pages/promotions.json
sed -i -e "s/<rel-ver>/$LATEST_REL_TAG/g" travis/pages/promotions.json

# Marks completion of pre-deployment tasks
touch before_deploy_complete

fi
