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
        echo "[DEBS]> CONFIGURE-ENV: VirtualBox environment selected"
        USR=debs
    elif [ "$ENVIRONMENT" == "aws" ]; then
        echo "[DEBS]> CONFIGURE-ENV: AWS environment selected"
        USR=admin
    else
        echo "[DEBS]> CONFIGURE-ENV: cannot configure app for environment $ENVIRONMENT"
        echo "Usage: $(basename "${BASH_SOURCE[0]}") [env:{vbox|aws}]"
        exit -1
    fi

    WSDIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
    ORACLE_DIR="/opt/oracle"
    APACHE_DIR="/opt/apache"
    REDISLAB_DIR="/opt/redislab"
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

    unset AUTOLOGIN_SERVICE
    unset AUTOLOGIN_SERVICE_NAME
    unset AUTOLOGIN_SERVICE_DIR
    unset AUTOLOGIN_SERVICE_FILE
    unset AUTOLOGIN_SERVICE_CONTENT

    unset JAVA
    unset JAVA_VERSION_RQST
    unset JAVA_URL
    unset JAVA_TAR
    unset JAVA_UNTAR
    unset JAVA_DIR

    unset MAVEN
    unset MAVEN_VERSION_RQST
    unset MAVEN_URL
    unset MAVEN_TAR
    unset MAVEN_UNTAR
    unset MAVEN_DIR

    unset FLINK
    unset FLINK_CLUSTER_START
    unset FLINK_CLUSTER_STOP
    unset FLINK_VERSION_RQST
    unset FLINK_URL
    unset FLINK_TAR
    unset FLINK_UNTAR
    unset FLINK_DIR

    unset REDIS_CLIENT
    unset REDIS_SERVER
    unset REDIS_VERSION_RQST
    unset REDIS_URL
    unset REDIS_TAR
    unset REDIS_UNTAR
    unset REDIS_DIR

    unset DATASET_URL
    unset DATASET_SRC
    unset DATASET_DST

    unset SSH_DIR
    unset SSH_CONFIG_FILE
    unset SSH_AUTHORIZED_KEYS_FILE
    unset SSH_LOCALHOST_KEY
    unset SSH_LOCALHOST_KEY_FILE
    unset SSH_LOCALHOST_KEY_URL
    unset SSH_DEBS_KEY
    unset SSH_DEBS_KEY_FILE
    unset SSH_DEBS_KEY_URL
    unset SSH_BITBUCKET_KEY
    unset SSH_BITBUCKET_KEY_URL
    unset SSH_DEPLOY_GIT_REPO
    unset SSH_DEPLOY_KEY_MATCH
    unset SSH_CONFIG_FILE
    unset SSH_CONFIG
    unset ATTEMPTS

    unset PROFILE_DIR
    unset ENV_FILE
    unset ENV_VARS_JAVA
    unset ENV_VARS_MAVEN
    unset ENV_VARS_FLINK
    unset ENV_VARS_REDIS
    unset ENV_PATH
    unset ENV_CONTENT
}

#**************************************************************************************************
# SOURCES
#**************************************************************************************************
function configureSources {
    sudo sed -i "/^deb cdrom:/s/^/# /" /etc/apt/sources.list
    sudo apt-get update -y
}

#**************************************************************************************************
# PACKAGES
#**************************************************************************************************
function installPackages {
    for PKG in "$@"; do
        sudo apt-get install -y $PKG
    done
}

function installGuestAdditions {
    sudo mount /media/cdrom
    sudo bash /media/cdrom/VBoxLinuxAdditions.run
}

#**************************************************************************************************
# OPT: JAVA
#**************************************************************************************************
function installJava {
    JAVA=java
    JAVA_VERSION_RQST=1.8.0_73
    JAVA_URL="http://download.oracle.com/otn-pub/java/jdk/8u73-b02/jdk-8u73-linux-x64.tar.gz"
    JAVA_TAR="jdk-8u73-linux-x64.tar.gz"
    JAVA_UNTAR="jdk1.8.0_73"
    JAVA_DIR="jdk-v1.8.0_73"

    if type -p $JAVA; then
        echo "[DEBS]> CONFIGURE-ENV: OPT: JAVA: ($JAVA) found in PATH"
    elif [ -n "$JAVA_HOME" ] && [ -x "$JAVA_HOME/bin/$JAVA" ];  then
        echo "[DEBS]> CONFIGURE-ENV: OPT: JAVA: ($JAVA) found in JAVA_HOME"
        JAVA="$JAVA_HOME/bin/$JAVA"
    else
        echo "[DEBS]> CONFIGURE-ENV: OPT: JAVA: ($JAVA) not found"

        if [ ! -d $ORACLE_DIR ]; then
            sudo mkdir -p $ORACLE_DIR
        fi

        wget --no-check-certificate --no-cookies --header "Cookie: oraclelicense=accept-securebackup-cookie" $JAVA_URL
        tar -zxf $JAVA_TAR
        rm $JAVA_TAR
        sudo mv $JAVA_UNTAR $ORACLE_DIR/$JAVA_DIR
        sudo update-alternatives --install /usr/bin/java java $ORACLE_DIR/$JAVA_DIR/bin/java 100
        sudo update-alternatives --install /usr/bin/javac javac $ORACLE_DIR/$JAVA_DIR/bin/javac 100
        sudo update-alternatives --install /usr/bin/javaws javaws $ORACLE_DIR/$JAVA_DIR/bin/javaws 100
    fi
}

#**************************************************************************************************
# OPT: MAVEN
#**************************************************************************************************
function installMaven {
    MAVEN=mvn
    MAVEN_VERSION_RQST=3.3.9
    MAVEN_URL="http://apache.panu.it/maven/maven-3/3.3.9/binaries/apache-maven-3.3.9-bin.tar.gz"
    MAVEN_TAR="apache-maven-3.3.9-bin.tar.gz"
    MAVEN_UNTAR="apache-maven-3.3.9"
    MAVEN_DIR="maven-v3.3.9"

    if type -p $MAVEN; then
        echo "[DEBS]> CONFIGURE-ENV: OPT: MAVEN: ($MAVEN) found in PATH"
    elif [ -n "$MAVEN_HOME" ] &&
         [ -x "$MAVEN_HOME/bin/$MAVEN" ];  then
        echo "[DEBS]> CONFIGURE-ENV: OPT: MAVEN: ($MAVEN) found in MAVEN_HOME"
        MAVEN="$MAVEN_HOME/bin/$MAVEN"
    else
        echo "[DEBS]> CONFIGURE-ENV: OPT: MAVEN: ($MAVEN) not found"

        if [ ! -d $APACHE_DIR ]; then
            sudo mkdir -p $APACHE_DIR
        fi

        wget $MAVEN_URL
        tar -zxf $MAVEN_TAR
        rm $MAVEN_TAR
        sudo mv $MAVEN_UNTAR $APACHE_DIR/$MAVEN_DIR
        sudo update-alternatives --install /usr/bin/mvn mvn $APACHE_DIR/$MAVEN_DIR/bin/mvn 100
    fi
}

#**************************************************************************************************
# OPT: FLINK
#**************************************************************************************************
function installFlink {
    FLINK=flink
    FLINK_CLUSTER_START=start-local.sh
    FLINK_CLUSTER_STOP=stop-local.sh
    FLINK_VERSION_RQST=2.10-1.0.0
    FLINK_URL="http://it.apache.contactlab.it/flink/flink-1.0.0/flink-1.0.0-bin-hadoop1-scala_2.10.tgz"
    FLINK_TAR="flink-1.0.0-bin-hadoop1-scala_2.10.tgz"
    FLINK_UNTAR="flink-1.0.0"
    FLINK_DIR="flink-v1.0.0"

    if type -p $FLINK; then
        echo "[DEBS]> CONFIGURE-ENV: OPT: FLINK: ($FLINK, $FLINK_CLUSTER_START $FLINK_CLUSTER_STOP) found in PATH"
    elif [ -n "$FLINK_HOME" ] &&
         [ -x "$FLINK_HOME/bin/$FLINK" ] &&
         [ -x "$FLINK_HOME/bin/$FLINK_CLUSTER_START" ] &&
         [ -x "$FLINK_HOME/bin/$FLINK_CLUSTER_STOP" ]; then
        echo "[DEBS]> CONFIGURE-ENV: OPT: FLINK: ($FLINK, $FLINK_CLUSTER_START $FLINK_CLUSTER_STOP) found in FLINK_HOME"
        FLINK="$FLINK_HOME/bin/$FLINK"
        FLINK_CLUSTER_START="$FLINK_HOME/bin/$FLINK_CLUSTER_START"
        FLINK_CLUSTER_STOP="$FLINK_HOME/bin/$FLINK_CLUSTER_STOP"
    else
        echo "[DEBS]> CONFIGURE-ENV: OPT: FLINK: ($FLINK, $FLINK_CLUSTER_START $FLINK_CLUSTER_STOP) not found"

        if [ ! -d $APACHE_DIR ]; then
            sudo mkdir -p $APACHE_DIR
        fi

        wget $FLINK_URL
        tar -zxf $FLINK_TAR
        rm $FLINK_TAR
        sudo mv $FLINK_UNTAR $APACHE_DIR/$FLINK_DIR
        sudo update-alternatives --install /usr/bin/flink flink $APACHE_DIR/$FLINK_DIR/bin/flink 100

        cp "${APACHE_DIR}/${FLINK_DIR}/conf/flink-conf.yaml" "${APACHE_DIR}/${FLINK_DIR}/conf/flink-conf-default.yaml"
    fi
}

#**************************************************************************************************
# OPT: REDIS
#**************************************************************************************************
function installRedis {
    REDIS_SERVER=redis-server
    REDIS_CLIENT=redis-cli
    REDIS_VERSION_RQST=3.0.7
    REDIS_URL="http://download.redis.io/releases/redis-3.0.7.tar.gz"
    REDIS_TAR="redis-3.0.7.tar.gz"
    REDIS_UNTAR="redis-3.0.7"
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

        if [ ! -d $REDISLAB_DIR ]; then
            sudo mkdir -p $REDISLAB_DIR
        fi

        wget $REDIS_URL
        tar -zxf $REDIS_TAR
        rm $REDIS_TAR
        sudo mv $REDIS_UNTAR $REDISLAB_DIR/$REDIS_DIR
        cd $REDISLAB_DIR/$REDIS_DIR
        make
        make PREFIX=$REDISLAB_DIR/$REDIS_DIR install
        make test
        sudo bash $REDISLAB_DIR/$REDIS_DIR/utils/install_server.sh
        cd $WSDIR
    fi
}

#**************************************************************************************************
# DATASET
#**************************************************************************************************
function downloadDataset {
    DATASET_URL="https://s3-eu-west-1.amazonaws.com/3cores/sostream/data/dataset.tar.gz"
    DATASET_SRC="dataset.tar.gz"
    DATASET_DST="$HOME/dataset"

    if [ ! -d $DATASET_DST ]; then
        wget $DATASET_URL
        tar -zvxf $DATASET_SRC
        rm $DATASET_SRC
    fi
}

#**************************************************************************************************
# USER
#**************************************************************************************************
function addMembership {
    for GRP in "$@"; do
        if [ ! [ $(groups $USR | grep "\b$GRP\b") ]]; then
            sudo gpasswd -a $USR $GRP
        fi
    done
}

function addOwnership {
    for FLD in "$@"; do
        sudo chown -R $USR $FLD
        sudo chmod ug+rwx $FLD
    done
}

function setupAutologin {
    AUTOLOGIN_SERVICE="getty@tty1.service"
    AUTOLOGIN_SERVICE_NAME="autologin.conf"
    AUTOLOGIN_SERVICE_DIR="/etc/systemd/system/$AUTOLOGIN_SERVICE.d"
    AUTOLOGIN_SERVICE_FILE="$AUTOLOGIN_SERVICE_DIR/$AUTOLOGIN_SERVICE_NAME"
    AUTOLOGIN_SERVICE_CONTENT="[Service]\nExecStart=\nExecStart=-/sbin/agetty --autologin $1 --noclear %I 38400 linux"

    if [ ! -f $AUTOLOGIN_SERVICE_FILE ] || [ ! "$(cat $AUTOLOGIN_SERVICE_FILE)" == "$(echo -e $AUTOLOGIN_SERVICE_CONTENT)" ]; then
        sudo mkdir -p $AUTOLOGIN_SERVICE_DIR
        echo -e $AUTOLOGIN_SERVICE_CONTENT > $AUTOLOGIN_SERVICE_NAME
        sudo mv $AUTOLOGIN_SERVICE_NAME $AUTOLOGIN_SERVICE_FILE
    fi

    sudo systemctl enable $AUTOLOGIN_SERVICE
}

#**************************************************************************************************
# SSH
#**************************************************************************************************
SSH_DIR="$HOME/.ssh"
SSH_CONFIG_FILE="$SSH_DIR/config"
SSH_AUTHORIZED_KEYS_FILE="$SSH_DIR/authorized_keys"

function initSSH {
    if [ ! -d $SSH_DIR ]; then
        mkdir $SSH_DIR
    fi

    if [ ! -f $SSH_AUTHORIZED_KEYS_FILE ]; then
        touch $SSH_AUTHORIZED_KEYS_FILE
    fi
}

function configureSSHDevelopers {
    SSH_LOCALHOST_KEY="id-rsa-gmarciani@debs-dev.pub"
    SSH_LOCALHOST_KEY_FILE="$SSH_DIR/$SSH_LOCALHOST_KEY"
    SSH_LOCALHOST_KEY_URL="http://gmarciani.com/workspace/sostream/ssh/$SSH_LOCALHOST_KEY"

    if [ ! -d $SSH_DIR ]; then
        mkdir $SSH_DIR
    fi

    wget $SSH_LOCALHOST_KEY_URL -O $SSH_LOCALHOST_KEY_FILE
    chmod 0400 $SSH_LOCALHOST_KEY_FILE
    cat $SSH_LOCALHOST_KEY_FILE >> $SSH_AUTHORIZED_KEYS_FILE
}

function configureSSHDEBS {
    SSH_DEBS_KEY="id-rsa-debs.pub"
    SSH_DEBS_KEY_FILE="$SSH_DIR/$SSH_DEBS_KEY"
    SSH_DEBS_KEY_URL="http://gmarciani.com/workspace/sostream/ssh/$SSH_DEBS_KEY"

    if [ ! -d $SSH_DIR ]; then
        mkdir $SSH_DIR
    fi

    wget $SSH_DEBS_KEY_URL -O $SSH_DEBS_KEY_FILE
    chmod 0400 $SSH_DEBS_KEY_FILE
    cat $SSH_DEBS_KEY_FILE >> $SSH_AUTHORIZED_KEYS_FILE
}

function configureSSHGithub {
    SSH_GITHUB_KEY_NAME="id-rsa-3cores-sostream"
    SSH_GITHUB_KEY="$SSH_DIR/$SSH_GITHUB_KEY_NAME"
    SSH_GITHUB_KEY_URL="http://gmarciani.com/workspace/sostream/ssh/$SSH_GITHUB_KEY_NAME"
    SSH_DEPLOY_GIT_REPO="git@github.com:3Cores/sostream"
    SSH_DEPLOY_KEY_MATCH="Hi 3Cores"

    SSH_CONFIG="Host github.com\n\tIdentityFile $SSH_GITHUB_KEY"

   if [ "$(echo "$(ssh -T -oStrictHostKeyChecking=no git@github.com 2>&1)" | grep -o "$SSH_DEPLOY_KEY_MATCH")" ]; then
        echo "[DEBS]> CHECK: SSH: ensured read access to repo"
    else
        echo "[DEBS]> CONFIGURE-ENV: configuring SSH ..."

        if [ ! -d $SSH_DIR ]; then
            mkdir $SSH_DIR
        fi

        if [ ! -f $SSH_GITHUB_KEY ]; then
            wget $SSH_GITHUB_KEY_URL -O $SSH_GITHUB_KEY
            wget $SSH_GITHUB_KEY_URL.pub -O $SSH_GITHUB_KEY.pub
        fi

        chmod 400 $SSH_GITHUB_KEY
        chmod 400 $SSH_GITHUB_KEY.pub

        echo -e $SSH_CONFIG >> $SSH_CONFIG_FILE

        ATTEMPTS=1
        while [[ ! "$(echo "$(ssh -T -oStrictHostKeyChecking=no git@github.com 2>&1)" | grep -o "$SSH_DEPLOY_KEY_MATCH")" ]] && [ ATTEMPTS -le 3 ]; do
            echo "[DEBS]> CONFIGURE-ENV: waiting for connection (attempt $ATTEMPTS) ..."
            sleep 1
            ATTEMPTS=$((ATTEMPTS + 1))
        done

        if [ $ATTEMPTS -gt 3 ]; then
            echo "[DEBS]> CONFIGURE-ENV: too many attempts, aborting ..."
            exit -1
        fi

        echo "[DEBS]> CONFIGURE-ENV: SSH configured"
    fi
    unset _ssh
}

#**************************************************************************************************
# ENVIRONMENT VARIABLES
#**************************************************************************************************
function configureEnvironmentVariables {
    PROFILE_DIR="/etc/profile.d"
    ENV_FILE="debs-environment.sh"
    ENV_VARS_JAVA="export JAVA_HOME=\"$ORACLE_DIR/$JAVA_DIR\""
    ENV_VARS_MAVEN="export MAVEN_HOME=\"$APACHE_DIR/$MAVEN_DIR\""
    ENV_VARS_FLINK="export FLINK_HOME=\"$APACHE_DIR/$FLINK_DIR\""
    ENV_VARS_REDIS="export REDIS_HOME=\"$REDISLAB_DIR/$REDIS_DIR\""
    ENV_PATH="export PATH=\$PATH:\$JAVA_HOME/bin:\$MAVEN_HOME/bin:\$FLINK_HOME/bin:\$REDIS_HOME/bin"
    ENV_CONTENT="$ENV_VARS_JAVA\n$ENV_VARS_MAVEN\n$ENV_VARS_FLINK\n$ENV_VARS_REDIS\n\n$ENV_PATH"

    if [ ! -f $PROFILE_DIR/$ENV_FILE ] || [ ! "$(cat $PROFILE_DIR/$ENV_FILE)" == "$(echo -e $ENV_CONTENT)" ]; then
        echo -e $ENV_CONTENT > $ENV_FILE
        sudo mv $ENV_FILE $PROFILE_DIR/$ENV_FILE
    fi
}

#**************************************************************************************************
# SYSTEM
#**************************************************************************************************
function updateSystem {
    sudo apt-get update -y
    sudo apt-get upgrade -y
    sudo apt-get dist-upgrade -y
    sudo apt-get update -y
}

function rebootSystem {
    deinit
    sudo shutdown -r now
}

#**************************************************************************************************
# BODY
#**************************************************************************************************
init "$@"

echo "[DEBS]> CONFIGURE-ENV: SOURCES: configuring sources list ..."
configureSources
echo "[DEBS]> CONFIGURE-ENV: SOURCES: sources configured"

echo "[DEBS]> CONFIGURE-ENV: PACKAGES: installing packages ..."
installPackages build-essential module-assistant dkms vim htop git ssh tcl zip unzip
echo "[DEBS]> CONFIGURE-ENV: PACKAGES: packages installed"

if [ "$ENVIRONMENT" == "vbox" ]; then
    echo "[DEBS]> CONFIGURE-ENV: PACKAGES: installing Guest Additions ..."
    installGuestAdditions
    echo "[DEBS]> CONFIGURE-ENV: PACKAGES: Guest Additions installed"
fi

echo "[DEBS]> CONFIGURE-ENV: OPT: installing optional software ..."
installJava
installMaven
installFlink
installRedis
echo "[DEBS]> CONFIGURE-ENV: OPT: optional software installed"

echo "[DEBS]> CONFIGURE-ENV: USER: setting up rights for user $USR ..."
addMembership sudo
addOwnership $ORACLE_DIR $APACHE_DIR $REDISLAB_DIR
echo "[DEBS]> CONFIGURE-ENV: USER: rights for user $USR set up"

initSSH

if [ "$ENVIRONMENT" == "vbox" ]; then
    echo "[DEBS]> CONFIGURE-ENV: configuring SSH for developers access ..."
    configureSSHDevelopers
    echo "[DEBS]> CONFIGURE-ENV: SSH for developers access configured"
    echo "[DEBS]> CONFIGURE-ENV: configuring SSH for DEBS access ..."
    configureSSHDEBS
    echo "[DEBS]> CONFIGURE-ENV: SSH for DEBS access configured"
fi

echo "[DEBS]> CONFIGURE-ENV: configuring Github repo ..."
configureSSHGithub
echo "[DEBS]> CONFIGURE-ENV: read access to repo configured"

echo "[DEBS]> CONFIGURE-ENV: VAR: configuring environment variables ..."
configureEnvironmentVariables
echo "[DEBS]> CONFIGURE-ENV: VAR: environment variables configured"

if [ "$ENVIRONMENT" == "aws" ]; then
    echo "[DEBS]> CONFIGURE-ENV: DATASET: downloading dataset ..."
    downloadDataset
    echo "[DEBS]> CONFIGURE-ENV: DATASET: dataset downloaded"
fi

echo "[DEBS]> CONFIGURE-ENV: SYSTEM: updating system ..."
updateSystem
echo "[DEBS]> CONFIGURE-ENV: SYSTEM: system up-to-date"

if [ "$ENVIRONMENT" == "vbox" ]; then
    echo "[DEBS]> CONFIGURE-ENV: SYSTEM: rebooting ..."
    rebootSystem
elif [ "$ENVIRONMENT" == "aws" ]; then
    echo "[DEBS]> CONFIGURE-ENV: SYSTEM: please reboot your system"
fi

deinit
