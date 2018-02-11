#!/usr/bin/env bash

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
