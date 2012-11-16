##############################################################################
# rbe.sh to run the RBE from TPC-W Java Implementation.
# 2003 by Jan Kiefer.
#
# This file is distributed "as is". It comes with no warranty and the 
# author takes no responsibility for the consequences of its use.
#
# Usage, distribution and modification is allowed to everyone, as long 
# as reference to the author is given and this license note is included.
##############################################################################

#!/bin/sh

# $1 = run nr
# $2 = nr of ebs (rbe)

help()
{
  cat <<HELP
rbe.sh - start rbe for tpc-w
usage: rbe [option]
where option can be:

    -r <nr>
	set the number of the run (for filename only), default: 1

    -n <nr>
	set the nummber of ebs to start, default: 20

    -u <seconds>
	ramp up time, default: 100

    -i <seconds>
	measurement interval time, default: 1200

    -d <seconds>
	ramp down time, default: 50

    -w <url>
	url of SUT to use, default: http://tiamak/tpcw/

    -t <nr>
	set the type for the rbe to use
		1: Browsing Mix
		2: Shopping mix (default)
		3: Ordering Mix

HELP
  exit 0
}

error()
{
    # print an error and exit
    echo "$1"
    exit 1
}

tftype=2
runnr=1
numebs=20
ru=100
mi=1200
rd=50
url="http://localhost/tpcw/"

# The option parser, change it as needed
# In this example -f and -h take no arguments -l takes an argument
# after the l
while [ -n "$1" ]; do
case $1 in
    -h) help;shift 1;; # function help is called
    -r) runnr=$2;shift 2;;
    -n) numebs=$2;shift 2;;
    -u) ru=$2;shift 2;;
    -i) mi=$2;shift 2;;
    -d) rd=$2;shift 2;;
    -w) url=$2;shift 2;;
    -t) tftype=$2;shift 2;;
    --) shift;break;; # end of options
    -*) echo "error: no such option $1. -h for help";exit 1;;
    *)  break;;
esac
done

datum=`date +%Y%m%d_%H%M`
filename="run"$runnr"_e"$numebs"_"$datum".m"

fact="rbe.EBTPCW"$tftype"Factory"

java rbe.RBE -EB $fact $numebs -OUT $filename -RU $ru -MI $mi -RD $rd -WWW $url -ITEM 1000 -CUST 288000 -GETIM false
