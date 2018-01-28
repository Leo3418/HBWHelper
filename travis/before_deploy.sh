#!/usr/bin/env bash

# Builds other branches
git checkout 1.8.9
./gradlew setupCiWorkspace
./gradlew build
git checkout 1.9-1.10.2
./gradlew setupCiWorkspace
./gradlew build