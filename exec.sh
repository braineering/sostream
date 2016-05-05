#!/usr/bin/env bash

#**************************************************************************************************
# APP DEBUG EXECUTION
#
# PRODUCTION USE
# $1 [frnds]: relative path to friendships data source
# $2 [posts]: relative path to posts data source
# $3 [cmnts]: relative path to comments data source
# $4 [likes]: relative path to likes data source
# $5 [ranks]: K, the rank size
# $6 [delay]: D, the window size in seconds
# $7 (conf ): relative path to a YAML configuration file
#
# DEBUG USE
# $1 [data ]: relative path to data source directory
# $5 [ranks]: K, the rank size
# $6 [delay]: D, the window size in seconds
# $7 (conf ): relative path to a YAML configuration file
#**************************************************************************************************

#**************************************************************************************************
# INIT
#**************************************************************************************************
function init {
	WSDIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
	QUERY_DIR="$WSDIR/query"
	OUTPUT_DIR="$WSDIR/output"
	RESULT_DIR="$WSDIR"
	PERFMC_FILE="$RESULT_DIR/performance.txt"
	LOGGER_FILE="$RESULT_DIR/log.txt"

	QUERIES=$(for f in $(ls query/*.jar); do _file="$(basename $f)"; echo ${_file%.*}; done;)

	DATA_DIR=""
	FRIENDSHIPS=""
	POSTS=""
	COMMENTS=""
	LIKES=""
	K=""
	D=""
	CONFG=""

	PARALLELISM=4

	FLINK=flink
	FLINK_START="$FLINK_HOME/bin/start-local.sh"
	FLINK_STOP="$FLINK_HOME/bin/stop-local.sh"

	if [ "$#" == 3 ] || [ "$#" == 4 ]; then
		DATA_DIR="$WSDIR/$1"
	elif [ "$#" == 6 ] || [ "$#" == 7 ]; then
		DATA_DIR="$WSDIR"
	else
		echo "Usage (Production): $(basename "${BASH_SOURCE[0]}") [friendships] [posts] [comments] [likes] [k] [d] (conf)"
		echo "Usage (Debug):      $(basename "${BASH_SOURCE[0]}") [data] [k] [d] (conf)"
	    exit -1
	fi

	FRIENDSHIPS="$DATA_DIR/friendships.dat"
	POSTS="$DATA_DIR/posts.dat"
	COMMENTS="$DATA_DIR/comments.dat"
	LIKES="$DATA_DIR/likes.dat"
	K=$2
	D=$3
	CONFG=""
	if [ $4 ]; then
		CONFG="$WSDIR/$4"
	fi

	if [ $DATA_DIR ]; then
		if [ ! -d $DATA_DIR ]; then
			echo "[DEBS]> RUN: cannot read data folder ($DATA_DIR)"
			exit -1
		fi
	fi

	if [ ! -r $FRIENDSHIPS ]; then
		echo "[DEBS]> RUN: cannot read friendships ($FRIENDSHIPS)"
		exit -1
	fi

	if [ ! -r $POSTS ]; then
		echo "[DEBS]> RUN: cannot read posts ($POSTS)"
		exit -1
	fi

	if [ ! -r $COMMENTS ]; then
		echo "[DEBS]> RUN: cannot read comments ($COMMENTS)"
		exit -1
	fi

	if [ ! -r $LIKES ]; then
		echo "[DEBS]> RUN: cannot read likes ($LIKES)"
		exit -1
	fi

	if [ $K -le 0 ]; then
		echo "[DEBS]> RUN: K should be greater than zero"
		exit -1
	fi

	if [ $D -le 0 ]; then
		echo "[DEBS]> RUN: D should be greater than zero"
		exit -1
	fi

	if [ $CONFG ]; then
		if [ ! -r $CONFG ]; then
			echo "[DEBS]> RUN: cannot read configuration file ()"
			exit -1
		fi
	fi

	echo "[DEBS]> RUN: preparing queries:" $QUERIES "with dataset in $DATA_DIR ..."

	if [ ! -d $OUTPUT_DIR ]; then
		mkdir $OUTPUT_DIR
	fi

	for Q in ${QUERIES[@]}; do
		Q_DIR="$OUTPUT_DIR/$Q"
		Q_OUTPUT_FILE="$Q_DIR/out.txt"
		Q_PERFMC_FILE="$Q_DIR/performance.txt"
		Q_LOGGER_FILE="$Q_DIR/log.txt"
		if [ ! -d $Q_DIR ]; then
			mkdir $Q_DIR
		fi
		if [ ! -f $Q_OUTPUT_FILE ]; then
			touch $Q_OUTPUT_FILE
		fi
		if [ ! -f $Q_PERFMC_FILE ]; then
			touch $Q_PERFMC_FILE
		fi
		if [ ! -f $Q_LOGGER_FILE ]; then
			touch $Q_LOGGER_FILE
		fi

		unset Q_DIR
		unset Q_OUTPUT_FILE
		unset Q_PERFMC_FILE
		unset Q_LOGGER_FILE
	done

	PRBLMS_TOT=0

	function totalCheckResult {
        if [ $PRBLMS_TOT -gt 0 ]; then
            echo "[DEBS]> RUN: completed with $PRBLMS_TOT problem(s)."
        else
            echo "[DEBS]> RUN: completed with no problems, everything ok."
        fi
    }
}

#**************************************************************************************************
# DEINIT
#**************************************************************************************************
function deinit {
	unset WSDIR
	unset QUERY_DIR
	unset QUERIES
	unset OUTPUT_DIR
	unset RESULT_DIR
	unset PERFMC_FILE
	unset LOGGER_FILE

	unset PRBLMS_TOT

	unset DATA_DIR
	unset FRIENDSHIPS
	unset POSTS
	unset COMMENTS
	unset LIKES
	unset K
	unset D
	unset CONFG

	unset FLINK
	unset FLINK_START
	unset FLINK_STOP
}

#**************************************************************************************************
# EXECUTION
#**************************************************************************************************
function runQueries {
	_MEMREC_SCRIPT="${WSDIR}/memrec.sh"
	for Q in "$@"; do
		Q_JAR="$QUERY_DIR/$Q.jar"
		if [ ! -r $Q_JAR ]; then
			echo "[DEBS]> RUN: EXECUTE: cannot read JAR of $Q ($Q_JAR)"
			echo "[DEBS]> RUN: EXECUTE: skipping $Q"
			PRBLMS_TOT=$(($PRBLMS_TOT + 1))
			continue
		fi

		echo "[DEBS]> RUN: starting environment for query $Q..."
		startEnvironment
		echo "[DEBS]> RUN: environment started"

		echo "[DEBS]> RUN: EXECUTE: submitting $Q ..."
		bash ${_MEMREC_SCRIPT} &
		_MEMREC_PID=$!
		$FLINK run $Q_JAR $FRIENDSHIPS $POSTS $COMMENTS $LIKES $K $D $OUTPUT_DIR -P$PARALLELISM
		kill ${_MEMREC_PID}

		echo "[DEBS]> RUN: stopping environment for query $Q..."
		stopEnvironment
		echo "[DEBS]> RUN: environment stopped"

		unset Q_JAR
	done
}

#**************************************************************************************************
# RESULT
#**************************************************************************************************
function writeResults {
	echo "[DEBS]> RUN: writing results for queries: $@"

	if [ ! -d $RESULT_DIR ]; then
		mkdir -p $RESULT_DIR
	else
		for Q in "$@"; do
			Q_OUT_FILE_DST="$RESULT_DIR/$Q.txt"
			if [ -f $Q_OUT_FILE_DST ]; then
				cp /dev/null $Q_OUT_FILE_DST
			else
				touch $Q_OUT_FILE_DST
			fi
			unset Q_OUT_FILE_DST
		done
		if [ -f $PERFMC_FILE ]; then
			cp /dev/null $PERFMC_FILE
		else
			touch $PERFMC_FILE
		fi
		if [ -f $LOGGER_FILE ]; then
			cp /dev/null $LOGGER_FILE
		else
			touch $LOGGER_FILE
		fi
	fi

	for Q in "$@"; do
		echo "[DEBS]> RUN: writing output of $Q"
		Q_OUT_FILE="$OUTPUT_DIR/$Q/out.txt"
		if [ ! -r $Q_OUT_FILE ]; then
			echo "[DEBS]> RUN: cannot read output of $Q ($Q_OUT_FILE)"
			echo "[DEBS]> RUN: skipping $Q"
			PRBLMS_TOT=$(($PRBLMS_TOT + 1))
			continue
		fi
		Q_OUT_FILE_DST="$RESULT_DIR/$Q.txt"
		mv $Q_OUT_FILE $Q_OUT_FILE_DST
		echo "[DEBS]> RUN: output of $Q written in $Q_OUT_FILE_DST"

		Q_PERFMC_FILE="$OUTPUT_DIR/$Q/performance.txt"
		echo "[DEBS]> RUN: writing performance of $Q"
		if [ ! -r $Q_PERFMC_FILE ]; then
			echo "[DEBS]> RUN: cannot read performance of $Q ($Q_PERFMC_FILE)"
			echo "[DEBS]> RUN: skipping performance of $Q"
			PRBLMS_TOT=$(($PRBLMS_TOT + 1))
		else
			Q_PERFMC_CONTENT=$(cat $Q_PERFMC_FILE)
			echo "$Q_PERFMC_CONTENT " >> $PERFMC_FILE
			echo "[DEBS]> RUN: performance of $Q written in $PERFMC_FILE"
		fi

		Q_LOGGER_FILE="$OUTPUT_DIR/$Q/log.txt"
		echo "[DEBS]> RUN: writing log of $Q"
		if [ ! -r $Q_LOGGER_FILE ]; then
			echo "[DEBS]> RUN: cannot read log of $Q ($Q_LOGGER_FILE)"
			echo "[DEBS]> RUN: skipping log of $Q"
			PRBLMS_TOT=$(($PRBLMS_TOT + 1))
		else
			Q_LOGGER_CONTENT=$(cat $Q_LOGGER_FILE)
			echo -e "### START OF LOG FOR QUERY: $Q ###\n\n" >> $LOGGER_FILE
			echo "$Q_LOGGER_CONTENT " >> $LOGGER_FILE
			echo -e "\n\n### END OF LOG FOR QUERY: $Q ###\n\n" >> $LOGGER_FILE
			echo "[DEBS]> RUN: log of $Q written in $LOGGER_FILE"
		fi
		unset Q_OUT_FILE
		unset Q_OUT_FILE_DST
		unset Q_PERFMC_FILE
		unset Q_PERFMC_CONTENT
		unset Q_LOGGER_FILE
		unset Q_LOGGER_CONTENT
	done
}

#**************************************************************************************************
# ENVIRONMENT START/STOP
#**************************************************************************************************
function stopAllFlink {
	while [[ ! $(bash $FLINK_STOP | grep "No jobmanager daemon to stop on host") ]]; do
		echo "[DEBS]> RUN: stopping previous Apache Flink instances ..."
	done
}

function startFlink {
	echo "[DEBS]> RUN: starting Apache Flink ..."
	while [[ ! $(bash $FLINK_START | grep "Starting jobmanager daemon on host") ]]; do
		echo "restarting Flink ..."
		stopAllFlink
	done
}

function flushAllRedis {
	echo "[DEBS]> RUN: starting Redis ..."
	while [[ "$(redis-cli dbsize)" != 0 ]]; do
		echo "[DEBS]> RUN: flushing Redis ..."
		if [[ "$(redis-cli flushall)" != "OK" ]]; then
			echo "[DEBS]> RUN: error while flushing Redis"
			exit -1
		else
			echo "[DEBS]> RUN: Redis flushed"
		fi
	done
}

function startEnvironment {
	stopAllFlink
	startFlink
	flushAllRedis
}

function stopEnvironment {
	#stopAllFlink
	flushAllRedis
}

#**************************************************************************************************
# BODY
#**************************************************************************************************
init "$@"

echo "[DEBS]> RUN: starting queries ..."
runQueries ${QUERIES[*]}
echo "[DEBS]> RUN: queries finished"

echo "[DEBS]> RUN: starting results reporting ..."
writeResults ${QUERIES[*]}
echo "[DEBS]> RUN: results reporting finished"

totalCheckResult

deinit
