#!/usr/bin/env bash

#**************************************************************************************************
# APP COMPILATION
#
# $1...$n: (query,...,query): default are "q1" and "q2"
#**************************************************************************************************

#**************************************************************************************************
# INIT
#**************************************************************************************************
function init {
	if [ "$#" == 0 ]; then
		QUERIES=("q1" "q2")
	else
		QUERIES=("$@")
	fi

	WSDIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
	TARGET_DIR="$WSDIR/target"
	QUERY_DIR="$WSDIR/query"

	PRBLMS_TOT=0

	function totalCheckResult {
        if [ $PRBLMS_TOT -gt 0 ]; then
            echo "[DEBS]> COMPILE: completed with $PRBLMS_TOT problem(s)."
        else
            echo "[DEBS]> COMPILE: completed with no problems, everything ok."
        fi
    }
}

#**************************************************************************************************
# DEINIT
#**************************************************************************************************
function deinit {
	unset QUERIES
	unset WSDIR
	unset TARGET_DIR
	unset QUERY_DIR

	unset PRBLMS_TOT

	unset JAR_NAME
	unset POM_FILE
}

#**************************************************************************************************
# COMPILATION
#**************************************************************************************************
function compileQueries {
	JAR_NAME="debs-1.0.0.jar"
	POM_FILE="$WSDIR/pom.xml"	

	echo "[DEBS]> COMPILE: compiling queries: $@ ..."

	if [ ! -d $QUERY_DIR ]; then
		mkdir $QUERY_DIR
	else
		rm -rf $QUERY_DIR/*.jar
		echo "[DEBS]> COMPILE: query folder cleaned"
	fi

	for Q in "$@"; do
		echo "[DEBS]> COMPILE: compiling query: $Q ..."
		mvn clean package -f $POM_FILE -P$Q -DskipTests
		mv "$TARGET_DIR/$JAR_NAME" "$QUERY_DIR/$Q.jar"

		if [ -r $QUERY_DIR/$Q.jar ]; then
			echo "[DEBS]> COMPILE: compiled query $Q"
		else
			echo "[DEBS]> COMPILE: failed compilation of query $Q"
			PRBLMS_TOT=$((PRBLMS_TOT + 1)) 
		fi
	done	
}

#**************************************************************************************************
# BODY
#**************************************************************************************************
init "$@"

echo "[DEBS]> COMPILE: starting ..."
compileQueries ${QUERIES[*]}
echo "[DEBS]> COMPILE: finished"

totalCheckResult

deinit
