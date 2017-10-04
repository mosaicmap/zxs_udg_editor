#!/bin/sh

cd `dirname $0`
script_dir=`pwd`
cd - > /dev/null

java -Djava.util.logging.config.file="$script_dir/logging.properties" -Dswing.defaultlaf=com.sun.java.swing.plaf.gtk.GTKLookAndFeel -jar "$script_dir/zxs_udg_editor.jar"
