#!/bin/sh

find_self() {
	SELF=`dirname $0`
}

usage() {
	echo "usage $0 <sim.properties files> <sim type> <param to vary> < values[] || -seq <first> <last> <inc> >"
	exit 1
}



#MAIN
if [ $# -lt 3  ]; then
	usage
fi

find_self

FILE=$1
TYPE=$2
PARAM=$3
OPTION=$4


if [ ! -r $FILE ]; then
	echo "cannot read $FILE"
	exit 1
fi

NEWPROP=/tmp/sim.prop.tmp

shift 3
VALUES=$*
if [ "$1" == "-seq" ]; then
	if [ $# -lt 4 ]; then
		usage
	fi

	FIRST=$2
	LAST=$3
	INC=$4

	VALUES=`seq $FIRST $INC $LAST`
fi

NRUNS=100
for newparam in $VALUES; do
	
	for i in `seq $NRUNS`; do
		
		echo "simulator.log simulator_${PARAM}-${newparam}_RUN-${i}.log"

		rm -f simulator.log
	
		sed "s/$PARAM  *=  *[0-9]*\.*[0-9]*/$PARAM = $newparam/" $FILE > $NEWPROP
		sh $SELF/run.sh $TYPE $NEWPROP | grep = | awk '{print $NF}'

		cp simulator.log simulator_${PARAM}-${newparam}_RUN-${i}.log
		
		rm -f $NEWPROP

	done
done
