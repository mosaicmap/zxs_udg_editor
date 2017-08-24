#!/bin/sh

cd `dirname $0`
script_dir=`pwd`
cd - > /dev/null

java -cp "$script_dir/zxs_udg_editor.jar" cz.mp.zxs.tools.udg_editor.UdgFilePreviewFrame
