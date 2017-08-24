#!/bin/sh

cd `dirname $0`
script_dir=`pwd`
cd - > /dev/null

java -Dswing.defaultlaf=com.sun.java.swing.plaf.gtk.GTKLookAndFeel -jar "$script_dir/zxs_udg_editor.jar"
