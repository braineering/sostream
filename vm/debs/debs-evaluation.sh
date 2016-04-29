#!/usr/bin/env bash

#**************************************************************************************************
# DEBS EVALUATION PLATFORM SCRIPT
#
# $1: [url]: the VM download url
#**************************************************************************************************

WSDIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
TMP_DIR="${WSDIR}/tmp"

VM_URL="$1"
VM_NAME="debs"
VM_USER="debs"
VM_HOME="/home/${VM_USER}"

VM_ZIP="vm.zip"
VM_OVA="${VM_NAME}.ova"

if [ ! -d ${TMP_DIR} ]; then
	mkdir ${TMP_DIR}
fi

wget ­-O ${TMP_DIR}/${VM_ZIP} ${VM_URL}

unzip ${TMP_DIR}/${VM_ZIP} ­d ${TMP_DIR} 
VBoxManage import ${TMP_DIR}/${VM_OVA} 
VBoxManage modifyvm ${VM_NAME} ­­natpf1 guestssh,tcp,,2222,,22 
VBoxManage modifyvm ${VM_NAME} ­­memory 8192 ­­cpus 4 ­­cpuhotplug off 
VBoxManage startvm ${VM_NAME} ­­type headless 
 
rsync ­e 'ssh ­p 2222' input_files/data/*.dat ${VM_USER}@127.0.0.1:/home/${VM_USER}/ 
timeout 3600 ssh ­p 2222 ${VM_USER}@127.0.0.1 'cd /home/${VM_USER}/; ./run.sh friendships.dat posts.dat comments.dat likes.dat 3 7200' 
rsync ­e 'ssh ­p 2222' ${VM_USER}@127.0.0.1:/home/${VM_USER}/q1.txt ${TMP_DIR}/1_q1.txt 
rsync ­e 'ssh ­p 2222' ${VM_USER}@127.0.0.1:/home/${VM_USER}/q2.txt ${TMP_DIR}/1_q2.txt 
rsync ­e 'ssh ­p 2222' ${VM_USER}@127.0.0.1:/home/${VM_USER}/log.txt ${TMP_DIR}/1_log.txt 
rsync ­e 'ssh ­p 2222' ${VM_USER}@127.0.0.1:/home/${VM_USER}/results.txt ${TMP_DIR}/1_results.txt 

VBoxManage controlvm ${VM_NAME} poweroff 
VBoxManage unregistervm ${VM_NAME} ­delete 
rm ${TMP_DIR}/${VM_OVA}