#!/bin/sh

cd `dirname $0`
script_dir=`pwd`
cd - > /dev/null

java -jar "$script_dir/zxs_udg_editor.jar"
