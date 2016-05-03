#!/usr/bin/env bash

#**************************************************************************************************
# ENVIRONMENT/APP VERIFICATION
#
# $1 [env]: the environment {vbox|aws}
#**************************************************************************************************

#**************************************************************************************************
# INIT
#**************************************************************************************************
function init {
    if [ $# -ne 1 ]; then
        echo "Usage: $(basename "${BASH_SOURCE[0]}") [env:{vbox|aws}]"
        exit -1
    fi

    ENVIRONMENT=$1

    if [ "$ENVIRONMENT" == "vbox" ]; then 
        echo "[DEBS]> CHECK: VirtualBox environment selected"
        USR=debs
    elif [ "$ENVIRONMENT" == "aws" ]; then
        echo "[DEBS]> CHECK: AWS environment selected"
        USR=admin
    else
        echo "[DEBS]> CHECK: cannot configure app for environment $ENVIRONMENT"
        echo "Usage: $(basename "${BASH_SOURCE[0]}") [env:{vbox|aws}]"
        exit -1
    fi

    WSDIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
    ORACLE_DIR="/opt/oracle"
    APACHE_DIR="/opt/apache"
    REDISLAB_DIR="/opt/redislab"

    PRBLMS=0
    PRBLMS_TOT=0

    function partialCheckResult {
        if [ $PRBLMS -gt 0 ]; then
            echo "[DEBS]> CHECK: $1: $PRBLMS problem(s) found"
            PRBLMS_TOT=$(($PRBLMS_TOT + $PRBLMS))
        else
            echo "[DEBS]> CHECK: $1: everything ok"
        fi        
        PRBLMS=0
    }

    function totalCheckResult {
        if [ $PRBLMS_TOT -gt 0 ]; then
            echo "[DEBS]> CHECK: completed with $PRBLMS_TOT problem(s)"
        else
            echo "[DEBS]> CHECK: completed with no problems, everything ok"
        fi
    }
}

#**************************************************************************************************
# DEINIT
#**************************************************************************************************
function deinit {
    unset ENVIRONMENT
    unset USR
    unset WSDIR
    unset ORACLE_DIR
    unset APACHE_DIR
    unset REDISLAB_DIR
    unset PRBLMS
    unset PRBLMS_TOT

    unset AUTOLOGIN_SERVICE
    unset AUTOLOGIN_SERVICE_DIR
    unset AUTOLOGIN_SERVICE_FILE
    unset AUTOLOGIN_SERVICE_CONTENT

    unset JAVA
    unset JAVA_VERSION
    unset JAVA_VERSION_RQST
    unset JAVA_DIR

    unset MAVEN
    unset MAVEN_VERSION
    unset MAVEN_VERSION_RQST
    unset MAVEN_DIR

    unset FLINK
    unset FLINK_CLUSTER_START
    unset FLINK_CLUSTER_STOP
    unset FLINK_VERSION
    unset FLINK_VERSION_RQST
    unset FLINK_DIR

    unset REDIS_SERVER
    unset REDIS_CLIENT
    unset REDIS_VERSION
    unset REDIS_VERSION_RQST
    unset REDIS_DIR

    unset SSH_DEPLOY_KEY_MATCH

    unset QUERY_DIR
    unset RUN_FILE
    unset CONFIGURE_ENV_FILE
    unset CONFIGURE_APP_FILE
    unset QUERIES
    unset XFILES
    unset JFILE
}

#**************************************************************************************************
# SOURCES
#**************************************************************************************************
function checkSources {
    if [ ! $(grep "^deb cdrom*" /etc/apt/sources.list) ]; then
        echo "[DEBS]> CHECK: SOURCES: everything ok"
    else
        echo "[DEBS]> CHECK: SOURCES: 1 problem found (cdrom should not be a package source)"
        PRBLMS=$(($PRBLMS + 1)) 
    fi
}

#**************************************************************************************************
# PACKAGES
#**************************************************************************************************
function checkPackages {
    for PKG in "$@"; do
        if [[ $(dpkg -l $PKG | grep "\b$PKG\b") ]]; then
            echo "[DEBS]> CHECK: PACKAGES: $PKG installed"
        else
            echo "[DEBS]> CHECK: PACKAGES: $PKG not installed"
            PRBLMS=$(($PRBLMS + 1))
        fi
    done
}

#**************************************************************************************************
# USER
#**************************************************************************************************
function checkMembership {
    for GRP in "$@"; do
        if [[ $(groups $USR | grep "\b$GRP\b") ]]; then
            echo "[DEBS]> CHECK: USER: $USR member of $GRP"
        else
            echo "[DEBS]> CHECK: USER: $USR not a member of $GRP"
            PRBLMS=$(($PRBLMS + 1))
        fi    
    done
}

function checkOwnership {    
    for FLD in "$@"; do
        if [[ $(stat -c '%U' $FLD) == $USR ]]; then
            echo "[DEBS]> CHECK: USER: $USR owner of $FLD"
        else
            echo "[DEBS]> CHECK: USER: $USR not owner of $FLD"
            PRBLMS=$(($PRBLMS + 1))
        fi
    done
}

function checkAutologin {
    AUTOLOGIN_SERVICE="getty@tty1.service"
    AUTOLOGIN_SERVICE_DIR="/etc/systemd/system/$AUTOLOGIN_SERVICE.d"
    AUTOLOGIN_SERVICE_FILE="$AUTOLOGIN_SERVICE_DIR/autologin.conf"
    AUTOLOGIN_SERVICE_CONTENT="[Service]\nExecStart=\nExecStart=-/sbin/agetty --autologin $1 --noclear %I 38400 linux"

    if [ -f $AUTOLOGIN_SERVICE_FILE ] && 
        [ "$(cat $AUTOLOGIN_SERVICE_FILE)" == "$(echo -e $AUTOLOGIN_SERVICE_CONTENT)"  ]; then
        echo "[DEBS]> CHECK: SERVICES: autologin found"
        if [ $(systemctl is-enabled $AUTOLOGIN_SERVICE) == "enabled" ]; then
            echo "[DEBS]> CHECK: SERVICES: autologin enabled"
        else
            echo "[DEBS]> CHECK: SERVICES: autologin disabled"
            PRBLMS_TOT=$(($PRBLMS_TOT + 1))
        fi
    else
        echo "[DEBS]> CHECK: SERVICES: autologin not found"  
        PRBLMS=$(($PRBLMS + 1)) 
    fi
}

#**************************************************************************************************
# OPT: JAVA
#**************************************************************************************************
function checkJava {
    JAVA=java
    JAVA_VERSION=""
    JAVA_VERSION_RQST=1.8.0_73
    JAVA_DIR="jdk-v1.8.0_73"

    if type -p $JAVA; then
        echo "[DEBS]> CHECK: OPT: JAVA: ($JAVA) found in PATH"
    elif [ -n "$JAVA_HOME" ] && 
         [ -x "$JAVA_HOME/bin/$JAVA" ];  then
        echo "[DEBS]> CHECK: OPT: JAVA: ($JAVA) found in JAVA_HOME"     
        JAVA="$JAVA_HOME/bin/$JAVA"
    else
        echo "[DEBS]> CHECK: OPT: JAVA: ($JAVA) not found"
        JAVA=
        PRBLMS=$(($PRBLMS + 1)) 
    fi

    if [ $JAVA ]; then
        JAVA_VERSION=$("$JAVA" -version 2>&1 | awk -F '"' '/version/ {print $2}')
        if [ "$JAVA_VERSION" == $JAVA_VERSION_RQST ]; then
            echo "[DEBS]> CHECK: OPT: JAVA: version found ($JAVA_VERSION) correct (should be $JAVA_VERSION_RQST)"
        else         
            echo "[DEBS]> CHECK: OPT: JAVA: version found ($JAVA_VERSION) not correct (should be $JAVA_VERSION_RQST)"
            PRBLMS=$(($PRBLMS + 1)) 
        fi
    fi
}

#**************************************************************************************************
# OPT: MAVEN
#**************************************************************************************************
function checkMaven {
    MAVEN=mvn
    MAVEN_VERSION=
    MAVEN_VERSION_RQST=3.3.9
    MAVEN_DIR="maven-v3.3.9"

    if type -p $MAVEN; then
        echo "[DEBS]> CHECK: OPT: MAVEN: ($MAVEN) found in PATH"
    elif [ -n "$MAVEN_HOME" ] && 
         [ -x "$MAVEN_HOME/bin/$MAVEN" ];  then
        echo "[DEBS]> CHECK: OPT: MAVEN: ($MAVEN) found in MAVEN_HOME"  
        MAVEN="$MAVEN_HOME/bin/$MAVEN"
    else
        echo "[DEBS]> CHECK: OPT: MAVEN: ($MAVEN) not found"
        MAVEN=
        PRBLMS=$(($PRBLMS + 1)) 
    fi

    if [ $MAVEN ]; then
        MAVEN_VERSION=$("$MAVEN" -version 2>&1 | awk -F ' ' '/Apache Maven/ {print $3}')
        if [ "$MAVEN_VERSION" == $MAVEN_VERSION_RQST ]; then
            echo "[DEBS]> CHECK: OPT: MAVEN: version found ($MAVEN_VERSION) correct (should be $MAVEN_VERSION_RQST)"
        else         
            echo "[DEBS]> CHECK: OPT: MAVEN: version found ($MAVEN_VERSION) not correct (should be $MAVEN_VERSION_RQST)"
            PRBLMS=$(($PRBLMS + 1)) 
        fi
    fi
}

#**************************************************************************************************
# OPT: FLINK
#**************************************************************************************************
function checkFlink {
    FLINK=flink
    FLINK_CLUSTER_START=start-local.sh 
    FLINK_CLUSTER_STOP=stop-local.sh
    FLINK_VERSION=
    FLINK_VERSION_RQST=2.10-1.0.0
    FLINK_DIR="flink-v1.0.0"

    if type -p $FLINK; then
        echo "[DEBS]> CHECK: OPT: FLINK: ($FLINK, $FLINK_CLUSTER_START $FLINK_CLUSTER_STOP) found in PATH"
    elif [ -n "$FLINK_HOME" ] && 
         [ -x "$FLINK_HOME/bin/$FLINK" ] &&
         [ -x "$FLINK_HOME/bin/$FLINK_CLUSTER_START" ] &&
         [ -x "$FLINK_HOME/bin/$FLINK_CLUSTER_STOP" ]; then
        echo "[DEBS]> CHECK: OPT: FLINK: ($FLINK, $FLINK_CLUSTER_START $FLINK_CLUSTER_STOP) found in FLINK_HOME"     
        FLINK="$FLINK_HOME/bin/$FLINK"
        FLINK_CLUSTER_START="$FLINK_HOME/bin/$FLINK_CLUSTER_START"
        FLINK_CLUSTER_STOP="$FLINK_HOME/bin/$FLINK_CLUSTER_STOP"
    else
        echo "[DEBS]> CHECK: OPT: FLINK: ($FLINK, $FLINK_CLUSTER_START $FLINK_CLUSTER_STOP) not found"
        FLINK=""
        PRBLMS=$(($PRBLMS + 1)) 
    fi

    if [ $FLINK ]; then
        FLINK_VERSION=$(ls "$APACHE_DIR/$FLINK_DIR/lib" | grep "flink-dist" | sed "s/^flink-dist_\(.*\).jar$/\1/")
        if [ "$FLINK_VERSION" == $FLINK_VERSION_RQST ]; then
            echo "[DEBS]> CHECK: OPT: FLINK: version found ($FLINK_VERSION) correct (should be $FLINK_VERSION_RQST)"
        else         
            echo "[DEBS]> CHECK: OPT: FLINK: version found ($FLINK_VERSION) not correct (should be $FLINK_VERSION_RQST)"
            PRBLMS=$(($PRBLMS + 1)) 
        fi
    fi
}

#**************************************************************************************************
# OPT: REDIS
#**************************************************************************************************
function checkRedis {
    REDIS_SERVER=redis-server
    REDIS_CLIENT=redis-cli
    REDIS_VERSION=""
    REDIS_VERSION_RQST=3.0.7
    REDIS_DIR="redis-v3.0.7"

    if type -p $REDIS_SERVER && type -p $REDIS_CLIENT && 
        [ -x "/etc/init.d/redis_6379" ]; then
        echo "[DEBS]> CONFIGURE-ENV: OPT: REDIS: ($REDIS_SERVER, $REDIS_CLIENT) found in PATH"
    elif [ -n "$REDIS_HOME" ] && 
         [ -x "$REDIS_HOME/bin/$REDIS_SERVER" ] &&
         [ -x "$REDIS_HOME/bin/$REDIS_CLIENT" ] &&
         [ -x "/etc/init.d/redis_6379" ]; then
        echo "[DEBS]> CONFIGURE-ENV: OPT: REDIS: ($REDIS_SERVER, $REDIS_CLIENT) found in REDIS_HOME"     
        REDIS_SERVER="$REDIS_HOME/bin/$REDIS_SERVER"
        REDIS_CLIENT="$REDIS_HOME/bin/$REDIS_CLIENT"        
    else
        echo "[DEBS]> CONFIGURE-ENV: OPT: REDIS: ($REDIS_SERVER, $REDIS_CLIENT) not found"

        REDIS_SERVER=
        REDIS_CLIENT=
        PRBLMS=$(($PRBLMS + 1))
    fi

    if [ $REDIS_SERVER ]; then
        REDIS_VERSION=$($REDIS_SERVER -v 2>&1 | awk -F ' ' '/Redis server/ {print $3}' | awk -F '=' '/v/ {print $2}')
        if [ "$REDIS_VERSION" == $REDIS_VERSION_RQST ]; then
            echo "[DEBS]> CHECK: OPT: REDIS: version found ($REDIS_VERSION) correct (should be $REDIS_VERSION_RQST)"
        else         
            echo "[DEBS]> CHECK: OPT: REDIS: version found ($REDIS_VERSION) not correct (should be $REDIS_VERSION_RQST)"
            PRBLMS=$(($PRBLMS + 1)) 
        fi
    fi
}

#**************************************************************************************************
# ENVIRONMENT VARIABLES
#**************************************************************************************************
function checkEnvironmentVariable {
    eval _VAR=\$$1
    if [ -z $_VAR ]; then
        echo "[DEBS]> CHECK: VAR: $1 not set"    
    else
        if [ $_VAR == $2 ]; then
            echo "[DEBS]> CHECK: VAR: $1 correctly set"
        else
            echo "[DEBS]> CHECK: VAR: $1 not correctly set (sould be $2)"
            PRBLMS=$(($PRBLMS + 1)) 
        fi
    fi

    if [[ $PATH == *"$2/bin"* ]]; then
        echo "[DEBS]> CHECK: VAR: $1 in PATH"
    else
        echo "[DEBS]> CHECK: VAR: $1 not in PATH"
        PRBLMS=$(($PRBLMS + 1)) 
    fi
}

#**************************************************************************************************
# SSH
#**************************************************************************************************
SSH_DIR="$HOME/.ssh"
SSH_AUTHORIZED_KEYS_FILE="$SSH_DIR/authorized_keys"

function checkSSHGithub {
    SSH_DEPLOY_KEY_MATCH="Hi 3Cores"

    if [ "$(echo "$(ssh -T -oStrictHostKeyChecking=no git@github.com 2>&1)" | grep -o "$SSH_DEPLOY_KEY_MATCH")" ]; then
        echo "[DEBS]> CHECK: SSH: ensured read access to repo"
    else
        echo "[DEBS]> CHECK: SSH: 1 problem(s) found: cannot read Github repo"
        PRBLMS=$(($PRBLMS + 1)) 
    fi
}

function checkSSHDevelopers {
    SSH_DEVELOPERS_KEY_MATCH="gmarciani@debs.dev"
    if [[ $(cat $SSH_AUTHORIZED_KEYS_FILE | grep "$SSH_DEVELOPERS_KEY_MATCH") ]]; then
        echo "[DEBS]> CHECK: SSH: verified authorized key for developers"
    else
        echo "[DEBS]> CHECK: SSH: 1 problem(s) found: cannot read SSH authorized keys for developers"
        PRBLMS=$(($PRBLMS + 1)) 
    fi
}

function checkSSHDEBS {
    SSH_DEBS_KEY_MATCH="admin_b@Precision-Tower-7810"
    if [[ $(cat $SSH_AUTHORIZED_KEYS_FILE | grep "$SSH_DEBS_KEY_MATCH") ]]; then
        echo "[DEBS]> CHECK: SSH: verified authorized key for DEBS"
    else
        echo "[DEBS]> CHECK: SSH: 1 problem(s) found: cannot read SSH authorized keys for DEBS"
        PRBLMS=$(($PRBLMS + 1)) 
    fi
}

#**************************************************************************************************
# DEBS PROJECT
#**************************************************************************************************
function checkProject {
    QUERY_DIR="$WSDIR/query"
    RUN_FILE="$WSDIR/run.sh"
    CONFIGURE_ENV_FILE="$WSDIR/configure-env.sh"
    CONFIGURE_APP_FILE="$WSDIR/configure-app.sh"

    QUERIES=("q1" "q2")

    XFILES=($RUN_FILE $CONFIGURE_ENV_FILE $CONFIGURE_APP_FILE)
    JFILE=""

    echo "[DEBS]> CHECK: PROJECT: checking JAR files ..."

    for Q in ${QUERIES[@]}; do
        JFILE="$QUERY_DIR/$Q.jar"
        if [ -r $JFILE ]; then
            echo "[DEBS]> CHECK: PROJECT: $JFILE found and readable"
        else
            echo "[DEBS]> CHECK: PROJECT: $JFILE not found or unreadable"
            PRBLMS=$(($PRBLMS + 1))
        fi
    done

    echo "[DEBS]> CHECK: PROJECT: checking SH files ..."

    for XFILE in ${XFILES[@]}; do
        if [ -x $XFILE ]; then
            echo "[DEBS]> CHECK: PROJECT: $XFILE found and executable"
        else
            echo "[DEBS]> CHECK: PROJECT: $XFILE not found or not executable"
            PRBLMS=$(($PRBLMS + 1))
        fi
    done
}

#**************************************************************************************************
# BODY
#**************************************************************************************************
init "$@"

echo "[DEBS]> CHECK: SOURCES: checking sources list ..."
checkSources
echo "[DEBS]> CHECK: SOURCES: sources list checked"
partialCheckResult "SOURCES"

echo "[DEBS]> CHECK: PACKAGES: checking ..."
checkPackages build-essential module-assistant dkms vim htop git ssh tcl zip unzip
echo "[DEBS]> CHECK: PACKAGES: checked"
partialCheckResult "PACKAGES"

echo "[DEBS]> CHECK: USER: checking memberhsip for user $USR ..."
checkMembership sudo
echo "[DEBS]> CHECK: USER: memberhsip for user $USR checked"
echo "[DEBS]> CHECK: USER: checking ownership for user $USR ..."
checkOwnership $ORACLE_DIR $APACHE_DIR $REDISLAB_DIR
echo "[DEBS]> CHECK: USER: ownership for user $USR checked"
partialCheckResult "USER"

echo "[DEBS]> CHECK: OPT: checking optional software ..."
checkJava
checkMaven
checkFlink
checkRedis
echo "[DEBS]> CHECK: OPT: optional software checked"
partialCheckResult "OPT"

echo "[DEBS]> CHECK: VAR: checking environment variables ..."
checkEnvironmentVariable "JAVA_HOME" "$ORACLE_DIR/$JAVA_DIR"
checkEnvironmentVariable "MAVEN_HOME" "$APACHE_DIR/$MAVEN_DIR"
checkEnvironmentVariable "FLINK_HOME" "$APACHE_DIR/$FLINK_DIR"
checkEnvironmentVariable "REDIS_HOME" "$REDISLAB_DIR/$REDIS_DIR"
echo "[DEBS]> CHECK: VAR: environment variables checked"
partialCheckResult "VAR"

echo "[DEBS]> CHECK: SSH: checking read access to Github repo ..."
checkSSHGithub
echo "[DEBS]> CHECK: SSH: read access to Bitbucket repo checked"

if [ "$ENVIRONMENT" == "vbox" ]; then 
    echo "[DEBS]> CHECK: SSH: checking SSH access for developers ..."
    checkSSHDevelopers
    echo "[DEBS]> CHECK: SSH: SSH access for developers checked"
    echo "[DEBS]> CHECK: SSH: checking SSH access for DEBS ..."
    checkSSHDEBS
    echo "[DEBS]> CHECK: SSH: SSH access for DEBS checked"
fi
partialCheckResult "SSH"

echo "[DEBS]> CHECK: PROJECT: checking project structure ..."
checkProject
echo "[DEBS]> CHECK: PROJECT: project structure checked"
partialCheckResult "PROJECT"

totalCheckResult

deinit