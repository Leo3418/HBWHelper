#!/usr/bin/env bash

if [ -f before_deploy_complete ]
then
    echo "Pre-deployment tasks already done"
else

# Building JAR artifact

# Builds the following branches and creates source archives
branches="1.9-1.10.2 1.8.9"

for branch in ${branches}; do
    git checkout ${branch}
    ./gradlew setupCiWorkspace
    ./gradlew build
    zip_name='build/libs/src-'${branch}'-'${TRAVIS_BRANCH}'.zip'
    zip -r -q ${zip_name} .
    zip -d -q ${zip_name} ".git/*"
    zip -d -q ${zip_name} ".gradle/*"
    zip -d -q ${zip_name} "build/*"
done

# Deletes source JARs
cd build/libs
find . -type f -iregex ".*-sources\.jar" -delete
cd ../..

# Generating update JSON

# Gets version tags of the latest development build and the latest release build
export LATEST_DEV_TAG="$(curl 'https://api.github.com/repos/Leo3418/HBWHelper/releases/tags/'${TRAVIS_BRANCH} | jq -r '.tag_name')"
export LATEST_REL_TAG="$(curl 'https://api.github.com/repos/Leo3418/HBWHelper/releases/latest' | jq -r '.tag_name')"

# Removes the "v" prefix of the version tags
export LATEST_DEV_TAG="$(echo ${LATEST_DEV_TAG:1:${#LATEST_DEV_TAG}})"
export LATEST_REL_TAG="$(echo ${LATEST_REL_TAG:1:${#LATEST_REL_TAG}})"

# Replaces placeholders in update JSON file with release information
sed -i -e "s/<dev-ver>/${LATEST_DEV_TAG}/g" travis/pages/promotions.json
sed -i -e "s/<rel-ver>/${LATEST_REL_TAG}/g" travis/pages/promotions.json

# Marks completion of pre-deployment tasks
touch before_deploy_complete

fi
