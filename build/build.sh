#!/bin/sh
if [ "$1" != "" ]; then
  args=$@;
else
  args="run";
fi
./build/build.py \
  --connection-timeout=15 \
  --socket-timeout=15 \
  --promiscuous-ssl=on \
  --name="Ready to check" \
  --results-title="Showing results" \
  --page-template="xml-src/NuPageEmitter.xml" \
  --form-template="xml-src/NuFormEmitter.xml" \
  --about-file="site/nu-about.html" \
  --script-file="site/nu-script.js" \
  --stylesheet-file="site/nu-style.css" \
  $args
