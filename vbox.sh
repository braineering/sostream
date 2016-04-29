#!/usr/bin/env bash

#**************************************************************************************************
# VBOX DEPLOY
# $1: [cmd]
#**************************************************************************************************

#**************************************************************************************************
# INIT
#**************************************************************************************************
function init {

	if [ $# -ne 1 ]; then
		echo "Usage: $(basename "${BASH_SOURCE[0]}") [cmd:{configure|deploy}]"
        exit -1
	fi

	if [ "$1" != "configure" ] && [ "$1" != "deploy" ]; then
		echo "Usage: $(basename "${BASH_SOURCE[0]}") [cmd:{configure|deploy}]"
        exit -1
	fi

	_WSDIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

	_USR="debs"

	_CMD=$1
	_LDIR="."
	_RDIR="/home/${_USR}/project"
	_EXCL="${_WSDIR}/vm/script/rsync-ignore"
	_PORT=3333
}

#**************************************************************************************************
# DEINIT
#**************************************************************************************************
function deinit {
	unset _CMD
	unset _WSDIR
	unset _VM
	unset _LDIR
	unset _RDIR
	unset _EXCL
	unset _PORT

	unset _BASE_URL
}

#**************************************************************************************************
# CONFIGURE
#**************************************************************************************************
function configureVBox {
	_BASE_URL="http://gmarciani.com/workspace/debs-2016/script"
	_SCRIPTS=("configure-env.sh" "configure-app.sh" "check.sh")

	for _script in ${_SCRIPTS[@]}; do
		ssh -p ${_PORT} ${_USR}@localhost "rm \$HOME/${_script}"
		ssh -p ${_PORT} ${_USR}@localhost "wget ${_BASE_URL}/${_script} \$HOME"
		ssh -p ${_PORT} ${_USR}@localhost "chmod +x \$HOME/${_script}"
	done

	ssh -p ${_PORT} ${_USR}@localhost "bash configure-env.sh vbox"
}

#**************************************************************************************************
# DEPLOY
#**************************************************************************************************
function deployAppOnVBox {
	rsync -rave "ssh -p ${_PORT} " --exclude-from "${_EXCL}" "${_LDIR}" ${_USR}@localhost:"${_RDIR}" # to be tested
	ssh -p ${_PORT} ${_USR}@localhost "cp project/vm/script/{configure-env.sh,configure-app.sh,check.sh} ./"
	ssh -p ${_PORT} ${_USR}@localhost "bash configure-app.sh local"
}

#**************************************************************************************************
# BODY
#**************************************************************************************************
init "$@"

if [ "$_CMD" == "configure" ]; then
	echo "[DEBS]> VBOX: configuring VirtualBox instance running at localhost on port ${_PORT} ..."
	configureVBox
	echo "[DEBS]> VBOX: VirtualBox instance running at localhost configured"
elif [ "$_CMD" == "deploy" ]; then
	echo "[DEBS]> VBOX: deploying project to VirtualBox instance running at localhost on port ${_PORT} ..."
	deployAppOnVBox
	echo "[DEBS]> VBOX: app deployed on VirtualBox instance running at localhost"
fi

deinit