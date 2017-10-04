#!/bin/sh

cd `dirname $0`
script_dir=`pwd`
cd - > /dev/null

java -Djava.util.logging.config.file="$script_dir/logging.properties" -cp "$script_dir/zxs_udg_editor.jar" cz.mp.zxs.tools.udg_editor.UdgFilePreviewFrame "$script_dir/udg_data_examples.txt"
