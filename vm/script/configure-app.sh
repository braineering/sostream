#!/usr/bin/env bash

#**************************************************************************************************
# APP CONFIGURATION
#
# $1 [src]:    the code repo {local|remote}
# $2 (branch): the repo's branch name to clone (mandatory only for src=remote)
#**************************************************************************************************

#**************************************************************************************************
# INIT
#**************************************************************************************************
function init {
    if [ $# -ne 1 ] && [ $# -ne 2 ]; then
        echo "Usage: $(basename "${BASH_SOURCE[0]}") [src:{local|remote}] (branch)"
        exit -1
    fi

    SRC=$1
    BRANCH=$2

    if [ "$SRC" == "local" ]; then 
        echo "[DEBS]> CONFIGURE-APP: local app building selected"
    elif [ "$SRC" == "remote" ] && [ $BRANCH ]; then
        echo "[DEBS]> CONFIGURE-APP: remote app building selected"
    else
        echo "[DEBS]> CONFIGURE-APP: cannot build remote app with branch $BRANCH"
        echo "Usage: $(basename "${BASH_SOURCE[0]}") [src:{local|remote}] (branch)"
        exit -1
    fi
}

#**************************************************************************************************
# DEINIT
#**************************************************************************************************
function deinit {
    unset ENVIRONMENT
    unset SRC
    unset BRANCH

    unset SSH_DIR
    unset SSH_BITBUCKET_KEY
    unset SSH_BITBUCKET_SOURCE
    unset SSH_DEPLOY_GIT_REPO
    unset SSH_DEPLOY_KEY_MATCH
    unset SSH_CONFIG_FILE
    unset SSH_CONFIG
    unset ATTEMPTS

    unset SSH_GIT_REPO
    unset PROJECT_DIR
    unset QUERY_DIR_SRC
    unset QUERY_DIR_DST
    unset COMPILE_SCRIPT
    unset RUN_FILE_SRC
    unset RUN_FILE_DST
    unset QUERIES
}

#**************************************************************************************************
# BITBUCKET
#**************************************************************************************************
function checkGithub {
    SSH_DEPLOY_KEY_MATCH="3Cores/sostream: debs@debian"

    if [[ ! $(ssh -T -oStrictHostKeyChecking=no git@github.com | grep "$SSH_DEPLOY_KEY_MATCH") ]]; then
        echo "[DEBS]> CONFIGURE-APP: cannot read Bitbucket repo. Please, run again configure-env.sh $SRC $BRANCH"
        exit -1
    fi
}

#**************************************************************************************************
# DEBS PROJECT
#**************************************************************************************************
PROJECT_DIR="$HOME/project"
QUERY_DIR_SRC="$PROJECT_DIR/query"
QUERY_DIR_DST="$HOME/query"
COMPILE_SCRIPT="$PROJECT_DIR/compile.sh"
RUN_FILE_SRC="$PROJECT_DIR/run.sh"
RUN_FILE_DST="$HOME/run.sh"

function cleanProject {
    if [ -d $QUERY_DIR_DST ]; then
        rm -rf $QUERY_DIR_DST
    fi
    if [ -f $RUN_FILE_DST ]; then
        rm $RUN_FILE_DST
    fi
}

function cloneProject {
    SSH_GIT_REPO="git@github.com:3Cores/sostream.git"

    if [ -d $PROJECT_DIR ]; then
        rm -rf $PROJECT_DIR
    fi    

    git clone $SSH_GIT_REPO --branch $BRANCH $PROJECT_DIR
}

function buildProject {
    FLINK_CONF_SRC="$PROJECT_DIR/conf/flink-conf.yaml"
    FLINK_CONF_DST="/opt/apache/flink-v1.0.0/conf/flink-conf.yaml"
    FLINK_CONF_DEF="/opt/apache/flink-v1.0.0/conf/flink-conf-default.yaml"
    
    bash $COMPILE_SCRIPT
    cp -rf $QUERY_DIR_SRC $QUERY_DIR_DST
    cp $RUN_FILE_SRC $RUN_FILE_DST
    chmod +x $RUN_FILE_DST
    cp $FLINK_CONF_SRC $FLINK_CONF_DST
}

function configureLocal {
    echo "[DEBS]> CONFIGURE-APP: cleaning workspace ..."
    cleanProject
    echo "[DEBS]> CONFIGURE-APP: workspace cleaned"
    
    echo "[DEBS]> CONFIGURE-APP: building project ..."
    buildProject
    echo "[DEBS]> CONFIGURE-APP: project built"
}

function configureRemote {
    echo "[DEBS]> CONFIGURE-APP: configuring Bitbucket repo ..."
    #checkBitbucket
    echo "[DEBS]> CONFIGURE-APP: read access to repo configured"

    echo "[DEBS]> CONFIGURE-APP: cleaning workspace ..."
    cleanProject
    echo "[DEBS]> CONFIGURE-APP: workspace cleaned"

    echo "[DEBS]> CONFIGURE-APP: cloning Bitbucket repo (branch: $BRANCH) ..."
    cloneProject
    echo "[DEBS]> CONFIGURE-APP: Bitbucket repo (branch: $BRANCH) cloned"  

    echo "[DEBS]> CONFIGURE-APP: building project ..."
    buildProject
    echo "[DEBS]> CONFIGURE-APP: project built"
}


#**************************************************************************************************
# BODY
#**************************************************************************************************
init "$@"

if [ "$SRC" == "local" ]; then
    echo "[DEBS]> CONFIGURE-APP: configuring local project ..."
    configureLocal
    echo "[DEBS]> CONFIGURE-APP: local project configured"
elif [ "$SRC" == "remote" ]; then
    echo "[DEBS]> CONFIGURE-APP: configuring remote project with branch $BRANCH ..."
    configureRemote
    echo "[DEBS]> CONFIGURE-APP: remote project configured"
fi

deinit