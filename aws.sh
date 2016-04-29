#!/usr/bin/env bash

#**************************************************************************************************
# AWS DEPLOY
# $1: [cmd]: the command to execute {configure|synchronize|deploy}
# $2: [ec2]: EC2 public DNs address
# $3: (pem): 
#**************************************************************************************************

#**************************************************************************************************
# INIT
#**************************************************************************************************
function init {

	if [ $# -ne 2 ] && [ $# -ne 3 ]; then
		echo "Usage: $(basename "${BASH_SOURCE[0]}") [cmd:{configure|synchronize|deploy}] [ec2] (pem)"
        exit -1
	fi

	if [ "$1" != "configure" ] && 
		[ "$1" != "synchronize" ] &&
		[ "$1" != "deploy" ]; then
		echo "Usage: $(basename "${BASH_SOURCE[0]}") [cmd:{configure|synchronize|deploy}] [ec2] (pem)"
        exit -1
	fi

	_WSDIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

	_USR="admin"

	_CMD=$1
	_EC2=$2
	_PEM="/home/$USER/.ssh/debs.pem"
	_LDIR="."
	_RDIR="/home/${_USR}/project"
	_EXCL="${_WSDIR}/vm/script/rsync-ignore"

	if [ $3 ]; then
		_PEM=$3
	fi	
}

#**************************************************************************************************
# DEINIT
#**************************************************************************************************
function deinit {
	unset _CMD
	unset _WSDIR
	unset _EC2
	unset _PEM
	unset _LDIR
	unset _RDIR
	unset _EXCL

	unset _BASE_URL
}

#**************************************************************************************************
# CONFIGURE
#**************************************************************************************************
function configureEC2 {
	_BASE_URL="http://gmarciani.com/workspace/debs-2016/script"
	_SCRIPTS=("configure-env.sh" "configure-app.sh" "check.sh")

	for _script in ${_SCRIPTS[@]}; do
		ssh -i "${_PEM}" ${_USR}@${_EC2} "rm \$HOME/${_script}"
		ssh -i "${_PEM}" ${_USR}@${_EC2} "wget ${_BASE_URL}/${_script} \$HOME"
		ssh -i "${_PEM}" ${_USR}@${_EC2} "chmod +x \$HOME/${_script}"
	done

	ssh -i "${_PEM}" ${_USR}@${_EC2} "bash \$HOME/configure-env.sh aws"
}

#**************************************************************************************************
# SYNCHRONIZE
#**************************************************************************************************
function synchronizeAppOnEC2 {
	rsync -rave "ssh -i ${_PEM}" --exclude-from "${_EXCL}" "${_LDIR}" ${_USR}@${_EC2}:"${_RDIR}"
	ssh -i "${_PEM}" ${_USR}@${_EC2} "cp ${_RDIR}/conf/flink-conf.yaml /opt/apache/flink-v1.0.0/conf/flink-conf.yaml"
}

#**************************************************************************************************
# DEPLOY
#**************************************************************************************************
function deployAppOnEC2 {
	rsync -rave "ssh -i ${_PEM}" --exclude-from "${_EXCL}" "${_LDIR}" ${_USR}@${_EC2}:"${_RDIR}"
	ssh -i "${_PEM}" ${_USR}@${_EC2} "wget http://gmarciani.com/workspace/debs-2016/script/{configure-env.sh,configure-app.sh,check.sh}"
	ssh -i "${_PEM}" ${_USR}@${_EC2} "chmod +x configure-env.sh configure-app.sh check.sh"
	ssh -i "${_PEM}" ${_USR}@${_EC2} "bash \$HOME/configure-app.sh local"
}

#**************************************************************************************************
# BODY
#**************************************************************************************************
init "$@"

if [ "$_CMD" == "configure" ]; then
	echo "[DEBS]> AWS: configuring EC2 instance running at ${_EC2} with key ${_PEM} ..."
	configureEC2
	echo "[DEBS]> AWS: EC2 instance running at ${_EC2} configured"
elif [ "$_CMD" == "synchronize" ]; then
	echo "[DEBS]> AWS: synchronizing project to EC2 instance running at ${_EC2} with key ${_PEM} ..."
	synchronizeAppOnEC2
	echo "[DEBS]> AWS: app synchronized on EC2 instance running at ${_EC2}"
elif [ "$_CMD" == "deploy" ]; then
	echo "[DEBS]> AWS: deploying project to EC2 instance running at ${_EC2} with key ${_PEM} ..."
	deployAppOnEC2
	echo "[DEBS]> AWS: app deployed on EC2 instance running at ${_EC2}"
fi

deinit