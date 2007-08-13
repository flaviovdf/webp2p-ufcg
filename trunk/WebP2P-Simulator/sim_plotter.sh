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

DATA=sim_data.dat
NEWPROP=/tmp/sim.prop.tmp

rm -f $DATA

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
	
	sum=0

	for i in `seq $NRUNS`; do

		rm -f simulator.log
	
		sed "s/$PARAM  *=  *[0-9]*\.*[0-9]*/$PARAM = $newparam/" $FILE > $NEWPROP
		value=`sh $SELF/run.sh $TYPE $NEWPROP | grep = | awk '{print $NF}'`

		if [ $value != "NaN" ]; then
			sum=`echo "scale = 5; $sum + $value" | bc`	
		fi

		rm -f $NEWPROP

	done

	AVG=`echo "scale = 5; $sum / $NRUNS" | bc`
	OUT="$newparam  $AVG"
	echo $OUT >> $DATA
done
